package dev.lightdream.pickaxelevel;

import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import org.bukkit.Bukkit;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
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

        for (String line : lore) {
            String search = Utils.color(PickaxeLevel.config.getString("level-lore")).replace("%level%", "");
            if (line.contains(search))
                level = Integer.parseInt(line.replace(search, ""));
        }

        int nextLevelBlocks = 0;
        for (int i : PickaxeLevel.levelMap.get(level + 1).keySet())
            nextLevelBlocks = i;

        System.out.println(nextLevelBlocks);

        if (brokenBlocks > nextLevelBlocks) {
            System.out.println(PickaxeLevel.levelMap.get(level + 1).get(nextLevelBlocks));
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), PlaceholderAPI.setPlaceholders(player, PickaxeLevel.levelMap.get(level + 1).get(nextLevelBlocks)));
            level++;
        }


        lore.removeIf(line -> line.contains(Utils.color(PickaxeLevel.config.getString("broken-blocks-lore").replace("%blocks%", ""))));
        lore.removeIf(line -> line.contains(Utils.color(PickaxeLevel.config.getString("level-lore").replace("%level%", ""))));

        lore.add(Utils.color(PickaxeLevel.config.getString("broken-blocks-lore").replace("%blocks%", String.valueOf(brokenBlocks))));
        lore.add(Utils.color(PickaxeLevel.config.getString("level-lore").replace("%level%", String.valueOf(level))));
        meta.setLore(lore);
        item.setItemMeta(meta);

        player.getInventory().setItemInMainHand(item);
    }

}
