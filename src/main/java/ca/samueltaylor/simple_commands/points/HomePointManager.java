package ca.samueltaylor.simple_commands.points;

import ca.samueltaylor.simple_commands.SimpleCommands;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class HomePointManager extends PointManager {
    protected HashMap<String, HomePoint> points = new HashMap<String, HomePoint>();

    static {
        fileName = "homes.json";
    }

    protected HomePoint pointFromJson(String name, JsonObject json) {
        return new HomePoint(name, json);
    }

    public static HomePointManager instance() {
        return SimpleCommands.homePointManager;
    }

    @Override
    public HomePoint getPoint(String name) {
        return (HomePoint) super.getPoint(name);
    }
}
