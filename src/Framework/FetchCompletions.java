package Framework;

import Completions.Entities.SettingContainer;
import Completions.Entities.SettingTreeNode;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.javascript.nodejs.interpreter.local.NodeJsLocalInterpreter;
import com.intellij.javascript.nodejs.interpreter.local.NodeJsLocalInterpreterManager;
import com.intellij.notification.*;
import com.intellij.openapi.progress.PerformInBackgroundOption;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task.Backgroundable;
import com.intellij.openapi.project.Project;
import icons.RocIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

class FetchCompletions extends Backgroundable implements PerformInBackgroundOption
{
    FetchCompletions(@Nullable Project project) { //noinspection DialogTitleCapitalization
        super(project, "Fetching Roc-completions..."); }

    @Override
    public void run(@NotNull ProgressIndicator progressIndicator)
    {
        try
        {
            GeneralCommandLine commandLine = createCommandLine();

            if (commandLine == null)
            {
                throw new Exception("Unable to initialize command-line.");
            }

            Process process = commandLine.createProcess();
            InputStreamReader resultStreamReader = new InputStreamReader(process.getInputStream());
            BufferedReader bufferedResultReader = new BufferedReader(resultStreamReader);

            String result = bufferedResultReader
                .lines()
                .collect(Collectors.joining());

            Gson gson = new GsonBuilder().create();

            Type targetType = new TypeToken<List<SettingTreeNode>>() {}.getType();
            SettingContainer completions = new SettingContainer(gson.fromJson(result, targetType));
            CompletionPreloader.setCompletions(completions);

            progressIndicator.stop();
        }
        catch (Exception e)
        {
            NotificationGroup group = new NotificationGroup("ROC_GROUP", NotificationDisplayType.STICKY_BALLOON, true, null, RocIcons.ROC);
            Notification notification = group.createNotification("Failed to fetch roc-completions!", "", e.getMessage(), NotificationType.ERROR);
            Notifications.Bus.notify(notification);
        }
    }

    private GeneralCommandLine createCommandLine()
    {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine
            .withParentEnvironmentType(GeneralCommandLine.ParentEnvironmentType.CONSOLE)
            .withCharset(StandardCharsets.UTF_8)
            .withWorkDirectory(myProject.getBasePath());

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
}
