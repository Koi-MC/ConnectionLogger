package koi.pond.connectionLogger;

//import com.destroystokyo.paper.profile.PlayerProfile;
import org.bukkit.event.Listener;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.TimeZone;
import java.util.logging.Level;

public class PreLoginEvent implements Listener {
    private final JavaPlugin ConnectionLogger;
    private final WhitelistVerifyEvent WhitelistVerifyEvent;

    public PreLoginEvent(JavaPlugin plugin, WhitelistVerifyEvent WhitelistVerifyEvent) {
        this.ConnectionLogger = plugin;
        this.WhitelistVerifyEvent = WhitelistVerifyEvent;
    }

    @EventHandler
    public void PlayerPreLoginEvent(AsyncPlayerPreLoginEvent playerPreLogin) {
        //String logFileAllConnectionsName = "CL_AllConnections.txt";
        //String logFileNotWhitelistedName = "CL_NotWhitelisted.txt";
        String logFileAllConnectionsName = ConnectionLogger.getConfig().getString("logAllConnectionsFileName");
        String logFileNotWhitelistedName = ConnectionLogger.getConfig().getString("logNotWhitelistedFileName");

        //Create timestamp pattern object, tell it the timezone, then convert it to printable string
        SimpleDateFormat sdfObj = new SimpleDateFormat("EEE, d MMM yyyy '@' h:mm:ss a z");
        String timeZoneCFG = ConnectionLogger.getConfig().getString("timezone");
        //sdfObj.setTimeZone(TimeZone.getTimeZone("America/New_York")); //region instead of code, easier for daylight savings
        sdfObj.setTimeZone(TimeZone.getTimeZone(timeZoneCFG)); //get timezone from cfg instead
        String timeStamp = sdfObj.format(new java.util.Date());

        String playerUsername = playerPreLogin.getName();
        String playerUUID = playerPreLogin.getUniqueId().toString();
        String playerIP = (playerPreLogin.getAddress().toString()).replace("/", "");

        boolean isWhitelistedBool = WhitelistVerifyEvent.isPlayerWhitelisted(playerPreLogin.getUniqueId());
        String isWhitelisted = (String.valueOf(isWhitelistedBool)).substring(0, 1).toUpperCase() +
                (String.valueOf(isWhitelistedBool)).substring(1);

        //AsyncPlayerPreLoginEvent.Result preLoginResult = playerPreLogin.getLoginResult();
        //PlayerProfile playerInfo = playerPreLogin.getPlayerProfile();

        String printToConsoleCFG = String.valueOf(ConnectionLogger.getConfig().getBoolean("printToConsole"));
        if(printToConsoleCFG.equals("true")) {
            String finalInfoConsolePrint = "[" + timeStamp + "] --- " +
                    "[" + playerUsername + "] " +
                    "[" + playerUUID + "] " +
                    "[" + playerIP + "] " +
                    "[Whitelisted: " + isWhitelisted + "]";
            ConnectionLogger.getLogger().info(finalInfoConsolePrint);
        }

        String finalInfoFilePrint = "[" + timeStamp + "] --- " +
                "[" + playerUsername + "] " +
                "[" + playerUUID + "] " +
                "[" + playerIP + "] " +
                "[Whitelisted: " + isWhitelisted + "]\n";

        //write every connection attempt regardless of whitelist to 'All Connections' file (CL_AllConnections.txt)
        String logAllConnectionsCFG = String.valueOf(ConnectionLogger.getConfig().getBoolean("logAllConnections"));
        if(logAllConnectionsCFG.equals("true")) {
            try {
                File connectionLogFileALL = new File(logFileAllConnectionsName);
                FileWriter fr1 = new FileWriter(connectionLogFileALL, true);
                fr1.write(finalInfoFilePrint);
                fr1.close();
            } catch (IOException e) {
                ConnectionLogger.getLogger().log(Level.SEVERE, "Failed to write to " + logFileAllConnectionsName + " file", e);
            }
        }

        //write only connection attempts made by non-whitelisted accounts to separate 'Not Whitelisted' file (CL_NotWhitelisted.txt)
        String logNotWhitelistedCFG = String.valueOf(ConnectionLogger.getConfig().getBoolean("logNotWhitelisted"));
        if(logNotWhitelistedCFG.equals("true")) {
            try {
                if (isWhitelisted.equals("False")) {
                    File connectionLogFileNotWhitelisted = new File(logFileNotWhitelistedName);
                    FileWriter fr2 = new FileWriter(connectionLogFileNotWhitelisted, true);
                    fr2.write(finalInfoFilePrint);
                    fr2.close();
                }
            } catch (IOException e) {
                ConnectionLogger.getLogger().log(Level.SEVERE, "Failed to write to " + logFileNotWhitelistedName + " file", e);
            }
        }
    }
}
