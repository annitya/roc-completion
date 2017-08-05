package Completions.Entities;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DefaultValue
{
    private Object value;
    private String path;
    private String type;

    void setType(String type) { this.type = type; }

    void setPath(String path)
    {
        this.path = path;

        if (path.equals("runtime.template.path") && value == null)
        {
            value = "";
        }
    }

    DefaultValue(Object o) { value = o; }

    @Override
    public String toString()
    {
        // Gson converts integers to doubles... lulz!
        try
        {
            Double doubleValue = Double.parseDouble(value.toString());
            return Integer.toString(doubleValue.intValue());
        }
        catch (Exception ignored) {}

        if (value instanceof List || value instanceof Map)
        {
            Gson gson = new GsonBuilder().create();
            return gson.toJson(value);
        }

        return value != null ? value.toString() : "";
    }

    List<LookupElement> getSubCompletionVariants()
    {
        String enumerationSource = type;

        Boolean hasCorrectEnding = enumerationSource.endsWith("$/") || enumerationSource.endsWith("/i");

        if (!enumerationSource.startsWith("/^") || !hasCorrectEnding)
        {
            return Collections.emptyList();
        }

        enumerationSource = enumerationSource
            .replace("/^", "")
            .replace("$/i", "")
            .replace("/i", "");

        List<LookupElement> subCompletions = new ArrayList<>();

        for (String subCompletion : enumerationSource.split("\\|"))
        {
            if (subCompletion.equals(this.toString()))
            {
                continue;
            }

            subCompletions.add(LookupElementBuilder.create(subCompletion));
        }

        return subCompletions;
    }

    String getTargetValue(String quote)
    {
        String defaultValueString = this.toString();

        String nativeType = type;
        Boolean isEnumeration = getSubCompletionVariants().size() > 0;

        if (isEnumeration)
        {
            nativeType = "Enumeration";
        }

        switch (nativeType)
        {
//        "type": "Array(Object(String))",
            case "Boolean / Integer":
                return wrap("false/0", quote);
            case "Boolean / Function":
                return wrap("Boolean/Function", quote);
            case "Enumeration":
                return wrap("", quote);
            case "Filepath":
            case "String":
            case "String / Array(String)":
            case "Array(String) / String":
                return wrap(defaultValueString, quote);
            case "Integer":
                if (path.equals("runtime.https.port") && value == null)
                {
                    defaultValueString = "443";
                }

                if (path.equals("dev.devMiddleware.aggregateTimeout"))
                {
                    return "0";
                }

                if (path.equals("dev.redux.devTools.instrument.maxAge"))
                {
                    return "0";
                }
            case "Array(String)":
            case "Unknown":
            case "Array(String / Array(String))":
            case "Filepath / Array(Filepath)":
                if (defaultValueString.length() == 0)
                {
                    return wrap("", quote);
                }

                return defaultValueString;
            case "Boolean":
                Boolean invertedDefaultValue = !Boolean.valueOf(defaultValueString);
                return invertedDefaultValue.toString();
            case "Object":
                return "{}";
            // You know... for readability.
            default:
                return defaultValueString;
        }
    }

    private String wrap(String s, String quote)
    {
        return String.format("%s%s%s", quote, s, quote);
    }
}
