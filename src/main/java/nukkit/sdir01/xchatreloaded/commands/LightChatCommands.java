package nukkit.sdir01.xchatreloaded.commands;

import cn.nukkit.command.CommandSender;
import cn.nukkit.utils.TextFormat;
import nukkit.sdir01.xchatreloaded.util.Message;
import nukkit.sdir01.xchatreloaded.LightChat;

import static nukkit.sdir01.xchatreloaded.LightChat.*;

/**
 * nukkit.sdir01.xchatreloaded.commands developed by undefined1
 * <p>
 * This project can be modified by another user, but you need paste link to original GitHub or other project page!
 * You can use software API and making addons without links to this project.
 * <p>
 * Project create date: 31.07.2016
 * Adv4Core and XonarTeam 2016 (c) All rights reserved.
 */
public class LightChatCommands extends CommandListener {

    private LightChat plugin;

    public LightChatCommands(LightChat plugin) {
        super("lchat", Message.COMMAND_USE, "");
        this.setAliases(new String[]{"lc","chat"});
        this.setPermission("LightChat.admin");
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        if (!sender.hasPermission(this.getPermission())) {
            Message.YOU_DONT_HAVE_PERMISSION.print(sender, "xchat.admin");
        } else {
            if (args.length == 0) {
                sender.sendMessage(TextFormat.colorize("&aLightChat &7developed by &aLightEyeTM"));
                sender.sendMessage(TextFormat.colorize("&7Type &a/" + commandLabel + " help &7for call help menu"));
                sender.sendMessage(TextFormat.colorize("&7Active permissions plugin is &a" + plugin.getHookedWithPlugin));

            } else if (args[0].equalsIgnoreCase("help")) {

                sender.sendMessage(TextFormat.colorize("&a/" + commandLabel + " reload (-save) &7- Reload all configurations."));

            } else if (args[0].equalsIgnoreCase("reload")) {
                if(args.length == 1) {
                    config.reload();
                    mute.reload();
                    chat.reload();
                    motd.reload();
                    Message.RELOAD_CMD.print(sender, 'a');
                } else if (args[1].equalsIgnoreCase("-save")) {
                    config.save();
                    mute.save();
                    chat.save();
                    motd.save();
                    config.reload();
                    mute.reload();
                    chat.reload();
                    motd.reload();
                    Message.RELOAD_CMD_with_save.print(sender, 'a');
                } else {
                    config.reload();
                    mute.reload();
                    chat.reload();
                    motd.reload();
                    Message.RELOAD_CMD.print(sender, 'a');
                }
            }
        }
        return true;
    }
}

