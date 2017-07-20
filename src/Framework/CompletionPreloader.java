package Framework;

import Completions.Container;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.javascript.nodejs.interpreter.local.NodeJsLocalInterpreter;
import com.intellij.javascript.nodejs.interpreter.local.NodeJsLocalInterpreterManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.stream.Collectors;

public class CompletionPreloader implements ProjectComponent
{
    private final Project project;
    private ArrayList<Container> completions;

    public CompletionPreloader(Project project)
    {
        this.project = project;
        completions = new ArrayList<>();
    }

    public ArrayList<Container> getCompletions() { return completions; }

    @Override
    public void projectOpened()
    {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withCharset(StandardCharsets.UTF_8)
            .withWorkDirectory(project.getBasePath());

        NodeJsLocalInterpreter interpreter = NodeJsLocalInterpreterManager
            .getInstance()
            .detectMostRelevant();

        if (interpreter == null)
        {
            return;
        }

        URL getSettingsResource = this
            .getClass()
            .getClassLoader()
            .getResource("js/getSettings.js");

        if (getSettingsResource == null)
        {
            return;
        }

        commandLine.setExePath(interpreter.getInterpreterSystemDependentPath());
        commandLine.addParameter(getSettingsResource.getFile());
        commandLine.addParameter("roc-config");

        String result = "";

        try
        {
            Process process = commandLine.createProcess();
            InputStreamReader resultStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedResultReader = new BufferedReader(resultStreamReader);

            InputStreamReader errorStreamReader = new InputStreamReader(process.getErrorStream());

            result = bufferedResultReader
                .lines()
                .collect(Collectors.joining());
        }
        catch (ExecutionException ignored) {}

        Gson gson = new GsonBuilder().create();

        try
        {
            completions = gson.fromJson(result, new TypeToken<ArrayList<Container>>(){}.getType());
        }
        catch (Exception ignored) {}
    }

    @Override
    public void projectClosed() {}

    @Override
    public void initComponent() {}

    @Override
    public void disposeComponent() { }

    @NotNull
    @Override
    public String getComponentName() { return "Framework.CompletionPreloader"; }
}
