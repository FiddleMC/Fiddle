
<div align="center">

  <img src="logo.png" alt="Gale logo" width="21%" align="right">
  <h1>
    Fiddle
  </h1>
  <h3>
    A Paper server fork that lets you
    <br>
    add custom blocks and items
  </h3>

  [![Discord](https://img.shields.io/discord/1091830813240348732?color=5865F2&label=discord&style=for-the-badge)](https://discord.gg/EduvcVmKS7)

</div>

<table>
  <tr>
    <td>
      <img src="https://media.discordapp.net/attachments/730768222298701824/1004518312295800853/6_fire.png">
    </td>
    <td>
      <img src="https://media.discordapp.net/attachments/730768222298701824/1004518309028450344/1_bookshelves.png">
    </td>
    <td>
      <img src="https://media.discordapp.net/attachments/730768222298701824/1004518310123143268/3_stone.png">
    </td>
  </tr>
  <tr>
    <td>
      <img src="https://media.discordapp.net/attachments/849617367214587924/994942217678487602/2022-07-08_14.16.47.png">
    </td>
    <td>
      <img src="https://media.discordapp.net/attachments/730768222298701824/1004518313247907880/8_newblocks.png">
    </td>
    <td>
      <img src="https://media.discordapp.net/attachments/730768222298701824/1004518314548142100/10_concrete.png">
    </td>
  </tr>
</table>

<div align="center">
  <i>
    Pictures from the version currently running live on the server ip <b>sucraft.org</b>
  </i>
</div>

## Introduction

Fiddle is a Paper server fork that lets you add new blocks and items into Minecraft.

Fiddle:
* works with (existing) Bukkit plugins[^1]
* adds blocks and items into the game in the same way a Minecraft update would
* works with the vanilla client, both with/without a resource pack, and also offers a client-side mod with extra performance
* modded blocks and items keep working after Minecraft updates
* adding new blocks and items is simple using the standard resource and data pack format
* allows Bukkit plugins to include custom blocks and items when used on a Fiddle server

## Installation

Fiddle is a drop-in replacement for Paper.\
You can download the latest stable JAR from [releases](https://github.com/FiddleMC/Fiddle/releases) and the latest development JAR from [actions](https://github.com/FiddleMC/Fiddle/actions).

After running Fiddle once, you must open `fiddle.txt`, read the warning carefully, and set `modded=true`.

<div align="center">
  <table>
    <tr>
      <td valign="center">
        <h1>🔨</h1>
      </td>
      <td valign="center">
        Note: Fiddle is in active development.
        <br>
        Some of the information below may be outdated or incorrect.
        <br>
        Do <b>NOT</b> run Fiddle on a production server!
      </td>
    </tr>
  </table>
</div>

[^1]: Some older plugins have still not updated to use 1.13+ namespaced ids. These plugins can still be used on Fiddle, but require having [backwards compatibility mode](https://github.com/FiddleMC/Fiddle/wiki/Plugin-compatibility#backwards-compatibility) enabled.

## Adding blocks/items

Modded blocks and items are defined in a pack (similar to a resource or data pack). 
You can download packs made by others, or [create your own packs](https://github.com/FiddleMC/Fiddle/wiki/Making-packs) and share them.

A pack is a `.zip` or `.rar` file. Packs can be placed in the `fiddle_packs` folder in the server root.\
*Example location:* `fiddle_packs/WillowTrees.zip`

Bukkit plugins can also include custom blocks and items when used on a Fiddle server.

## Demo

An experimental version of Fiddle is already running on a small community server.
You can join *sucraft.org* and do `/warp Demo` to see it in action yourself.



You are very welcome to join the project by adding code parts, testing, sharing knowledge or giving suggestions.

## Goals of the project

### Supports any player

* A client mod that supports the custom blocks and items directly
* A resource pack that allows vanilla clients to see the custom blocks and items
* Players do not have to accept the resource pack, and will be shown custom blocks and items as closely as possible

### Seamless updates

Mods adding custom blocks frequently have the problem that they break with a new Minecraft version, and then sometimes stop being updated.

The blocks and items added to a Fiddle server do not need to be updated, and always remain saved in the world and loadable, placeable and breakable as before.

### Works with plugins

Custom blocks and items work fully with Bukkit plugins out of the box.

## Architecture

<img src="design/architecture.svg" width="100%">

## Acknowledgements

This project has been made possible by:
* the generous donation from <a href="https://github.com/pontaoski">Janet&nbsp;Blackquill</a>
* the authors and maintainers of the Bukkit, [Spigot](https://www.spigotmc.org/) and [Paper](https://github.com/PaperMC/Paper) projects
* everyone on GitHub and the [Discord](https://discord.gg/EduvcVmKS7) server who help test Fiddle and provide feedback and suggestions