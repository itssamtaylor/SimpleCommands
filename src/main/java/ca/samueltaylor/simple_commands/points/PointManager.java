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

    public HashMap<String, Point> points = new HashMap<>();
    protected File pointFile;

    public PointManager() {
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
               this.add(this.pointFromJson(set.getKey(), set.getValue().getAsJsonObject()));
           });
           Logger.log(this.getFileName() + " - Successfully loaded " + this.points.size() + " points.");
        } catch(FileNotFoundException exception) {
           Logger.fatal("Could not load " + this.getFileName());
        }
    }

    public void add(Point point) {
        this.points.put(point.name, point);
    }

    public void delete(String pointName) {
        this.points.remove(pointName);
    }

    abstract public void save();

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

    abstract public String getFileName();
}
