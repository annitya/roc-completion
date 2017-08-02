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

If you for some reason miss your regular completions, feel free to initiate completions a second time  
while the dialogue is still visible. 

If you attempt to start Roc via the toolbar and the terminal-tab just flickers, doublecheck that you actually  
have Roc installed globally. Dumbass!  
How did I know that? Uh... nevermind that...

Known issues
------------
If the Roc-terminal is a bit sluggish to start, it will not be properly named.

First release
------------
- Determine how to provide user with information about setting.
    - Provide said information.
- Publish the damned thing.
- Add default-values by type as placeholders when there is no default-value.
- Move cursor to relevant places when applicable.
    - That should be easy... not!
- Auto-open dialogue for sub-completion-types.
    - Make sure they are wrapped in quotes.
- Honor preferred quotes.
- Only add trailing commas if enabled in CodeStyle.
        
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
            
        