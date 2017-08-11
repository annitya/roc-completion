package Framework;

import Completions.Entities.DefaultValue;
import Completions.Entities.DefaultValueDeserializer;
import Completions.Entities.SettingContainer;
import Completions.Entities.SettingTreeNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.javascript.nodejs.interpreter.local.NodeJsLocalInterpreter;
import com.intellij.javascript.nodejs.interpreter.local.NodeJsLocalInterpreterManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import java.io.*;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

public class FetchCompletions
{
    private Project project;

    public FetchCompletions(Project project)
    {
        this.project = project;
    }

    public void run() throws Exception
    {
        // Prepare and run node-command which will fetch settings.
        GeneralCommandLine commandLine = createCommandLine();
        Process process = commandLine.createProcess();

        String input = readInputStream(process.getInputStream());
        Gson gson = new GsonBuilder()
            .registerTypeAdapter(DefaultValue.class, new DefaultValueDeserializer())
            .create();

        Type targetType = new TypeToken<List<SettingTreeNode>>() {}.getType();
        List<SettingTreeNode> settings = gson.fromJson(input, targetType);

        if (settings == null)
        {
            String error = readInputStream(process.getErrorStream());
            String message = error.length() > 0 ? error : input;

            throw new Exception("Error occured while fetching completions: " + message);
        }

        SettingContainer completions = new SettingContainer(settings);
        CompletionPreloader.setCompletions(completions);
    }

    private GeneralCommandLine createCommandLine() throws Exception
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
            throw new Exception("Unable to create node-interpreter.");
        }
        // Move getSettings.js resource to .idea-folder within current project.
        ensureJsFile();

        commandLine.setExePath(interpreter.getInterpreterSystemDependentPath());
        commandLine.addParameter(getJsFilePath());
        commandLine.addParameter("roc-config");

        return commandLine;
    }

    private void ensureJsFile() throws Exception
    {
        String jsFilePath = getJsFilePath();
        File jsFile = new File(jsFilePath);

        if (jsFile.isFile())
        {
            return;
        }

        URL getSettingsResource = this
            .getClass()
            .getClassLoader()
            .getResource("js/getSettings.js");

        if (getSettingsResource == null)
        {
            throw new Exception("Unable to locate getSettings.js in resources!");
        }

        String content = readInputStream(getSettingsResource.openStream());

        if (content.length() == 0)
        {
            throw new Exception("Unable to read getSettings.js from plugin!");
        }

        PrintWriter out = new PrintWriter(jsFile);
        out.println(content);
        out.close();
    }

    private String getJsFilePath() throws Exception
    {
        VirtualFile projectFile = project.getProjectFile();

        if (projectFile == null)
        {
            throw new Exception("Project file is missing!");
        }

        String projectFileDirectory = projectFile
            .getParent()
            .getPath();

        return projectFileDirectory + "/" + "getSettings.js";
    }

    private String readInputStream(InputStream stream)
    {
        InputStreamReader reader = new InputStreamReader(stream);
        BufferedReader bufferedReader = new BufferedReader(reader);

        return bufferedReader
            .lines()
            .collect(Collectors.joining());
    }
}
