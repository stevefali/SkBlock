# My Version of Skyblock

### This Is my version of Skyblock, which will be one part of The Mega Randomizer Server. For this server project, I decided to make as many of the plugins as possible by myself from scratch.
### I plan to create a proper README later, but for now, I have pasted my project overview notes.

## Mega Randomizer Server Structure

The Mega Randomizer Server uses several individual Spigot servers linked together with a Velocity proxy. So for example, Skyblock runs on its own server. At the time of this writing, the main lobby has its own separate server too.

## Skyblock World Template Creation

For my take on Skyblock, I decided I wanted to have an initial starting island, and then as players progress, new islands appear with different biomes.
	
  - I settled on a 3x3 grid of biomes for each new island, with the initial Plains biome island in the middle.
	- Since I have no way of predicting or controlling what and where people build things in their Skyblock world, I decided to have barriers around each section to prevent players from going there before they reach the stage where that island appears. As each island appears, the barriers around it are removed.
	- The obvious way to reproduce worlds like that seems to be to have a "template" world with all the biomes and barriers preset to their starting position, then new Skyblock worlds can be created by just copying that template world.
	- To create the template world, I started with a void world and sketched out the grid of biome sections I wanted on a piece of paper, with the X and Z coordinates I calculated for each section.
	
  Then, I created some helper methods `CuboidOperation.java` that I could use to iterate all of the blocks in each 3D section and set the biome I wanted for each section. 
	Then, using a similar method, I placed barrier blocks between the sections and around the outside of the grid.
	I also created a "showBiome" command that placed a plane of color coded stained glass blocks to help me see if the biomes were setup correctly. One thing I noticed was that the biomes "overflowed" a bit out of the sections, since I hadn't realized yet that biomes actually get set in 4x4-block sections, so I had to fix the edges of some of the sections.
	
  I created the islands themselves in a separate "building" world and copied them as schematics using WorldEdit. The Skyblock world template has the initial center island already in-place, and new islands are pasted in when required using WorldEdit's API.
	
  For the "Nether" and "End" islands, I created custom "Sky Nether" and "Sky End" biomes that have a lot of the features of the Nether and End, while of course still remaining technically in the overworld. 
	For example, the Sky Nether biome has the colors, particles, and sound effects of the Nether, and in the End biome, Shulkers can spawn on the island. I even prevented Hoglins and Piglins from zombifying in the Sky Nether biome.

## Skyblock World Creation/Loading/Deletion

For creating new Skyblock worlds (since each player gets their own), I decided to name each world based on the user's UUID, i.e.,  "skyblock_<user's UUID>". 
The world is created from a template world that lives in the parent folder of the Mega Randomizer server, and all of the skyblock worlds are placed into a "SkyblockWorlds" folder to keep the Skyblock Server folder clean.
	
  --> Partly to handle that, partly to handle having custom LevelStem and ResourceKeys, and partly just to try it with NMS, the world creation and loading is done using NMS.
		
  - For this, I created the `SkyblockWorldFactory` class that has methods `loadPlayerWorld()`, `getOrCreateWorldPath()`, and `deleteWorld()`.
      - These methods run largely async, which is handy.
			- The `loadPlayerWorld()` took quite a bit of time to figure out because doing this with NMS requires a lot of lines of code, and I had to work it all out largely by trial and error.
			- The `deleteWorld()` method (which also unloads the world first) was built using a similar approach, and initially erased the world completely, but I later decided to save a backup of the old world in a "backup" folder so that, in case a user ever deleted their world unintentionally, I could essentially just copy/paste the deleted folder back into place. (I have a feature that allows users to delete their skyblock world so they can start over if they want)
				
        - I may eventually add a feature that allows users to keep multiple worlds so they can start a fresh Skyblock game without deleting their old world if they want.
		
    - To go with those methods, I created the commands `LoadWorldCommand()` and `deleteWorldCommand()` for both testing  and admin functionality.
		- Of course, the Skyblock world is automatically created and/or loaded for players when they pass through the portal in the Skyblock lobby or select the option in the menu.
		- To avoid unnecessary load on the server, players' worlds are automatically unloaded if they leave the server for more than 30 seconds.

