package pl.m4code.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import pl.m4code.Main;
import pl.m4code.utils.TextUtil;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class BlockedCommand implements Listener
{
    private final Main plugin;
    private final Map<UUID, String> lastMessages = new HashMap<>();

    public BlockedCommand(Main plugin) {
        this.plugin = plugin;
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onBlockedCmd(PlayerCommandPreprocessEvent e) {
        Player p = e.getPlayer();
        String message = e.getMessage();
        String[] splittedMessage = message.split(" ");
        String[] pluginCommands = { "/?",
                "/bossbar" };

        for (String string : pluginCommands) {
            if (string.equalsIgnoreCase(splittedMessage[0])) {

                e.setCancelled(true);
            }
        }
    }
}