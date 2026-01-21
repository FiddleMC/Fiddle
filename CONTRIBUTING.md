Thanks for wanting to contribute!

Fiddle is still not finished, and maybe things that you would like are missing.
If you would like to fix/add small things, feel free to make the desired changes and send in a PR.
If you are considering large changes, feel free to do the same, but you can always ask on Discord if ideas for that were already worked on.

### Code style

Generally, stick to the same style as Paper, with a few things to keep in mind:
* To stay agile, we try to keep changes to Minecraft/Paper files to a minimum. Try to create new classes under `src/main` rather than add fields and methods to Minecraft/Paper classes. An exception is creating inline fields instead of external maps (because of performance).
* Every package acts like a small separate module: they have a clear purpose and documentation (including a recognizable name) in their `package-info.java`.
* Changes to Minecraft/Paper files should have a `Fiddle - <module name> - <reason for this code change>` comment.
