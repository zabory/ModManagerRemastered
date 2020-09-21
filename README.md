# ModManagerRemastered

This project has 4 smaller projects in it at once

Admin manager [coming soon]
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

How to setup
============
The whole goal of this is to be simple for the client to setup. But, the client isn't the one reading this so lets talk server owner. To have the server go, simply put the mod manager into the server's main directory and launch it with a batch file with the following command:"java -jar modmanager.jar -server". This will create a properties file that you will edit before starting the server again. Lets talk about the different things in the file:
  
  port=[Port server is to run on]
  
  dirs_to_sync=[Directories you would like to sync comma delimited]
  
  ignore=[Names of files to ignore comma delimited]
  
Heres what an example properties file would look like:

  port=42069
  
  dirs_to_sync=mods,config,resources
  
  ignore=Morpheus-1.12.2-3.5.106.jar,1.12.2,memory_repo
  
This will sync all the mods, configs, and resources excluding the morpheus mod (since its a serverside only mod), and some folders in the mods folder that dont need synced.
Pretty easy at this point. Download the version of forge your modpack uses, and deploy them to clients together. All the client has to do is put the forge installer and modmanager in the directory they want the pack to be installed and launch the mod manager. It will install the profile, launch the forge installer, and start syncing the mods from the server to the client.
