package Inspections.Redundant;

import Completions.Entities.Setting;
import Framework.CompletionPreloader;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.lang.javascript.psi.JSRecursiveWalkingElementVisitor;
import java.util.ArrayList;
import java.util.List;

public class SettingVisitor extends JSRecursiveWalkingElementVisitor
{
    private ProblemsHolder holder;
    private List<String> done;

    SettingVisitor(ProblemsHolder holder)
    {
        this.holder = holder;
        done = new ArrayList<>();
    }

    private void inspectProperty(JSProperty node)
    {
        String namespace = node.getQualifiedName();

        done.add(namespace);

        Setting setting = CompletionPreloader
            .getCompletions()
            .getSetting(namespace);

        JSExpression nodeValue = node.getValue();

        if (setting == null || nodeValue == null)
        {
            return;
        }

        String value;
        // We don't want the quotes included.
        try
        {
            JSLiteralExpression literalExpression = (JSLiteralExpression)nodeValue;
            value = (String)literalExpression.getValue();
        }
        catch (Exception e)
        {
            value = nodeValue.getText();
        }

        String defaultValue = setting.getDefaultValue();

        if (value == null || !value.equals(defaultValue))
        {
            return;
        }

        holder.registerProblem(node, "Value equals default-value", new QuickFix());
    }

    @Override
    public void visitJSProperty(JSProperty node)
    {
        if (done.contains(node.getQualifiedName()))
        {
            return;
        }

        if (!CompletionPreloader.isRocConfigFile(node.getContainingFile()))
        {
            this.stopWalking();
            return;
        }

        inspectProperty(node);
    }
}
