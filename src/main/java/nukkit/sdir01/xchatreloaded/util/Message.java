package nukkit.sdir01.xchatreloaded.util;

/**
 * nukkit.sdir01.xchatreloaded.util developed by undefined1
 * <p>
 * This project can be modified by another user, but you need paste link to original GitHub or other project page!
 * You can use software API and making addons without links to this project.
 * <p>
 * Project create date: 31.07.2016
 * Adv4Core and XonarTeam 2016 (c) All rights reserved.
 */
/**
 * This module is created by fromgate specially for Nukkit Community, http://nukkit.ru
 * You may use this code absolutely free in your nukkit-plugins.
 *
 */

import cn.nukkit.Player;
import cn.nukkit.Server;
import cn.nukkit.command.CommandSender;
import cn.nukkit.level.Location;
import cn.nukkit.plugin.PluginBase;
import cn.nukkit.utils.Config;
import cn.nukkit.utils.TextFormat;

import java.io.*;
import java.text.DecimalFormat;


public enum Message {

    //Default messages required by this in18 class
    LNG_LOAD_FAIL ("Failed to load languages from file. Default message used"),
    LNG_SAVE_FAIL ("Failed to save lang file"),
    LNG_PRINT_FAIL ("Failed to print message %1%. Sender object is null."),
    LNG_CONFIG ("[MESSAGES] Messages: %1% Language: %2% Debug mode: %3%"),

    // COMMANDS
    RELOAD_CMD ("Configuration files reloaded!"),
    RELOAD_CMD_with_save ("Configuration files saved and reloaded!"),
    // MUTE
    YOU_ARE_MUTED ("Can't send message, because you are muted by %1%"),
    YOU_ARE_MUTED_BY ("You are muted by %1%!"),
    YOU_ARE_UNMUTED_BY ("You are unmuted by %1%!"),
    YOU_MUTE ("You muted a %1%!"),
    YOU_UNMUTE ("You unmuted a %1%!"),
    MUTE_BYPASS ("You don't have permissions to mute %1%!"),
    COMMAND_USE ("For %1%, use: %2%"),
    // MAIN
    // SUBCOMMANDS
    // ARGUMENTS
    // OTHER
    XCHAT_LOADED ("Developed by sdir01 and LightEyeTM", '2'),

    XCHAT_FAILED ("Failed."),
    XCHAT_INIT ("Initialization...", 'a'),
    XCHAT_CHEKING ("Checking for permission plugin...", 'a'),
    XCHAT_REGISTERED ("Parameter %1% with description %2% registered", '7', 'a'),
    XCHAT_CONFIG_CHEKING ("Checking configuration version...", 'a'),
    XCHAT_CONFIG_DATA ("Required config version %1% | Your config version is %2%", '7', 'a'),
    XCHAT_CONFIG_OUTOFDATE ("Your configuration file is outdated! Please remove him from directory!"),
    SEPERATOR ("&m|=============%1%=============|", '8', 'e'),
    XCHAT_SUCCESS ("Successful!", 'a'),
    XCHAT_DISABLED ("Plugin has been disabled!"),
    NO_LOADED_PLUGINS ("You don't have any permisssion plugin! [Plugin disabled]"),
    HOOKED_WITH_PLUGIN ("Connect initialization with %1%..."),
    YOU_DONT_HAVE_PERMISSION ("You don't have permission %1% to do this!")

    /**
     * Add your messages here:
     ** Example:
     * EXAMPLE_MESSAGE ("Example message"),
     * EXAMPLE_MESSAGE2 ("Another example message"),
     *
     */
    ;

    private static boolean debugMode = false;
    private static String language = "english";
    private static char c1 = '7';
    private static char c2 = 'a';

    private static PluginBase plugin = null;

    /**
     * This is my favorite debug routine :) I use it everywhere to print out variable values
     * @param s - array of any object that you need to print out.
     * Example:
     * Message.BC ("variable 1:",var1,"variable 2:",var2)
     */
    public static void BC (Object... s){
        if (!debugMode) return;
        if (s.length==0) return;
        StringBuilder sb = new StringBuilder("&3[").append(plugin.getDescription().getName()).append("]&f ");
        for (Object str : s)
            sb.append(str.toString()).append(" ");
        plugin.getServer().broadcastMessage(TextFormat.colorize(sb.toString().trim()));
    }



