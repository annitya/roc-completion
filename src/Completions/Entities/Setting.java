package Completions.Entities;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.javascript.psi.JSProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@SuppressWarnings("unused")
public class Setting
{
    private String name;
    private String description;
    private String type;
    private Boolean required;
    private Boolean canBeEmpty;
    private String cli;
    private String path;
    private Object defaultValue;
    private List<String> extensions;

    public String getDescription() { return description; }

    String getPath() { return path; }

    public String getNamespace()
    {
        return SettingContainer.ROOT_NAMESPACE + "." + path;
    }

    public String getDefaultValue() { return defaultValue != null ? defaultValue.toString() : ""; }

    String getName() { return name; }

    public String getType() { return type; }

    public List<LookupElement> getSubCompletionVariants()
    {
        String enumerationValueString = type;

        Boolean hasCorrectEnding = enumerationValueString.endsWith("$/") || enumerationValueString.endsWith("/i");

        if (!enumerationValueString.startsWith("/^") || !hasCorrectEnding)
        {
            return Collections.emptyList();
        }

        enumerationValueString = enumerationValueString
            .replace("/^", "")
            .replace("$/", "")
            .replace("/i", "");

        List<LookupElement> subCompletions = new ArrayList<>();

        for (String subCompletion : enumerationValueString.split("\\|"))
        {
            if (subCompletion.equals(getDefaultValue()))
            {
                continue;
            }

            subCompletions.add(LookupElementBuilder.create(subCompletion));
        }

        return subCompletions;
    }

    private String getTargetValue(String quote)
    {
        String defaultValueString = getDefaultValue();

        String nativeType = type;
        Boolean isEnumeration = getSubCompletionVariants().size() > 0;

        if (isEnumeration)
        {
            nativeType = "Enumeration";
        }

        switch (nativeType)
        {
//        "type": "Array(Object(String))",
//        "type": "Boolean / Function",
//        "type": "Boolean / Integer",
            case "Enumeration":
                return quote + quote;
            case "Filepath":
            case "String":
                return String.format("%s%s%s", quote, defaultValueString, quote);
            case "Integer":
            case "Array(String)":
            case "Unknown":
            // Sooo... do these actually work?
            case "String / Array(String)":
            case "Array(String / Array(String))":
            case "Array(String) / String":
            case "Filepath / Array(Filepath)":
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

    private String getFormattedRemainderPart(List<String> list, Integer index, String quote)
    {
        String remainder = list.get(index);

        if (remainder.contains("-"))
        {
            remainder = String.format("%s%s%s", quote, remainder, quote);
        }

        return remainder;
    }

    public String toRelativeJsNotation(JSProperty property, String quote)
    {
        String remainder = getNamespace().replaceFirst(property.getQualifiedName() + ".", "");
        // This is the js-properties we need to create.
        List<String> remainderParts = Lists.newArrayList(remainder.split("\\."));

        String jsObject = "%s";
        // Create all needed object-levels.
        for  (int i = 0; i < remainderParts.size() - 1; i++)
        {
            String remainderPart = getFormattedRemainderPart(remainderParts, i, quote);
            String jsPart = remainderPart + ": { %s }";
            jsObject = String.format(jsObject, jsPart);
        }

        String valueName = getFormattedRemainderPart(remainderParts, remainderParts.size() - 1, quote);
        // Insert an "intelligent" default-value.
        String valuePart = String.format("%s: %s,", valueName, getTargetValue(quote));

        jsObject = String.format(jsObject, valuePart);

        // If we needed to create additional levels, odds are we need a comma.
        if (remainderParts.size() > 1)
        {
            jsObject += ",";
        }

        return jsObject;
    }
}
