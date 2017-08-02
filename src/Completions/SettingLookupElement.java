package Completions;

import Completions.Entities.Setting;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.apache.commons.lang.ArrayUtils;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Map;

class SettingLookupElement extends LookupElement
{
    private final String namespace;
    private final Setting setting;
    private final String originalSettings;

    SettingLookupElement(Setting setting, String namespace, String originalSettings)
    {
        this.setting = setting;
        this.namespace = namespace;
        this.originalSettings = originalSettings;
    }

    @NotNull
    @Override
    public String getLookupString()
    {
        return setting
            .getNamespace()
            .replaceFirst(namespace + ".", "");
    }

    @Override
    public void renderElement(LookupElementPresentation presentation)
    {
        presentation.setTypeText(setting.getType());
        presentation.setItemText(getLookupString());

        String defaultValue = setting.getDefaultValue();

        if (defaultValue.length() != 0)
        {
            presentation.appendTailTextItalic(" (" + defaultValue + ") ", true);
        }

        super.renderElement(presentation);
    }

    private JSProperty getTargetProperty(PsiFile configFile)
    {
        // Braaaaaaiiins!!!
        RecursivePropertyWalker walker = new RecursivePropertyWalker();
        walker.visitFile(configFile);

        Map<String, JSProperty> existingProperties = walker.getExistingProperties();
        ArrayList<String> parts = Lists.newArrayList(setting.getNamespace().split("\\."));

        JSProperty property;

        // Begin at the bottom and locate the nearest property.
        while (true)
        {
            String propertyNamespace = String.join(".", parts);
            property = existingProperties.get(propertyNamespace);

            if (property != null || parts.size() == 0)
            {
                break;
            }

            parts.remove(parts.size() - 1);
        }

        return property;
    }

    @Override
    public void handleInsert(InsertionContext context)
    {
        // Revert document to original state... good times.
        // This was actually the easiest way to remove any text the user entered.
        context.getDocument().setText(originalSettings);
        context.commitDocument();

        // Grab the deepest property available for insertion...
        JSProperty targetProperty = getTargetProperty(context.getFile());
        // Generate the remaining jsNotation.
        String jsNotation = setting.toRelativeJsNotation(targetProperty);

        // Create a new file to initiate all lexers, parsers grumpkins and snarks...
        PsiFile psiContainer = PsiFileFactory
            .getInstance(context.getProject())
            .createFileFromText(targetProperty.getLanguage(), jsNotation);

        JSExpression value = targetProperty.getValue();

        if (value == null)
        {
            return;
        }

        PsiElement[] childrenForInsertion = psiContainer.getChildren();
        // Insert in reverse order. Probably an easier way, but this works just fine...
        ArrayUtils.reverse(childrenForInsertion);

        // Grab all created elements in the temporary file and insert them into the document.
        for (PsiElement completion : childrenForInsertion)
        {
            value.addAfter(completion, value.getFirstChild());
        }

        PsiElement formattedValue = CodeStyleManager
            .getInstance(context.getProject())
            .reformat(value);

        value.replace(formattedValue);
    }
}
