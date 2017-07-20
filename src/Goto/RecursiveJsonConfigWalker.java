package Goto;

import com.intellij.json.psi.JsonFile;
import com.intellij.json.psi.JsonProperty;
import com.intellij.psi.PsiElement;

class RecursiveJsonConfigWalker extends ConfigWalker
{
    RecursiveJsonConfigWalker(String propertyName) { super(propertyName); }

    @Override
    public void visitElement(PsiElement psiElement)
    {
        try {
            JsonProperty property = (JsonProperty)psiElement;
            PsiElement grandParent = psiElement.getParent().getParent();

            Boolean levelMatches = grandParent instanceof JsonFile;
            Boolean propertyMatches = property
                .getName()
                .equals(propertyName);

            if (levelMatches && propertyMatches)
            {
                match = psiElement;
                stopWalking();
            }
        }
        catch (Exception ignored) {}
        finally
        {
            super.visitElement(psiElement);
        }
    }
}
