package Framework;

import Completions.Entities.SettingContainer;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

public class CompletionPreloader implements ProjectComponent
{
    private final Project project;
    private static SettingContainer completions;

    public CompletionPreloader(Project project)
    {
        this.project = project;
        completions = new SettingContainer();
    }

    public static SettingContainer getCompletions() { return completions; }

    public static void setCompletions(SettingContainer completions) { CompletionPreloader.completions = completions; }

    @Override
    public void projectOpened()
    {
        FetchCompletions task = new FetchCompletions(project);
        ProgressManager.getInstance().run(task);
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
