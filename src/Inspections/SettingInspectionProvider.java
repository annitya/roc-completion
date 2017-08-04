package Inspections;

import Inspections.Redundant.RedundantSetting;
import com.intellij.codeInspection.InspectionToolProvider;
import org.jetbrains.annotations.NotNull;

public class SettingInspectionProvider implements InspectionToolProvider
{
    @NotNull
    @Override
    public Class[] getInspectionClasses()
    {
        return new Class[] {RedundantSetting.class};
    }
}
