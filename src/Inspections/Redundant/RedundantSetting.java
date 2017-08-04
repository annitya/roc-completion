package Inspections.Redundant;

import com.intellij.codeInspection.LocalInspectionToolSession;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.javascript.inspections.JSInspection;
import com.intellij.psi.PsiElementVisitor;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;

public class RedundantSetting extends JSInspection
{
    @NotNull
    @Override
    protected PsiElementVisitor createVisitor(ProblemsHolder problemsHolder, LocalInspectionToolSession localInspectionToolSession)
    {
        return new SettingVisitor(problemsHolder);
    }

    @NotNull
    @Override
    public String[] getGroupPath()
    {
        return new String[]{"JavaScript", "Roc"};
    }

    @Nls
    @NotNull
    @Override
    public String getDisplayName()
    {
        return "Redeclaration of default-value.";
    }
}
