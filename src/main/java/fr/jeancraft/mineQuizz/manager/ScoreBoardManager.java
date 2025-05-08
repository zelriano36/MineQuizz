package fr.jeancraft.mineQuizz.manager;

import fr.jeancraft.mineQuizz.MineQuizz;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.model.user.User;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.scoreboard.*;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.*;

public class ScoreBoardManager implements Listener {

    private final MineQuizz main;
    private final LuckPerms luckPerms;
    private final PointManager pointManager;

    public ScoreBoardManager(MineQuizz main, LuckPerms luckPerms, PointManager pointManager) {
        this.main = main;
        this.luckPerms = luckPerms;
        this.pointManager = pointManager;
    }

    public void createScoreboard(Player player) {
        // Charger le fichier config.yml

        File configFile = new File(main.getDataFolder(), "config.yml");
        if (!configFile.exists()) {
            player.sendMessage("§cLe fichier config.yml est introuvable !");
            return;
        }

        // Charger la configuration
        YamlConfiguration fileConfig = YamlConfiguration.loadConfiguration(configFile);

        // Créer le scoreboard
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective(
                "rank",
                "dummy",
                fileConfig.getString("scoreboard.name", "§f§lJeanCraft").replace("&", "§"),
                RenderType.INTEGER
        );
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        // Récupérer les lignes du scoreboard
        ConfigurationSection linesSection = fileConfig.getConfigurationSection("scoreboard.line");
        if (linesSection == null) {
            player.sendMessage("§cAucune ligne définie dans la configuration !");
            return;
        }

        // Parcourir et ajouter les lignes au scoreboard
        linesSection.getKeys(false).stream()
                .filter(lineKey -> {
                    try {
                        Integer.parseInt(lineKey); // Vérifier si la clé est un entier
                        return true;
                    } catch (NumberFormatException e) {
                        main.getLogger().warning("Clé invalide détectée dans la configuration des lignes : " + lineKey);
                        return false;
                    }
                })
                .sorted((a, b) -> Integer.compare(Integer.parseInt(b), Integer.parseInt(a))) // Trier par ordre décroissant
                .forEach(lineKey -> {
                    int lineNumber = Integer.parseInt(lineKey);
                    String text = linesSection.getString(lineKey, "");
                    CreateScoreboardLine(player, objective, text, lineNumber);
                });

        // Appliquer le scoreboard au joueur
        player.setScoreboard(scoreboard);
    }
    public void CreateScoreboardLine(Player player, Objective objective, String score_text, int score_number) {
//        File fileC = new File(main.getDataFolder(), "classement.yml");
//        if (!fileC.exists()) {
//            player.sendMessage("§cLe fichier classement.yml est introuvable !");
//            return;
//        }
//
//        // Charger le fichier YAML
//        YamlConfiguration configC = YamlConfiguration.loadConfiguration(fileC);
//        String playerKey = "players." + player.getUniqueId();
//        int playerPoints = configC.getInt(playerKey + ".point", 0);
//        // Récupérer tous les scores
//        HashMap<String, Integer> scores = new HashMap<>();
//        if (configC.contains("players")) {
//            for (String key : configC.getConfigurationSection("players").getKeys(false)) {
//                int points = configC.getInt("players." + key + ".point", 0);
//                scores.put(key, points);
//            }
//        }
//
//        // Trier les joueurs par score
//        List<Map.Entry<String, Integer>> sortedScores = new ArrayList<>(scores.entrySet());
//        sortedScores.sort((a, b) -> b.getValue().compareTo(a.getValue()));
//
//        // Trouver le classement du joueur
//        int rank = 1;
//        for (Map.Entry<String, Integer> entry : sortedScores) {
//            if (entry.getKey().equals(player.getUniqueId().toString())) {
//                break;
//            }
//            rank++;
//        }
        User user = luckPerms.getUserManager().getUser(player.getUniqueId());
        String prefixFormatted = user.getCachedData().getMetaData().getPrefix().replace("&", "§");
        String suffixFormatted = user.getCachedData().getMetaData().getPrefix().replace("&", "§");

        objective.getScore(score_text
                .replace("&", "§")
                .replace("[playername]",player.getName())
                .replace("[lp_prefix]",prefixFormatted)
                .replace("[lp_suffix]",suffixFormatted)
                .replace("[online_player]",""+Bukkit.getOnlinePlayers().size())
                .replace("[quizz_point]",""+pointManager.GetPoints(player))
                .replace("[date_time]",new SimpleDateFormat("dd/MM/yy HH:mm:ss").format(new Date()))
        ).setScore(score_number);

    }
}
