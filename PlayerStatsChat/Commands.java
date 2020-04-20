package alant7_;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender s, Command c, String l, String[] a) {

        if (c.getName().equalsIgnoreCase("pstats")) {

            if (s instanceof Player) {
                Player p = (Player) s;
                if ((!p.hasPermission("pstats.stats") && a.length == 0)
                        || (!p.hasPermission("pstats.stats.player") && a.length >= 1 && a[0].toLowerCase() != p.getName().toLowerCase())) {
                    p.sendMessage("§cYou have no access to this command.");
                    return false;
                }
            }

            // SELF STATS CHECK
            OfflinePlayer Target = null;
            if (a.length == 0) {
                if (!(s instanceof Player)) {
                    s.sendMessage("§cCommand usage: §r/pstats [player]");
                    return false;
                }
                Target = (Player) s;
            } else Target = Bukkit.getOfflinePlayer(a[0]);

            if (Target != null) {

                File f = new File(Main.FOLDER_PLAYERSTATS, Target.getUniqueId() + ".json");
                if (f.exists()) {

                    try {
                        BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(f)));

                        String playerStats = "", line;
                        while ((line = br.readLine()) != null) playerStats += line;

                        br.close();

                        List<String> statList = Configuration.Stats;
                        if (a.length == 2) {
                            if (s instanceof Player && !((Player)s).hasPermission("pstats.stats.specific"));
                            else statList = Arrays.asList(Configuration.SingleStatMessage
                                .replace("{value}", "{" + a[1] + "}")
                                .replace("{stat}", a[1]));
                        }

                        for (int j = 0; j < statList.size(); j++) {

                            String stat = statList.get(j).replace("{player}", Target.getName());
                            String statLine = "";

                            boolean Open = false;
                            for (int i = 0; i < stat.length(); i++) {
                                if (stat.charAt(i) == '{') {
                                    Open = true;
                                    continue;
                                }

                                if (stat.charAt(i) == '}') {
                                    Open = false;
                                    continue;
                                }

                                if (Open) statLine += stat.charAt(i);
                            }

                            if (statLine.length() > 0 && statLine.contains(":")) {
                                String statGroup = statLine.split(":")[0];
                                String statName = statLine.split(":")[1];

                                s.sendMessage(ChatColor.translateAlternateColorCodes('&', stat.replace("{" + statGroup + ":" + statName + "}", GetPlayerStatValue(playerStats, statGroup, statName))));
                                continue;
                            }
                            s.sendMessage(ChatColor.translateAlternateColorCodes('&', stat));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return false;
                }
            }

            s.sendMessage("§cPlayer not found");
            return false;
        }

        return false;
    }

    public String GetPlayerStatValue(String playerStats, String statGroup, String statName) {
        String statValue = "0";

        statGroup = statGroup.replace("minecraft.", "");
        statName = statName.replace("minecraft.", "");

        if (playerStats.contains("\"minecraft:" + statGroup + "\"")) {
            try {
                String allStatsInGroup = playerStats.split("\"minecraft:" + statGroup + "\"\\:\\{")[1].split("\\}")[0];

                String tempStatValue = allStatsInGroup.split("\"minecraft:" + statName + "\":")[1];
                if (tempStatValue.contains(",")) tempStatValue = tempStatValue.split(",")[0];
                if (tempStatValue.contains("}")) tempStatValue = tempStatValue.split("\\}")[0];

                statValue = tempStatValue;

                if (statName.equals("play_one_minute")) {
                    int Ticks = Integer.parseInt(statValue);

                    int S = Ticks / 20;
                    int M = S / 60;
                    
                    int H = M / 60;
                    M -= H * 60;

                    int D = H / 24;
                    H -= D * 24;

                    statValue = Configuration.TimePlayedFormat
                                .replace("{D}", "" + D)
                                .replace("{H}", "" + H)
                                .replace("{M}", "" + M);
                }

            } catch (Exception e) {}
        }

        return statValue;
    }

}