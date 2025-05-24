package fr.jeancraft.mineQuizz.commands;

import fr.jeancraft.mineQuizz.MineQuizz;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.util.*;

public class Classement implements CommandExecutor {
    private final MineQuizz main;

    public Classement(MineQuizz main) {
        this.main = main;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (!label.equalsIgnoreCase("classement")) {
            return false;
        }

        if (!(sender instanceof Player)) {
            sender.sendMessage("§cSeuls les joueurs peuvent exécuter cette commande.");
            return true;
        }

        Player player = (Player) sender;

        // Charger le fichier classement.yml
        File fileC = new File(main.getDataFolder(), "classement.yml");
        if (!fileC.exists()) {
            player.sendMessage("§cLe fichier classement.yml est introuvable !");
            return true;
        }

        YamlConfiguration configC = YamlConfiguration.loadConfiguration(fileC);

        // Vérifier si la section "players" existe
        if (!configC.contains("players")) {
            player.sendMessage("§cAucun joueur n'est enregistré dans le classement.");
            return true;
        }

        // Récupérer les points de tous les joueurs
        Map<String, Integer> playerPoints = new HashMap<>();
        for (String key : configC.getConfigurationSection("players").getKeys(false)) {
            int points = configC.getInt("players." + key + ".point", 0);
            playerPoints.put(key, points);
        }

        // Trier les joueurs par points (décroissant)
        List<Map.Entry<String, Integer>> sortedPlayers = new ArrayList<>(playerPoints.entrySet());
        sortedPlayers.sort((a, b) -> b.getValue().compareTo(a.getValue()));

        // Afficher les 10 premiers joueurs
        player.sendMessage("§6§l--- CLASSEMENT DES JOUEURS ---");
        int rank = 1;
        for (Map.Entry<String, Integer> entry : sortedPlayers) {
            if (rank > 10) break; // Limiter à 10 joueurs

            String playerName = Bukkit.getOfflinePlayer(UUID.fromString(entry.getKey())).getName();
            if (playerName == null) {
                playerName = "Inconnu"; // Si le joueur n'a pas de nom (au cas où)
            }

            player.sendMessage("§e#" + rank + " §a" + playerName + " §7- §b" + entry.getValue() + " points");
            rank++;
        }

        return true;
    }
}
