package References;

import com.intellij.patterns.StandardPatterns;
import com.intellij.psi.*;
import org.jetbrains.annotations.NotNull;

public class ConfigContributor extends PsiReferenceContributor
{
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar psiReferenceRegistrar)
    {
        ConfigReferenceProvider provider = new ConfigReferenceProvider();
        psiReferenceRegistrar.registerReferenceProvider(StandardPatterns.instanceOf(PsiElement.class), provider);
    }
}
