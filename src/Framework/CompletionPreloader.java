package Framework;

import Completions.Entities.SettingContainer;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.DumbService;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiFile;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import org.jetbrains.annotations.NotNull;

public class CompletionPreloader implements ProjectComponent
{
    private static final String ROC_CONFIG_FILE = "roc.config.js";
    private final Project project;
    private static SettingContainer completions;

    public CompletionPreloader(Project project)
    {
        this.project = project;
        completions = new SettingContainer();
    }

    public static Boolean isRocConfigFile(PsiFile file)
    {
        return file
            .getName()
            .equals(ROC_CONFIG_FILE);
    }

    public static SettingContainer getCompletions() { return completions; }

    public static void setCompletions(SettingContainer completions) { CompletionPreloader.completions = completions; }

    private Boolean isRocProject()
    {
        PsiFile[] configFiles = FilenameIndex.getFilesByName(project, ROC_CONFIG_FILE, GlobalSearchScope.projectScope(project));
        // No config-file, we're done here.
        if (configFiles.length != 1)
        {
            return false;
        }

        String expectedLocation = project
            .getBaseDir()
            .getPath() + "/" + ROC_CONFIG_FILE;

        String actualLocation = configFiles[0].getVirtualFile().getPath();

        return expectedLocation.equals(actualLocation);
    }

    @Override
    public void projectOpened()
    {
        DumbService.getInstance(project).runWhenSmart(() ->
        {
            if (!isRocProject())
            {
                return;
            }

            FetchCompletions task = new FetchCompletions(project);
            ProgressManager.getInstance().run(task);
        });
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
