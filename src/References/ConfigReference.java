package References;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReferenceBase;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class ConfigReference extends PsiReferenceBase<PsiElement>
{
    private final PsiElement target;

    ConfigReference(@NotNull PsiElement element, PsiElement target)
    {
        super(element);
        this.target = target;
    }

    @Override
    public PsiElement getElement() { return myElement; }

    @Override
    public TextRange getRangeInElement()
    {
        return myElement.getTextRange();
    }

    @Nullable
    @Override
    public PsiElement resolve() { return target; }

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
        return null;
    }

    @Override
    public boolean isReferenceTo(PsiElement psiElement)
    {
        return target.equals(psiElement);
    }

    @NotNull
    @Override
    public Object[] getVariants()
    {
        return new String[] { target.getText() };
    }

    @Override
    public boolean isSoft() { return false; }
}
