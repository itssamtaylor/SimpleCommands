package ca.samueltaylor.simple_commands.points;

import ca.samueltaylor.simple_commands.SimpleCommands;
import ca.samueltaylor.simple_commands.helpers.Logger;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;

public class WarpPointManager {

    public HashMap<String, WarpPoint> points = new HashMap<>();
    protected File pointFile;
    protected boolean changed = false;

    public static WarpPointManager instance() {
        return SimpleCommands.warpPointManager;
    }

    public WarpPointManager() {
        Path pointFilePath = SimpleCommands.worldPath.resolve(SimpleCommands.MOD_ID);

        if(!pointFilePath.toFile().exists()) {
            Logger.log(pointFilePath + " not found, creating...");
            pointFilePath.toFile().mkdir();
        }

        pointFile = pointFilePath.resolve(this.getFileName()).toFile();

        if(!pointFile.exists()) {
            Logger.log(pointFile.getName() + " not found, creating...");
            createFile();
        }
    }

    protected void createFile() {
        try {
            FileWriter writer = new FileWriter(pointFile);
            new GsonBuilder().setPrettyPrinting().create().toJson(new JsonObject(), writer);
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void load() {
        try {
            JsonObject json = new JsonParser().parse(new FileReader(pointFile)).getAsJsonObject();
            json.entrySet().iterator().forEachRemaining(set -> {
                this.points.put(set.getKey(), this.pointFromJson(set.getKey(), set.getValue().getAsJsonObject()));
            });
            Logger.log("Successfully loaded " + pointFile.getName());
        } catch(FileNotFoundException exception) {
            Logger.fatal("Could not load " + this.getFileName());
        }
    }

    protected void changed() {
        this.changed = true;
    }

    public void add(WarpPoint point) {
        this.points.put(point.name, point);
        this.changed();
    }

    public void delete(String pointName) {
        this.points.remove(pointName);
        this.changed();
    }

    public void save() {
        if(this.changed) {
            try {
                String data = new GsonBuilder().setPrettyPrinting().create().toJson(this.points);

                FileWriter writer = new FileWriter(pointFile);
                writer.write(data);
                writer.close();

                this.changed = false;
                Logger.log(pointFile.getName() + " saved successfully.");
            } catch (IOException exception) {
                exception.printStackTrace();
            }
        }
    }

    public WarpPoint getPoint(String name) {
        return this.points.get(name);
    }

    public String listPoints() {
        return this.points.keySet().toString();
    }

    protected WarpPoint pointFromJson(String name, JsonObject json) {
        return new WarpPoint(name, json);
    };

    public String getFileName() {
        return "warps.json";
    }
}
