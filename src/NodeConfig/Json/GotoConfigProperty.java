package NodeConfig.Json;

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler;
import com.intellij.json.psi.JsonProperty;
import com.intellij.lang.ecmascript6.psi.ES6FromClause;
import com.intellij.openapi.actionSystem.DataContext;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiReference;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.Collectors;

public class GotoConfigProperty implements GotoDeclarationHandler
{
    @Nullable
    @Override
    public PsiElement[] getGotoDeclarationTargets(@Nullable PsiElement psiElement, int offset, Editor editor)
    {
        if (psiElement == null)
        {
            return PsiElement.EMPTY_ARRAY;
        }

        ES6FromClause fromClause = PsiTreeUtil.getNextSiblingOfType(psiElement.getParent(), ES6FromClause.class);

        if (fromClause == null)
        {
            return PsiElement.EMPTY_ARRAY;
        }

        List<PsiReference> references = Arrays
            .stream(fromClause.getReferences())
            .filter(reference -> reference instanceof JsonConfigReference)
            .collect(Collectors.toList());

        List<JsonProperty> properties = new ArrayList<>();

        references
            .forEach(reference -> Arrays
                .stream(reference.getVariants())
                .filter(variant -> variant instanceof JsonProperty)
                .map(variant -> (JsonProperty)variant)
                .filter(property -> property.getName().equals(psiElement.getText()))
                .forEach(properties::add)
            );

        return properties.toArray(new PsiElement[0]);
    }

    @Nullable
    @Override
    public String getActionText(DataContext dataContext)
    {
        return "Goto config property.";
    }

}
