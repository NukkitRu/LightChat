package ru.undefined1.xChat;

import cn.nukkit.command.Command;
import cn.nukkit.command.CommandSender;
import cn.nukkit.event.Listener;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.File;
import java.util.List;

public class xChat extends PluginBase implements Listener {

    private static xChat instance;

    public static xChat getPlugin() {
        return instance;
    }

    private File chatSettings;
    public static Config chat;

    private File cfg;
    public static Config config;

    private File muted;
    public static Config mute;

    private File Tags;
    public static Config tags;

    public String getTranslation(String message) {
        return config.getString(TextFormat.colorize(message));
    }

    public String getPrefixTranslation(String message) {
            String prefix = config.getString("global.prefix");
            return prefix + " " + config.getString(TextFormat.colorize(message));
    }

    public static Boolean isMuted(String playerName) {
        if(mute.getList("mutedPlayers").contains(playerName)) {
            return  true;
        } else {
            return false;
        }
    }

    public static List<String> getMutedPlayers() {
        if(!mute.getList("mutedPlayers").isEmpty()) {
            return mute.getStringList("mutedPlayers");
        } else {
            return null;
        }
    }

    public static String getMutedBy(String playerName) {
        if(mute.getString("mutedBy." + playerName) == "") {
            return null;
        } else {
            return mute.getString("mutedBy." + playerName);
        }
    }

    private void sendChatMessage(String message) {
        getServer().getConsoleSender().sendMessage(TextFormat.colorize("&8[&bxChat&8] &7" + message));
    }

    private void loadData() {
        this.getDataFolder().mkdirs();
        this.saveDefaultConfig();

        this.saveResource("chat.yml", false);
        this.saveResource("mute.yml", false);
        this.saveResource("config.yml", false);

        this.cfg = new File(this.getDataFolder(), "config.yml");
        config = new Config(this.cfg, 2);

        this.chatSettings = new File(this.getDataFolder(), "chat.yml");
        chat = new Config(this.chatSettings, 2);

        this.muted = new File(this.getDataFolder(), "mute.yml");
        mute = new Config(this.muted, 2);

        this.Tags = new File(this.getDataFolder(), "config.yml");
        tags = new Config(this.Tags, 2);

        sendChatMessage("&7######### &bxChat &7by &eimpact &7###########");
    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {

        this.loadData();

        if(getServer().getPluginManager().getPlugin("Multipass") != null) {
            sendChatMessage("&e* Associated with &aMultipass");
            getServer().getPluginManager().enablePlugin(getServer().getPluginManager().getPlugin("xChat"));
        } else {
            sendChatMessage("&c* You don't have a Multipass plugin! Install it from &enukkit.ru");
            getServer().getPluginManager().disablePlugin(getServer().getPluginManager().getPlugin("xChat"));
        }

        if(getServer().getPluginManager().getPlugin("xClans") != null) {
            sendChatMessage("&e* Associated with &axClans");
        }

        if(getServer().getPluginManager().getPlugin("CraftWallet") != null) {
            sendChatMessage("&e* Associated with &aCraftWallet");
        }


        this.getServer().getPluginManager().registerEvents(new xListener(this), this);
    }

    @Override
    public boolean onCommand(CommandSender s, Command cmd, String cmdLabel, String[] args) {

        if(cmdLabel.equalsIgnoreCase("xChat")) {
            if(s.hasPermission("xChat.admin")) {
                if (args.length == 0) {

                    s.sendMessage(TextFormat.colorize("&8[&dxChat&8] &8Made by &eundefined1"));
                    s.sendMessage(TextFormat.colorize("&aType &7/xChat help &ato call help menu"));

                } else if (args[0].equalsIgnoreCase("help")) {

                    s.sendMessage(TextFormat.colorize("&7- &d/xChat reload &8- Reload all configurations."));
                    s.sendMessage(TextFormat.colorize("&7- &d/xChat format &8- Edit chat formatting."));

                } else if (args[0].equalsIgnoreCase("format")) {
                    if(args.length == 1) {
                        s.sendMessage(TextFormat.colorize("&7- &d/xChat format <format> <group>"));
                    } else if(args.length == 2) {
                        chat.set("format", args[1]);
                        chat.save();
                        chat.reload();
                        s.sendMessage(TextFormat.colorize(getPrefixTranslation("messages.FORMAT")));
                    } else {
                        chat.set(args[1], args[2].replaceAll("_", " "));
                        chat.save();
                        chat.reload();
                        s.sendMessage(TextFormat.colorize(getPrefixTranslation("messages.GROUP-FORMAT").replaceAll("<group>", args[1])));
                    }

                } else if (args[0].equalsIgnoreCase("reload")) {
                    config.reload();
                    mute.reload();
                    chat.reload();
                    s.sendMessage(TextFormat.colorize(getPrefixTranslation("messages.RELOAD")));
                }
            }
        } else if(cmdLabel.equalsIgnoreCase("mute")) {
            List muted = mute.getList("mutedPlayers");
            if(s.hasPermission("xChat.mute")) {
                if (args.length == 0) {
                    s.sendMessage(TextFormat.colorize(getPrefixTranslation("messages.MUTE-DESC")));
                } else {
                    if (!muted.contains(args[0])) {
                        mute.set("mutedBy." + args[0], s.getName());
                        muted.add(args[0]);
                        s.sendMessage(TextFormat.colorize(getPrefixTranslation("messages.MUTE")).replaceAll("<target>", args[0]));
                        getServer().broadcastMessage(TextFormat.colorize(getTranslation("messages.MUTE-ANNOUNCE")).replaceAll("<target>", args[0]).replaceAll("<player>", s.getName()));
                    } else if (muted.contains(args[0])) {
                        mute.remove("mutedBy." + args[0]);
                        muted.remove(args[0]);
                        s.sendMessage(TextFormat.colorize(getPrefixTranslation("messages.UNMUTE")).replaceAll("<target>", args[0]));
                        getServer().broadcastMessage(TextFormat.colorize(getTranslation("messages.UNMUTE-ANNOUNCE")).replaceAll("<target>", args[0]).replaceAll("<player>", s.getName()));
                    }
                    mute.save();
                    mute.reload();
                }
            }
        }
        return true;
    }



}
