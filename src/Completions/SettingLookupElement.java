package Completions;

import com.google.common.collect.Lists;
import com.intellij.codeInsight.completion.InsertionContext;
import com.intellij.codeInsight.lookup.LookupElement;
import com.intellij.codeInsight.lookup.LookupElementPresentation;
import com.intellij.lang.PsiBuilderFactory;
import com.intellij.lang.impl.PsiBuilderFactoryImpl;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;
import com.intellij.psi.PsiFileFactory;
import com.intellij.psi.codeStyle.CodeStyleManager;
import com.intellij.psi.util.PsiUtilBase;
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
        context.getDocument().setText(originalSettings);
        context.commitDocument();

        PsiElement currentElement = PsiUtilBase.getElementAtCaret(context.getEditor());

        // Uh...
        if (currentElement == null)
        {
            return;
        }

        JSProperty targetProperty = getTargetProperty(context.getFile());
        String json = setting.toRelativeJsNotation(targetProperty);

        PsiFile psiContainer = PsiFileFactory
            .getInstance(context.getProject())
            .createFileFromText(targetProperty.getLanguage(), json);

        JSExpression value = targetProperty.getValue();

        if (value == null)
        {
            return;
        }

        PsiElement[] childrenForInsertion = psiContainer.getChildren();
        ArrayUtils.reverse(childrenForInsertion);

        for (PsiElement completion : childrenForInsertion)
        {
            value.addAfter(completion, value.getFirstChild());
        }

        PsiElement formattedValue = CodeStyleManager
            .getInstance(context.getProject())
            .reformat(value);

        value.replace(formattedValue);

        // Like O M G!!!
//        PsiFile commaContainer = PsiFileFactory
//            .getInstance(context.getProject())
 //           .createFileFromText(targetProperty.getLanguage(), ",");

  //      if (!json.startsWith(setting.getName()))
   //     {
    //        insertedElement.addAfter(commaContainer.getLastChild(), insertedElement.getLastChild());
     //   }

    }
}
