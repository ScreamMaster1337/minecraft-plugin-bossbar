package pl.m4code.utils;

import org.bukkit.entity.Player;
import pl.m4code.Main;


import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;

public class SendPlayer {
    public static void toServer(Player p, String target) {


        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeUTF("Connect");
            dos.writeUTF(target);
        } catch (Exception e) {
            e.printStackTrace();
        }

        p.sendPluginMessage(Main.getPlugin(Main.class), "BungeeCord", baos.toByteArray());
    }
}
