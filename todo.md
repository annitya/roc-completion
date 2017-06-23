Current
-------
- Completions for config-settings
    - Parse config-files (js/json)
    - Provide references from import { lulz } from 'config';
    - Probably only need to reference base-elements.

Todo
----
- Notifications about new versions.
- Toggle auto-refresh of browser-tab after dev-build.
- completions and goto for:
    - getSettings()
    - Routes defined in configured middleware
    - Redux-state/props/actions

Done, but not quite
-------------------

- Start "roc dev"
    - Put a name on the tab.
    - Where is my effin' icon?
    - Re-use existing terminal-tab.
    - Force NODE_ENV to development.

- completions for roc.config.js:
    - Remove existing values from suggestions.
    - Would goto be valuable?
    - Settings containing dashes must be quoted.