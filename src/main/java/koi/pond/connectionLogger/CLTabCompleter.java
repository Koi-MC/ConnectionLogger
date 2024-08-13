package koi.pond.connectionLogger;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.List;

public class CLTabCompleter implements TabCompleter {
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        List<String> completions = new ArrayList<>();

        if (args.length == 1) {
            if (args[0].equalsIgnoreCase("printToConsole")) {
                completions.add("true");
                completions.add("false");
            } else if (args[0].equalsIgnoreCase("logAllConnections")) {
                completions.add("true");
                completions.add("false");
            } else if (args[0].equalsIgnoreCase("logNotWhitelisted")) {
                completions.add("true");
                completions.add("false");
            } else {
                completions.add("version");
                completions.add("reload");
                completions.add("printToConsole");
                completions.add("logAllConnections");
                completions.add("logNotWhitelisted");
            }
        } else if (args.length == 2 && args[0].equalsIgnoreCase("printToConsole")) {
            completions.add("true");
            completions.add("false");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("logAllConnections")) {
            completions.add("true");
            completions.add("false");
        } else if (args.length == 2 && args[0].equalsIgnoreCase("logNotWhitelisted")) {
            completions.add("true");
            completions.add("false");
        }

        return completions;
    }
}