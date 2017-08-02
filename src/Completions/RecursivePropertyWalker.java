package Completions;

import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.lang.javascript.psi.JSRecursiveWalkingElementVisitor;
import java.util.HashMap;
import java.util.Map;

class RecursivePropertyWalker extends JSRecursiveWalkingElementVisitor
{
    private final Map<String, JSProperty> existingProperties = new HashMap<>();

    Map<String, JSProperty> getExistingProperties()
    {
        return existingProperties;
    }

    @Override
    public void visitJSProperty(JSProperty node)
    {
        existingProperties.put(node.getQualifiedName(), node);
        super.visitJSProperty(node);
    }
}
