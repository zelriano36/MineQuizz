package fr.jeancraft.mineQuizz.commands;

import fr.jeancraft.mineQuizz.MineQuizz;
import fr.jeancraft.mineQuizz.event.GestionInvetory;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class QuizzCommand implements CommandExecutor{
	
public MineQuizz main;

public QuizzCommand(MineQuizz main ) {


	this.main = main;
}
  

	

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		
		if(label.equals("quizz")) {
			if(sender instanceof Player) {
				 Player player = (Player) sender;
				GestionInvetory gst = new GestionInvetory(null, player);
				
				gst.ON_Inventory_Start(player);
				
				return true;
			}


		}
		return false;
		
	}
				

}
