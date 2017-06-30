package Completions;

import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.util.PsiUtilBase;
import org.jetbrains.annotations.NotNull;

public class SettingLookupElement extends LookupElement
{
    private String completion;
    private String lookupString;
    private String defaultValue;
    private String tailText;

    SettingLookupElement(Setting setting, String namespace)
    {
        completion = setting.getPath();

        namespace = namespace.replaceFirst(Setting.ROOT_NAMESPACE + "\\.", "");
        String currentCompletion = completion.replaceFirst(namespace + "\\.", "");
        String[] parts = currentCompletion.split("\\.");

        lookupString = parts[0];
        tailText = currentCompletion.replaceFirst(lookupString, "");
        defaultValue = setting.getDefaultValue();
    }

    @NotNull
    @Override
    public String getLookupString()
    {
        return lookupString;
    }

    @Override
    public void renderElement(LookupElementPresentation presentation)
    {
        if (tailText != null)
        {
            presentation.appendTailText(tailText, true);
        }

        if (defaultValue.length() != 0)
        {
            presentation.appendTailTextItalic(" (" + defaultValue + ") ", true);
        }

        presentation.setItemText(completion);
        super.renderElement(presentation);
    }

    @Override
    public void handleInsert(InsertionContext context)
    {
        PsiElement currentElement = PsiUtilBase.getElementAtCaret(context.getEditor());

        // Uh...
        if (currentElement == null) {
            return;
        }

        PsiElement insertedElement = currentElement.getPrevSibling();

        // Okay...
        if (insertedElement == null)
        {
            return;
        }

        // Nothing to do here...
        if (!insertedElement.getText().contains("-"))
        {
            return;
        }

        String wrappedProperty = "'" + insertedElement.getText() + "'";

        PsiFile psiContainer = PsiFileFactory
            .getInstance(context.getProject())
            .createFileFromText(insertedElement.getLanguage(), wrappedProperty);

        insertedElement.replace(psiContainer.getFirstChild());
    }
}
