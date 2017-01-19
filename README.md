# Overview

This is a game I am working on for [The Banana Network](http://banananetwork.us) . The object of the game is to advance yourself and your race further and further as you unlock new places, new items, and new abilities.

You start off choosing one of three races: Elf, Dwarf, or Human. Then you choose a class. A class is either an occupation or a specific talent (eg. Wizard, Blacksmith, Magician)

You start off in a city of your choice and must "work" until you've collected enough supplies to go on a quest. Adventure through the world alone or with friends, but be carefull: You only live once.

Once you die you are given 30s to be revived by another player. If you aren't then you are sent back to the
lobby.

# Dependencies
* Spigot 1.11 jar file (made for 1.10, but 1.10 jar file doesn't include enchantment classes for some reason)
* ProtocolLib (1.10 compatible)
* ServerBase (see my GitHub repository)

# Installing

To install simply download the desired Spigot and Java version file from the releases page, place it into your server's ```plugins``` directory, and start the server. This will generate the default configuration and you'll be ready to go.

The plugin will crash because it will try and load un-set configuration values so make sure to stop it and configure first.

# Configuration

Running the server for the first time will generate some configuration files. Check each one for instructions on how to configure it.

```config.yml```: The general plugin configuraton
```messages.yml```: These are pre-configured, but they allow you to have full control over messages sent from the plugin (color is not alterable)
```maps.yml```: Configuration for maps. This includes lobby and map spawns, cities, and bosses.

# Permissions

### Basic Permissions
* "fantasy.build.lobby" - Allow player to alter blocks in the lobby world

### Race Permissions - Allow player to use Race
* "fantasy.race.elf"
* "fantasy.race.dwarf"
* "fantasy.race.human"

### Class Permissions - Allow player to use class

#### Human Classes - Requires Human Race permission
* "fantasy.class.civilian"
* "fantasy.class.builder"
* "fantasy.class.farmer"
* "fantasy.class.mariner"

#### Dwarf Classes - Requires Dwarf Race permission
* "fantasy.class.miner"
* "fantasy.class.blacksmith"
* "fantasy.class.warrior"

#### Elf Classes - Requires Elf Race permission
* "fantasy.class.archer"
