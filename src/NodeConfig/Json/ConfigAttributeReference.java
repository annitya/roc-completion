package NodeConfig.Json;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.json.psi.JsonProperty;
import com.intellij.lang.ecmascript6.psi.ES6FromClause;
import com.intellij.lang.ecmascript6.psi.ES6ImportDeclaration;
import com.intellij.psi.*;
import com.intellij.psi.search.FilenameIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.*;

public class ConfigAttributeReference extends PsiReferenceBase<PsiElement> implements PsiPolyVariantReference
{
    ConfigAttributeReference(@NotNull PsiElement element)
    {
        super(element);
    }

    @Nullable
    @Override
    public PsiElement resolve()
    {
        return null;
    }

    @NotNull
    @Override
    public Object[] getVariants()
    {
        return EMPTY_ARRAY;
    }

    private static JsonProperty castChild(Object child)
    {
        try
        {
            return (JsonProperty)child;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    @NotNull
    @Override
    public ResolveResult[] multiResolve(boolean incompleteCode)
    {
        List<ResolveResult> results = new ArrayList<>();

        ES6FromClause fromClause = PsiTreeUtil.getNextSiblingOfType(myElement, ES6FromClause.class);

        if (fromClause == null)
        {
            return ResolveResult.EMPTY_ARRAY;
        }

        Arrays
            .stream(fromClause.getReferences())
            .filter(reference -> reference instanceof JsonConfigReference)
            .map(PsiReference::getVariants)
            .map(Arrays::stream)
            .forEach(children -> children
                .map(ConfigAttributeReference::castChild)
                .filter(Objects::nonNull)
                .filter(child -> child.getName().equals(myElement.getText()))
                .map(PsiElementResolveResult::new)
                .forEach(results::add));

        return results.toArray(ResolveResult.EMPTY_ARRAY);
    }

}
