# SimpleCommands

[![No Maintenance Intended](http://unmaintained.tech/badge.svg)](http://unmaintained.tech/)

Server commands for Fabric 1.17

Originally a fork of [Markus23/mycommands](https://github.com/Markus23/mycommands)

1. [Changes](#changes)
2. [Commands](#commands)
	- [Home Commands](#home)
	- [Teleport Commands](#teleport)
	- [Warp Commands](#warp)
	- [Health Commands](#health)
	- [Item Commands](#items)
	- [Weather/Time Commands](#weathertime)
	- [Player Commands](#player)
3. [Config](#config)
	- [Commands Config](#commands-config)
	- [Logging](#logging)
	- [Additional Settings](#additional-settings)

## Changes

#### v1.0.2-1.17

- Fix cross dimensional travel

#### v1.0.0-1.17

- Complete rewrite of mod
- Renamed from TaylorCommands to SimpleCommands
- Removed `/rndtp` command
- Allowed for each world to have their own warps and homes save file

#### [v0.4.0-1.17.01](https://github.com/samueljtaylor/TaylorCommands/pull/4)

- Updated to 1.17
- Added suicide command
- Fixed cross-dimensional travel

#### v0.3.1-20w19a.01

- Updated version to 20w19a snapshot

#### v0.3.1-20w18a.01

- Updated code for 20w18a snapshot

#### v0.3.0-20w16a.01

- Added `/listall` command to list warps and homes
	- `/listall` will list the default list in config
	- `/listall home` will list all the player's homes
	- `/listall warp` will list all the server's warps
	- Aliases for each are `home`, `homes`, `h` for homes and `warp`, `warps`, `w` for warps
- If you're having trouble with this command, you'll need to either delete your config file to allow a new one to be created or add the settings to the bottom of your existing, see [/listall settings](#listall)
	

#### v0.2.0-20w16a.01

- Redesigned the config file so you should delete your current one to allow the new one to be created
- Added console logging, customizable in the config
- Added ability to make some commands enabled/disabled or OP Only

## Commands
### Home
#### Delete a home
`/delhome`

#### Warp to a set home
`/home`

#### Set a home
`/sethome`

#### List all homes
`/listall homes`

### Teleport
#### Back to previous location
`/back`

#### Request to teleport
`/tpa`

#### Accept a teleport request
`/tpaccept`

#### Deny a teleport request
`/tpdeny`

### Warp
#### Delete a warp
`/delwarp`

#### Warp to the spawn
`/spawn`

#### Set the spawn warp
`/setspawn`

#### Set a warp
`/setwarp`

#### Warp to a warp point
`/warp`

#### List all warps
`/listall warps`


### Health
#### Restore full health and hunger
`/heal`

### Items
#### Repair item in hand
`/repair`

### Weather/Time
#### Set to daytime
`/day`

#### Set weather to raining
`/rain`

#### Set weather to sunny
`/sun`

### Player
#### Enable flying (like in creative)
`/fly`

#### Make player invulnerable
`/god`

#### Commit Suicide
`/suicide`


## Config

### Commands Config
Each command can be toggled as enabled, disabled or opOnly in the config. 
- `enabled` commands all players can use
- `opOnly` commands only players that are OP status can use
- `disabled` commands nobody can use

The config is laid out as follows:

```json
{
	"commands": {
		"fly": {
			"enabled": true,
			"opOnly": false
		}
	}
}
```

Note: opOnly commands must have both `enabled` and `opOnly` set to `true`

### Logging

By default only `opOnly` commands are logged in the console, to log all commands, set the `player` value to `true`

```json
{
	"log": {
		"opOnly": true,
		"player": true,
		"includeModName": false
	}
}
```

`opOnly` will log the commands that have `opOnly: true` and `player` will log anything that has `opOnly: false`

The `includeModName` setting will prepend `[TaylorCommands]` before the logging output.

### Additional Settings

Some commands have additional settings

##### /sethome

```json
{
	"sethome": {
		"enabled": true,
		"opOnly": false,
		"max": 5
	}
}
```

In this case `max` refers to the maximum number of homes a player can have, feel free to customize.

##### /listall

```json
{
	"listall": {
		"enabled": true,
		"opOnly": false,
		"defaultList": "warp"
	}
}
```

If `/listall` is provided with no argument the `defaultList` will be used.


