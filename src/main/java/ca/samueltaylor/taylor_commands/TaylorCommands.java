package ca.samueltaylor.taylor_commands;

import ca.samueltaylor.taylor_commands.commands.*;
import ca.samueltaylor.taylor_commands.helper.Config;
import ca.samueltaylor.taylor_commands.helper.HomePoint;
import ca.samueltaylor.taylor_commands.helper.WarpPoint;
import com.google.gson.JsonObject;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v1.CommandRegistrationCallback;
import net.fabricmc.fabric.api.event.server.ServerStartCallback;
import net.fabricmc.fabric.api.registry.CommandRegistry;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.*;
import java.util.HashMap;


public class TaylorCommands implements ModInitializer {

    public static final String MODID = "taylor_commands";
    public static final String MOD_NAME = "TaylorCommands";

    private static File worldDir;
    public static Logger LOGGER = LogManager.getLogger();
    public static File configDir = FabricLoader.getInstance().getConfigDirectory();

    public static File taylorCommandsDir = new File(configDir, "taylor_commands");
    public static Config config;

    public static HashMap<String, Boolean> perms = new HashMap<>();


    @Override
    public void onInitialize() {
        log(Level.INFO, "Starting init...");

        if (!taylorCommandsDir.exists()) makedir(taylorCommandsDir);

        config = new Config();

        initWorlds();

        CommandRegistrationCallback.EVENT.register(((dispatcher, dedicated) -> {
            CommandBack.register(dispatcher);
            CommandDay.register(dispatcher);
            CommandDelHome.register(dispatcher);
            CommandDelWarp.register(dispatcher);
            CommandFly.register(dispatcher);
            CommandGod.register(dispatcher);
            CommandHeal.register(dispatcher);
            CommandHome.register(dispatcher);
            CommandListAll.register(dispatcher);
            CommandRain.register(dispatcher);
            CommandRepair.register(dispatcher);
            CommandRndTp.register(dispatcher);
            CommandSetHome.register(dispatcher);
            CommandSetSpawn.register(dispatcher);
            CommandSetWarp.register(dispatcher);
            CommandSpawn.register(dispatcher);
            CommandSun.register(dispatcher);
            CommandTpa.register(dispatcher);
            CommandTpAccept.register(dispatcher);
            CommandTpDeny.register(dispatcher);
            CommandWarp.register(dispatcher);
        }));

//        CommandRegistry.INSTANCE.register(false, CommandBack::register);
//        CommandRegistry.INSTANCE.register(false, CommandDay::register);
//        CommandRegistry.INSTANCE.register(false, CommandDelHome::register);
//        CommandRegistry.INSTANCE.register(false, CommandDelWarp::register);
//        CommandRegistry.INSTANCE.register(false, CommandFly::register);
//        CommandRegistry.INSTANCE.register(false, CommandGod::register);
//        CommandRegistry.INSTANCE.register(false, CommandHeal::register);
//        CommandRegistry.INSTANCE.register(false, CommandHome::register);
//        CommandRegistry.INSTANCE.register(false, CommandListAll::register);
//        CommandRegistry.INSTANCE.register(false, CommandRain::register);
//        CommandRegistry.INSTANCE.register(false, CommandRepair::register);
//        CommandRegistry.INSTANCE.register(false, CommandRndTp::register);
//        CommandRegistry.INSTANCE.register(false, CommandSetHome::register);
//        CommandRegistry.INSTANCE.register(false, CommandSetSpawn::register);
//        CommandRegistry.INSTANCE.register(false, CommandSetWarp::register);
//        CommandRegistry.INSTANCE.register(false, CommandSpawn::register);
//        CommandRegistry.INSTANCE.register(false, CommandSun::register);
//        CommandRegistry.INSTANCE.register(false, CommandTpa::register);
//        CommandRegistry.INSTANCE.register(false, CommandTpAccept::register);
//        CommandRegistry.INSTANCE.register(false, CommandTpDeny::register);
//        CommandRegistry.INSTANCE.register(false, CommandWarp::register);


        log(Level.INFO, "Registered commands.");
    }

    public static void log(Level level, String message) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message);
    }

    public static void log(Level level, String message, Exception e) {
        LOGGER.log(level, "[" + MOD_NAME + "] " + message, e);
    }

    public static void log(String message) {
        log(Level.INFO, message);
    }

    public static void logCommand(PlayerEntity player, String command, String message) {
        JsonObject logConfig = config.get("log").getAsJsonObject();
        JsonObject commandConfig = config.get("commands").getAsJsonObject();
        boolean performLog = false;

        if(commandConfig.get(command).getAsJsonObject().get("opOnly").getAsBoolean()) {
            if(logConfig.get("opOnly").getAsBoolean()) {
                performLog = true;
            }
        } else {
            if(logConfig.get("player").getAsBoolean()) {
                performLog = true;
            }
        }

        if(performLog) {
            if(logConfig.get("includeModName").getAsBoolean()) {
                log("[" + player.getName().getString() + " " + message + "]");
            } else {
                LOGGER.log(Level.INFO, "[" + player.getName().getString() + " " + message + "]");
            }
        }
    }

    public static void logCommand(PlayerEntity player, String command) {
        logCommand(player, command, "ran command /" + command);
    }

    private static void initWorlds() {
        ServerStartCallback.EVENT.register(server -> {
            WarpPoint.loadAll();
            HomePoint.loadAll();
        });


    }

    public static File getWorldDir() {
        return new File(".");
    }

    public void makeFile(File file) {
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public void makedir(File file) {
        if (!file.exists()) {
            file.mkdir();
        }
    }
}
