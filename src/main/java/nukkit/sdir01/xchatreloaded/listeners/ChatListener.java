package nukkit.sdir01.xchatreloaded.listeners;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.player.PlayerChatEvent;
import cn.nukkit.utils.TextFormat;
import nukkit.sdir01.xchatreloaded.LightChat;
import nukkit.sdir01.xchatreloaded.util.Message;
import ru.nukkit.multipass.Multipass;

public class ChatListener implements cn.nukkit.event.Listener {

    LightChat plugin;

    public ChatListener(LightChat plugin) {
        this.plugin = plugin;
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
    public void chatEvent(PlayerChatEvent event) {

        if(!plugin.chat.getStringList("disabled-worlds").contains(event.getPlayer().getLevel().getName())) {
        if(plugin.getHookedWithPlugin == "Multipass") {
            String player_group = Multipass.getGroup(event.getPlayer());
            if (!plugin.getMutedPlayers().contains(event.getPlayer().getName())) {
                if(plugin.chat.getString("general.custom-formatting." + Multipass.getGroup(event.getPlayer())) != null && plugin.chat.getString("general.custom-formatting." + Multipass.getGroup(event.getPlayer())) != "") {
                    event.setFormat(TextFormat.colorize(plugin.chat.getString("general.custom-formatting." + player_group)
                            .replaceAll("<prefix>", Multipass.getPrefix(event.getPlayer()))
                            .replaceAll("<player>", event.getPlayer().getDisplayName())
                            .replaceAll("<message>", event.getMessage())
                            .replaceAll("<world>", event.getPlayer().getLevel().getName())
                            .replaceAll("<level>", String.valueOf(event.getPlayer().getLevel()))));
                } else {
                    event.setFormat(TextFormat.colorize(plugin.chat.getString("general.format")
                            .replaceAll("<prefix>", Multipass.getPrefix(event.getPlayer()))
                            .replaceAll("<player>", event.getPlayer().getDisplayName())
                            .replaceAll("<message>", event.getMessage())
                            .replaceAll("<world>", event.getPlayer().getLevel().getName())
                            .replaceAll("<level>", String.valueOf(event.getPlayer().getLevel()))));
                }
            } else {
                event.setCancelled(true);
                Message.YOU_ARE_MUTED.print(event.getPlayer(), 'c');
            }

            // Disabled, because MadPerms and LeetPerms didn't have a methods to get player group, prefix and etc.
            // Contact the MadPerms developer if you want to see his plugin support here!
        /*} else if(plugin.getHookedWithPlugin == "LeetPerms") {

            if(plugin.chat.getString(LeetPerms.getPlugin().getDataManager().getDataProvider().getPlayerGroup(event.getPlayer().getName(), event.getPlayer().getName())) != null) {

                event.setFormat(TextFormat.colorize(plugin.chat.getString("format")
                        .replaceAll("<prefix>", LeetPerms.getAPI().getPrefix(event.getPlayer()))
                        .replaceAll("<player>", event.getPlayer().getDisplayName())
                        .replaceAll("<message>", event.getMessage())
                        .replaceAll("<world>", event.getPlayer().getLevel().getName())
                        .replaceAll("<level>", String.valueOf(event.getPlayer().getLevel()))));

            } else {

                event.setFormat(TextFormat.colorize(plugin.chat.getString(LeetPerms.getPlugin().getDataManager().getDataProvider().getPlayerGroup(event.getPlayer().getName(), event.getPlayer().getName()))
                        .replaceAll("<prefix>", LeetPerms.getAPI().getPrefix(event.getPlayer()))
                        .replaceAll("<player>", event.getPlayer().getDisplayName())
                        .replaceAll("<message>", event.getMessage())
                        .replaceAll("<world>", event.getPlayer().getLevel().getName())
                        .replaceAll("<level>", String.valueOf(event.getPlayer().getLevel()))));

            }

        } else if(plugin.getHookedWithPlugin == "MadPerms") {

            if(plugin.chat.getString(MadPerms.getPlugin().getAPI().) != null) {

                event.setFormat(TextFormat.colorize(plugin.chat.getString("format")
                        .replaceAll("<prefix>", Multipass.getGroupPrefix(Multipass.getGroup(event.getPlayer())))
                        .replaceAll("<player>", event.getPlayer().getDisplayName())
                        .replaceAll("<message>", event.getMessage())
                        .replaceAll("<world>", event.getPlayer().getLevel().getName())
                        .replaceAll("<level>", String.valueOf(event.getPlayer().getLevel()))));


            } else {

                event.setFormat(TextFormat.colorize(plugin.chat.getString(Multipass.getGroup(event.getPlayer()))
                        .replaceAll("<prefix>",Multipass.getGroupPrefix(Multipass.getGroup(event.getPlayer())))
                        .replaceAll("<player>", event.getPlayer().getDisplayName())
                        .replaceAll("<message>", event.getMessage())
                        .replaceAll("<world>", event.getPlayer().getLevel().getName())
                        .replaceAll("<level>", String.valueOf(event.getPlayer().getLevel()))));
            }*/
        }
        }

    }

}
