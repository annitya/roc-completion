package Terminal;

import com.intellij.openapi.project.Project;
import com.pty4j.PtyProcess;

import java.util.concurrent.ExecutionException;

public class ListSettings extends CustomCommandRunner
{
    public ListSettings(Project project)
    {
        super(project, "roc list-settings | grep '|' --color=never");
    }

    public PtyProcess execute() throws ExecutionException
    {
        return createProcess(null);
    }
}
