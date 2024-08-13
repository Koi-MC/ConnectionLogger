package koi.pond.connectionLogger;

import com.destroystokyo.paper.event.profile.ProfileWhitelistVerifyEvent;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Level;

public class WhitelistVerifyEvent implements Listener {
    private final JavaPlugin ConnectionLogger;
    private final Map<UUID, Boolean> whitelistStatusMap = new HashMap<>();

    public WhitelistVerifyEvent(JavaPlugin plugin) {
        this.ConnectionLogger = plugin;
        loadWhitelistData();
    }

    private void loadWhitelistData() {
        File whitelistFile = new File("whitelist.json");
        if (!whitelistFile.exists()) {
            ConnectionLogger.getLogger().warning("Whitelist file not found!");
            return;
        }

        try{
            FileReader fr = new FileReader(whitelistFile);
            JsonArray jsonArray = JsonParser.parseReader(fr).getAsJsonArray();
            whitelistStatusMap.clear(); //clean out old entries before re-filling
            for (int i = 0; i < jsonArray.size(); i++) {
                JsonObject entry = jsonArray.get(i).getAsJsonObject();
                UUID uuid = UUID.fromString(entry.get("uuid").getAsString());
                whitelistStatusMap.put(uuid, true);
            }
            fr.close();
        } catch (IOException e) {
            ConnectionLogger.getLogger().log(Level.SEVERE, "Failed to read whitelist file", e);
        }
    }

    @EventHandler
    public void onProfileWhitelistVerify(ProfileWhitelistVerifyEvent event) {
        UUID playerUUID = event.getPlayerProfile().getId();
        boolean isWhitelisted = event.isWhitelisted();
        synchronized (whitelistStatusMap) {
            whitelistStatusMap.put(playerUUID, isWhitelisted);
        }
    }

    public boolean isPlayerWhitelisted(UUID playerUUID) {
        loadWhitelistData();
        synchronized (whitelistStatusMap) {
            return whitelistStatusMap.getOrDefault(playerUUID, false);
        }
    }
}
