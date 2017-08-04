package Inspections.Redundant;

import com.intellij.codeInspection.LocalQuickFix;
import com.intellij.codeInspection.ProblemDescriptor;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.openapi.project.Project;
import com.intellij.psi.PsiElement;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class QuickFix implements LocalQuickFix
{
    @Nls
    @NotNull
    @Override
    public String getFamilyName()
    {
        return "Remove setting";
    }

    @Override
    public void applyFix(@NotNull Project project, @NotNull ProblemDescriptor problemDescriptor)
    {
        PsiElement problemElement = problemDescriptor.getPsiElement();
        PsiElement parent = getParentProperty(problemElement);

        // If there are still other settings present, just remove this one.
        if (childCount(parent) > 1)
        {
            problemElement.delete();

            CodeStyleManager
                .getInstance(project)
                .adjustLineIndent(parent.getContainingFile(), parent.getTextRange());

            return;
        }

        // Navigate upwards until a node containing more than one setting is found.
        while (true)
        {
            PsiElement nextParent = getParentProperty(parent);

            if (nextParent == null || childCount(nextParent) != 1)
            {
                // Remove entire unused tree.
                parent.delete();
                return;
            }

            parent = nextParent;
        }
    }

    private Integer childCount(PsiElement psiElement)
    {
        // firstChild and getChildren()[0] is apparently not the same thing...
        PsiElement[] children = psiElement.getChildren();

        if (children.length == 0)
        {
            return 0;
        }

        return children[0]
            .getChildren()
            .length;
    }

    private JSProperty getParentProperty(PsiElement psiElement)
    {
        return PsiTreeUtil.getParentOfType(psiElement, JSProperty.class);
    }

}
