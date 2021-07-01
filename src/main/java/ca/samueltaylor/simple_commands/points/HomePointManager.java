package ca.samueltaylor.simple_commands.points;

import ca.samueltaylor.simple_commands.SimpleCommands;
import ca.samueltaylor.simple_commands.helpers.Logger;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.minecraft.entity.player.PlayerEntity;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.HashMap;

public class HomePointManager extends PointManager {

    protected HashMap<String, HashMap<String, HomePoint>> points = new HashMap<>();

    public HomePointManager() {
        super();
    }

    protected HomePoint pointFromJson(String name, JsonObject json) {
        return new HomePoint(name, json);
    }

    public static HomePointManager instance() {
        return SimpleCommands.homePointManager;
    }

    public HashMap<String, HomePoint> getHomes(PlayerEntity player) {
        return this.points.get(player.getUuidAsString());
    }

    public String listPoints(PlayerEntity player) {
        return this.getHomes(player).keySet().toString();
    }

    public HomePoint getPoint(PlayerEntity player, String homeName) {
        return this.getHomes(player).get(homeName);
    }

    @Override
    public void load() {
        try {
            JsonObject json = new JsonParser().parse(new FileReader(pointFile)).getAsJsonObject();
            json.entrySet().iterator().forEachRemaining(set -> {
                HashMap<String, HomePoint> playerHomes = new HashMap<>();

                set.getValue().getAsJsonObject().entrySet().iterator().forEachRemaining(home -> {
                    playerHomes.put(home.getKey(), this.pointFromJson(home.getKey(), home.getValue().getAsJsonObject()));
                });

                this.points.put(set.getKey(), playerHomes);
            });
            this.changed = false;
            Logger.log("Successfully loaded " + pointFile.getName());
        } catch (FileNotFoundException exception) {
            Logger.fatal("Could not load " + this.getFileName());
        }
    }

    public void add(PlayerEntity player, HomePoint homePoint) {
        if (this.points.containsKey(player.getUuidAsString())) {
            this.points.get(player.getUuidAsString()).put(homePoint.name, homePoint);
        } else {
            HashMap<String, HomePoint> playerHomes = new HashMap<>();
            playerHomes.put(homePoint.name, homePoint);
            this.points.put(player.getUuidAsString(), playerHomes);
        }
        this.changed = true;
    }

    public void delete(PlayerEntity player, String name) {
        this.points.get(player.getUuidAsString()).remove(name);
        this.changed = true;
    }

    @Override
    public String getFileName() {
        return "homes.json";
    }

}