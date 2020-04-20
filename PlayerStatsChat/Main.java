package alant7_;

import java.io.File;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static File FOLDER_PLAYERSTATS = null;

    @Override
    public void onEnable() {

        FindStatsDirectory();
        LoadConfiguration();

        getCommand("pstats").setExecutor(new Commands());
        Bukkit.getPluginManager().registerEvents(new Events(), this);

    }

    private void LoadConfiguration() {
        System.out.println("[PlayerStatsChat] Loading configuration...");
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            this.saveDefaultConfig();
            System.out.println("[PlayerStatsChat] Default configuration created");
        }
        
        Configuration.LoadConfiguration(getConfig());
        System.out.println("[PlayerStatsChat] Configuration loaded");
    }

    private void FindStatsDirectory() {
        System.out.println("[PlayerStatsChat] Looking for players' stats directory...");

        File server_dir = Bukkit.getServer().getWorldContainer();
        for (World w : Bukkit.getWorlds()) {
            File wf = new File(server_dir, w.getName());

            File pdata = new File(wf, "stats");
            if (pdata.exists() && pdata.isDirectory()) {
                FOLDER_PLAYERSTATS = pdata;
                break;
            }
        }

        if (FOLDER_PLAYERSTATS == null) {
            System.out.println("[PlayerStatsChat] Could not find stats directory. Plugin will not work");
            return;
        }

        System.out.println("[PlayerStatsChat] Stats directory found");
    }

    @Override
    public void onDisable() {

    }

}