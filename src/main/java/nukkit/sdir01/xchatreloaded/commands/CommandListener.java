package nukkit.sdir01.xchatreloaded.commands;

/**
 * nukkit.sdir01.xchatreloaded.commands developed by undefined1
 * <p>
 * This project can be modified by another user, but you need paste link to original GitHub or other project page!
 * You can use software API and making addons without links to this project.
 * <p>
 * Project create date: 31.07.2016
 * Adv4Core and XonarTeam 2016 (c) All rights reserved.
 */
import cn.nukkit.command.Command;
import nukkit.sdir01.xchatreloaded.util.Message;

public abstract class CommandListener extends Command {
    public CommandListener(String cmd, Message description, String cmd2) {
        super(cmd, description.toString(), cmd2);
    }
}
