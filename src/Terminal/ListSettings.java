package Terminal;

import com.intellij.openapi.project.Project;
import com.pty4j.PtyProcess;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class ListSettings extends CustomCommandRunner
{
    public ListSettings(Project project)
    {
        super(project, "roc list-settings --no-color");
    }

    public ArrayList<String> getSettings()
    {
        ArrayList<String> settings = new ArrayList<>();

        try
        {
            PtyProcess process = createProcess(null);
            settings = readLines(process.getInputStream());
        }
        catch (ExecutionException | InterruptedException | IOException ignored) {}

        return settings;
    }

    private ArrayList<String> readLines(InputStream stream) throws IOException, InterruptedException
    {
        ArrayList<String> candidates = new ArrayList<>();

        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

        while (true)
        {
            String line = reader.readLine();

            if (line == null)
            {
                break;
            }

            candidates.add(line);
        }

        return candidates;
    }
}
