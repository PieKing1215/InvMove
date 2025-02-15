<h1>InvMove<br>
  <a href="https://modrinth.com/mod/invmove"><img src="http://cf.way2muchnoise.eu/versions/%20For%20MC%20_581854_all(555-0C8E8E-fff-010101).svg" alt="Supported Versions"></a>
  <a href="https://github.com/PieKing1215/InvMove/blob/master/COPYING"><img src="https://img.shields.io/github/license/PieKing1215/InvMove?style=flat&color=0C8E8E" alt="License"></a>
  <a href="https://modrinth.com/mod/invmove"><img src="https://img.shields.io/modrinth/dt/REfW2AEX?label=Modrinth downloads&logo=modrinth" alt="Modrinth Download Count"></a>
  <!-- <a href="https://www.curseforge.com/minecraft/mc-mods/invmove"><img src="http://cf.way2muchnoise.eu/full_581854_downloads(E04E14-555-fff-010101-1C1C1C).svg" alt="CF Download Count"></a> -->
  <a href="https://ko-fi.com/X8X34Y6MZ"><img src="https://ko-fi.com/img/githubbutton_sm.svg" alt="Donate on ko-fi" width="160px"></a>
</h1>

### Minecraft Fabric/NeoForge mod that adds the ability to walk around while in inventories

<table>
<tr>
  <td>InvMove</td>
  <td><a href="https://github.com/PieKing1215/InvMove">GitHub</a></td>
  <td><a href="https://modrinth.com/mod/invmove">Modrinth</a></td>
  <td><a href="https://www.curseforge.com/minecraft/mc-mods/invmove">CurseForge</a> (Mirror)</td>
</tr>
<tr>
  <td>InvMoveCompats</td>
  <td><a href="https://github.com/PieKing1215/InvMoveCompats">GitHub</a></td>
  <td><a href="https://modrinth.com/mod/invmovecompats">Modrinth</a></td>
  <td><a href="https://www.curseforge.com/minecraft/mc-mods/invmovecompats">CurseForge</a> (Mirror)</td>
</tr>
</table>

![demo.gif](https://raw.githubusercontent.com/PieKing1215/InvMove/media/demo.gif)

Enables moving, jumping, sprinting, etc. from within inventories/screens.

Also hides the darkened background tint in screens that don't pause the game.

Both features can be toggled on or off per-screen (including modded ones) in the config menu.

Additional mod compatibilities (REI/JEI/EMI) have been moved into an addon mod: [InvMoveCompats](https://github.com/PieKing1215/InvMoveCompats)

This mod is client-side, but it may raise alarms if used on servers with anticheat.<br>
I take no responsibility if you get banned or something because you used this on public servers.

### [Releases](https://github.com/PieKing1215/InvMove/releases)

Requires [Cloth Config](https://modrinth.com/mod/cloth-config) on all mod loaders.<br/>
Requires [Fabric API](https://modrinth.com/mod/fabric-api) on Fabric.<br/>
For Fabric, you need [Mod Menu](https://modrinth.com/mod/modmenu) to be able to open the config screen.<br/>
For (Neo)Forge, I also recommend [Game Menu Mod Option](https://modrinth.com/mod/gamemenumodoption) so you can change settings in-game.

## Config (In-game)

#### General:
- `Enable Mod`: Enable the entire mod
- `Debug Display`: Enables a debug overlay that can help debug compatibility problems.

#### UI Movement:
- `Move In Inventories`: Enable movement in inventories<br>
  There is a keybind you can set to toggle this setting (unbound by default)
- `Allow Jumping`: Allow jumping in inventories
- `Sneak Mode`: How to handle sneaking in inventories:
  - `Off` = No sneaking
  - `Maintain` = Keep sneaking if you were when the inventory opened (default)
  - `Pressed` = Sneak only while holding the sneak button (can be distracting when shift-clicking)
- `Allow Dismounting`: Allow dismounting from mounts in inventories (overrides "Sneak Mode" while on a mount)
- `Text Field Disables Movement`: Disable movement when a text field is focused (like search bars or in an anvil)
- `Unrecognized Screens Default`: Sets the default movement mode for unrecognized screens.
- `Allowed Keys`: Configure what keys InvMove will force update inside inventories.
  - Defaults to just the vanilla movement keys to be safer for mod compatibility.

#### UI Background:
- `Hide Inventory Backgrounds`: Hides the background tint while in inventories.
- `Pause Screens Background`: How to handle screens that pause the game:
  - `Show` = Always show background
  - `ShowSP` = Show background in singleplayer, otherwise allow hide
  - `AllowHide` = Allow background to be hidden
- `Unrecognized Screens Default`: Sets the default background mode for unrecognized screens.

The Movement and Background pages also have expandable categories containing toggles for individual inventory types.<br>
The base mod has a category for vanilla screens, as well as a section for `Unrecognized UI Types` which are automatically grouped by modid.<br/>
For mod compatibilities that can't be solved with this system (REI/JEI/EMI), see [InvMoveCompats](https://github.com/PieKing1215/InvMoveCompats)<br/>

## Usage

Feel free to use in packs if you wish.

The only official downloads are from the InvMove GitHub, Modrinth, or CurseForge page.<br/>
Be careful downloading them from elsewhere, as unauthorized reposts are not monitored and could contain malware.<br/>
(If you are interested in the mod being added to another platform, please open an issue!)

The mod is licensed under the [GNU Lesser General Public License v3.0](https://github.com/PieKing1215/InvMove/blob/master/COPYING)

