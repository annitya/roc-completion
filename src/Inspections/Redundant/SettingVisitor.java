package Inspections.Redundant;

import Completions.Entities.Setting;
import Framework.CompletionPreloader;
import com.intellij.codeInspection.ProblemsHolder;
import com.intellij.lang.javascript.psi.JSExpression;
import com.intellij.lang.javascript.psi.JSLiteralExpression;
import com.intellij.lang.javascript.psi.JSProperty;
import com.intellij.lang.javascript.psi.JSRecursiveWalkingElementVisitor;

import java.util.ArrayList;
import java.util.List;

public class SettingVisitor extends JSRecursiveWalkingElementVisitor
{
    private ProblemsHolder holder;
    private List<String> done;

    SettingVisitor(ProblemsHolder holder)
    {
        this.holder = holder;
        done = new ArrayList<>();
    }

    private void inspectProperty(JSProperty node)
    {
        String namespace = node.getQualifiedName();

        done.add(namespace);

        Setting setting = CompletionPreloader
            .getCompletions()
            .getSetting(namespace);

        JSExpression nodeValue = node.getValue();

        if (setting == null || nodeValue == null)
        {
            return;
        }

        String value;
        // We don't want the quotes included.
        try
        {
            JSLiteralExpression literalExpression = (JSLiteralExpression)nodeValue;
            value = (String)literalExpression.getValue();
        }
        catch (Exception e)
        {
            value = nodeValue.getText();
        }

        String defaultValue = setting.getDefaultValue();

        if (value == null)
        {
            return;
        }

        // Can't get hold of CodeStyleManager here because not dispatch. Manual labor ahead.
        List<String> valueCandidates = generateCandidates(value);
        List<String> defaultValueCandidates = generateCandidates(defaultValue);

        Boolean someSortOfMatch = false;

        for (String valueCandidate : valueCandidates)
        {
            if (defaultValueCandidates.contains(valueCandidate))
            {
                someSortOfMatch = true;
                break;
            }
        }

        if (!someSortOfMatch)
        {
            return;
        }

        holder.registerProblem(node, "Value equals default-value", new QuickFix());
    }

    // Good times!
    private List<String> generateCandidates(String value)
    {
        List<String> candidates = new ArrayList<>();

        String singleQuoteValue = value.replace("\"", "'");
        String singleQuoteNoSpaceValue = singleQuoteValue.replace(" ", "");
        String doubleQuoteValue = value.replace("'", "\"");
        String doubleQuoteNoSpaceValue = doubleQuoteValue.replace(" ", "");

        candidates.add(value);
        candidates.add(singleQuoteValue);
        candidates.add(singleQuoteNoSpaceValue);
        candidates.add(doubleQuoteValue);
        candidates.add(doubleQuoteNoSpaceValue);

        return candidates;
    }

    @Override
    public void visitJSProperty(JSProperty node)
    {
        if (done.contains(node.getQualifiedName()))
        {
            return;
        }

        if (!CompletionPreloader.isRocConfigFile(node.getContainingFile()))
        {
            this.stopWalking();
            return;
        }

        inspectProperty(node);
    }
}
