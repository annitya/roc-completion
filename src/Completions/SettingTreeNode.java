package Completions;

import java.util.List;

public class SettingTreeNode
{
    private String name;
    private Integer level;
    private List<Setting> objects;
    private List<SettingTreeNode> children;
    private Boolean raw;

    List<Setting> getObjects() { return objects; }

    public List<SettingTreeNode> getChildren() { return children; }
}
