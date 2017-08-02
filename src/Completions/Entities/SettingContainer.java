package Completions.Entities;

import com.intellij.lang.javascript.psi.JSProperty;
import java.util.*;
import java.util.stream.Collectors;

public class SettingContainer
{
    public static final String ROOT_NAMESPACE = "module.exports.settings";

    private NavigableMap<String, Setting> flatList;

    public SettingContainer() {}

    public SettingContainer(List<SettingTreeNode> settings)
    {
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

    public List<Setting> getSettings(String namespace, Map<String, JSProperty> existingSettings)
    {
        return flatList
            .subMap(namespace, namespace + Character.MAX_VALUE)
            .values()
            .stream()
            .filter(setting -> !existingSettings.containsKey(setting.getNamespace()))
            .collect(Collectors.toList());
    }
}
