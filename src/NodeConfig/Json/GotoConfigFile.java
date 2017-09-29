package NodeConfig.Json;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import org.jetbrains.annotations.Nullable;
import java.util.Arrays;
import java.util.stream.Collectors;

public class GotoConfigFile implements GotoDeclarationHandler
{
    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement psiElement, int i, Editor editor)
    {
        if (psiElement == null)
        {
            return PsiElement.EMPTY_ARRAY;
        }

        PsiReference[] references = psiElement
            .getParent()
            .getReferences();

        return Arrays
            .stream(references)
            .filter(reference -> reference instanceof JsonConfigReference)
            .map(PsiReference::resolve)
            .collect(Collectors.toList())
            .toArray(new PsiElement[0]);
    }

    @Nullable
    @Override
    public String getActionText(DataContext context) { return "Goto config file."; }
}
