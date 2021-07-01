package ca.samueltaylor.simple_commands.helpers;

import ca.samueltaylor.simple_commands.SimpleCommands;
import com.google.gson.JsonObject;
import net.minecraft.entity.player.PlayerEntity;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;

public class Logger {
    protected static org.apache.logging.log4j.Logger logger = LogManager.getLogger();
    protected static Config config = SimpleCommands.config;

    protected static String getPrefix() {
        return "[" + SimpleCommands.MOD_NAME + "] ";
    }

    public static void log(Level level, String message) {
        logger.log(level, getPrefix() + message);
    }

    public static void log(Level level, String message, Exception e) {
        logger.log(level, getPrefix() + message, e);
    }

    public static void log(String message) {
        log(Level.INFO, message);
    }

    public static void fatal(String message) {
        log(Level.FATAL, message);
    }

    public static void logCommand(PlayerEntity player, String command, String message) {
        JsonObject logConfig = config.get("log").getAsJsonObject();
        JsonObject commandConfig = config.get("commands").getAsJsonObject();
        boolean performLog;

        if(commandConfig.get(command).getAsJsonObject().get("opOnly").getAsBoolean()) {
            performLog = logConfig.get("opOnly").getAsBoolean();
        } else {
            performLog = logConfig.get("player").getAsBoolean();
        }

        if(performLog) {
            if(logConfig.get("includeModName").getAsBoolean()) {
                log("[" + player.getName().getString() + " " + message + "]");
            } else {
                log(Level.INFO, "[" + player.getName().getString() + " " + message + "]");
            }
        }
    }

    public static void logCommand(PlayerEntity player, String command) {
        logCommand(player, command, "ran command /" + command);
    }
}
