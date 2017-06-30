package Terminal;

import com.intellij.execution.Executor;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.jediterm.terminal.ui.TerminalWidget;
import com.pty4j.PtyProcess;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.plugins.terminal.JBTabbedTerminalWidget;
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner;

import java.awt.*;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class CustomCommandRunner extends LocalTerminalDirectRunner {
    private String customCommand;

    public CustomCommandRunner(Project project, String customCommand)
    {
        super(project);
        this.customCommand = customCommand;
    }

    @Override
    protected String getTerminalConnectionName(PtyProcess process)
    {
        return "some name";
    }

    @NotNull
    @Override
    public JBTabbedTerminalWidget createTerminalWidget(@NotNull Disposable parent)
    {
        return super.createTerminalWidget(parent);
    }

    @Override
    public void openSession(@NotNull TerminalWidget terminal)
    {
        super.openSession(terminal);
    }

    @Override
    protected void showConsole(Executor defaultExecutor, @NotNull RunContentDescriptor myDescriptor, Component toFocus)
    {
        super.showConsole(defaultExecutor, myDescriptor, toFocus);
    }

    @Override
    public void openSessionInDirectory(@NotNull TerminalWidget terminalWidget, @Nullable String directory)
    {
        super.openSessionInDirectory(terminalWidget, directory);
    }

    @Override
    public String runningTargetName()
    {
        return "Roc terminal";
    }

    @Override
    public String[] getCommand(Map<String, String> envs)
    {
        String[] command = super.getCommand(envs);
        String rocCommand[] = {"-c", customCommand};

        return Stream
            .concat(Arrays.stream(command), Arrays.stream(rocCommand))
            .toArray(String[]::new);
    }
}
