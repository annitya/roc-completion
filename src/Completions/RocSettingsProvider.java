package Completions;

import Terminal.ListSettings;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.ide.DataManager;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.project.Project;
import com.intellij.util.AsynchConsumer;
import com.pty4j.PtyProcess;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class RocSettingsProvider extends CompletionContributor {
    private ArrayList<String> candidates;

    public RocSettingsProvider()
    {
        candidates = new ArrayList<>();

        DataManager
                .getInstance()
                .getDataContextFromFocus()
                .doWhenDone(new AsynchConsumer<DataContext>()
                {
                    @Override
                    public void finished() {}

                    @Override
                    public void consume(DataContext dataContext)
                    {
                        Project project = CommonDataKeys.PROJECT.getData(dataContext);
                        ListSettings listSettings = new ListSettings(project);
                        try {
                            PtyProcess process = listSettings.execute();
                            readLines(process.getInputStream());
                        } catch (ExecutionException | InterruptedException | IOException ignored) {}
                    }
                });
    }

    private void readLines(InputStream stream) throws IOException, InterruptedException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        while (true)
        {
            String line = reader.readLine();

            if (line == null)
            {
                break;
            }

            candidates.add(line);
        }
    }

    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result)
    {
        if (candidates == null)
        {
            return;
        }

        ArrayList<SettingCompletion> elements = SettingCompletionBuilder.parse(parameters, candidates);
        result.addAllElements(elements);
        result.stopHere();

        super.fillCompletionVariants(parameters, result);
    }
}
