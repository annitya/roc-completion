package Actions;

import Framework.CompletionPreloader;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;

public class RefreshCompletions extends AnAction
{
    @Override
    public void actionPerformed(AnActionEvent event)
    {
        CompletionPreloader.fetchCompletions(event.getProject());
    }
}
