package dev.lightdream.pickaxelevel;

import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("unchecked")
public class Events implements Listener {

    @EventHandler(ignoreCancelled = true)
    public void onBlockBreak(BlockBreakEvent event) {

        ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
        Player player = event.getPlayer();
        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        Integer brokenBlocks = 0;

        if (nmsItem.getTag() == null)
            nmsItem.setTag(new NBTTagCompound());

        assert nmsItem.getTag() != null;
        if (nmsItem.getTag().hasKey("broken_blocks")) {
            nmsItem.getTag().set("broken_blocks", new NBTTagInt(nmsItem.getTag().getInt("broken_blocks") + 1));
            brokenBlocks = nmsItem.getTag().getInt("broken_blocks");
        } else
            nmsItem.getTag().set("broken_blocks", new NBTTagInt(1));


        item = CraftItemStack.asBukkitCopy(nmsItem);

        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();

        if (lore == null)
            lore = new ArrayList<>();

        int level = 0;

        String search = "";

        for(String configLine : (List<String>)PickaxeLevel.config.getList("lore"))
            if(configLine.contains("%level%"))
                search = configLine;
        search = Utils.color(search).replace("%level%", "");
        if(!search.equals(""))
            for (String line : lore)
                if (line.contains(search))
                    level = Integer.parseInt(line.replace(search, ""));


        int nextLevelBlocks = 0;
        for (int i : PickaxeLevel.levelMap.get(level + 1).keySet())
            nextLevelBlocks = i;


        if (brokenBlocks > nextLevelBlocks) {
            for(String command : PickaxeLevel.levelMap.get(level + 1).get(nextLevelBlocks))
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, command));
            level++;
        }


        /*
        List<String> remove = new ArrayList<>();


        for(String line : lore)
            for(String lineConfig : (List<String>) PickaxeLevel.config.getList("lore"))
                if(line.contains(Utils.color(lineConfig.replace("%blocks%", "").replace("%levels%", ""))))
                    remove.add(line);

        lore.removeAll(remove);

         */

        lore = new ArrayList<>();

        for(Enchantment e : item.getEnchantments().keySet())
            System.out.println(e.getName() + " " + item.getEnchantments().get(e));

        for(String line : (List<String>) PickaxeLevel.config.getList("lore"))
            lore.add(PlaceholderAPI.setPlaceholders(player, line.replace("%blocks%", String.valueOf(brokenBlocks)).replace("%level%", String.valueOf(level))));

        meta.setLore(lore);
        item.setItemMeta(meta);

        player.getInventory().setItemInMainHand(item);
    }

}
