package dev.lightdream.pickaxelevel;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("unchecked")
public final class PickaxeLevel extends JavaPlugin {

    public static final String NAME = "PickaxeLevel";
    public static Logger logger;
    public static PickaxeLevel INSTANCE;
    public static FileConfiguration config;
    public static HashMap<Integer, HashMap<Integer, String>> levelMap = new HashMap<>();

    public static double getLevel(double xp) {
        if (xp == 0)
            return 0;
        return log((13 * xp + 1599) / (13000 - 1000 * Math.pow(130, 1.0 / 2.0)), 13.0 / 10.0);
    }

    public static double log(double a, double b) {
        return Math.log(a) / Math.log(b);
    }

    @Override
    public void onEnable() {
        INSTANCE = this;
        logger = super.getLogger();
        getServer().getPluginManager().registerEvents(new Events(), this);
        config = Utils.loadFile("config.yml");

        List<String> list = (List<String>) config.getList("levels");
        for (int i = 0; i < list.size(); i++) {
            HashMap<Integer, String> map = new HashMap<>();
            String[] str = list.get(i).split("\\|");
            try {
                map.put(Integer.parseInt(str[0]), str[1]);
            } catch (NumberFormatException e) {
                logger.severe("Unable to parse " + str[0]);
                e.printStackTrace();
            }
            levelMap.put(i, map);
        }
    }

    @Override
    public void onDisable() {

    }


}
