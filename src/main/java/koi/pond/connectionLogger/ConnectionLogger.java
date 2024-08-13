package koi.pond.connectionLogger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.InputStreamReader;

public final class ConnectionLogger extends JavaPlugin {

    @Override
    public void onEnable() {
        // Plugin startup logic

        //1) Get/load the default config.yml
        this.saveDefaultConfig();
        FileConfiguration configFile = this.getConfig();
        String timeZoneCFG = configFile.getString("timezone");
        String printToConsoleCFG = String.valueOf(configFile.getBoolean("printToConsole"));
        String logAllConnectionsCFG = String.valueOf(configFile.getBoolean("logAllConnections"));
        String logAllConnectionsFileNameCFG = configFile.getString("logAllConnectionsFileName");
        String logNotWhitelistedCFG = String.valueOf(configFile.getBoolean("logNotWhitelisted"));
        String logNotWhitelistedFileNameCFG = configFile.getString("logNotWhitelistedFileName");
        getLogger().info("CURRENT CONFIG:");
        getLogger().info("--- Time Zone is set to: " + timeZoneCFG);
        if(printToConsoleCFG.equals("true")) {
            getLogger().info("--- Print Connections to Console: ENABLED");
        }else{
            getLogger().info("--- Print Connections to Console: DISABLED");
        }
        if(logAllConnectionsCFG.equals("true")) {
            getLogger().info("--- Primary Log for All Connections: ENABLED");
            getLogger().info("--- [Info] Logging All Connections to: " + logAllConnectionsFileNameCFG);
        }else{
            getLogger().info("--- Primary Log for All Connections: DISABLED");
        }
        if(logNotWhitelistedCFG.equals("true")) {
            getLogger().info("--- Secondary Log for only Not Whitelisted Connections: ENABLED");
            getLogger().info("--- [Info] Logging only Not Whitelisted Connections to: " + logNotWhitelistedFileNameCFG);
        }else{
            getLogger().info("--- Secondary Log for only Not Whitelisted Connections: DISABLED");
        }

        //2) Register user in-game commands
        if (this.getCommand("connectionlogger") != null) {
            this.getCommand("connectionlogger").setExecutor(this);
            this.getCommand("connectionlogger").setTabCompleter(new CLTabCompleter());
        } else {
            getLogger().severe("Command 'connectionlogger' not found in plugin.yml!");
        }

        //3) Begin main logging routines
        koi.pond.connectionLogger.WhitelistVerifyEvent whitelistVerifyEvent = new WhitelistVerifyEvent(this);
        getServer().getPluginManager().registerEvents(whitelistVerifyEvent, this);
        getServer().getPluginManager().registerEvents(new PreLoginEvent(this, whitelistVerifyEvent), this);

        this.getLogger().info("ConnectionLogger version 1.0 enabled");
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("connectionlogger")) {
            if (args.length == 0) {
                sender.sendMessage("Usage: /connectionlogger <version | reload | " +
                        "printToConsole true/false | " +
                        "logAllConnections true/false | " +
                        "logNotWhitelisted true/false>");
                return true;
            }

            if (args[0].equalsIgnoreCase("version")) {
                // Load the plugin.yml and get the version string
                InputStreamReader reader = new InputStreamReader(this.getResource("plugin.yml"));
                YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(reader);
                String version = yamlConfig.getString("version");

                sender.sendMessage("ConnectionLogger version: " + version);
                return true;
            }

            if (args[0].equalsIgnoreCase("reload")) {
                reloadConfig();
                sender.sendMessage("ConnectionLogger config reloaded successfully");
                return true;
            }

            if (args[0].equalsIgnoreCase("printToConsole")) {
                if (args.length != 2) {
                    sender.sendMessage("Usage: /connectionlogger printToConsole <true|false>");
                    return true;
                }

                String inputValue = args[1].toLowerCase();
                if (!inputValue.equals("true") && !inputValue.equals("false")) {
                    sender.sendMessage("Invalid value. Please use true or false.");
                    return true;
                }
                boolean newValue = Boolean.parseBoolean(inputValue);

                getConfig().set("printToConsole", newValue);
                saveConfig();
                reloadConfig();
                sender.sendMessage("ConnectionLogger config updated: printToConsole set to " + newValue);
                return true;
            }

            if (args[0].equalsIgnoreCase("logAllConnections")) {
                if (args.length != 2) {
                    sender.sendMessage("Usage: /connectionlogger logAllConnections <true|false>");
                    return true;
                }

                String inputValue = args[1].toLowerCase();
                if (!inputValue.equals("true") && !inputValue.equals("false")) {
                    sender.sendMessage("Invalid value. Please use true or false.");
                    return true;
                }
                boolean newValue = Boolean.parseBoolean(inputValue);

                getConfig().set("logAllConnections", newValue);
                saveConfig();
                reloadConfig();
                sender.sendMessage("ConnectionLogger config updated: logAllConnections set to " + newValue);
                return true;
            }

            if (args[0].equalsIgnoreCase("logNotWhitelisted")) {
                if (args.length != 2) {
                    sender.sendMessage("Usage: /connectionlogger logNotWhitelisted <true|false>");
                    return true;
                }

                String inputValue = args[1].toLowerCase();
                if (!inputValue.equals("true") && !inputValue.equals("false")) {
                    sender.sendMessage("Invalid value. Please use true or false.");
                    return true;
                }
                boolean newValue = Boolean.parseBoolean(inputValue);

                getConfig().set("logNotWhitelisted", newValue);
                saveConfig();
                reloadConfig();
                sender.sendMessage("ConnectionLogger config updated: logNotWhitelisted set to " + newValue);
                return true;
            }

            sender.sendMessage("Unknown Command. Usage: /connectionlogger <version | reload | " +
                    "printToConsole true/false | " +
                    "logAllConnections true/false | " +
                    "logNotWhitelisted true/false>");
            return true;
        }
        return false;
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        this.getLogger().info("ConnectionLogger version 1.0 disabled");
    }
}
