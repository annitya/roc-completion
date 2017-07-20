package Goto;

import com.intellij.lang.Language;
import com.intellij.lang.javascript.psi.JSRecursiveWalkingElementVisitor;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiFile;

class ConfigWalker extends JSRecursiveWalkingElementVisitor
{
    final String propertyName;
    PsiElement match;

    ConfigWalker(String propertyName)
    {
        this.propertyName = propertyName;
    }

    static Boolean isJson(Language language)
    {
        return language.is(Language.findLanguageByID("JSON"));
    }

    static Boolean isJavaScript(Language language)
    {
        return language.isKindOf(Language.findLanguageByID("JavaScript"));
    }

    static ConfigWalker factory(PsiFile psiFile, String propertyName)
    {
        if (isJson(psiFile.getLanguage()))
        {
            return new RecursiveJsonConfigWalker(propertyName);
        }

        if (isJavaScript(psiFile.getLanguage()))
        {
            return new RecursiveConfigWalker(propertyName);
        }

        return null;
    }
}
