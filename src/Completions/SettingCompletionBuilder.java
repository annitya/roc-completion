package Completions;

import Completions.Entities.Setting;
import Completions.Entities.SettingContainer;
import Framework.CompletionPreloader;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiTreeUtil;
import java.util.*;
import java.util.stream.Collectors;

class SettingCompletionBuilder
{
    static List<SettingLookupElement> buildCompletions(CompletionParameters parameters, String prefix)
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

        SettingContainer settingContainer = CompletionPreloader.getCompletions();

        Map<String, JSProperty> existingSettings = findExistingSettings(parameters, prefix);
        List<Setting> settings = settingContainer.getSettings(namespace, existingSettings);

        return settings
            .stream()
            .map(setting -> new SettingLookupElement(setting, namespace))
            .collect(Collectors.toCollection(ArrayList::new));
    }

    static private Map<String, JSProperty> findExistingSettings(CompletionParameters parameters, String prefix)
    {
        PsiFile file = parameters.getOriginalFile();
        StringBuffer buffer;

        // User was typing and probably screwed up the syntax in the process :-/
        if (parameters.isAutoPopup())
        {
            PsiElement position = parameters.getPosition();

            Integer startOffset = position.getTextOffset();
            Integer endOffset = startOffset + prefix.length();

            buffer = new StringBuffer(file.getText());
            buffer = buffer.replace(startOffset, endOffset, "");

            file = PsiFileFactory
                .getInstance(position.getProject())
                .createFileFromText(position.getLanguage(), buffer.toString());
        }

        try
        {
            RecursivePropertyWalker walker = new RecursivePropertyWalker();
            walker.visitFile(file);

            return walker.getExistingProperties();
        }
        catch (Exception e)
        {
            return Collections.emptyMap();
        }
    }
}
