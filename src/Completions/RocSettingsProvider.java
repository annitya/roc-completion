package Completions;

import Completions.Entities.Setting;
import Framework.CompletionPreloader;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.psi.PsiElement;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

public class RocSettingsProvider extends CompletionContributor
{
    @Nullable
    @Override
    public AutoCompletionDecision handleAutoCompletionPossibility(@NotNull AutoCompletionContext context)
    {
        return super.handleAutoCompletionPossibility(context);
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result)
    {
        PsiElement position = parameters.getPosition();
        // Wrong file.
        if (!CompletionPreloader.isRocConfigFile(position.getContainingFile()))
        {
            return;
        }

        List<LookupElement> subCompletions = getSubCompletions(position);
        // Is this a request for sub-completons?
        if (subCompletions.size() > 0)
        {
            result.addAllElements(subCompletions);
            result.stopHere();

            return;
        }

        String prefix = result
            .getPrefixMatcher()
            .getPrefix();

        List<SettingLookupElement> completions = SettingCompletionBuilder.buildCompletions(parameters, prefix);

        Boolean foundCompletions = completions.size() > 0;
        Boolean isExtendedCompletion = parameters.isExtendedCompletion();

        // Add regular completions to the beginning of the list if user requested extended completions.
        if (!foundCompletions || isExtendedCompletion)
        {
            result.runRemainingContributors(parameters, true);
            return;
        }

        result.addAllElements(completions);
        // Don't display all the other junk.
        result.stopHere();
    }

    private List<LookupElement> getSubCompletions(PsiElement psiElement)
    {
        JSProperty property = PsiTreeUtil.getParentOfType(psiElement, JSProperty.class);

        if (property == null)
        {
            return Collections.emptyList();
        }

        String name = property.getQualifiedName();

        if (name == null)
        {
            return Collections.emptyList();
        }

        Setting setting = CompletionPreloader
            .getCompletions()
            .getSetting(name);

        return setting != null ? setting.getSubCompletionVariants() : Collections.emptyList();
    }
}
