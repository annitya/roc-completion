package Completions.Entities;

import com.google.common.collect.Lists;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.lang.javascript.psi.JSProperty;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    private DefaultValue defaultValue;
    private List<String> extensions;

    public String getDescription() { return description; }

    private String getPath() { return path; }

    public String getNamespace()
    {
        return SettingContainer.ROOT_NAMESPACE + "." + path;
    }

    public String getDefaultValue()
    {
        return defaultValue.toString();
    }

    String getName() { return name; }

    public String getType()
    {
        if (path.equals("build.i18n.usePolyfill"))
        {
            type = "Boolean";
        }

        if (path.equals("build.templateValues"))
        {
            type = "String";
        }

        return type;
    }

    void forwardDefaultValues()
    {
        if (defaultValue == null)
        {
            defaultValue = new DefaultValue(null);
        }

        if (path.equals("runtime.head.meta"))
        {
            String parsedValue =
                defaultValue.toString()
                .replace("\\u003d", "-");

            defaultValue = new DefaultValue(parsedValue);
        }

        defaultValue.setPath(getPath());
        defaultValue.setType(getType());
    }

    public List<LookupElement> getSubCompletionVariants()
    {
        return defaultValue.getSubCompletionVariants();
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
        String valuePart = String.format("%s: %s,", valueName, defaultValue.getTargetValue(quote));

        jsObject = String.format(jsObject, valuePart);

        // If we needed to create additional levels, odds are we need a comma.
        if (remainderParts.size() > 1)
        {
            jsObject += ",";
        }

        return jsObject;
    }
}
