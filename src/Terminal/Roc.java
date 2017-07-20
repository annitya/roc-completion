package Terminal;

import com.intellij.openapi.project.Project;
import com.jediterm.terminal.ui.JediTermWidget;
import com.jediterm.terminal.ui.TerminalSession;
import com.jediterm.terminal.ui.TerminalTabs;
import com.jediterm.terminal.ui.TerminalWidget;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.terminal.JBTabbedTerminalWidget;
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class Roc extends LocalTerminalDirectRunner {
    public Roc(Project project) { super(project); }

    @Override
    public void openSessionInDirectory(@NotNull TerminalWidget terminalWidget, @Nullable String directory)
    {
        super.openSessionInDirectory(terminalWidget, directory);

        JBTabbedTerminalWidget tabbedWidget;

        try
        {
             tabbedWidget = (JBTabbedTerminalWidget)terminalWidget;
        }
        catch (Exception ignored)
        {
            return;
        }

        TerminalTabs tabs = tabbedWidget.getTerminalTabs();

        if (tabs == null)
        {
            return;
        }

        TerminalSession currentSession = terminalWidget.getCurrentSession();

        for (int i = 0; i < tabs.getTabCount(); i++)
        {
            JediTermWidget tab = tabs.getComponentAt(i);

            if (!tab.getCurrentSession().equals(currentSession))
            {
                continue;
            }

            tabs.setTitleAt(i, "Roc JS");
            break;
        }
    }

    @Override
    public String[] getCommand(Map<String, String> envs)
    {
        envs.put("NODE_ENV", "development");
        String[] command = super.getCommand(envs);

        String rocCommand[] = {"-c", "roc dev"};

        return Stream
            .concat(Arrays.stream(command), Arrays.stream(rocCommand))
            .toArray(String[]::new);
    }
}
