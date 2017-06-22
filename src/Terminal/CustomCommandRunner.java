package Terminal;

import com.intellij.openapi.project.Project;
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner;
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
