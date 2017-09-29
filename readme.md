Roc Plugin
==========
This plugin Rocks! Hue, hue, hue!  
No, but seriously, it does!

Installation
------------
Install as you would any JetBrains-plugin.

Supported products
------------------
IntelliJ IDEA, PhpStorm, WebStorm, PyCharm, RubyMine, AppCode, CLion, Gogland and Rider.

Config-completions
------------------
    1. Open roc.config.js.
    2. Don't bother creating a newline, empty space or anything of the sort.
    3. Move cursor to applicable node, or not. Cursor-position will filter available completions.
    4. The cursor must however be somewhere within the settings-property.
    5. Initiate completions (usually <ctrl-space>) or just start typing.
    6. Search completions by typing relevant letters.
    7. Press <enter> when ready.
    8. Be amazed of how great this experience was.
    9. Profit! 

Config-sub-completions
----------------------
    1. Did you perhaps select an enumeration?
    2. Expecto eneramtion-completions!
    2. You can also request autocomplete manually (<ctrl-space>).
    3. Don't worry if the list seems a bit short. Default-values has been removed.

Documentation
-------------   
    1. Request quick-documentation (usually <ctrl-j>)
    2. Know what the setting does.
    3. Live a year longer as a result of lowered stress-levels.

Toolbar - Start Roc
-------------------
    1. The toolbar now has a cute little Roc-icon.
    2. Click it to start dev-mode.
    3. Think to yourself: "meh...".
    4. Submit your much better idea to me.

Toolbar - Refresh completions
-----------------------------
    1. Despair when adding a new fancy plugin.
    2. Despair if roc.config.js had syntax-errors on project-load.
    3. Click the "refresh completions" icon.
    4. Despair no more.

Gotchas
-------
If you for some reason miss your regular completions, feel free to initiate completions a second time  
while the dialogue is still visible. 

When using js-config files IntelliJ will choose from the environment-candidates and select what it considers "the best one".
Any setting that solely exists in the other config-files will not be visible nor resolved.

If you attempt to start Roc via the toolbar and the terminal-tab just flickers, doublecheck that you actually  
have Roc installed globally. Dumbass!  

How did I know that? Uh... nevermind that...

Troubleshooting
---------------
    Try running the command: "node .idea/getSettings.js roc-config" from your project root-directory.
    Output should be pure parsable json.

Known issues
------------
Did autocomplete mess up your config? Make sure there is no syntax-errors before you attempt autocomplete.
If there are, the completion will hit all the branches of your ugly config-tree, and come out wrong.


In progress
-----------
- node-config:
    - Resolve 'config' module.
- Debugging: automatically add debugging for all environments.   
    - This should replace the run button.                      
Next version
------------
- Add tests!
- Unused config-settings inspection?
- Fix "missing dependency" inspection.

- html.head.htmlAttributes refuses to be formatted properly.
    - Bring in the Nashorn.
    - Testing of default-value seems pretty broken too.
        - Should I perhaps make use of parallel-processing to prepare a formatted-version of the default-value?
        - Maybe Nashorn can help here?

- Generate searchable documentation and show to the user.
    - for "roc docs"
    - and for available settings.

- debug
    - Add debug-runtime configuration-provider.
    - Make debugging easy as pie!    

- Inspections
    - Registered dependency already exported by Roc.
    - Wrong type for setting. 

- Add babel (object) and webpack (function or object) completions.
    - Json-schemas as are available as part of the JavaScriptLanguage.jar
    
- Notifications about new roc-versions with changelog.

- Toggle auto-refresh of browser-tab after dev-build.
    - Should be doable.
    - Is there a Chrome-cli utility available in Java?
    - How do I make a checkbox within the toolbar?
        - Just make an action and toggle the icon.
    - Persist between restarts?
        - Maybe add as a setting?
        
- Gather Roc-statistics. (config-settings, commands etc...)
    - Should be optional.

- Expose actions/hooks in a good way.
        
Future versions
---------------
- Special tab-color for you my friend?

- Add auto-completion for available babel/webpack settings.
    - Standalone plugin?
    - Take a look at the internal-configuration validation present in WebPack.

- node-config:
    - Resolve import { ... } from 'config'
        - LegacyResolver will cache config.js as target.
    - Separate plugin?
        - A shared library perhaps?       
                    
- completions and goto for:       
    - getSettings()
    - Routes defined in configured middleware?
    - Redux-state/props/actions.
        - Is it even possible to resolve composite redux-state?
    - Yup... WebPack is on it's own node...
        - So... Where's that parsable documentation?
                    
