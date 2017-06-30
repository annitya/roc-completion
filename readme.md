Current
-------
- Provide references/goto from import { lulz } from 'config';
    - Probably only need to reference base-elements.

Todo
----
- Notifications about new versions.
- Toggle auto-refresh of browser-tab after dev-build.
- completions and goto for:
    - getSettings()
    - Routes defined in configured middleware?
    - Redux-state/props/actions.
        - Is it even possble to resolve composite redux-state?

Done, but not quite
-------------------
- completions for roc.config.js:
    - If settings changes, project must be reopened.
        - Add to readme.
    - Yup... WebPack is on it's own node...
    - Automatically expand complete-node.
        - Can fetch json with info about complete structure.
            - get js-snippet which achieves this from Gustaf.
        - Change filtering mechanism to show unused sub-nodes.