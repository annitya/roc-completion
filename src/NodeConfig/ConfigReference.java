package NodeConfig;

import com.intellij.openapi.util.TextRange;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ConfigReference extends PsiReferenceBase<PsiElement>
{
    protected PsiFile configFile;

    public ConfigReference(@NotNull PsiElement element, PsiFile targetFile)
    {
        super(element);
        configFile = targetFile;
    }

    @Nullable
    @Override
    public PsiElement resolve()
    {
        return configFile;
    }

    @NotNull
    @Override
    public Object[] getVariants() { return EMPTY_ARRAY; }

    @Override
    public TextRange getRangeInElement()
    {
        return myElement
            .getFirstChild()
            .getNextSibling()
            .getNextSibling()
            .getTextRange();
    }
}
