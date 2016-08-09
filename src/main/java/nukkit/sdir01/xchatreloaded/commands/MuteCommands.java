package nukkit.sdir01.xchatreloaded.commands;

import cn.nukkit.command.CommandSender;
import nukkit.sdir01.xchatreloaded.util.Message;
import nukkit.sdir01.xchatreloaded.LightChat;

import java.util.List;

import static nukkit.sdir01.xchatreloaded.LightChat.mute;

/**
 * nukkit.sdir01.xchatreloaded.commands developed by undefined1
 * <p>
 * This project can be modified by another user, but you need paste link to original GitHub or other project page!
 * You can use software API and making addons without links to this project.
 * <p>
 * Project create date: 31.07.2016
 * Adv4Core and XonarTeam 2016 (c) All rights reserved.
 */
public class MuteCommands extends CommandListener {

    private LightChat plugin;

    public MuteCommands(LightChat plugin) {
        super("mute", Message.COMMAND_USE, "");
        this.setAliases(new String[]{"unmute"});
        this.setPermission("LightChat.mute");
        this.plugin = plugin;
    }

    public boolean execute(CommandSender sender, String commandLabel, String[] args) {
        List muted = mute.getList("muted-players");
        if (sender.hasPermission(this.getPermission())) {
            if (args.length == 0) {
                Message.COMMAND_USE.print(sender, "player mute/unmute", "/" + commandLabel);
            } else {
                if (!muted.contains(args[0])) {
                    mute.set("muted-players-data." + args[0], sender.getName());
                    muted.add(args[0]);
                    Message.YOU_MUTE.print(sender, args[0]);
                } else if (muted.contains(args[0])) {
                    mute.remove("muted-players-data." + args[0]);
                    muted.remove(args[0]);
                    Message.YOU_UNMUTE.print(sender, args[0]);
                }
                mute.save();
                mute.reload();
            }
        }
        return true;
    }

}