    /**
     * Send current message to log files
     * @param s
     * @return — always returns true.
     * Examples:
     * Message.ERROR_MESSAGE.log(variable1); // just print in log
     * return Message.ERROR_MESSAGE.log(variable1); // print in log and return value true
     */
    public boolean log(Object... s){
        plugin.getLogger().info(getText (s));
        return true;
    }

    /**
     * Same as log, but will printout nothing if debug mode is disabled
     * @param s
     * @return — always returns true.
     */
    public boolean debug(Object... s){
        if (debugMode) plugin.getLogger().info(TextFormat.clean(getText (s)));
        return true;
    }

    /**
     * Show a message to player in center of screen (this routine unfinished yet)
     * @param seconds — how much time (in seconds) to show message
     * @param sender — Player
     * @param s
     * @return — always returns true.
     */
    public boolean tip (int seconds, CommandSender sender, Object... s){
        if (sender == null) return Message.LNG_PRINT_FAIL.log(this.name());
        final Player player = sender instanceof Player ? (Player) sender : null;
        final String message = getText(s);
        if (player==null) sender.sendMessage(message);
        else for (int i=0;i<seconds;i++) Server.getInstance().getScheduler().scheduleDelayedTask(new Runnable() {
            public void run() {
                if (player.isOnline()) player.sendTip(message);
            }
        },20*i);
        return true;
    }

    /**
     * Show a message to player in center of screen
     * @param sender — Player
     * @param s
     * @return — always returns true.
     */
    public boolean tip (CommandSender sender, Object... s){
        if (sender == null) return Message.LNG_PRINT_FAIL.log(this.name());
        Player player = sender instanceof Player ? (Player) sender : null;
        String message = getText(s);
        if (player==null) sender.sendMessage(message);
        else player.sendTip(message);
        return true;
    }

    /**
     * Send message to Player or to ConsoleSender
     * @param sender
     * @param s
     * @return — always returns true.
     */
    public boolean print (CommandSender sender, Object... s){
        if (sender == null) return Message.LNG_PRINT_FAIL.log(this.name());
        sender.sendMessage(getText(s));
        return true;
    }

    /**
     * Send message to all players or to players with defined permission
     * @param permission
     * @param s
     * @return — always returns true.
     *
     * Examples:
     * Message.MSG_BROADCAST.broadcast ("pluginname.broadcast"); // send message to all players with permission "pluginname.broadcast"
     * Message.MSG_BROADCAST.broadcast (null); // send message to all players
     */
    public boolean broadcast (String permission, Object... s){
        for (Player player : plugin.getServer().getOnlinePlayers().values()){
            if (permission==null || player.hasPermission(permission)) print (player,s);
        }
        return true;
    }


    /**
     * Get formated text.
     * @param keys
     *
     ** Keys - are parameters for message and control-codes.
     * Parameters will be shown in position in original message according for position.
     * This keys are used in every method that prints or sends message.
     *
     * Example:
     *
     * EXAMPLE_MESSAGE ("Message with parameters: %1%, %2% and %3%");
     * Message.EXAMPLE_MESSAGE.getText("one","two","three"); //will return text "Message with parameters: one, two and three"
     *
     ** Color codes
     * You can use two colors to define color of message, just use character symbol related for color.
     *
     * Message.EXAMPLE_MESSAGE.getText("one","two","three",'c','4');  // this message will be red, but word one, two, three - dark red
     *
     ** Control codes
     * Control codes are text parameteres, that will be ignored and don't shown as ordinary parameter
     * - "SKIPCOLOR" - use this to disable colorizing of parameters
     * - "NOCOLOR" (or "NOCOLORS") - return uncolored text, clear all colors in text
     * - "FULLFLOAT" - show full float number, by default it limit by two symbols after point (0.15 instead of 0.1483294829)
     *
     * @return
     */
    public String getText (Object... keys){
        if (keys.length ==0) return TextFormat.colorize("&"+ color1 +this.message);
        String str = this.message;
        boolean noColors = false;
        boolean skipDefaultColors = false;
        boolean fullFloat = false;
        int count=1;
        char [] colors = new char[]{color1 == null ? c1 : color1 , color2 == null ? c2 : color2};
        int c = 0;
        DecimalFormat fmt = new DecimalFormat("####0.##");
        for (int i = 0; i<keys.length; i++){
            String s = keys[i].toString();
            if (c<2&&keys[i] instanceof Character){
                colors[c] = (Character) keys[i];
                c++;
                continue;
            } else if (s.equals("SKIPCOLOR")) {
                skipDefaultColors = true;
                continue;
            } else if (s.equals("NOCOLORS")||s.equals("NOCOLOR")) {
                noColors = true;
                continue;
            } else if (s.equals("FULLFLOAT")) {
                fullFloat = true;
                continue;
            } else if (keys[i] instanceof Location) {
                Location loc = (Location) keys[i];
                if (fullFloat) s = loc.getLevel().getName()+"["+loc.getX()+", "+loc.getY()+", "+loc.getZ()+"]";
                else s = loc.getLevel().getName()+"["+fmt.format(loc.getX())+", "+fmt.format(loc.getY())+", "+fmt.format(loc.getZ())+"]";
            } else if (keys[i] instanceof Double || keys[i] instanceof Float) {
                if (!fullFloat) s = fmt.format((Double) keys[i]);
            }

            String from = (new StringBuilder("%").append(count).append("%")).toString();
            String to = skipDefaultColors ? s :(new StringBuilder("&").append(colors[1]).append(s).append("&").append(colors[0])).toString();
            str = str.replace(from, to);
            count++;
        }
        str = TextFormat.colorize("&"+colors[0]+str);
        if (noColors) str = TextFormat.clean(str);
        return str;
    }

