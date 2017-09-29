package NodeConfig.Json;

import com.intellij.json.psi.impl.JsonPropertyImpl;
import com.intellij.lang.ASTNode;
import com.intellij.navigation.ItemPresentation;
import org.jetbrains.annotations.Nullable;

public class PresentableProperty extends JsonPropertyImpl
{
    PresentableProperty(ASTNode node)
    {
        super(node);
    }

    @Nullable
    @Override
    public ItemPresentation getPresentation()
    {
        return new GotoPresentation(this);
    }
}
