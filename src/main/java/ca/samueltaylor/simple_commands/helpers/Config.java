package ca.samueltaylor.simple_commands.helpers;

import ca.samueltaylor.simple_commands.SimpleCommands;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;

import java.io.*;

public class Config {
    protected static final String path;
    protected static final String fileName;
    protected static final File configFile;
    protected static final String defaultsFile;

    protected JsonObject config;

    static {
        path = FabricLoader.getInstance().getConfigDir().toString() + "/" + SimpleCommands.MOD_ID + "/";
        fileName = SimpleCommands.MOD_ID + ".json";
        configFile = new File(path + fileName);
        defaultsFile = "/assets/" + SimpleCommands.MOD_ID + "/config/defaults.json";
    }

    public Config() {
        this.load();
    }

    protected void load() {
        Logger.log("Trying to load config file...");

        if(!configFile.exists()) {
            Logger.log("Config file not found.");
            this.createConfigFile();
        }

        try {
            this.config = new JsonParser().parse(new FileReader(configFile)).getAsJsonObject();
        } catch (FileNotFoundException exception) {
            Logger.log(Level.FATAL, "Config file couldn't be read or created.", exception);
        }

        Logger.log("Config loaded successfully.");
    }

    protected void createConfigFile() {
        Logger.log("Creating new config file...");
        this.checkForConfigPath();

        InputStream configStream = SimpleCommands.class.getResourceAsStream(defaultsFile);
        assert configStream != null;
        JsonElement json = (new Gson()).fromJson(new InputStreamReader(configStream), JsonElement.class);

        try {
            FileWriter writer = new FileWriter(configFile);
            new GsonBuilder().setPrettyPrinting().create().toJson(json, writer);
            writer.close();

            Logger.log(fileName + " created successfully.");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    protected void checkForConfigPath() {
        File configPath = new File(path);
        if(!configPath.exists()) {
            Logger.log("Config path does not exist, creating...");
            if(!configPath.mkdir()) {
                Logger.fatal( "Could not create config path.");
            }
        }
    }

    public String get(String key, String defaultValue) {
        return this.config.has(key) ? this.config.get(key).getAsString() : defaultValue;
    }

    public Boolean get(String key, Boolean defaultValue) {
        return this.config.has(key) ? this.config.get(key).getAsBoolean() : defaultValue;
    }

    public Integer get(String key, Integer defaultValue) {
        return this.config.has(key) ? this.config.get(key).getAsInt() : defaultValue;
    }

    public JsonObject get(String key, JsonObject defaultValue) {
        return this.config.has(key) ? this.config.get(key).getAsJsonObject() : defaultValue;
    }

    public JsonObject get(String key) {
        return this.get(key, new JsonObject());
    }
}