Although this is functionally a pretty basic operation, it really was one of the most satisfying features to have up and running during the early phases of the server's development.


## MegaNPCs

Fairly early in the development process of Skyblock (which was the first game I made for my server) I made the decision to try and build as much of the server's functionality myself as possible, rather than relying on a bunch of other 3rd party plugins. In fact, WorldEdit is the only 3rd party plugin that my Skyblock plugin/game actually requires (aside from what WorldEdit itself requires).
While I initially used a 3rd party NPC plugin, I chose to remove it and create all the NPC functionality myself.

This turned out to be a much bigger rabbit hole than I expected… 
(See some of my notes in the "Problems and Challenges" section)

It became clear early on that the NPCs ideally should not be normal "Player" objects that are actually in the server, but rather, it's better to just send packets to the users. 

Since there are a number of different packets that are required for this, I decided it would be better to have it all managed by its own standalone plugin, along with an API that my other plugins can import and use. I call this plugin MegaNPCs.

As mentioned elsewhere in my notes, this was my first attempt at making a plugin/api combination, so I built most of it partly "blind"; I didn't directly test any of it until it was pretty much completed, because I hadn't fully figured out yet how to host the API publicly so I could import it in my other plugins.
	
  - Instead, for each piece of it, I built "mockup" code in another plugin that I would trigger with things like block break events to test out the different pieces.

One of the features of MegaNPCs is that it detects user interactions with NPCs by attaching packet listeners to each player as they join the server (and removing them when they leave) that filter for `ServerboundInteractPacket`s and match up the packet's id field with the registered NPCs.
	
  --> This is one of the things that is nice to have managed by a standalone plugin/API because consuming plugins don't have to worry about this logic at all. They just define the interaction behavior (with a callback method) and/or listen for the NpcInteract Event.
	
My `NpcManager` class maintains a registry of NPCs and what Minecraft Worlds they are in.
	
  - That way, all of the NPC objects can live in in one place, and MegaNPCs takes care of loading, unloading, maintaining any state, and cleaning up.
	- On the API side,  I have an `NpcService` interface that exposes methods for consuming plugins to create, manipulate, and remove NPCs.
	- The `NPC` class is not exposed publicly; consuming plugins just reference the NPCs by their UUID. For example, `setNpcPosition(UUID npcId, double x, double y, double z)`, etc.

As I alluded to earlier, some of the features of MegaNPCs were pretty tedious and fiddly to figure out. For example, working out how to consistently get the NPCs to face the direction I want was a bit tricky, especially when the NPC is placed in the center of a block instead of the origin (corner).

For displaying lines of text above the NPCs' heads, I settled on a system that allows the use of TextDisplays, invisible ArmorStands, or a combination of both. TextDisplays are nice for displaying larger font, but ArmorStands are the more traditional approach, so I wanted to make sure I had a good grasp of how to do either.

For the skins of the NPCs, I created a few custom skins and set them to my Minecraft username, then used Mojang's API to get the JSON of the skin data (the "textures" and "signature" values). I then copied them into a JSON file that I keep in the parent/common directory of MegaRandomizer Server so all the servers using MegaNPCs can access it. The JSON file's values are loaded at server startup so that they are ready to go when any NPC needs them.

One of the problems I encountered was that when the server doesn't get shut down properly, it can leave behind extra ArmorStands and TextDisplays in the world that are no longer tied to an NPC instance. This means that if you were to move the NPC, or remove it completely, the text lines will still be there! I call these left-behind ArmorStands/TextDisplays "orphans".
	
  -->  I realized that even if I took care of some edge cases that were causing them to get orphaned, I would still inevitably end up with some orphans, especially from cases where a world or server crashed or didn't get shut down correctly. 
  
  - So I decided to add an "Orphan cleaner" system to MegaNPCs that would find and remove orphans for me. I created a command to run this manually, but more importantly, I added it to the API so that consuming plugins can trigger it at certain times, such as world loading, or even schedule it to run automatically if necessary.

