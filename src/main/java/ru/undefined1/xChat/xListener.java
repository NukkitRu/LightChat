package ru.undefined1.xChat;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.event.player.PlayerJoinEvent;
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
    public void onJoin(PlayerJoinEvent e) {
        String getGroup = Multipass.getGroup(e.getPlayer());
            if (plugin.tags.getString("tags." + getGroup) != null) {
                if (plugin.tags.getString("tags." + e.getPlayer().getName()) != null) {
                    e.getPlayer().setNameTag(TextFormat.colorize(plugin.tags.getString("tags." + e.getPlayer().getName()) + " " + e.getPlayer().getDisplayName()));
                } else {
                    e.getPlayer().setNameTag(TextFormat.colorize(plugin.tags.getString("tags." + getGroup) + " " + e.getPlayer().getDisplayName()));
                }
                e.getPlayer().setNameTagVisible(true);
            }
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

                String GroupPrefix = (Multipass.getGroupPrefix(Multipass.getGroup(e.getPlayer())));
                if(plugin.chat.getString(Multipass.getGroup(e.getPlayer())) != null && plugin.chat.getString(Multipass.getGroup(e.getPlayer())) != "") {
                    String groupFormatting = plugin.chat.getString(Multipass.getGroup(e.getPlayer()));
                    e.setFormat(TextFormat.colorize(groupFormatting.replaceAll("<message>", message).replaceAll("<time>", sdf.format(cal.getTime())).replaceAll("<player>", playername).replaceAll("<world>", world).replaceAll("<level>", String.valueOf(level)).replaceAll("<prefix>", GroupPrefix)));
                } else {
                    String groupFormatting = plugin.chat.getString("format");
                    e.setFormat(TextFormat.colorize(groupFormatting.replaceAll("<message>", message).replaceAll("<time>", sdf.format(cal.getTime())).replaceAll("<player>", playername).replaceAll("<world>", world).replaceAll("<level>", String.valueOf(level)).replaceAll("<prefix>", GroupPrefix)));
                }

        } else {
            String MutedBy = plugin.mute.getString("mutedBy." + e.getPlayer().getName());
            String mute = plugin.getTranslation(TextFormat.colorize("chat.MUTED"));
            e.getPlayer().sendMessage(TextFormat.colorize(mute).replaceAll("%mutedBy%", MutedBy));
            e.setCancelled(true);
        }

    }

}
