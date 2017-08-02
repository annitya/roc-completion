Current
-------

- Remove all the "reference-junk" to a different branch.
- Update readme.
- Determine how to provide user with information about setting.
    - Provide said information.
- Add comments. They are needed!
- Deploy the damned thing.
- Add default-values by type as placeholders when there is no default-value.
- Move cursor to relevant places if applicable.
- Auto-open dialogue for sub-completion-types.
    - Make sure they are wrapped in quotes.
- Provide user with feedback if completions can't be fetched.
- Move preloading to separate process?
    - How did taskRunner work again?
- Honor preferred quotes and usage of trailing commas.
- Add to readme.
    - If available settings changes, project must be reopened.
        
Todo
----
- inspections: redundant, wrong type.
- Provide references/goto from import { lulz } from 'config';
    - Probably only need to reference base-elements.
- Notifications about new roc-versions with changelog.
- Toggle auto-refresh of browser-tab after dev-build.
- completions and goto for:       
    - getSettings()
    - Routes defined in configured middleware?
    - Redux-state/props/actions.
        - Is it even possble to resolve composite redux-state?
    - Yup... WebPack is on it's own node...
        - So... Where's that parsable documentation?
            
        