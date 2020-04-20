package alant7_;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.configuration.file.FileConfiguration;

public class Configuration {

    public static List<String> Stats = new ArrayList<>();
    
    public static String SingleStatMessage = "";

    public static String TimePlayedFormat = "";

    public static void LoadConfiguration(FileConfiguration fc) {
        Stats = fc.getStringList("stats");

        SingleStatMessage = fc.getString("single-stat-message");

        TimePlayedFormat = fc.getString("time-played-format");
    }

}