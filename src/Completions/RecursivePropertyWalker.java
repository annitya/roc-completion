package Completions;

import com.intellij.lang.javascript.psi.JSNamespace;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.lang.javascript.psi.JSRecursiveWalkingElementVisitor;

import java.util.ArrayList;

public class RecursivePropertyWalker extends JSRecursiveWalkingElementVisitor
{
    private ArrayList<JSProperty> existingProperties;
    private String namespace;

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
