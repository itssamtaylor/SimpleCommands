package ca.samueltaylor.simple_commands;

import ca.samueltaylor.simple_commands.commands.*;
import ca.samueltaylor.simple_commands.helpers.Config;
import ca.samueltaylor.simple_commands.helpers.Logger;
import ca.samueltaylor.simple_commands.points.HomePointManager;
import ca.samueltaylor.simple_commands.points.WarpPointManager;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.WorldSavePath;

import java.nio.file.Path;

public class SimpleCommands implements ModInitializer {

    public static final String MOD_ID = "simple_commands";
    public static final String MOD_NAME = "SimpleCommands";
    public static Path worldPath;

    public static Config config;
    public static HomePointManager homePointManager;
    public static WarpPointManager warpPointManager;

    @Override
    public void onInitialize() {
        Logger.log("Start mod init...");
        config = new Config();
        this.registerCommands();
        this.registerWarpListeners();
    }

    protected void registerCommands() {
        CommandRegistrationCallback.EVENT.register((dispatcher, dedicated) -> {
            new Back().register(dispatcher);
            new Day().register(dispatcher);
            new Fly().register(dispatcher);
            new God().register(dispatcher);
            new Heal().register(dispatcher);
            new Home().register(dispatcher);
            new HomeDel().register(dispatcher);
            new HomeSet().register(dispatcher);
            new ListAll().register(dispatcher);
            new Rain().register(dispatcher);
            new Repair().register(dispatcher);
            new Spawn().register(dispatcher);
            new SpawnSet().register(dispatcher);
            new Suicide().register(dispatcher);
            new Sun().register(dispatcher);
            new TeleportAccept().register(dispatcher);
            new TeleportAsk().register(dispatcher);
            new TeleportDeny().register(dispatcher);
            new Warp().register(dispatcher);
            new WarpDel().register(dispatcher);
            new WarpSet().register(dispatcher);
        });
    }

    protected void registerWarpListeners() {
        ServerLifecycleEvents.SERVER_STARTED.register(server -> {
            worldPath = server.getSavePath(WorldSavePath.ROOT);

            homePointManager = new HomePointManager();
            homePointManager.load();

            warpPointManager = new WarpPointManager();
            warpPointManager.load();
        });

        ServerLifecycleEvents.SERVER_STOPPING.register(server -> {
            homePointManager.save();
            warpPointManager.save();
        });
    }

}