In the end, I am very pleased with MegaNPCs. I feel like I made it in a way that gives plugins that use it all the control they need, while still abstracting away all the stuff they shouldn't have to worry about.
As time has gone on, I have slowly found and fixed little bugs with it, so it's something I come back to from time to time, but mostly just does all its magic in the background without me having to worry about it :)


## SkyblockSession

Each player gets their own Skyblock world (Although I may add multiplayer Skyblock some day). All of those worlds, as well as the Skyblock Lobby world (separate from the main server lobby) are all on the same server and managed by the same Skyblock plugin.

It seems logical to have a place for most of the player-specific/world-specific logic and data to live, or at least be referenced from. That's where the `SkyblockSession` class comes in. 

Each `SkyblockSession` instance is tied to a Skyblock world (and its player), so it holds a UUID for the world and one for the player who is the owner of that world as well. 
It also holds a reference to:

  - The world's BossBar
	- The player's Skyblock Score
	- The player's Skyblock quest progress

This is also where I handle in-game-specific event handlers (see "Event Routing" below) and calls to update the Sidebar.

I also have a `SessionRegistry` class that holds a `Map<UUID, SkyblockSession>` to reference all the loaded Skyblock Sessions, as well as handling SkyblockSession creation, loading, unloading, etc.


## Event Routing

Several Bukkit events (and some custom) are used for logic that is specific to individual players and their Skyblock worlds. So I wanted a way to handle them on a per-player or per-world basis. Therefore, I figured it would be nice to be able to handle such events directly in the `SkyblockSession` class.

To make this happen (and make it clean) I "route" those events to their relevant SkyblockSession by essentially checking if the event occurred in a loaded SkyblockSession, i.e., the event was caused by a player in their Skyblock world, like this example:

```Java
 @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {

        SkyblockSession skyblockSession = sessionRegistry.getSession(event.getBlock().getWorld());

        if (skyblockSession != null) {
            skyblockSession.onBlockBreak(event);
            return;
        }
    …
}
```

## Menu System (Inventory Menus)

