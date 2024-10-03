package pl.m4code.commands;

import lombok.NonNull;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import pl.m4code.Main;
import pl.m4code.commands.api.CommandAPI;
import pl.m4code.utils.TextUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class BossBarCommand extends CommandAPI {
    private static final Map<UUID, BossBar> activeBossBars = new HashMap<>();

    public BossBarCommand() {
        super("bossbar",
                "",
                "",
                "/bossbar <color> <style> <time> <title>",
                List.of("bb"));
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof Player player)) {
            TextUtil.sendMessage(sender, "&cPodana komenda jest dostępna tylko dla graczy!");
            return;
        }

        if (!player.hasPermission("m4code.bossbar")) {
            TextUtil.sendMessage(player, "&cNie masz uprawnień do używania tej komendy!");
            return;
        }

        if (args.length < 4) {
            TextUtil.sendMessage(player, "&4Błąd &cPoprawne użycie: &7/bossbar <color> <style> <time> <title>");
            return;
        }

        String colorValue = args[0];
        String styleValue = args[1];
        String timeValue = args[2];
        String titleValue = String.join(" ", Arrays.copyOfRange(args, 3, args.length));
        titleValue = TextUtil.fixColor(titleValue);

        try {
            BarColor color = BarColor.valueOf(colorValue.toUpperCase());
            BarStyle style = BarStyle.valueOf(styleValue.toUpperCase());
            int time = parseTime(timeValue);

            BossBar bossBar = Bukkit.createBossBar(titleValue, color, style);
            bossBar.setVisible(true);

            for (Player onlinePlayer : Bukkit.getOnlinePlayers()) {
                bossBar.addPlayer(onlinePlayer);
                activeBossBars.put(onlinePlayer.getUniqueId(), bossBar);
            }

            Bukkit.getScheduler().runTaskLater(Main.getInstance(), () -> {
                bossBar.removeAll();
                activeBossBars.clear();
            }, time * 20L);
            TextUtil.sendMessage(player, "&aBossbar został ustawiony!");

        } catch (IllegalArgumentException e) {
            TextUtil.sendMessage(player, "&cNieprawidłowy kolor lub styl. Dostępne kolory: " + Arrays.toString(BarColor.values()) + ", dostępne style: " + Arrays.toString(BarStyle.values()));
        }
    }

    private int parseTime(String timeValue) throws NumberFormatException {
        Pattern pattern = Pattern.compile("^(\\d+)([smhd])$");
        Matcher matcher = pattern.matcher(timeValue);
        if (matcher.matches()) {
            int time = Integer.parseInt(matcher.group(1));
            String unit = matcher.group(2);
            switch (unit) {
                case "s":
                    return time;
                case "m":
                    return time * 60;
                case "h":
                    return time * 3600;
                case "d":
                    return time * 86400;
                default:
                    throw new NumberFormatException();
            }
        } else {
            throw new NumberFormatException();
        }
    }

    @Override
    public List<String> tab(@NonNull Player player, @NotNull @NonNull String[] args) {
        if (!player.hasPermission("m4code.bossbar")) {
            return Collections.emptyList();
        }

        if (args.length == 1) {
            return Arrays.stream(BarColor.values()).map(Enum::name).collect(Collectors.toList());
        } else if (args.length == 2) {
            return Arrays.stream(BarStyle.values()).map(Enum::name).collect(Collectors.toList());
        } else if (args.length == 3) {
            return List.of("10s", "10m", "10h", "10d");
        }
        return null;
    }

    public static BossBar getActiveBossBar(UUID playerId) {
        return activeBossBars.get(playerId);
    }
}