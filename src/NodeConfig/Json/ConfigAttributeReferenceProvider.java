package NodeConfig.Json;

import com.intellij.lang.ecmascript6.psi.ES6FromClause;
import com.intellij.lang.ecmascript6.psi.ES6ImportDeclaration;
import com.intellij.lang.ecmascript6.psi.ES6ImportSpecifier;
import com.intellij.lang.javascript.psi.JSReferenceExpression;
import com.intellij.patterns.PlatformPatterns;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.ProcessingContext;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.Objects;

public class ConfigAttributeReferenceProvider extends PsiReferenceContributor
{
    @Override
    public void registerReferenceProviders(@NotNull PsiReferenceRegistrar registrar)
    {
        registrar.registerReferenceProvider(PlatformPatterns.psiElement(JSReferenceExpression.class), new AttributeReferenceProvider());
    }
}

class AttributeReferenceProvider extends PsiReferenceProvider
{
    private static Boolean isConfigImport(ES6ImportDeclaration importDeclaration)
    {
        ES6FromClause importFromClause = importDeclaration.getFromClause();
        PsiReference importReference = importFromClause != null ? importFromClause.getReference() : null;
        String moduleName = importReference != null ? importReference.getCanonicalText() : "";

        return moduleName.equals("config");
    }

    private static ES6ImportSpecifier getCorrectImport(PsiElement element, ES6ImportSpecifier importSpecifier)
    {
        String declaredName = importSpecifier.getDeclaredName();
        Boolean isCorrectSpecifier = declaredName != null && declaredName.equals(element.getText());

        return isCorrectSpecifier ? importSpecifier : null;
    }

    @NotNull
    @Override
    public PsiReference[] getReferencesByElement(@NotNull PsiElement element, @NotNull ProcessingContext context)
    {
        ES6ImportDeclaration[] importDeclarations = PsiTreeUtil.getChildrenOfType(element.getContainingFile(), ES6ImportDeclaration.class);

        if (importDeclarations == null)
        {
            return PsiReference.EMPTY_ARRAY;
        }

        return Arrays
            .stream(importDeclarations)
            .filter(AttributeReferenceProvider::isConfigImport)
            .map(ES6ImportDeclaration::getImportSpecifiers)
            .map(Arrays::stream)
            .map(importSpecifiers -> importSpecifiers
                .map(specifier -> AttributeReferenceProvider.getCorrectImport(element, specifier))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null)
            )
            .filter(Objects::nonNull)
            .map(ConfigAttributeReference::new)
            .toArray(PsiReference[]::new);
    }
}