It's obviously pretty common in Minecraft servers to use Inventories as menus. For my own implementation of this, one of my main concerns was that I really wanted to be able to create items for the menus that had callback methods for performing whatever action was specific to that menu item. That way, I could just have a `handleClick()` method that calls the callback, and adding new functionality to the menu would be as simple as defining new menu items.
	
  --> The main challenge I encountered with that was how to access those custom MenuItems from an `InventoryClickEvent`. I tried at this for a while with pretty limited success.
	Finally, I decided to have a peek at some other people's repos to see how have approached this. That is where I got the idea that, instead of trying to implement the `Inventory` interface, my custom `InventoryMenu` class should instead implement `InventoryHolder`!
		
   - That way, when I call `Bukkit.createInventory()` I can pass my custom `InventoryMenu` class as the owner of the inventory, since in the event handler, I can then access it via `event.getClickedInventory().getHolder()`.
	 - My `InventoryMenu` class then holds a `Map<Integer, Consumer<InventoryClickEvent> of the click hadler callbacks of the items, keyed by the items' rawSlot in the inventory.
		 - `InventoryMenu` then has a `handleClick(InventoryClickEvent event)` method that just finds and calls the callback method in that Map using the rawSlot of the InventoryClickEvent.

Then, I created a `MenuProvider` class that creates `InventoryMenu`s and `MenuItem`s at server startup.


## Quest System

One of the systems I am quite proud of is the Quest system. I wanted players to have a way to "progress" through some set of tasks so that I can do things like award points, and more crucially, add islands and biomes in stages that would also unlock new generators and shop items. This gives the player a way of "levelling up". It also gives me a way of presenting them with the features of my Skyblock game that I took the time to build.

Since I knew I would want a lot of quests, I certainly didn't want to hard-code every single task in some kind of enormous if-else statement or something.
	
  So, I figured a good solution would be to create a reausable logic system that would allow me to "script" quests in a YML file.
	But, of course, I didn't want every single quest to just be "gather x amount of y"; I wanted to have some variety in the types of quests the user is tasked with completing.
	
  This took quite a bit of pondering and mind-mapping, but eventually, I worked out that I could have a set of "quest types" that I could assign explicitly to each quest in the YML (i.e., `type: "gather"`, etc.)
    
  - Each quest type is defined by a class and has its own requirements. For example, a "gather" quest needs a `Material` ("item type"), and a quantity to gather.
  - Each quest definition in the YML has the required data for its quest type.

    - I made a `QuestRegistry` class that iterates through the YML and delegates/assigns each quest based on its Quest Type.
				
        - This is done by having a `Map<String, QuestTypeFactory>` where the keys are as strings representing the quest type (e.g., "harvest") and the value is a functional interface, `QuestTypeFactory`, which has the method `build(String id, ConfigurationSection section)`.
				- Then, I "register" each quest type by basically defining which Quest Type class to use and how to extract each parameter in its constructor from the YML object for that quest.
				
        Like this:
      
		```Java
			questTypeFactories.put(
				    "harvest", (id, section) -> new HarvestQuest(
				           id,
				           section.getString(TITLE_KEY),
				           section.getString(DESCRIPTION_KEY),
				           Material.getMaterial(section.getString(MATERIAL_KEY)),
				           section.getInt(AMOUNT_KEY),
				           section.getInt(REWARD_KEY),
				           buildRewardActionFromConfig(section.getConfigurationSection(ACTION_KEY)),
				           section.getString(NEXT_KEY)
				       )
				  );
		```

		- Then as I iterate over "quests" section of the YML, I can call the `build()` method, passing in the quest's id and YML ConfigurationSection (the YML "object" section for that quest) like this:
      
		```Java
		for (String questId : questsSection.getKeys(false)) {
		    ConfigurationSection section = questsSection.getConfigurationSection(questId);
				String questType = section.getString(TYPE_KEY);
				
				QuestTypeFactory questTypeFactory = questTypeFactories.get(questType);
				
				SkyblockQuest skyblockQuest = questTypeFactory.build(questId, section);
				skyblockQuests.put(questId, skyblockQuest);
				 }
		}
				
		```
  - Once I had this system figured out, I added another layer to allow me to have more complex "rewards" for quest completion, mainly performing different actions. I did this by using the same idea to add "action types", similar to defining quest types. 
  
  For example, my action types can be "give_item" for when I want to reward the player by giving them some item, "set_island" for when I want reward the player by giving them a new island, or "custom" for when I want to perform some other type of action.
			(That is what the `buildRewardActionFromConfig()` line is about above)
				
   - The "custom" action type is basically another layer of types again that allows me to define "one-off" action types in a custom action type class.



## SkyblockProgress / Data Persistence

Since I created a quest system for players to progress through, I of course needed the state of that progression to persist, rather than just living in memory. Since each player's progress is strongly tied to their Skyblock world, it made a lot of sense to just save their progress in the world's NBT data.

I plan to implement an SQL database later, probably when I make Bedwars, but for now, NBT works fine. The two main downsides to this Skyblock world NBT system I'm currently using are that the player's world has to be loaded for their data to be available, and that the data is only really available in the Skyblock server, which doesn't work very well for things like scoreboards in the main lobby server.
	
  To accommodate my plan of switching to a database later on, I tried to set up the data persistence system in a way that would be as easy to migrate as possible when the time comes.
	To that end, I made a `SkblockDataStore` interface and created two implementations of it: `NbtSkyblockDataStore` and `SqlSkyblockDataStore`. For now, the SQL implementation just overrides the methods generically to satisfy the compiler, with a TODO note that it needs to be implemented properly later on.
	
The persisted data is broken up into two main pieces: 
	
1. The player's Skyblock Score, which is just an integer representing the player's overall score.

2. The player's `SkyblockProgress`, which is basically a JSON string that holds the player's current quest id, their numerical progress within the current quest, and a list of generators they have unlocked (see "Generators" below).

When the data loads, the quest system is able to just find the current quest using its ID. This way, I can persist everything I need to, without storing more information than necessary.


## Sidebar / MegaHUD

Like in many other Minecraft servers, the Sidebar is a handy place to display information to the user.
The vanilla sidebar, though is a bit of an awkward way to do that, but with NMS, there are a lot more formatting options.
Also, having the Bukkit server manage sidebars for every player is a bit messy.

What I found to be a much better system is to just send client sidebar packets directly myself instead of using the vanilla system.
	This again seemed like a good thing to package into its own plugin/API, so I created MegaHUD. 
	MegaHUD has a similar basic structure to MegaNPCs: a plugin to do all the work, with an API that exposes a minimal amount of code through an interface, which I called `MegaHudService`.
	
As I mentioned, the vanilla Sidebar doesn't give you very much control over the look of the sidebar. You can't change the color of scores, and every line in the sidebar must show a numeric score on the right hand side. Also, the vanilla sidebar automatically rearranges the order of the lines in descending order of score.
	
  However, in NMS, there is a `NumberFormat` class that allows you to format the numerical score value, leave it blank, or display text in its place.
		
  So taking advantage of that, my API allows consuming plugins to easily create sidebar lines with text on the left, text on the right, both, or to make a completely blank line. 
		After some fiddling, I decided that in cases where I do want to display a numerical score on the right, it is better to actually just have the number in a String.
			
  - Again, this is because the client will reorder the lines based on the numerical score, which I found a bit annoying.
	- Using Strings instead allows me to get around this issue.
	   
     - To allow the consuming plugin to directly control the order of the sidebar lines, MegaHUD has the consuming plugin set a 0-based index on each sidebar line, with the order of the lines corresponding to the ascending value of the index. 
		 - This is done by assigning the line's score value based on the index value in the background. (In reverse by using an arbitrary value of `1000 - index` because the sidebar normally puts the lines in DESCENDING order).
		 - The "index"/"score" value is never shown to the user because I force the right side of the sidebar lines to be either a String or blank.
				
	Unlike MegaNPCs, which only exposes vanilla Spigot types in the API, MegaHUD adds a small `SidebarLine` class that I created, with static constructor methods, such as `leftSideOnly`, `rightSideOnly`, and `bothSides`, which return SidebarLines with text on the left side, right side, and both sides respectively. It also has a static `BLANK` member for blank lines.
		
    - This makes it easy for consuming plugins to set sidebar lines without having to worry about any of the implementation details.
		- The API is intended to be as idempotent as possible, so that consuming plugins can mostly just set sidebar lines based on their index at any time without having to worry about removing or changing values of pre-existing lines.

      - Most of that was easy enough to implement because of the way the relevant NMS packets work.
				
        - However, when it comes to creating/updating the overall sidebar itself, which requires the `ClientboundSetObjectivePacket`, you have to be careful, because the "create" type is separate from the "update" type, and if you send the "create" method code when the sidebar objective already exists on the client, it will cause an error that will kick the player!!!

          - To prevent that from happening, MegaHUD tracks a boolean `isSidebarObjectiveSent` for each player, and then sets that "method" code accordingly, i.e., "if the objective has already been sent to the client, use the 'update' code, else use the 'create' code".
					
	I quite like this system, because it strictly uses packets directly and does not allow the internal Spigot server to manage (or even really be aware of) the sidebars, which gives me complete control, avoiding all the weirdness of the vanilla sidebar.


## Island Setting / Barrier Removal

Like I mentioned above, as players progress through the questline, new islands with different biomes appear, and the barriers around them are removed.
I made a method, `performCuboidSectionOperation()` to set the blocks in a given area, and used this for barrier removal.
Origially, I was using FastAsyncWorldEdit's API to paste the island schematics into the worlds, and for a time, I used FastAsyncWorldEdit to perform the barrier removal as well, so that I could do as much as possible async. However, this lead to several headaches, mainly due to FAWE's relighter system.
	
  - Sometimes, large areas of the world where barriers were removed would end up with incorrect lighting.
	- Even worse, the relighter would sometimes get "stuck", preventing any of the other barrier removal and island pasting operations from continuing correctly, which would very often end up crashing the server. 

Clearly, this would not due. Eventually, I decided to perform the barrier removal using standard Bukkit operations instead of FAWE, letting it run on the main thread, but with so many blocks affected, this was also a bit flaky.
		So, I decided to "throttle" the barrier removal operations. I created a modified version of `performCuboidSectionOperation()`  called `performCuboidSectionOperationBatched()`. This new method sets a maximum number of blocks to modify per tick, and schedules a repeating task using `runTaskTimer()` that runs the "batchSize" number of operations per tick until it is completed.
		
  This solved a lot of the lag issues and made barrier removal work really reliably. However, I noticed that I was still having lots of issues with the relighter of FAWE when pasting the island schematics. (it was still getting hung up sometimes) This was really frustrating because the island schematics are not particularly large, so it didn't seem like it should be giving me as much trouble as it was.
			
  - Because of this, I decided to try disabling the relighting phase of the paste operation.

    That improved performance really nicely, and I stopped getting the bog-down and/or crashes that were happening before.
    
    However, I noticed that this actually broke the lighting in the schematics quite a lot. For example, my Ice Spikes island has an igloo with a dungeon below and a zombie villager in it, but the zombie villagers were getting burned from daylight every time, even though there should have been no daylight down there at all.
    I believed I should be able to solve this by using the LightCleaner plugin, but unfortunately, it did not fix the problem.
    This was a really frustrating issue.
    After a while, I decided to just forget about pasting the island schematics async, and just switch to using WorldEdit instead.
     Happily, this completely solved the problem. Since the island schematics are not too big, the paste with WorldEdit really just takes a small fraction of a second, and does not lag the main thread at all. Best of all, WorldEdit doesn't try the fancy relighting tricks that FAWE does, so the lighting is correct every time.
					
I created an `IslandSetter` class with methods that perform all the necessary actions for each island section: it pastes the island, removes the relevant barriers, and when that's completed, performs any "finish-up" tasks, such as spawning entities, placing items in chests, etc. It also unlocks the relevant generator, if needed, for that section (see "Generators" below). These operations can be triggered with an admin command if needed (mostly just for testing during development), but mainly, they are triggered by the player reaching certain spots in the questline.


## Generators 

One of the features of my version of Skyblock is some custom block generators I created.
When flowing lava meets water, it of course normally creates a cobblestone block. I wanted the user to be able to also get ores from these generators, so I made a system that allows ores to form instead of cobblestone, based on probability. 
Most of the time, you'd get cobble, but there's a chance of getting coal, iron ore, etc., and a smaller chance of getting more "rare" valuable things like diamonds.

Later on, I decided to expand this generator system: Since each island section has its own biome, I created new generator types that produce different blocks based on the biome. 
	
  For example, in the desert biome, the generator produces sand instead of cobblestone.
	This also ended up being a nice way for me to make certain things more available "later" in the game: Getting new islands "unlocks" new generator types, and therefore different materials and ores. Diamonds, for example, won't form until the user unlocks the "deepslate" generator, and in the "Sky Nether" biome, the generator even has a small chance of producing Ancient Debris.
  
 My initial generator, `CobbleGenerator.java`, used a min and max value for the "probability", and then hard-coded numbers adding up to the max value in a series of if-else statements. This worked, but was extremely annoying to modify!
 So, I created a new system, `MegaGenerator`, that uses a builder pattern to "build up" the total probability denominator as you add more "numerator" items in the builder:
	
```Java
	public static final MegaGenerator<Material> DEFAULT_GENERATOR = MegaGenerator.<Material>builder()
	    .add(Material.COBBLESTONE, 170)
	    .add(Material.COAL_ORE, 30)
	    .add(Material.IRON_ORE, 25)
	    .add(Material.COPPER_ORE, 20)
	    .add(Material.LAPIS_ORE, 5)
	    .build();
	
