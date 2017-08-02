package References;

import Goto.ConfigHandler;
import com.intellij.lang.ecmascript6.psi.ES6ImportDeclaration;
import com.intellij.lang.ecmascript6.psi.impl.ES6ImportPsiUtil;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import java.util.Collection;
import java.util.List;

class ConfigReferenceProvider extends PsiReferenceProvider
{
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext)
    {
        ConfigHandler configHandler = new ConfigHandler();

        try
        {
            Collection<ES6ImportDeclaration> importDeclarations = ES6ImportPsiUtil.getImportDeclarations(psiElement.getContainingFile());

            for (ES6ImportDeclaration importDeclaration : importDeclarations)
            {
                if (!configHandler.isConfigReference(importDeclaration))
                {
                    continue;
                }

                String propertyName = psiElement.getText();
                List<PsiFile> configFiles = configHandler.getConfigFiles(psiElement.getProject());
                PsiElement[] configTargets = configHandler.getConfigTargets(configFiles, propertyName);

                ConfigReference[] configReferences = new ConfigReference[configTargets.length];

                for (int i = 0; i < configTargets.length; i++)
                {
                    configReferences[i] = new ConfigReference(psiElement, configTargets[i]);
                }

                return configReferences;
            }
        }
        catch (Exception ignored) {}

        return PsiReference.EMPTY_ARRAY;
    }
}
