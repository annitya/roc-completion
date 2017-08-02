package Framework;

import Completions.Entities.SettingContainer;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiManager;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

public class CompletionPreloader implements ProjectComponent
{
    public static final String ROC_CONFIG_FILE = "roc.config.js";
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
        PsiFile[] configFiles = FilenameIndex.getFilesByName(project, ROC_CONFIG_FILE, GlobalSearchScope.projectScope(project));

        // No config-file, we're done here.
        if (configFiles.length != 1)
        {
            return;
        }

        String expectedLocation = project
            .getBaseDir()
            .getPath() + "/" + ROC_CONFIG_FILE;

        String actualLocation = configFiles[0].getVirtualFile().getPath();
        // Uh... lets just... go...
        if (!expectedLocation.equals(actualLocation))
        {
            return;
        }

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
