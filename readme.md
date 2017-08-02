Roc Plugin
==========
This plugin Rocks! Hue, hue, hue!  
No but seriously, it does!

Installation
------------
Install as you would any IntelliJ-plugin.

Config-completions
------------------

    1. Open roc.config.js.
    2. Don't bother creating a newline, empty space or anything of the sort.
    3. Move cursor to applicable node, or not.
        - Cursor-position will filter available completions.
    4. Initiate completions (usually <ctrl-space>)
    5. Search completions by typing relevant letters.
    6. Press <enter> when ready.
    7. Be amazed of how great this experience was.
    8. Profit! 

Toolbar
-------
    1. The toolbar now has a cute little Roc-icon.
    2. Click it to start dev-mode.
    3. Think to yourself: "meh...".
    4. Submit your much better idea to me.

Gotchas
-------
If a newly installed package provides additional settings, you will need to reopen the project.  
Let me know if this ruins your day, and I'll fix it.

Next version
------------

- Remove all the "reference-junk" to a different branch.
- Determine how to provide user with information about setting.
    - Provide said information.
- Add code-comments. They are needed!
- Publish the damned thing.
- Add default-values by type as placeholders when there is no default-value.
- Move cursor to relevant places when applicable.
    - That should be easy... not!
- Auto-open dialogue for sub-completion-types.
    - Make sure they are wrapped in quotes.
- Provide user with feedback if completions can't be fetched.
    - Fire burn and cauldron bubble
- Move preloading to separate process
    - How did taskRunner work again?
- Honor preferred quotes and usage of trailing commas.
        
Future versions
---------------
- inspections: redundant setting, wrong type.
- Provide references/goto from import { something } from 'config';
    - Probably only need to reference base-elements.
        - And should not override existing references as this will break the spacetime-continuum.
- Notifications about new roc-versions with changelog.
- Toggle auto-refresh of browser-tab after dev-build.
- completions and goto for:       
    - getSettings()
    - Routes defined in configured middleware?
    - Redux-state/props/actions.
        - Is it even possible to resolve composite redux-state?
    - Yup... WebPack is on it's own node...
        - So... Where's that parsable documentation?
            
        