```
	
I also wanted to display the probabilities in the Sidebar for each item in the generators for the biome where the user is currently located. However, I decided to abandon that when I realized there is a hard cap on the number of lines you can display in the Sidebar. 
I kept the "probability strings" logic in MegaGenerator, though, in case I ever decide to display it in the Tab menu or something.
	
The Generator unlocking system also happens to be a convenient place to tie-in the unlock logic for Skyblock Shop items. (See below)


## NPC Store / Skyblock Shop

For my first iteration of the Shop, I started out with creating a `ShopMenu` subclass of the `InventoryMenu` class I had previously built for my in-game menus. 
My initial plan was to have a `ShopItem` class for the items in the menu that would handle the state of each item internally, and to persist the MenuItem object for each `SkyblockSession` and its ShopItems, rather than rebuilding everything each time.  
	
  - I created methods in `ShopItem` to automatically set the item titles and lore, and determine onClick logic based on whether the item is unlocked and whether the user can afford to pay the price.
	- The `ShopMenu` class had `ShopItem` objects defined statically in it since I wanted to be able to easily access them directly from outside for the unlocking logic, which is tied to the generator unlocking logic.

    - This of course turned out to be a mistake because they need to be specific to each player!
	
Overall, this system basically worked, but felt a bit yucky because it was complicated and a bit convoluted to try to maintain the state correctly all the time.
Another problem with this first draft was that when purchasing an item, the user was given the exact ItemStack stored in the ShopItem object, which meant that the item meta (the title and lore) of the items displayed in the shop were still present in the items given to the player! 🤣 Obviously not ideal…

--> So for the second iteration, I decided it was better to instead rebuild/repopulate the ShopMenu every time it's opened and whenever an item is purchased. All of the logic is moved to the ShopMenu class, and ShopItem is now a Record. 
		
  - This is FAR easier than trying to maintian and update state on multiple ShopItems.
	- Now, the entire ShopMenu is populated with fresh ShopMenu items when the menu is shown to the player. It's also rebuilt every time a purchase is made, because purchases can change the affordability of different items in the shop.
	- Each ShopItem is now defined in a `Map<ShopItem, GeneratorType>` which is a much simpler way of tying each item to which generator type unlocks it.
	- I also greatly simplified my system for determining which items are locked and unlocked, by creating an `isUnlocked()` method, which can now simply check the Map<ShopItem, GeneratorType> directly.
	- This iteration is more careful to separate "icon" ShopItems that the user clicks on in the ShopMenu from the actual items the user is given when purchasing, so they do not have the custom shop ItemMeta attached to them.


## Compile-Time NMS BuildTools vs Paperweight: Lesson Learned 

I saw somewhere that for NMS development on a Spigot plugin, you can actually just use the Paperweight dependency, rather than the standard Spigot BuildTools approach. That way, you can avoid having to run the BuildTools with the proper flags for NMS development, and as a bonus, Paper uses the Mojang mappings, instead of the Spigot obfuscated ones.

I went ahead and used that for my Lobby and Skyblock plugins, but after a while, I started to notice a major downside when your server jar is a Spigot, not a Paper one:
 The Paper library has a LOT of methods, constructors, etc., that don't actually exist in the Spigot jar.
	This became more and more annoying as time went on, because I would find lots of code that compiles just fine, but doesn't work at runtime.
	In particular, the first time I really ran into this problem, which was when I was working out how to use NMS packets for creating NPCs, it was really bewildering, and took me quite a while to solve.
	Once I was aware of this problem, it was a lot easier to work around, but it's still pretty annoying to have to double-check which code is actually going to exist at runtime.
	
  Having gone through all that, I told myself that next time I make a plugin, I would just use the normal Spigot BuildTools-created dependencies. Even with the obfuscation, it's much better than the compiler showing a bunch of code that doesn't actually exist at runtime. So when it came time to make my MegaHUD plugin, I used the normal Spigot NMS dependencies, and decided to try a 3rd-party mappings dependency as well, so I could still have the Mojang mappings.
		
   I found this MUCH easier, so I have learned my lesson: Since the server is Spigot and not Paper, it's NOT worth using the Paperweight dependency!! 🤣🤣

