package Completions;

import Completions.Entities.Setting;
import Framework.CompletionPreloader;
import com.intellij.codeInsight.completion.CompletionConfidence;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ThreeState;
import org.jetbrains.annotations.NotNull;

public class SubCompletionConfidence extends CompletionConfidence
{
    @NotNull
    @Override
    public ThreeState shouldSkipAutopopup(@NotNull PsiElement contextElement, @NotNull PsiFile psiFile, int offset)
    {
        // Wrong file.
        if (!CompletionPreloader.isRocConfigFile(psiFile))
        {
            return ThreeState.UNSURE;
        }

        JSProperty property = PsiTreeUtil.getParentOfType(contextElement, JSProperty.class);
        // Wrong place in file.
        if (property == null)
        {
            return ThreeState.UNSURE;
        }

        Setting setting = CompletionPreloader
            .getCompletions()
            .getSetting(property.getQualifiedName());

        // Not a roc-setting.
        if (setting == null)
        {
            return ThreeState.UNSURE;
        }

        return setting.getSubCompletionVariants().size() > 1 ? ThreeState.NO : ThreeState.UNSURE;
    }
}
