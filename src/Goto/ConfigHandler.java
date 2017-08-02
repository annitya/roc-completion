package Goto;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.lang.Language;
import com.intellij.lang.ecmascript6.psi.ES6ImportDeclaration;
import com.intellij.lang.ecmascript6.psi.ES6ImportedBinding;
import com.intellij.lang.ecmascript6.psi.impl.ES6ImportPsiUtil;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;
import java.util.List;

public class ConfigHandler implements GotoDeclarationHandler
{
    public Boolean isConfigReference(ES6ImportDeclaration importDeclaration)
    {
        String fromClauseText = ES6ImportPsiUtil.getFromClauseText(importDeclaration);
        return fromClauseText != null && fromClauseText.equals("config");

    }

    public List<PsiFile> getConfigFiles(Project project)
    {
        List<PsiFile> configFiles = new ArrayList<>();

        VirtualFile configDirectory = project
            .getBaseDir()
            .findFileByRelativePath("config");

        if (configDirectory == null)
        {
            return configFiles;
        }

        for (VirtualFile virtualFile : configDirectory.getChildren())
        {
            PsiFile psiFile = PsiManager
                .getInstance(project)
                .findFile(virtualFile);

            // So... uh... this is awkward.
            if (psiFile == null)
            {
                continue;
            }

            Language language = psiFile.getLanguage();

            if (ConfigWalker.isJson(language) || ConfigWalker.isJavaScript(language))
            {
                configFiles.add(psiFile);
            }
        }

        return configFiles;
    }

    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement psiElement, int i, Editor editor)
    {
        if (psiElement == null)
        {
            return PsiElement.EMPTY_ARRAY;
        }

        ES6ImportDeclaration importDeclaration = PsiTreeUtil.getParentOfType(psiElement, ES6ImportDeclaration.class);

        if (importDeclaration == null || !isConfigReference(importDeclaration))
        {
            return PsiElement.EMPTY_ARRAY;
        }

        List<PsiFile> configFiles = getConfigFiles(psiElement.getProject());

        // Whole config was imported, so just return list of config-files.
        if (psiElement.getParent() instanceof ES6ImportedBinding)
        {
            return configFiles.toArray(new PsiElement[configFiles.size()]);
        }

        String propertyName = psiElement.getText();
        return getConfigTargets(configFiles, propertyName);
    }

    public PsiElement[] getConfigTargets(List<PsiFile> configFiles, String propertyName)
    {
        List<PsiElement> matches = new ArrayList<>();

        for (PsiFile configFile : configFiles)
        {
            ConfigWalker walker = ConfigWalker.factory(configFile, propertyName);

            if (walker == null)
            {
                continue;
            }

            walker.visitFile(configFile);
            PsiElement match = walker.match;

            if (match != null)
            {
                matches.add(match);
            }
        }

        return matches.toArray(new PsiElement[matches.size()]);
    }

    @Nullable
    @Override
    public String getActionText(DataContext dataContext)
    {
        return null;
    }
}
