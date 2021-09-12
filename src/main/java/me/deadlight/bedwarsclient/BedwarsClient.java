package me.deadlight.bedwarsclient;
import me.deadlight.bedwarsclient.Listeners.CommandListener;
import me.deadlight.bedwarsclient.Listeners.PluginListeners;
import me.deadlight.bedwarsclient.Listeners.TntModifier;
import me.deadlight.bedwarsclient.Listeners.YLevelListener;
import me.deadlight.bedwarsclient.Oldway.SocketClient;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import ro.Fr33styler.ClashWars.Handler.Manager.GameManager;
import ro.Fr33styler.ClashWars.Main;

public final class BedwarsClient extends JavaPlugin {
    private static BedwarsClient plugin;
    public static BedwarsClient getInstance() {
        return plugin;
    }
    public String theServerName;
    public Main bedwarsMain;
    public GameManager gameManager;
    public static JedisPool pool;
    public SocketClient Sclient;

    @Override
    public void onEnable() {
        // Plugin startup logic

        plugin = this;
        saveDefaultConfig();
        theServerName = getConfig().getString("server");
        logConsole(Utils.prefix + " &eLoading client bedwars plugin....");
        getServer().getMessenger().registerOutgoingPluginChannel(this, "BungeeCord");
        getServer().getPluginManager().registerEvents(new PluginListeners(), this);

        Bukkit.getScheduler().runTaskLater(this, new java.lang.Runnable() {
            @Override
            public void run() {
                //things
                bedwarsMain = (Main)Bukkit.getPluginManager().getPlugin("ClashWars");
                gameManager = bedwarsMain.getManager();
                getServer().getPluginManager().registerEvents(new YLevelListener(), getInstance());
                getServer().getPluginManager().registerEvents(new CommandListener(), getInstance());
                getServer().getPluginManager().registerEvents(new TntModifier(), getInstance());
                WorldCheck worldCheck = new WorldCheck(getInstance());
                pool = new JedisPool("127.0.0.1", 6379);

                Runnable.startSendingData();
                logConsole(Utils.prefix + " &aClient successfully connected");
            }
        }, 20 * 10);

//        Bukkit.getScheduler().scheduleSyncDelayedTask(this, new java.lang.Runnable() {
//            @Override
//            public void run() {
//                bedwarsMain = (Main)Bukkit.getPluginManager().getPlugin("ClashWars");
//                gameManager = bedwarsMain.getManager();
//                Runnable.startSendingData();
//                getServer().getPluginManager().registerEvents(new YLevelListener(), getInstance());
//                getServer().getPluginManager().registerEvents(new CommandListener(), getInstance());
//                WorldCheck worldCheck = new WorldCheck(getInstance());
//                logConsole(Utils.prefix + " &aClient successfully connected");
//                //run the runnables
//            }
//        }, 20 * 10);


//        Bukkit.getScheduler().runTaskAsynchronously(this, new java.lang.Runnable() {
//            @Override
//            public void run() {
//                SocketClient client = new SocketClient();
//                Sclient = client;
//                client.startConnection("127.0.0.1", 5555);
//            }
//        });


    }


    @Override
    public void onDisable() {
        System.out.println("shutting down bedwars client");
        JSONObject jsonObject = new JSONObject();
        Jedis j = null;
        try {
            j = pool.getResource();
            // If you want to use a password, use
            j.auth("piazcraftmc");
            j.set(theServerName, jsonObject.toJSONString());
        } finally {
            // Be sure to close it! It can and will cause memory leaks.
            if (j != null) {
                j.close();
            }

        }

        pool.close();
        // Plugin shutdown logic
        try {
            //Sclient.sendMessage("offline@" + theServerName);
            Bukkit.getScheduler().cancelTasks(this);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void logConsole(String str) {
        getServer().getConsoleSender().sendMessage(Utils.colorify(str));
    }

}
