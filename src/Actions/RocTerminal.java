package Actions;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.jediterm.terminal.TtyConnector;
import com.pty4j.PtyProcess;
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

class RocTerminal extends LocalTerminalDirectRunner {
    RocTerminal(Project project) { super(project); }

    @Override
    protected TtyConnector createTtyConnector(PtyProcess process)
    {
        return new RocTtyConnector(process, CharsetToolkit.UTF8_CHARSET);
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
