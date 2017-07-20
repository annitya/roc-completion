package Completions;

import Framework.CompletionPreloader;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class RocSettingsProvider extends CompletionContributor {
    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result)
    {
        Project project = parameters
                .getPosition()
                .getProject();

        CompletionPreloader preloader = project.getComponent(CompletionPreloader.class);
        // ArrayList<Setting> candidates = preloader.getCompletions();
        // ArrayList<SettingLookupElement> validCompletions = SettingCompletionBuilder.filterCompletions(candidates, parameters);

        // result.addAllElements(validCompletions);

        // if (validCompletions.size() > 0) {
            // result.stopHere();
        // }
        // else {
            result.runRemainingContributors(parameters, true);
        // }
    }
}
