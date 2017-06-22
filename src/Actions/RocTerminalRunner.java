package Actions;

import com.intellij.openapi.project.Project;
import com.intellij.util.ArrayUtil;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.plugins.terminal.LocalTerminalDirectRunner;

import java.util.Arrays;
import java.util.Map;
import java.util.stream.Stream;

public class RocTerminalRunner extends LocalTerminalDirectRunner {
    RocTerminalRunner(Project project) {
        super(project);
    }

    @Override
    public String runningTargetName() {
        return "Roc terminal";
    }

    @Override
    public String[] getCommand(Map<String, String> envs) {
        String[] command = super.getCommand(envs);
        String rocCommand[] = {"-c", "roc dev"};

        return Stream
            .concat(Arrays.stream(command), Arrays.stream(rocCommand))
            .toArray(String[]::new);
    }
}
