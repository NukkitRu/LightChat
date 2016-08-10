package nukkit.sdir01.xchatreloaded;

import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import nukkit.sdir01.xchatreloaded.commands.MuteCommands;
import nukkit.sdir01.xchatreloaded.commands.LightChatCommands;
import nukkit.sdir01.xchatreloaded.listeners.ChatListener;
import nukkit.sdir01.xchatreloaded.listeners.MotdListener;
import nukkit.sdir01.xchatreloaded.util.Message;

import java.io.File;
import java.util.List;

public class LightChat extends PluginBase implements cn.nukkit.event.Listener {

    private static LightChat instance;

    public static LightChat getPlugin() {
        return instance;
    }

    public static String getHookedWithPlugin = "none";

    private File chatSettings;
    public static Config chat;

    private File cfg;
    public static Config config;

    private File muted;
    public static Config mute;

    private File Motd;
    public static Config motd;

    public static Boolean isMuted(String playerName) {
        if(mute.getList("muted-players").contains(playerName)) {
            return  true;
        } else {
            return false;
        }
    }

    public static List<String> getMutedPlayers() {
        if(!mute.getList("muted-players").isEmpty()) {
            return mute.getStringList("muted-players");
        } else {
            return null;
        }
    }

    public static String getMutedBy(String playerName) {
        if(mute.getString("muted-players-data." + playerName) == "") {
            return null;
        } else {
            return mute.getString("muted-players-data." + playerName);
        }
    }

    private void loadData() {
        this.getDataFolder().mkdirs();
        this.saveDefaultConfig();

        this.saveResource("chat.yml", false);
        this.saveResource("mute.yml", false);
        this.saveResource("config.yml", false);
        this.saveResource("motd.yml", false);

        this.cfg = new File(this.getDataFolder(), "config.yml");
        config = new Config(this.cfg, 2);

        this.chatSettings = new File(this.getDataFolder(), "chat.yml");
        chat = new Config(this.chatSettings, 2);

        this.muted = new File(this.getDataFolder(), "mute.yml");
        mute = new Config(this.muted, 2);

        this.Motd = new File(this.getDataFolder(), "motd.yml");
        motd = new Config(this.Motd, 2);

        Message.init(this);

        Message.XCHAT_LOADED.log();
        Message.SEPERATOR.log(" Plugin activation ");
        Message.XCHAT_INIT.log();
        this.getServer().getCommandMap().register("lchat", new LightChatCommands(this));
        Message.XCHAT_REGISTERED.log("command", "Command /lchat");
        this.getServer().getCommandMap().register("mute", new MuteCommands(this));
        Message.XCHAT_REGISTERED.log("command", "Command /mute");
        this.getServer().getPluginManager().registerEvents(new ChatListener(this), this);
        Message.XCHAT_REGISTERED.log("event", "PlayerChatEvent listener");
        this.getServer().getPluginManager().registerEvents(new MotdListener(this), this);
        Message.XCHAT_REGISTERED.log("event", "PlayerJoinEvent listener");
        Message.XCHAT_SUCCESS.log();

        Message.SEPERATOR.log(" Configuration ");
        Message.XCHAT_CONFIG_CHEKING.log();
        if(config.getString("general.config-version") != null && config.getInt("general.config-version") == 1) {
            Message.XCHAT_CONFIG_DATA.log("1", config.getString("general.config-version"));
            Message.XCHAT_SUCCESS.log();
        } else {
            Message.XCHAT_CONFIG_DATA.log("1", config.getString("general.config-version"));
            Message.XCHAT_CONFIG_OUTOFDATE.log('c');
            Message.XCHAT_FAILED.log('c');
        }

    }

    @Override
    public void onLoad() {
        instance = this;
    }

    @Override
    public void onEnable() {

        this.loadData();
        Message.SEPERATOR.log(" Software checking ");
        Message.XCHAT_CHEKING.log();
        if(getServer().getPluginManager().getPlugin("LightEyeCore") != null) {
            Message.HOOKED_WITH_PLUGIN.log("SdirCore (Official)");

        }

        if(getServer().getPluginManager().getPlugin("Multipass") != null) {
            Message.HOOKED_WITH_PLUGIN.log( "Multipass");
            Message.XCHAT_SUCCESS.log();
            getHookedWithPlugin = "Multipass";
        /*} else if(getServer().getPluginManager().getPlugin("LeetPerms") != null) {
            Message.HOOKED_WITH_PLUGIN.log( "LeetPerms");
            Message.XCHAT_SUCCESS.log();
            getHookedWithPlugin = "LeetPerms";*/

		} else if (getServer().getPluginManager().getPlugin("MadPerms") != null) {
			Message.HOOKED_WITH_PLUGIN.log("MadPerms");
			Message.XCHAT_SUCCESS.log();
			getHookedWithPlugin = "MadPerms";
		} else {
	        // Disabled, because LeetPerms didn't have a methods to get player group, prefix and etc.
	        // Contact the LeetPerms developer if you want to see his plugin support here!
			Message.NO_LOADED_PLUGINS.log('c');
			Message.XCHAT_FAILED.log('c');
			getServer().getPluginManager().disablePlugin(this);
		}
    }
}
