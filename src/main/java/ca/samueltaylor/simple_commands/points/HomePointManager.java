package ca.samueltaylor.simple_commands.points;

import ca.samueltaylor.simple_commands.SimpleCommands;
import ca.samueltaylor.simple_commands.helpers.Logger;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.player.PlayerEntity;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;

public class HomePointManager {

    protected HashMap<String, HashMap<String, HomePoint>> homes = new HashMap<String, HashMap<String, HomePoint>>();
    protected File homeFile;
    protected boolean changed = false;
    
    public HomePointManager() {
        Path homeFilePath = SimpleCommands.worldPath.resolve(SimpleCommands.MOD_ID);

        if(!homeFilePath.toFile().exists()) {
            Logger.log(homeFilePath + " not found, creating...");
            homeFilePath.toFile().mkdir();
        }

        homeFile = homeFilePath.resolve(this.getFileName()).toFile();

        if(!homeFile.exists()) {
            Logger.log(homeFile.getName() + " not found, creating...");
            createFile();
        }
    }

    protected void createFile() {
        try {
            FileWriter writer = new FileWriter(homeFile);
            new GsonBuilder().setPrettyPrinting().create().toJson(new JsonObject(), writer);
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    protected HomePoint pointFromJson(String name, JsonObject json) {
        return new HomePoint(name, json);
    }

    public static HomePointManager instance() {
        return SimpleCommands.homePointManager;
    }

    public HashMap<String, HomePoint> getHomes(PlayerEntity player) {
        return this.homes.get(player.getUuidAsString());
    }

    public String listHomes(PlayerEntity player) {
        return this.getHomes(player).keySet().toString();
    }

    public HomePoint getPoint(PlayerEntity player, String homeName) {
        return this.getHomes(player).get(homeName);
    }
    
    public void load() {
        try {
            JsonObject json = new JsonParser().parse(new FileReader(homeFile)).getAsJsonObject();
            json.entrySet().iterator().forEachRemaining(set -> {
                HashMap<String, HomePoint> playerHomes = new HashMap<>();

                set.getValue().getAsJsonObject().entrySet().iterator().forEachRemaining(home -> {
                    playerHomes.put(home.getKey(), this.pointFromJson(home.getKey(), home.getValue().getAsJsonObject()));
                });

                this.homes.put(set.getKey(), playerHomes);
            });
            this.changed = false;
            Logger.log("Successfully loaded " + homeFile.getName());
        } catch (FileNotFoundException exception) {
            Logger.fatal("Could not load " + this.getFileName());
        }
    }

    public void add(PlayerEntity player, HomePoint homePoint) {
        if(this.homes.containsKey(player.getUuidAsString())) {
            this.homes.get(player.getUuidAsString()).put(homePoint.name, homePoint);
        } else {
            HashMap<String, HomePoint> playerHomes = new HashMap<String, HomePoint>();
            playerHomes.put(homePoint.name, homePoint);
            this.homes.put(player.getUuidAsString(), playerHomes);
        }
        this.changed();
    }

    public void delete(PlayerEntity player, String name) {
        this.homes.get(player.getUuidAsString()).remove(name);
        this.changed();
    }

    protected void changed() {
        this.changed = true;
    }

    public void save() {
        if(this.changed) {
            try {
                String data = new GsonBuilder().setPrettyPrinting().create().toJson(this.homes);

                FileWriter writer = new FileWriter(homeFile);
                writer.write(data);
                writer.close();

                this.changed = false;
                Logger.log(homeFile.getName() + " saved successfully.");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }
    
    public String getFileName() {
        return "homes.json";
    }

}