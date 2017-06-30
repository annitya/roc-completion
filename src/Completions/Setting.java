package Completions;

public class Setting
{
    static final String ROOT_NAMESPACE = "module.exports.settings";

    private String description;
    private String path;
    private String defaultValue;

    public Setting(String line)
    {
        String parts[] = line.split("\\|");

        if (parts.length != 7)
        {
            return;
        }

        description = parts[1].trim();
        path = parts[2].trim();

        String defaultValue = parts[3].trim();
        this.defaultValue = defaultValue.equals("No default value") ? "" : defaultValue;
    }

    String getDescription() { return description; }
    String getPath() { return path; }
    String getDefaultValue() { return defaultValue; }

    public Boolean isValid()
    {
        return
            description != null &&
            path != null &&
            defaultValue != null &&
            !description.equals("Description") &&
            path.contains(".");
    }

    Boolean isApplicable(String namespace)
    {
        String subNamespace = namespace.replaceFirst(Setting.ROOT_NAMESPACE, "");

        // Root-node, everything goes.
        if (subNamespace.length() == 0)
        {
            return true;
        }

        subNamespace = subNamespace.replaceFirst("\\.", "");
        return path.startsWith(subNamespace);
    }
}
