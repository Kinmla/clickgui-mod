# ClickGUI Style Mod

A client-side Fabric mod for Minecraft 1.21.11. Press **Right Shift** in-game
to open a panel-style settings screen: dark rounded columns, category
headers, toggle switches, and sliders — same visual language as the
"ClickGUI" style you see in some client mods, but with **no actual
functionality wired in**. Every entry is a harmless placeholder
(`Module` objects) that just flips a boolean or moves a slider — there's
no combat/movement/render cheating logic anywhere in this project.

## What's included

- `ClickGuiScreen` — draws the panels, headers, toggles and sliders, and
  handles clicking/dragging.
- `Category` / `Module` — plain data holders. `Module.enabled` and
  `Module.actualSliderValue()` are the only "state" — nothing reads them
  to affect gameplay.
- A keybinding (default **Right Shift**) that opens the screen when no
  other screen is open.

Rename the categories/modules in `ClickGuiScreen.buildLayout()` to
whatever you like, restyle colors via the constants at the top of that
file, and hook `Module.enabled` up to real features if you build any —
just make sure whatever you add stays within your server's rules.

## Requirements

- Java 21
- [Fabric Loader](https://fabricmc.net/) 0.16.10+
- [Fabric API](https://modrinth.com/mod/fabric-api) matching Minecraft 1.21.11

## First-time setup

This project doesn't ship the Gradle wrapper binary (`gradle-wrapper.jar`)
since it's a downloaded binary file. Generate it once, locally, with a
system install of Gradle:

```bash
gradle wrapper --gradle-version 8.10
```

After that, use `./gradlew` (or `gradlew.bat` on Windows) for everything
below. Loom will download Minecraft, mappings, and Fabric API on first run.

## Building

```bash
./gradlew build
```

The output jar will be in `build/libs/`.

If the build fails on the mappings/loader versions, open
https://fabricmc.net/develop/, select Minecraft `1.21.11`, and copy the
exact `yarn_mappings` / `loader_version` numbers into `gradle.properties`
— Fabric updates these frequently and the values baked into this project
may drift out of date.

## Running in a dev environment

```bash
./gradlew runClient
```

## Installing the built jar

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.21.11.
2. Drop `fabric-api-*.jar` and this mod's jar into your `.minecraft/mods` folder.
3. Launch the Fabric profile, join a world, and press **Right Shift**.
