package NodeConfig.Json;

import NodeConfig.ConfigReference;
import com.intellij.codeInsight.lookup.LookupElementBuilder;
import com.intellij.json.psi.JsonProperty;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JsonConfigReference extends ConfigReference
{
    public JsonConfigReference(@NotNull PsiElement element, PsiFile targetFile)
    {
        super(element, targetFile);
    }

    @NotNull
    @Override
    public Object[] getVariants()
    {
        JsonProperty[] properties = PsiTreeUtil.getChildrenOfType(configFile.getFirstChild(), JsonProperty.class);

        if (properties == null)
        {
            return EMPTY_ARRAY;
        }

        return Arrays
            .stream(properties)
            .collect(Collectors.toList())
            .toArray();
    }
}
