package Completions;

import Completions.Entities.Setting;
import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.*;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.lang.javascript.formatter.JSCodeStyleSettings;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.openapi.editor.Editor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.CodeStyleManager;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.jetbrains.annotations.NotNull;
import java.util.ArrayList;
import java.util.Map;

class SettingLookupElement extends LookupElement
{
    private final String namespace;
    private final Setting setting;

    SettingLookupElement(Setting setting, String namespace)
    {
        this.setting = setting;
        this.namespace = namespace;
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
        // Remove the text the user typed.
        context
            .getDocument()
            .deleteString(context.getStartOffset(), context.getTailOffset());

        context.commitDocument();

        // Grab the deepest property available for insertion... hø hø hø!
        JSProperty targetProperty = getTargetProperty(context.getFile());
        // Generate the remaining jsNotation.
        String quote = JSCodeStyleSettings.getQuote(targetProperty);
        String jsNotation = setting.toRelativeJsNotation(targetProperty, quote);

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
        // User does not need to edit bools.
        if (setting.getType().equals("Boolean"))
        {
            return;
        }

        context.commitDocument();
        moveCursor(context);
    }

    private void moveCursor(InsertionContext context)
    {
        // Target will now resolve to newly inserted element.
        JSProperty insertedValue = getTargetProperty(context.getFile());

        if (insertedValue == null)
        {
            return;
        }

        PsiElement[] children = insertedValue.getChildren();

        if (children.length != 1)
        {
            return;
        }

        JSLiteralExpression expression;
        Object expressionValue;

        try
        {
            expression = (JSLiteralExpression)children[0];
            expressionValue = expression.getValue();
        }
        catch (Exception e)
        {
            // Default-value is missing for numeric.
            context
                .getEditor()
                .getCaretModel()
                .moveToOffset(children[0].getTextOffset() + 1);

            return;
        }

        if (expressionValue == null)
        {
            return;
        }

        String value = expressionValue.toString();
        // Don't need quote-offset for numerics.
        Integer quoteOffset = NumberUtils.isNumber(value) ? 0 : 1;

        Integer startOffset = expression.getTextOffset() + quoteOffset;
        Editor editor = context.getEditor();

        editor
            .getCaretModel()
            .moveToOffset(startOffset);

        Integer endOffset = startOffset + value.length();

        editor
            .getSelectionModel()
            .setSelection(startOffset, endOffset);
    }
}
