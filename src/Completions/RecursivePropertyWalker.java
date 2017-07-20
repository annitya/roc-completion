package Completions;

import com.intellij.lang.javascript.psi.JSNamespace;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.lang.javascript.psi.JSRecursiveWalkingElementVisitor;

import java.util.ArrayList;

class RecursivePropertyWalker extends JSRecursiveWalkingElementVisitor
{
    private final ArrayList<JSProperty> existingProperties;
    private final String namespace;

    RecursivePropertyWalker(JSProperty parentProperty)
    {
        super();

        existingProperties = new ArrayList<>();
        namespace = parentProperty.getJSNamespace().toString();
    }

    ArrayList<JSProperty> getExistingProperties()
    {
        return existingProperties;
    }

    @Override
    public void visitJSProperty(JSProperty node)
    {
        JSNamespace jsNamespace = node.getJSNamespace();

        if (jsNamespace.toString().equals(namespace)) {
            existingProperties.add(node);
        }

        super.visitJSProperty(node);
    }
}
