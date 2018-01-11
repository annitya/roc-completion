package NodeConfig.Json;

import NodeConfig.ConfigReference;
import com.intellij.codeInsight.completion.CompletionContributor;
import com.intellij.codeInsight.completion.CompletionParameters;
import com.intellij.codeInsight.completion.CompletionResultSet;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.json.psi.JsonProperty;
import com.intellij.lang.ecmascript6.psi.ES6FromClause;
import com.intellij.lang.ecmascript6.psi.ES6ImportDeclaration;
import com.intellij.lang.ecmascript6.psi.ES6ImportSpecifier;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;

public class ImportCompletionContributor extends CompletionContributor
{
    @Override
    public void fillCompletionVariants(@NotNull CompletionParameters parameters, @NotNull CompletionResultSet result)
    {
        PsiElement position = parameters
            .getPosition()
            .getParent();

        ES6FromClause fromClause = PsiTreeUtil.getNextSiblingOfType(position, ES6FromClause.class);

        if (fromClause == null)
        {
            return;
        }

        List<PsiReference> configReferences = Arrays
            .stream(fromClause.getReferences())
            .filter(reference -> reference instanceof ConfigReference)
            .collect(Collectors.toList());

        Map<String, LookupElement> completions = new HashMap<>();

        configReferences.forEach(psiReference -> Arrays
            .stream(psiReference.getVariants())
            .forEach(variant -> {
                JsonProperty property = (JsonProperty)variant;
                String name = property.getName();

                LookupElement lookupElement = LookupElementBuilder.create(name);
                completions.put(name, lookupElement);
            }));

        ES6ImportDeclaration importDeclaration = PsiTreeUtil.getParentOfType(position, ES6ImportDeclaration.class);
        ES6ImportSpecifier[] importSpecifiers = importDeclaration != null ? importDeclaration.getImportSpecifiers() : ES6ImportSpecifier.EMPTY_ARRAY;

        Arrays
            .stream(importSpecifiers)
            .forEach(importSpecifier -> {
                String name = importSpecifier.getDeclaredName();
                completions.remove(name);
            });
        result.addAllElements(completions.values());
    }
}
