# Loading Progress Details

**English** | [中文](README_cn.md)

A client-side Fabric mod for **Minecraft 1.21.11** that shows detailed resource
reload progress right below the vanilla loading progress bar, while the Mojang
splash screen is displayed.

## Features

While a resource reload is running (startup or F3+T), two extra lines are drawn
under the progress bar:

- **Line 1** – current stage (`Preparing resources` / `Applying resources` /
  `Reload complete`), progress percentage matching the bar, and elapsed time.
- **Line 2** – prepared/applied task counts, plus the reloaders that are still
  pending (e.g. `model_loader`, `sprite_uploader`), so you can see exactly what
  is taking so long.

Reloaders registered through Fabric API show their Fabric id instead of an
obfuscated class name.

## Compatibility with RRLS

The mod is designed to coexist with
[RRLS (Remove Reloading Screen)](https://modrinth.com/mod/rrls):

- It injects at the tail of `SplashOverlay#renderProgressBar` and does not wrap,
  redirect or cancel any vanilla call, so it does not conflict with RRLS's own
  injections into the same method (which only recolor/re-lerp the bar itself).
- When RRLS is configured to skip the splash screen entirely, the dummy graphics
  it uses no-ops all draw calls, so this mod's text silently disappears instead
  of crashing.
- When RRLS is set to `PROGRESS` mode, the detail lines are drawn under RRLS's
  own progress bar as well.

## Building

```bash
./gradlew build
```

The built jar can be found in `build/libs/`.

## License

[MIT](LICENSE)
