package ru.undefined1.xChat;

import cc.leet.leetperms.util.PermissionAPI;
import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.utils.TextFormat;
import ru.nukkit.multipass.Multipass;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class xListener implements Listener {

    xChat plugin;

    public xListener(xChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler (priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onChat(PlayerChatEvent e) {

        if(!plugin.mute.getList("mutedPlayers").contains(e.getPlayer().getName())) {

            String message = e.getMessage();
            String playername = e.getPlayer().getDisplayName();
            String world = e.getPlayer().getLevel().getName();
            int level = e.getPlayer().getExperienceLevel();

            Calendar cal = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            plugin.chat.reload();

            if(plugin.getServer().getPluginManager().getPlugin("Multipass").isEnabled()) {
                String GroupPrefix = (Multipass.getGroupPrefix(Multipass.getGroup(e.getPlayer())));
                if(plugin.chat.getString(Multipass.getGroup(e.getPlayer())) != null && plugin.chat.getString(Multipass.getGroup(e.getPlayer())) != "") {
                    String groupFormatting = plugin.chat.getString(Multipass.getGroup(e.getPlayer()));
                    e.setFormat(TextFormat.colorize(groupFormatting.replaceAll("<message>", message).replaceAll("<time>", sdf.format(cal.getTime())).replaceAll("<player>", playername).replaceAll("<world>", world).replaceAll("<level>", String.valueOf(level)).replaceAll("<prefix>", GroupPrefix)));
                } else {
                    String groupFormatting = plugin.chat.getString("format");
                    e.setFormat(TextFormat.colorize(groupFormatting.replaceAll("<message>", message).replaceAll("<time>", sdf.format(cal.getTime())).replaceAll("<player>", playername).replaceAll("<world>", world).replaceAll("<level>", String.valueOf(level)).replaceAll("<prefix>", GroupPrefix)));
                }
            } else if(plugin.getServer().getPluginManager().getPlugin("LeetPerms").isEnabled()) {
                PermissionAPI api = new PermissionAPI();
                String GroupPrefix = api.getPrefix(e.getPlayer());
                String formatting = plugin.chat.getString("format");
                e.setFormat(TextFormat.colorize(formatting.replaceAll("<message>", message).replaceAll("<time>", sdf.format(cal.getTime())).replaceAll("<player>", playername).replaceAll("<world>", world).replaceAll("<level>", String.valueOf(level)).replaceAll("<prefix>", GroupPrefix)));
            }

        } else {
            String MutedBy = plugin.mute.getString("mutedBy." + e.getPlayer().getName());
            String mute = plugin.getTranslation(TextFormat.colorize("chat.MUTED"));
            e.getPlayer().sendMessage(TextFormat.colorize(mute).replaceAll("%mutedBy%", MutedBy));
            e.setCancelled(true);
        }

    }

}
