package Actions;

import Framework.FetchCompletions;
import com.intellij.notification.*;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import icons.RocIcons;
import javax.swing.*;

public class RefreshCompletions extends AnAction
{
    @Override
    public void actionPerformed(AnActionEvent event)
    {
        FetchCompletions fetchCompletions = new FetchCompletions(event.getProject());
        try
        {
            fetchCompletions.run();

            NotificationGroup group = new NotificationGroup("ROC_GROUP", NotificationDisplayType.BALLOON, false, null, RocIcons.ROC);
            Notification notification = group.createNotification("Completions updated!", "", "", NotificationType.INFORMATION);
            Notifications.Bus.notify(notification);

            Timer timer = new Timer(2500, e -> notification.expire());
            timer.setRepeats(false);
            timer.start();
        }
        catch (Exception e)
        {
            NotificationGroup group = new NotificationGroup("ROC_GROUP", NotificationDisplayType.STICKY_BALLOON, true, null, RocIcons.ROC);
            Notification notification = group.createNotification("Failed to fetch roc-completions!", "", e.getMessage(), NotificationType.ERROR);
            Notifications.Bus.notify(notification);
        }
    }
}
