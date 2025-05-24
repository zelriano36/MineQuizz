package fr.jeancraft.mineQuizz.manager;

import fr.jeancraft.mineQuizz.MineQuizz;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;

public class PointManager {
    private final MineQuizz main;

    public PointManager(MineQuizz minequizz) {
        this.main = minequizz;
    }

    public int GetPoints(Player player) {
        File classementFile = new File(main.getDataFolder(), "classement.yml");
        if (!classementFile.exists()) {
            player.sendMessage("§cLe fichier classement.yml est introuvable !");
        }
        YamlConfiguration configC = YamlConfiguration.loadConfiguration(classementFile);
        int playerPoints = configC.getInt("players." + player.getUniqueId() + ".point", 0);
        return playerPoints;
    }

    public void checkPoints(Player player) {
        player.sendMessage("§aVous avez actuellement " + GetPoints(player) + " points !");

    }
    public void AddPoints(Player player, int newpoints) {
        int points = GetPoints(player);
        points += newpoints;    }
    public void RemovePoints(Player player, int newpoints) {
        int points = GetPoints(player);
        points -= newpoints;

    }
    public void SetPoints(Player player, int newpoints) {
        int points =GetPoints(player);
        points = newpoints;

    }
}
