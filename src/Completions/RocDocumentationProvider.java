package Completions;

import Completions.Entities.Setting;
import Completions.Entities.SettingContainer;
import Framework.CompletionPreloader;
import com.intellij.lang.documentation.DocumentationProvider;
import com.intellij.lang.documentation.ExternalDocumentationProvider;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiManager;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RocDocumentationProvider implements DocumentationProvider, ExternalDocumentationProvider
{
    @Nullable
    @Override
    public List<String> getUrlFor(PsiElement psiElement, PsiElement originalElement)
    {
        List<String> urls = new ArrayList<>();

        if (!CompletionPreloader.isRocConfigFile(psiElement.getContainingFile()))
        {
            return urls;
        }

        try
        {
            JSProperty property = (JSProperty)psiElement;

            String namespace = property
                .getJSNamespace()
                .toString();

            if (!namespace.startsWith(SettingContainer.ROOT_NAMESPACE))
            {
                return urls;
            }

            urls.add(property.getQualifiedName());
        }
        catch (Exception ignored) {}

        return urls;
    }

    @Nullable
    @Override
    public String fetchExternalDocumentation(Project project, PsiElement psiElement, List<String> list)
    {
        String qualifiedName = list.get(0);
        SettingContainer completions = CompletionPreloader.getCompletions();

        Setting setting = completions.getSetting(qualifiedName);

        if (setting == null)
        {
            return "Just a container. Try looking at the sub-nodes instead.";
        }

        return setting.getDescription();
    }

    //region Unused methods.

    @Override
    public boolean hasDocumentationFor(PsiElement psiElement, PsiElement originalElement) { return false; }

    @Nullable
    @Override
    public String getQuickNavigateInfo(PsiElement psiElement, PsiElement originalElement) { return null; }

    @Nullable
    @Override
    public String generateDoc(PsiElement psiElement, @Nullable PsiElement originalElement) { return null; }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLookupItem(PsiManager psiManager, Object o, PsiElement psiElement) { return null; }

    @Nullable
    @Override
    public PsiElement getDocumentationElementForLink(PsiManager psiManager, String s, PsiElement psiElement) { return null; }

    @Override
    public boolean canPromptToConfigureDocumentation(PsiElement psiElement) { return false; }

    @Override
    public void promptToConfigureDocumentation(PsiElement psiElement) {}

    //endregion
}
