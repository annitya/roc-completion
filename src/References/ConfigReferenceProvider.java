package References;

import Goto.ConfigHandler;
import com.intellij.lang.ecmascript6.psi.ES6ImportDeclaration;
import com.intellij.lang.javascript.psi.JSFile;
import com.intellij.lang.javascript.psi.JSReferenceExpression;
import com.intellij.lang.javascript.psi.JSSourceElement;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiReference;
import com.intellij.psi.PsiReferenceProvider;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class ConfigReferenceProvider extends PsiReferenceProvider
{
    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement psiElement, @NotNull ProcessingContext processingContext)
    {
        ConfigHandler configHandler = new ConfigHandler();

        try
        {
            JSFile file = (JSFile)psiElement.getContainingFile();
            ES6ImportDeclaration[] importDeclarations = PsiTreeUtil.getChildrenOfType(file, ES6ImportDeclaration.class);

            if (importDeclarations == null)
            {
                return PsiReference.EMPTY_ARRAY;
            }

            for (ES6ImportDeclaration importDeclaration : importDeclarations)
            {
                if (!configHandler.isConfigReference(importDeclaration))
                {
                    continue;
                }

                String propertyName = psiElement.getText();
                ArrayList<PsiFile> configFiles = configHandler.getConfigFiles(psiElement.getProject());
                PsiElement[] configTargets = configHandler.getConfigTargets(configFiles, propertyName);

                ConfigReference[] configReferences = new ConfigReference[configTargets.length];

                for (int i = 0; i < configTargets.length; i++)
                {
                    configReferences[i] = new ConfigReference(configTargets[i]);
                }

                return configReferences;
            }
        }
        catch (Exception ignored) {}

        return PsiReference.EMPTY_ARRAY;
    }
}