    private void initMessage (String message){
        this.message = message;
    }

    private String message;
    private Character color1;
    private Character color2;
    Message (String msg){
        message = msg;
        this.color1 = null;
        this.color2 = null;
    }
    Message (String msg, char color1, char color2){
        this.message = msg;
        this.color1 = color1;
        this.color2 = color2;
    }
    Message (String msg, char color){
        this (msg,color,color);
    }

    @Override
    public String toString(){
        return this.getText("NOCOLOR");
    }

    /**
     * Initialize current class, load messages, etc.
     * Call this file in onEnable method after initializing plugin configuration
     * @param plg
     */
    public static void init(PluginBase plg){
        plugin = plg;
        language = plg.getConfig().getNested("general.language","english");
        plg.getConfig().set("general.language", language);
        debugMode = plg.getConfig().getNested("general.debug-mode",false);
        plg.getConfig().set("general.debug-mode",debugMode);
        plg.saveConfig();
        initMessages();
        saveMessages();
        LNG_CONFIG.debug(Message.values().length,language,true,debugMode);
    }

    /**
     * Enable debugMode
     * @param debug
     */
    public static void setDebugMode (boolean debug){
        debugMode = debug;
    }

    private static boolean copyLanguage(){
        return plugin.saveResource("lang/" +language+".lng",language+".lng",false);
    }

    private static void initMessages(){
        copyLanguage();

        Config lng = null;
        try {
            File f = new File (plugin.getDataFolder()+File.separator+language+".lng");
            lng = new Config(f,Config.YAML);
        } catch (Exception e){
            LNG_LOAD_FAIL.log();
            if (debugMode) e.printStackTrace();
            return;
        }
        for (Message key : Message.values())
            key.initMessage((String) lng.get(key.name().toLowerCase(), key.message));
    }

    private static void saveMessages(){
        File f = new File (plugin.getDataFolder()+File.separator+language+".lng");
        Config lng = new Config(f,Config.YAML);
        for (Message key : Message.values())
            lng.set(key.name().toLowerCase(), key.message);
        try {
            lng.save();
        } catch (Exception e){
            LNG_SAVE_FAIL.log();
            if (debugMode) e.printStackTrace();
            return;
        }
    }

    /**
     * Send message (formed using join method) to server log if debug mode is enabled
     * @param s
     */
    public static boolean debugMessage (Object... s){
        if (debugMode) plugin.getLogger().info(TextFormat.clean(join (s)));
        return true;
    }

    /**
     * Join object array to string (separated by space)
     * @param s
     */
    public static String join (Object... s){
        StringBuilder sb = new StringBuilder();
        for (Object o : s){
            if (sb.length()>0) sb.append(" ");
            sb.append(o.toString());
        }
        return sb.toString();
    }
}
