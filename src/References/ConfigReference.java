package References;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.psi.ResolveResult;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    public ConfigReference(@NotNull PsiElement element)
    {
        super(element);
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean b)
    {
        return new ResolveResult[0];
    }

    @Override
    public PsiElement getElement()
    {
        return null;
    }

    @Override
    public TextRange getRangeInElement()
    {
        return null;
    }

    @Nullable
    @Override
    public PsiElement resolve()
    {
        return null;
    }

    @NotNull
    @Override
    public String getCanonicalText()
    {
        return null;
    }

    @Override
    public PsiElement handleElementRename(String s) throws IncorrectOperationException
    {
        return null;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement psiElement) throws IncorrectOperationException
    {
        return null;
    }

    @Override
    public boolean isReferenceTo(PsiElement psiElement)
    {
        return false;
    }

    @NotNull
    @Override
    public Object[] getVariants()
    {
        return new Object[0];
    }

    @Override
    public boolean isSoft()
    {
        return false;
    }
}
