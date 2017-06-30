Current
-------


Todo
----
- Provide references/goto from import { lulz } from 'config';
    - Probably only need to reference base-elements.
- Notifications about new versions.
- Toggle auto-refresh of browser-tab after dev-build.
- completions and goto for:
    - getSettings()
    - Routes defined in configured middleware?
    - Redux-state/props/actions

Done, but not quite
-------------------

- Start "roc dev"
    - Put a name on the tab.
    - Where is my effin' icon?
    - Re-use existing terminal-tab.
    - Force NODE_ENV to development.

- completions for roc.config.js:
    - If settings changes, project must be reopened.
        - Add to readme.
    - Yup... WebPack is on it's own node...
    - Automatically expand complete-node.
        - Can fetch json with info about complete structure.
            - get js-snippet which achieves this from Gustaf.
        - Change filtering mechanism to show unused sub-nodes.