<h1>InvMove<br>
  <a href="https://www.curseforge.com/minecraft/mc-mods/invmove"><img src="http://cf.way2muchnoise.eu/versions/%20For%20MC%20_581854_all(555-0C8E8E-fff-010101).svg" alt="Supported Versions"></a>
  <a href="https://github.com/PieKing1215/InvMove/blob/master/COPYING"><img src="https://img.shields.io/github/license/PieKing1215/InvMove?style=flat&color=0C8E8E" alt="License"></a>
  <a href="https://www.curseforge.com/minecraft/mc-mods/invmove"><img src="http://cf.way2muchnoise.eu/full_581854_downloads(E04E14-555-fff-010101-1C1C1C).svg" alt="CF Download Count"></a>
  <a href="https://modrinth.com/mod/invmove"><img src="https://modrinth-utils.vercel.app/api/badge/downloads?id=REfW2AEX&logo=true" alt="Modrinth Download Count"></a>
  <a href="https://ko-fi.com/X8X34Y6MZ"><img src="https://ko-fi.com/img/githubbutton_sm.svg" alt="Donate on ko-fi" width="160px"></a>
</h1>

### Minecraft Forge/Fabric mod that adds the ability to walk around while in inventories

<table>
<tr>
  <td><a href="https://github.com/PieKing1215/InvMove">GitHub</a></td>
  <td><a href="https://www.curseforge.com/minecraft/mc-mods/invmove">CurseForge</a></td>
  <td><a href="https://modrinth.com/mod/invmove">Modrinth</a></td>
</tr>
</table>

This is a rewrite of the previously separate Forge and Fabric versions.<br>
Mod compatibilities have been moved into an addon mod: [InvMoveCompats](https://github.com/PieKing1215/InvMoveCompats)

![demo.gif](https://raw.githubusercontent.com/PieKing1215/InvMove/media/demo.gif)

Enables moving, jumping, sprinting, etc. from within inventories.

Also hides the darkened background tint in inventories that don't pause the game.

Both features can be toggled on or off per-inventory in the config menu.

This mod is client-side, but it may raise alarms if used on servers with anticheat.<br>
I take no responsibility if you get banned or something because you used this on public servers.

### [Releases](https://github.com/PieKing1215/InvMove/releases)

Requires Cloth Config ([Forge](https://www.curseforge.com/minecraft/mc-mods/cloth-config-forge)/[Fabric](https://www.curseforge.com/minecraft/mc-mods/cloth-config)).
For Fabric, you need [Mod Menu](https://www.curseforge.com/minecraft/mc-mods/modmenu) to be able to open the config screen.

## Config (In-game)

#### General:
- Enable Mod: Enable the entire mod
- Debug Display: Enables a debug overlay that can help debug compatibility problems.

#### UI Movement:
- Move In Inventories: Enable movement in inventories
- Allow Jumping: Allow jumping in inventories
- Allow Sneaking: Allow sneaking in inventories (disabled by default because it's distracting when shift-clicking)
- Allow Dismounting: Allow dismounting from mounts in inventories (overrides "Allow Sneaking" while on a mount)
- Text Field Disables Movement: Disable movement when a text field is focused (like search bars or in an anvil)

#### UI Background:
- Hide Inventory Backgrounds: Hides the background tint while in inventories.

The Movement and Background pages also have expandable categories containing toggles for individual inventory types.<br>
The base mod only has a category for vanilla inventories. For more mod compatibilities, see [InvMoveCompats](https://github.com/PieKing1215/InvMoveCompats)<br>
Any screens not handled by installed mod compatibilities are added to the "Unrecognized UI Types" section, grouped by modid.

## Usage

Feel free to use in packs if you wish.

The only official downloads are from the InvMove GitHub, Curseforge, or Modrinth page.<br>
Be careful downloading them from elsewhere, as unauthorized reposts are not monitored and could contain malware.<br>
(If you are interested in the mod being added to another platform, please open an issue!)

The mod is licensed under the [GNU Lesser General Public License v3.0](https://github.com/PieKing1215/InvMove-Fabric/blob/master/COPYING)
test2
