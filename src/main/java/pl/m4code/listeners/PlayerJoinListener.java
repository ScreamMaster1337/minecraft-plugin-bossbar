package pl.m4code.listeners;

import org.bukkit.boss.BossBar;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import pl.m4code.commands.BossBarCommand;

public class PlayerJoinListener implements Listener {

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        BossBar bossBar = BossBarCommand.getActiveBossBar(event.getPlayer().getUniqueId());
        if (bossBar != null) {
            bossBar.addPlayer(event.getPlayer());
        }
    }
}