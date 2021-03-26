package dev.lightdream.pickaxelevel;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@SuppressWarnings("unchecked")
public final class PickaxeLevel extends JavaPlugin {

    public static final String NAME = "PickaxeLevel";
    public static Logger logger;
    public static PickaxeLevel INSTANCE;
    public static FileConfiguration config;
    public static HashMap<Integer, HashMap<Integer, List<String>>> levelMap = new HashMap<>();

    @Override
    public void onEnable() {
        INSTANCE = this;
        logger = super.getLogger();
        getServer().getPluginManager().registerEvents(new Events(), this);
        config = Utils.loadFile("config.yml");

        List<String> list = (List<String>) config.getList("levels");
        for (int i = 0; i < list.size(); i++) {
            HashMap<Integer, List<String>> map = new HashMap<>();
            String[] str = list.get(i).split("\\|");
            try {
                List<String> commands = new ArrayList<>();
                Collections.addAll(commands, str);
                commands.remove(str[0]);
                map.put(Integer.parseInt(str[0]), commands);
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
