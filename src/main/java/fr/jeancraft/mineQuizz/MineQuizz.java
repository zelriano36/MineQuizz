package fr.jeancraft.mineQuizz;

import fr.jeancraft.mineQuizz.Autre.ChangeDifficult;
import fr.jeancraft.mineQuizz.commands.Classement;
import fr.jeancraft.mineQuizz.commands.PointCommand;
import fr.jeancraft.mineQuizz.commands.QuizzCommand;
import fr.jeancraft.mineQuizz.event.*;
import fr.jeancraft.mineQuizz.manager.PointManager;
import fr.jeancraft.mineQuizz.manager.ScoreBoardManager;
import net.luckperms.api.LuckPerms;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class MineQuizz extends JavaPlugin {


        private InventoryQuestionPnj inventoryPnj;
        private FileConfiguration playerData;
        private File playerDataFile;

        @Override
        public void onEnable() {
            PointManager pointManager = new PointManager(this);
            LuckPerms luckPermsApi = setupLuckPerms();
            // Initialisation du gestionnaire de tableaux de score
            ScoreBoardManager scoreBoardManager = new ScoreBoardManager(this, luckPermsApi, pointManager);
            Bukkit.getScheduler().runTaskTimer(this, () -> {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    scoreBoardManager.createScoreboard(player);
                }

            }, 0L, 20L);
            saveResource("config.yml",false);
            saveResource("classement.yml",false);

            // Initialisation des fonctionnalités
            ChangeDifficult changeDifficult = new ChangeDifficult();
            this.inventoryPnj = new InventoryQuestionPnj(this, "");

            // Enregistrement des événements
            registerEvents(changeDifficult, pointManager);

            // Enregistrement des commandes
            getCommand("quizz").setExecutor(new QuizzCommand(this));
            getCommand("classement").setExecutor(new Classement(this));
            getCommand("point").setExecutor(new PointCommand(pointManager));
            getCommand("point").setTabCompleter(new PointCommand(pointManager));

            // Chargement des données des joueurs
            loadPlayerData();
        }

        //peti test push 08/05/25 à 20h35

        @Override
        public void onDisable() {
            // Actions lors de la désactivation du plugin si nécessaire
        }

        private LuckPerms setupLuckPerms() {
            RegisteredServiceProvider<LuckPerms> provider = Bukkit.getServicesManager().getRegistration(LuckPerms.class);
            return provider != null ? provider.getProvider() : null;
        }

        private void registerEvents(ChangeDifficult changeDifficult, PointManager pointManager) {
            getServer().getPluginManager().registerEvents(new InventoryClickMatier(this), this);
            getServer().getPluginManager().registerEvents(new InventoryClickDifficultAndQuestion(changeDifficult, this, this.inventoryPnj, pointManager), this);
            getServer().getPluginManager().registerEvents(new ClickItemStart(this), this);
            getServer().getPluginManager().registerEvents(this.inventoryPnj, this);
            getServer().getPluginManager().registerEvents(new PassageClassrom(this), this);
            getServer().getPluginManager().registerEvents(new InventoryDefisQuestionGestion(this, this.inventoryPnj), this);
        }

        private void loadPlayerData() {
            playerDataFile = new File(getDataFolder(), "player_data.yml");
            if (!playerDataFile.exists()) {
                saveResource("player_data.yml", false);
            }
            playerData = YamlConfiguration.loadConfiguration(playerDataFile);
        }
    }
