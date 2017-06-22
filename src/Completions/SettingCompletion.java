package Completions;

import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class SettingCompletion extends LookupElement {
    private String completion;

    SettingCompletion(ArrayList<String> parts)
    {
        this.completion = parts.get(2);
    }

    @NotNull
    @Override
    public String getLookupString()
    {
        String[] parts = completion.split("\\.");
        return parts[0];
    }

    @Override
    public void renderElement(LookupElementPresentation presentation)
    {
        String[] parts = completion.split("\\.");
        if (parts.length > 1) {
            presentation.appendTailText(completion.replaceFirst(parts[0], ""), true);
        }

        presentation.setItemText(completion);
        super.renderElement(presentation);
    }
}
