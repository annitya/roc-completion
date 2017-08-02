package Actions;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.terminal.TerminalView;

public class Dev extends com.intellij.openapi.actionSystem.AnAction {
    @Override
    public void actionPerformed(AnActionEvent event)
    {
        Project project = event.getProject();

        if (project == null)
        {
            return;
        }

        TerminalView terminalView = TerminalView.getInstance(project);
        RocTerminal terminal = new RocTerminal(project);
        terminalView.createNewSession(project, terminal);
    }
}
