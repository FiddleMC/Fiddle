
<div align="center">

  <img src="logo.png" alt="Gale logo" width="21%" align="right">
  <h1>
    Fiddle
  </h1>
  <h3>
    A Paper server fork that lets you<br>add server-side custom blocks and items
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

To add custom blocks to Minecraft, but stay within the Paper ecosystem, and allow vanilla players to join, a fork of Paper that does exactly this was originally written for a small community server.
You can join *sucraft.org* and do `/warp Demo` to see it in action yourself.
The goal of this project is to re-write it in a generic way, so that everyone can use it, and add their own custom blocks just as easily, smoothly and reliably.

If you have the same idea, you are welcome to join the project by adding code parts, testing, sharing knowledge or giving suggestions.

## Goals of the project

### Supports any client

The blocks and items can be used by anyone.
* Players with a corresponding client mod receive the custom blocks and items directly.
* Players with a resource pack can see the custom blocks and items through a collection of replaced block states and item and entity models.
* Anyone without the resource pack sees the closest vanilla equivalent, with the custom item's name as display name.

### Can be seamlessly updated

Mods adding custom blocks frequently have the problem that they stop being updated, or need to be updated for every version, which makes it hard for servers to use them.
The custom blocks added to a Fiddle server are updated automatically, and remain saved in the world and placeable/breakable as before.

### Works with plugins

Custom blocks and items work fully with almost all plugins out of the box.

## Architecture

<img src="design/architecture.svg" width="100%">

## Design considerations

### Namespaced keys

Minecraft introduced namespaced keys as a way of differentiating what source resources belong to.
The `minecraft:` namespace is for vanilla resources, and other namespaces (for example `quark:`) can be used by mods.

However, neither Bukkit, nor most Bukkit plugins, work with this.
There are many plugins that assume that Bukkit's `Material` can be turned directly into a `minecraft:` namespaced key (so `Material.OAK_PLANKS` corresponds to `minecraft:oak_planks`).
It is not possible to add a fitting value to `Material` for other namespaces, like `quark:birch_bookshelf`.

There are many plugins that make the hard-coded assumption that `Material` corresponds to the namespaced keys, including common plugins like WorldEdit and CoreProtect.
Such plugins simply do not work with different namespaces.
If all custom resources use the `minecraft:` namespace, nearly all of these plugins fully work.

Therefore, against the purpose of namespaced keys, Fiddle uses the `minecraft:` namespace, with a prefix in the key, for example `minecraft:quark_birch_bookshelf`.

Furthermore, most plugins assume that `Material` names contain uppercase letters, digits and underscores, and that namespaced keys are identical but lowercase.
Therefore, all namespaced keys in Fiddle can only contain lowercase letters, digits and underscores.

### Block entities

Duplicate visual block states (such as note block properties, leaves distance, infested stone bricks) can be overridden in a resource pack with the look of a custom block.
However, some properties of these overridden block states are hard-coded in the client, and limit what they can be used for.

Most importantly, the collision box of a block state is hard-coded in the client.
This means that to be able to add custom stairs, the client must always receive a block state with the collision box as the custom block state, or else it won't be possible to walk on it properly.
There are only 4 duplicate visual stairs (waxed copper stairs) that could be used.
This allows the addition of only 4 types of custom stairs.

An alternative method is sending some regular stairs as a block, and drawing the desired block as an entity around it.
Many such entities will cause the client FPS to drop.

To allow the potential use of any collision box, using the entity method is supported.

### Breaking speed

Block breaking speed is initially managed client-side.
When breaking a block, the client determines how much the damage that has been done to a block, and when to send a packet to the server indicating it has finished.
The server then only checks whether the breaking time was reasonable, and approves or denies the breaking.

There is no clean way to make breaking the block go slower.
Because of the short time needed to break most blocks, packets sent to influence the breaking will often take too much time to arrive, leading to visual glitches, including invisible blocks.

The client determines the block breaking speed only from the block type (not the specific block state) and whether an appropriate tool is being used.
The breaking speed per block is hard-coded and cannot be modified.
This means that for example, if a note block state is sent to a client with a resource pack, then the client will assume the block takes as long to break as a regular note block.
If the resource pack overrides the block state's texture to be calcite bricks, then the block breaking speed is unnaturally fast.

However, no good solution for this appears to be possible.
Denying the block break from the client does not slow down block breaking, but forces the client to begin again.
Therefore, it is chosen that, if a server block X (for example, calcite bricks) is sent to the client as block Y (for example, a note block), then we will accept any block breaks sent by the client using a calculation for block Y.

Similarly, because the appropriate tools to break a block can be determined by the server, but only for a block type as a whole (not individual block states), if a note block state is overridden to look like calcite bricks, and calcite bricks must be breakable quickly by using a pickaxe, then all note block states will be breakable quickly by using a pickaxe.
It may be possible to modify the appropriate tools to break a block at the last second when a player looks at it, by sending an [Update Tags](https://wiki.vg/Protocol#Update_Tags) packet.

## Issues

### Plugin data

Many plugins store data, usually referring to blocks and items by their namespaced key (or using the `Material` names, which is worse).
This means that deleting namespaced keys from the server (which also happens in vanilla Minecraft, such as with the renaming of `grass_path` to `dirt_path`) may affect the plugin.

Therefore, it is generally best to assume that added blocks or items must always keep the same namespaced key from that moment on.
Even if the world data can be updated, there is no guarantee that plugins can handle such a change.