package ca.samueltaylor.simple_commands.points;

import ca.samueltaylor.simple_commands.helpers.Logger;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class JsonFileWriter {
    public static void write(File file, Object object) {
        try {
            FileWriter writer = new FileWriter(file);
            new GsonBuilder().setPrettyPrinting().create().toJson(object, writer);
            writer.close();

            Logger.log(file.getName() + " saved successfully.");
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
