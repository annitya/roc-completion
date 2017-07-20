package Goto;

import com.intellij.lang.javascript.psi.JSAssignmentExpression;
import com.intellij.lang.javascript.psi.JSProperty;

class RecursiveConfigWalker extends ConfigWalker
{
    RecursiveConfigWalker(String propertyName) { super(propertyName); }

    @Override
    public void visitJSProperty(JSProperty node)
    {
        String nodeNamespace = node.getJSNamespace().toString();
        String nodeName = node.getName() != null ? node.getName() : "";

        if (nodeNamespace.equals("ClientConfig") && nodeName.equals(propertyName))
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
