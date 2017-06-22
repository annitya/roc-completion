package Completions;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.ArrayList;
import java.util.StringJoiner;

class SettingCompletionBuilder {
    static ArrayList<SettingCompletion> parse(CompletionParameters parameters, ArrayList<String> candidates)
    {
        ArrayList<SettingCompletion> validCompletions = new ArrayList<>();
        String propertyFilter = buildPropertyFilter(parameters);

        if (propertyFilter == null)
        {
            return validCompletions;
        }

        for (String candidate : candidates)
        {
            SettingCompletion completion = parseCandidate(candidate, propertyFilter);

            if (completion == null)
            {
                continue;
            }

            validCompletions.add(completion);
        }

        return validCompletions;
    }

    static private SettingCompletion parseCandidate(String candidate, String propertyFilter)
    {
        String filteredCandidate = candidate
                .replace("|", "")
                .replace("-", "")
                .trim();

        if (filteredCandidate.length() == 0) {
            return null;
        }

        String parts[] = candidate.split("\\|");
        ArrayList<String> trimmedParts = new ArrayList<>();

        for (String part : parts)
        {
            trimmedParts.add(part.trim());
        }

        if (trimmedParts.get(1).equals("Description")) {
            return null;
        }

        String name = trimmedParts.get(2);

        if (!name.startsWith(propertyFilter))
        {
            return null;
        }

        name = name.replaceFirst(propertyFilter, "");

        trimmedParts.set(2, name);

        return new SettingCompletion(trimmedParts);
    }

    static private String buildPropertyFilter(CompletionParameters parameters)
    {
        PsiElement currentElement = parameters.getPosition();
        ArrayList<JSProperty> properties = new ArrayList<>();

        while (true)
        {
            if (currentElement == null)
            {
                break;
            }

            JSProperty property = PsiTreeUtil.getParentOfType(currentElement, JSProperty.class);

            if (property == null)
            {
                break;
            }

            String name = property.getName();

            if (name == null || name.equals("IntellijIdeaRulezzz"))
            {
                currentElement = currentElement.getParent();
                continue;
            }

            properties.add(property);
            currentElement = property;
        }

        StringJoiner joiner = new StringJoiner(".", "", ".");

        for (JSProperty property : Lists.reverse(properties))
        {
            joiner.add(property.getName());
        }

        String propertyFilter = joiner.toString();

        if (!propertyFilter.startsWith("settings.")) {
            return null;
        }

        return propertyFilter.replaceFirst("settings.", "");
    }
}
