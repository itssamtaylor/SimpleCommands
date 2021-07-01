package ca.samueltaylor.simple_commands.points;

import ca.samueltaylor.simple_commands.SimpleCommands;
import com.google.gson.JsonObject;

import java.util.HashMap;

public class WarpPointManager extends PointManager {
    protected HashMap<String, WarpPoint> points = new HashMap<String, WarpPoint>();

    static {
        fileName = "warps.json";
    }

    protected WarpPoint pointFromJson(String name, JsonObject json) {
        return new WarpPoint(name, json);
    }

    public static WarpPointManager instance() {
        return SimpleCommands.warpPointManager;
    }

    @Override
    public WarpPoint getPoint(String name) {
        return (WarpPoint) super.getPoint(name);
    }
}
