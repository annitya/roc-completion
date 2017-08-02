package Completions.Entities;

import java.util.List;

@SuppressWarnings("unused")
public class SettingTreeNode
{
    private String name;
    private Integer level;
    private List<Setting> objects;
    private List<SettingTreeNode> children;
    private Boolean raw;

    List<Setting> getObjects() { return objects; }

    List<SettingTreeNode> getChildren() { return children; }
}
