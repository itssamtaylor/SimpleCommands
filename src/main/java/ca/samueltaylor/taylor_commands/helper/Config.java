package ca.samueltaylor.taylor_commands.helper;

import ca.samueltaylor.taylor_commands.TaylorCommands;
import com.google.gson.*;
import net.fabricmc.loader.api.FabricLoader;
import org.apache.logging.log4j.Level;

import java.io.*;

public class Config {
    protected String fileName;
    protected File configFile;
    protected JsonObject config;

    public Config(String name) {
        this.fileName = name + ".json";
        this.configFile = new File(getFileLocation() + this.fileName);
        this.loadConfig();
    }

    public Config() {
        this(TaylorCommands.MODID);
    }

    public void generateConfigFile() {
        TaylorCommands.log("Creating new config file " + this.fileName);

        InputStream inputStream = TaylorCommands.class.getResourceAsStream("/assets/" + TaylorCommands.MODID + "/config/defaults.json");
        JsonElement json = (new Gson()).fromJson(new InputStreamReader(inputStream), JsonElement.class);

        try {
            FileWriter writer = new FileWriter(this.configFile);
            new GsonBuilder().setPrettyPrinting().create().toJson(json, writer);
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void loadConfig() {
        TaylorCommands.log("Loading config file " + this.fileName);

        if (!this.configFile.exists()) {
            TaylorCommands.log("Config file not found, creating...");
            this.generateConfigFile();
        }

        try {
            this.config = new JsonParser().parse(new FileReader(this.configFile)).getAsJsonObject();
        } catch (FileNotFoundException exception) {
            TaylorCommands.log(Level.FATAL, "Config file couldn't be read or created.", exception);
        }

        TaylorCommands.log("Config loaded successfully.");
    }

    public String getFileLocation() {
        return FabricLoader.getInstance().getConfigDirectory() + "/" + TaylorCommands.MODID + "/";
    }

    public String get(String key, String defaultValue) {
        return this.config.has(key) ? this.config.get(key).getAsString() : defaultValue;
    }

    public boolean get(String key, boolean defaultValue) {
        return this.config.has(key) ? this.config.get(key).getAsBoolean() : defaultValue;
    }

    public int get(String key, int defaultValue) {
        return this.config.has(key) ? this.config.get(key).getAsInt() : defaultValue;
    }

    public JsonObject get(String key, JsonObject defaultValue) {
        return this.config.has(key) ? this.config.get(key).getAsJsonObject() : defaultValue;
    }

    public JsonObject get(String key) {
        return this.get(key, new JsonObject());
    }
}
