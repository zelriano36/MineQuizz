package fr.jeancraft.mineQuizz.commands;

import fr.jeancraft.mineQuizz.manager.PointManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class PointCommand implements CommandExecutor, TabCompleter{
    private final PointManager pointManager;

    public PointCommand(PointManager pointManager) {
        this.pointManager = pointManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 2) {
            sender.sendMessage("§cUsage : /point <player> <set|add|remove|check> [number]");
            return true;
        }

        // Récupérer le joueur
        Player target = Bukkit.getPlayer(args[0]);
        if (target == null) {
            sender.sendMessage("§cLe joueur spécifié est introuvable ou hors ligne.");
            return true;
        }

        String action = args[1].toLowerCase();
        if (action.equals("check")) {
            int points = pointManager.GetPoints(target);
            sender.sendMessage("§aLe joueur §e" + target.getName() + "§a a §e" + points + "§a points.");
            return true;
        }

        if (args.length < 3) {
            sender.sendMessage("§cVous devez spécifier un nombre pour cette action !");
            return true;
        }

        int amount;
        try {
            amount = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("§cLe nombre spécifié est invalide !");
            return true;
        }

        switch (action) {
            case "set" -> {
                pointManager.SetPoints(target, amount);
                sender.sendMessage("§aLes points de §e" + target.getName() + "§a ont été définis à §e" + amount + "§a.");
            }
            case "add" -> {
                pointManager.AddPoints(target, amount);
                sender.sendMessage("§aVous avez ajouté §e" + amount + "§a points à §e" + target.getName() + "§a.");
            }
            case "remove" -> {
                pointManager.RemovePoints(target, amount);
                sender.sendMessage("§aVous avez retiré §e" + amount + "§a points à §e" + target.getName() + "§a.");
            }
            default -> sender.sendMessage("§cAction invalide ! Utilisez : set, add, remove ou check.");
        }

        return true;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (args.length == 1) {
            List<String> points = new ArrayList<>();
            points.add("set");
            points.add("add");
            points.add("remove");
        }
        return null;
    }

}
