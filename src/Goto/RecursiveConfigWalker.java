package Goto;

import com.intellij.lang.javascript.psi.JSAssignmentExpression;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.psi.PsiElement;

public class RecursiveConfigWalker extends ConfigWalker
{
    RecursiveConfigWalker(String propertyName) { super(propertyName); }

    @Override
    public void visitJSProperty(JSProperty node)
    {
        String nodeNamespace = node.getJSNamespace().toString();

        if (nodeNamespace.equals("ClientConfig") && node.getName().equals(propertyName))
        {
            match = node;
            stopWalking();
        }

        super.visitJSProperty(node);
    }

    @Override
    public void visitJSAssignmentExpression(JSAssignmentExpression node)
    {
        try {
            String assignment = node.getLOperand().getText();
            String[] parts = assignment.split("\\.");

            if (parts.length > 1 && parts[1].equals(propertyName))
            {
                match = node;
                stopWalking();
            }
        }
        catch (Exception ignored) {}
        finally
        {
            super.visitJSAssignmentExpression(node);
        }
    }
}
