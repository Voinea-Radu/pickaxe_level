package dev.lightdream.pickaxelevel;

import org.apache.commons.io.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class Utils {

    public static void createFile(String path) {

        try {
            FileUtils.copyInputStreamToFile(PickaxeLevel.INSTANCE.getResource(path), new File(PickaxeLevel.INSTANCE.getDataFolder(), path));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(FileConfiguration config, String path) {

        File file = new File(Bukkit.getServer().getPluginManager().getPlugin(PickaxeLevel.NAME).getDataFolder(), path);
        try {
            if (!file.exists()) {
                boolean created = file.createNewFile();
                if (created)
                    PickaxeLevel.logger.info(path + " created!");
            }
            config.save(file);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public static FileConfiguration loadFile(String path) {

        File file = new File(Bukkit.getServer().getPluginManager().getPlugin(PickaxeLevel.NAME).getDataFolder(), path);
        if (file.exists()) {
            return YamlConfiguration.loadConfiguration(file);
        } else {
            createFile(path);
            return loadFile(path);
        }
    }

    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

}
