package ca.samueltaylor.simple_commands.points;

import ca.samueltaylor.simple_commands.SimpleCommands;
import ca.samueltaylor.simple_commands.helpers.Logger;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.*;
import java.nio.file.Path;
import java.util.HashMap;

abstract public class PointManager {

    public static String fileName;
    public HashMap<String, Point> points = new HashMap<String, Point>();
    protected Path pointFilePath;
    protected File pointFile;

    public PointManager() {
        this.pointFilePath = SimpleCommands.worldPath.resolve(SimpleCommands.MOD_ID);

        if(!this.pointFilePath.toFile().exists()) {
            Logger.log(this.pointFilePath.toString() + " not found, creating...");
            this.pointFilePath.toFile().mkdir();
        }

        this.pointFile = this.pointFilePath.resolve(fileName).toFile();

        if(!this.pointFile.exists()) {
            Logger.log(fileName + " not found, creating...");
            createFile();
        }
    }

    protected void createFile() {
        try {
            FileWriter writer = new FileWriter(this.pointFile);
            new GsonBuilder().setPrettyPrinting().create().toJson(new JsonObject(), writer);
            writer.close();
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public void load() {
        try {
           JsonObject json = new JsonParser().parse(new FileReader(this.pointFile)).getAsJsonObject();
           json.entrySet().iterator().forEachRemaining(set -> {
               this.add(this.pointFromJson(set.getKey(), set.getValue().getAsJsonObject()));
           });
           Logger.log(fileName + " - Successfully loaded " + this.points.size() + " points.");
        } catch(FileNotFoundException exception) {
           Logger.fatal("Could not load " + fileName);
        }
    }

    public void add(Point point) {
        this.points.put(point.name, point);
    }

    public void save() {
        try {
            FileWriter writer = new FileWriter(this.pointFile);
            new GsonBuilder().setPrettyPrinting().create().toJson(this.points, writer);
            writer.close();

            Logger.log(fileName + " saved successfully.");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }

    public static PointManager instance() {
        return SimpleCommands.warpPointManager;
    }

    public Point getPoint(String name) {
        return this.points.get(name);
    }

    public String listPoints() {
        return this.points.keySet().toString();
    }

    abstract protected Point pointFromJson(String name, JsonObject json);
}
