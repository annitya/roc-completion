package Completions;

import com.intellij.lang.javascript.psi.JSProperty;

import java.util.*;
import java.util.stream.Collectors;

public class SettingContainer
{
    static final String ROOT_NAMESPACE = "module.exports.settings";

    private List<SettingTreeNode> settings;
    private NavigableMap<String, Setting> flatList;

    public SettingContainer(List<SettingTreeNode> settings)
    {
        this.settings = settings;
        flatList = new TreeMap<>();

        populateFlatList(settings);
    }

    private void populateFlatList(List<SettingTreeNode> settings)
    {
        settings
            .forEach(treeNode -> treeNode.getObjects()
                .forEach(setting -> flatList.put(setting.getNamespace(), setting)));

        settings.forEach(treeNode -> populateFlatList(treeNode.getChildren()));
    }

    List<Setting> getSettings(String namespace, Map<String, JSProperty> existingSettings)
    {
        return flatList
            .subMap(namespace, namespace + Character.MAX_VALUE)
            .values()
            .stream()
            .filter(setting -> !existingSettings.containsKey(setting.getNamespace()))
            .collect(Collectors.toList());
    }
}
