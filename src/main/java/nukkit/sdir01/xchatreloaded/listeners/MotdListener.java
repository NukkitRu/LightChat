package nukkit.sdir01.xchatreloaded.listeners;

import cn.nukkit.event.EventHandler;
import cn.nukkit.event.EventPriority;
import cn.nukkit.event.Listener;
import cn.nukkit.event.player.PlayerJoinEvent;
import cn.nukkit.utils.TextFormat;
import me.imjack.permissions.MadPerms;
import me.imjack.permissions.group.Group;
import nukkit.sdir01.xchatreloaded.LightChat;
import ru.nukkit.multipass.Multipass;

/**
 * nukkit.sdir01.xchatreloaded.listeners developed by undefined1
 * <p>
 * This project can be modified by another user, but you need paste link to original GitHub or other project page!
 * You can use software API and making addons without links to this project.
 * <p>
 * Project create date: 31.07.2016
 * Adv4Core and XonarTeam 2016 (c) All rights reserved.
 */
public class MotdListener implements Listener {

	LightChat plugin;

	public MotdListener(LightChat plugin) {
		this.plugin = plugin;
	}

	@EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = false)
	public void motdShow(PlayerJoinEvent event) {

		if (plugin.motd.getBoolean("general.enabled", false)) {
			if (plugin.getHookedWithPlugin == "Multipass") {
				for (String message : plugin.motd.getStringList("general.motd-lines")) {
					event.getPlayer().sendMessage(TextFormat.colorize(message
                            .replaceAll("<prefix>", Multipass.getPrefix(event.getPlayer()))
                            .replaceAll("<player>", event.getPlayer().getDisplayName())
                            .replaceAll("<world>", event.getPlayer().getLevel().getName())
                            .replaceAll("<group>", Multipass.getGroup(event.getPlayer()))));
				}
			} else if (plugin.getHookedWithPlugin == "MadPerms") {
				Group group = MadPerms.getPlugin().getAPI().getMainGroup(event.getPlayer());
				for (String message : plugin.motd.getStringList("general.motd-lines")) {
				event.getPlayer().sendMessage(TextFormat.colorize(message
                        .replaceAll("<prefix>", group.getPrefix())
                        .replaceAll("<suffix>", group.getSuffix())
                        .replaceAll("<player>", event.getPlayer().getDisplayName())
                        .replaceAll("<world>", event.getPlayer().getLevel().getName())
                        .replaceAll("<group>", group.getName())));
				}
			}
		}
	}
}
