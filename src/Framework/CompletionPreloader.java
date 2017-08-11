package Framework;

import Completions.Entities.SettingContainer;
import com.intellij.notification.*;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import com.intellij.psi.PsiFile;
import icons.RocIcons;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class CompletionPreloader implements StartupActivity, DumbAware
{
    private static final String ROC_CONFIG_FILE = "roc.config.js";
    private static SettingContainer completions;

    public CompletionPreloader()
    {
        completions = new SettingContainer();
    }

    public static Boolean isRocConfigFile(PsiFile file)
    {
        return file
            .getName()
            .equals(ROC_CONFIG_FILE);
    }

    public static SettingContainer getCompletions() { return completions; }

    public static void setCompletions(SettingContainer completions) { CompletionPreloader.completions = completions; }

    @Override
    public void runActivity(@NotNull Project project)
    {
        String expectedPath = project.getBasePath() + "/roc.config.js";
        File configFile = new File(expectedPath);

        if (!configFile.isFile())
        {
            return;
        }

        try
        {
            FetchCompletions fetchCompletions = new FetchCompletions(project);
            fetchCompletions.run();
        }
        catch (Exception e)
        {
            NotificationGroup group = new NotificationGroup("ROC_GROUP", NotificationDisplayType.STICKY_BALLOON, true, null, RocIcons.ROC);
            Notification notification = group.createNotification("Failed to fetch roc-completions!", "", e.getMessage(), NotificationType.ERROR);
            Notifications.Bus.notify(notification);
        }
    }
}
