package Framework;

import Completions.Setting;
import Terminal.ListSettings;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;

public class CompletionPreloader implements ProjectComponent
{
    private Project project;
    private ArrayList<Setting> completions;

    public CompletionPreloader(Project project)
    {
        this.project = project;
        completions = new ArrayList<>();
    }

    public ArrayList<Setting> getCompletions() { return completions; }

    @Override
    public void projectOpened()
    {
        ListSettings listSettings = new ListSettings(project);
        ArrayList<String> settings = listSettings.getSettings();

        for (String candidate : settings)
        {
            Setting setting = new Setting(candidate);

            if (!setting.isValid())
            {
                continue;
            }

            completions.add(setting);
        }
    }

    @Override
    public void projectClosed() {}

    @Override
    public void initComponent() {}

    @Override
    public void disposeComponent() { }

    @NotNull
    @Override
    public String getComponentName() { return "Framework.CompletionPreloader"; }
}
