package ca.fastis;

import java.util.List;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Server;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import com.google.common.collect.Lists;

public class CommandReplace implements CommandExecutor, TabCompleter {

	Server server = SpeedEdit.server;

	@Override
	public boolean onCommand(CommandSender sender, Command arg1, String arg2, String[] arg3) {
		if (sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("speededit.replace")) {
				if(!SpeedEdit.ListPosition1.containsKey(player) || !SpeedEdit.ListPosition2.containsKey(player)) {
					player.sendMessage(ChatColor.DARK_GRAY + "You need to select " + ChatColor.DARK_RED + "2 positions" + ChatColor.DARK_GRAY + ", use right and left click with a brick on blocks");
					return true;
				}
				if(SpeedEdit.ListPosition1.get(player).getWorld() != SpeedEdit.ListPosition2.get(player).getWorld()) {
					player.sendMessage(ChatColor.DARK_GRAY + "Your 2 positions must be in the " + ChatColor.DARK_RED + "same world");
					return true;
				}
				if(arg3.length < 1 || arg3.length > 2) {
					player.sendMessage(ChatColor.DARK_GRAY + "Use the command like this " + ChatColor.DARK_RED + "/Replace Material" + ChatColor.DARK_GRAY + " or " + ChatColor.DARK_RED + "/Replace Material Material");
					return true;
				}
				if(Material.matchMaterial(arg3[0]) == null) {
					player.sendMessage(ChatColor.DARK_GRAY + "The material " + ChatColor.DARK_RED + arg3[0] + ChatColor.DARK_GRAY + " can't be found");
					return true;
				}
				if(arg3.length > 1 && Material.matchMaterial(arg3[1]) == null) {
					player.sendMessage(ChatColor.DARK_GRAY + "The material " + ChatColor.DARK_RED + arg3[1] + ChatColor.DARK_GRAY + " can't be found");
					return true;
				}

				try {
					List<Block> Selected = SpeedEdit.SelectedBlocks.get(player);
					int Changed = 0;
					if(arg3.length == 2) {
						Material preMaterial = Material.matchMaterial(arg3[0]);
						Material postMaterial = Material.matchMaterial(arg3[1]);
						for(Block block : Selected) {
							if(block.getType() == preMaterial) { Events.blockChange(player, block, postMaterial); Changed++; }
						}
					} else {
						for(Block block : Selected) {
							if(block.getType() != Material.AIR) { Events.blockChange(player, block, Material.matchMaterial(arg3[0])); Changed++; }
						}
					}
					player.sendMessage(ChatColor.DARK_GRAY + "You replaced " + ChatColor.GREEN + Changed + ChatColor.DARK_GRAY + " blocks to " + ChatColor.GREEN + arg3[0]);
					for (Player online : server.getOnlinePlayers()) {
						if (online.isOp()) {
							online.sendMessage(ChatColor.GREEN + "" + player.getName() + ChatColor.DARK_GRAY + " replaced " + ChatColor.GREEN + Changed + ChatColor.DARK_GRAY + " blocks to " + ChatColor.GREEN + arg3[0]);
						}
					}
					return true;
				} catch(Exception e) {
					player.sendMessage(ChatColor.DARK_GRAY + "Speed Edit Error : " + ChatColor.DARK_RED + e.getMessage());
				}
				return true;
			}
		}
		return false;
	}

	@Override
	public List<String> onTabComplete(CommandSender sender, Command cmd, String Label, String[] args) {
		Material[] list = Material.values();
		List<String> fList = Lists.newArrayList();
		if (args.length == 1) {
			for (Material s : list) {
				if (s.name().toLowerCase().startsWith(args[0].toLowerCase())) {
					fList.add(s.name().toLowerCase());
				}
			}
			return fList;
		} else if (args.length == 2) {
			for (Material s : list) {
				if (s.name().toLowerCase().startsWith(args[1].toLowerCase())) {
					fList.add(s.name().toLowerCase());
				}
			}
			return fList;
		} else {
			return null;
		}
	}
}
