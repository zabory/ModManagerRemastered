# ModManagerRemastered

This project has 4 smaller projects in it at once

Admin manager
=============
This goes through the mods in the modpack, finds them on curseforge, and updates them automatically. There are also basic modpack manging functionality like disabling mods etc.

Sync client
===========
This gets put in the modpack home directory. When launched, the user is able to connect to the modded server and download/update any config files, mod, etc. they need to run the pack. This is esspecially useful when you want to manage your own pack not using any of the fancy modpack managers but still update it regularly, as distributing mods to clients are a pain in the butt.

Sync server
===========
This gets run in the server directory. Clients will connect to this and it will compare the files in both directories, and send files the clients need to match the server. 

Modpack Installer
=================
This installs the mod pack using command line, launching the forge installer, creating the profile, launching a sync client to download all the mods.
