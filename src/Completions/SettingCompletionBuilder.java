package Completions;

import Framework.CompletionPreloader;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.*;
import java.util.stream.Collectors;

class SettingCompletionBuilder
{
    static List<SettingLookupElement> buildCompletions(CompletionParameters parameters)
    {
        PsiElement position = parameters.getPosition();
        JSProperty property = PsiTreeUtil.getParentOfType(position, JSProperty.class);

        // Unable to determine where in roc.settings.js we are.
        if (property == null)
        {
            return Collections.emptyList();
        }

        String namespace = property
            .getJSNamespace()
            .toString();

        // Wrong file, or wrong structure.
        if (!namespace.startsWith(SettingContainer.ROOT_NAMESPACE)) {
            return Collections.emptyList();
        }

        SettingContainer settingContainer = position
            .getProject()
            .getComponent(CompletionPreloader.class)
            .getCompletions();

        Map<String, JSProperty> existingSettings = findExistingSettings(parameters);
        List<Setting> settings = settingContainer.getSettings(namespace, existingSettings);

        return settings
            .stream()
            .map(setting -> new SettingLookupElement(setting, namespace, parameters.getOriginalFile().getText()))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    static private Map<String, JSProperty> findExistingSettings(CompletionParameters parameters)
    {
        try
        {
            RecursivePropertyWalker walker = new RecursivePropertyWalker();
            walker.visitFile(parameters.getOriginalFile());

            return walker.getExistingProperties();
        }
        catch (Exception e)
        {
            return Collections.emptyMap();
        }
    }
}
