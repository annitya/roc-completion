package Framework;

import Completions.SettingContainer;
import Completions.SettingTreeNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.javascript.nodejs.interpreter.local.NodeJsLocalInterpreter;
import com.intellij.javascript.nodejs.interpreter.local.NodeJsLocalInterpreterManager;
import com.intellij.openapi.application.Application;
import com.intellij.openapi.application.impl.ApplicationImpl;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class CompletionPreloader implements ProjectComponent
{
    private final Project project;
    private SettingContainer completions;

    public CompletionPreloader(Project project)
    {
        this.project = project;
    }

    public SettingContainer getCompletions() { return completions; }

    @Override
    public void projectOpened()
    {
        GeneralCommandLine commandLine = createCommandLine();

        if (commandLine == null)
        {
            return;
        }

        String result = "";

        try
        {
            Process process = commandLine.createProcess();
            InputStreamReader resultStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedResultReader = new BufferedReader(resultStreamReader);

            result = bufferedResultReader
                .lines()
                .collect(Collectors.joining());
        }
        catch (ExecutionException ignored) {}

        Gson gson = new GsonBuilder().create();

        try
        {
            Type targetType = new TypeToken<List<SettingTreeNode>>() {}.getType();
            completions = new SettingContainer(gson.fromJson(result, targetType));
        }
        catch (Exception ignored) {}
    }

    private GeneralCommandLine createCommandLine()
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
            return null;
        }

        URL getSettingsResource = this
            .getClass()
            .getClassLoader()
            .getResource("js/getSettings.js");

        if (getSettingsResource == null)
        {
            return null;
        }

        commandLine.setExePath(interpreter.getInterpreterSystemDependentPath());
        commandLine.addParameter(getSettingsResource.getFile());
        commandLine.addParameter("roc-config");
        return commandLine;
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
