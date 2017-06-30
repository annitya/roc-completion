package Completions;

import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.psi.util.PsiTreeUtil;

import java.util.*;

class SettingCompletionBuilder
{
    static ArrayList<SettingLookupElement> filterCompletions(ArrayList<Setting> candidates, CompletionParameters parameters)
    {
        ArrayList<SettingLookupElement> validCompletions = new ArrayList<>();
        JSProperty property = PsiTreeUtil.getParentOfType(parameters.getPosition(), JSProperty.class);

        // Unable to determine where in roc.settings.js we are.
        if (property == null)
        {
            return validCompletions;
        }

        String namespace = property
            .getJSNamespace()
            .toString();

        // Wrong file, or wrong structure.
        if (!namespace.startsWith(Setting.ROOT_NAMESPACE)) {
            return validCompletions;
        }

        ArrayList<String> existingSettings = findExistingSettings(parameters);

        for (Setting candidate : candidates)
        {
            // Setting is below or beyond current scope.
            if (!candidate.isApplicable(namespace))
            {
                continue;
            }

            SettingLookupElement lookupElement = new SettingLookupElement(candidate, namespace);

            if (existingSettings.contains(lookupElement.getLookupString()))
            {
                continue;
            }

            validCompletions.add(lookupElement);
        }

        return validCompletions;
    }

    static private ArrayList<String> findExistingSettings(CompletionParameters parameters)
    {
        ArrayList<String> existingSettings = new ArrayList<>();

        try
        {
            JSProperty parentProperty = PsiTreeUtil.getParentOfType(parameters.getPosition(), JSProperty.class);
            RecursivePropertyWalker walker = new RecursivePropertyWalker(parentProperty);

            walker.visitFile(parameters.getOriginalFile());

            for (JSProperty existingProperty : walker.getExistingProperties())
            {
                existingSettings.add(existingProperty.getName());
            }
        }
        catch (Exception ignored) {}

        return existingSettings;
    }
}
