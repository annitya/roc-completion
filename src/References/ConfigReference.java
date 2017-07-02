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
        ResolveResult result = new ResolveResult()
        {
            @Nullable
            @Override
            public PsiElement getElement()
            {
                return myElement;
            }

            @Override
            public boolean isValidResult()
            {
                return true;
            }
        };

        ResolveResult[] results = new ResolveResult[1];
        results[0] = result;

        return results;
    }

    @Override
    public PsiElement getElement()
    {
        return myElement;
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
        return myElement;
    }

    @NotNull
    @Override
    public String getCanonicalText()
    {
        return "canon?";
    }

    @Override
    public PsiElement handleElementRename(String s) throws IncorrectOperationException
    {
        return null;
    }

    @Override
    public PsiElement bindToElement(@NotNull PsiElement psiElement) throws IncorrectOperationException
    {
        return myElement;
    }

    @Override
    public boolean isReferenceTo(PsiElement psiElement)
    {
        return true;
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
