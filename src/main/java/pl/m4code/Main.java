package pl.m4code;

import lombok.Getter;
import lombok.SneakyThrows;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;
import pl.m4code.commands.BossBarCommand;
import pl.m4code.commands.api.CommandAPI;
import pl.m4code.listeners.PlayerJoinListener;

import java.lang.reflect.Field;
import java.util.List;

@Getter
public final class Main extends JavaPlugin {
    @Getter private static Main instance;

    public static Main getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {
        saveDefaultConfig();
        unregisterDefaultCommand("bossbar");

        try {
            registerCommands();
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        registerListeners();
    }

    @SneakyThrows
    private void registerCommands() throws NoSuchFieldException, IllegalAccessException {
        final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
        bukkitCommandMap.setAccessible(true);
        final CommandMap commandMap = (CommandMap) bukkitCommandMap.get(getServer());
        for (CommandAPI commands : List.of(
                new BossBarCommand()
        )) {
            commandMap.register(commands.getName(), commands);
        }
    }

    private void unregisterDefaultCommand(String commandName) {
        try {
            final Field bukkitCommandMap = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            bukkitCommandMap.setAccessible(true);
            final CommandMap commandMap = (CommandMap) bukkitCommandMap.get(Bukkit.getServer());

            Command command = commandMap.getCommand(commandName);
            if (command != null) {
                command.unregister(commandMap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerJoinListener(), this);
    }
}