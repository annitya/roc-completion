package Completions;

import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionInitializationContext;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import org.jetbrains.annotations.NotNull;
import java.util.List;

public class RocSettingsProvider extends CompletionContributor
{
    @Override
    public void beforeCompletion(@NotNull CompletionInitializationContext context)
    {
        context.setDummyIdentifier("property: 0,");
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result)
    {
        List<SettingLookupElement> completions = SettingCompletionBuilder.buildCompletions(parameters);
        boolean foundCompletions = completions.size() > 0;

        // Add regular completions to the beginning of the list if user requested extended completions.
        if (!foundCompletions || parameters.isExtendedCompletion())
        {
            result.runRemainingContributors(parameters, true);
        }
        else
        {
            // Don't display all the other junk.
            result.stopHere();
        }

        result.addAllElements(completions);
    }
}
