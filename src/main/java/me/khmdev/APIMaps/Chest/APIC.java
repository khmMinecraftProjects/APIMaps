package me.khmdev.APIMaps.Chest;

import me.khmdev.APIBase.API;
import me.khmdev.APIBase.Almacenes.Almacen;
import me.khmdev.APIBase.Almacenes.Datos;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class APIC implements Datos{
	private AlmacenChest cofres=new AlmacenChest();
	public APIC(){
		API.getInstance().getCentral().insertar(this);
	}
	private String help() {
		String s = "";
		s += "/apic <Command>\n";
		s += "Commands:  guarda\n";
		s += "           carga\n";


		return s;
	}

	@SuppressWarnings("deprecation")
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (cmd.getName().equalsIgnoreCase("apic")) {
			if (args.length < 2) {
				sender.sendMessage(help());
				return true;
			}
			if (args[0].equalsIgnoreCase("guarda")) {
				Player pl = Bukkit.getServer().getPlayer(sender.getName());
				if (sender.getName() == "CONSOLE") {
					return true;
				}
				
				Block b = pl.getTargetBlock(null, 3);
				if (b != null
						&& (b.getType() == Material.CHEST ||
								b.getType()==Material.TRAPPED_CHEST)) {
					Chest c=(Chest) b.getState();
					
					cofres.addChest(args[1], new Cofre(b.getType().name(),b.getData(), 
							c.getInventory(), b.getLocation()));
					sender.sendMessage("Almacenado Chest");
					return true;
				}
				sender.sendMessage("No es un Chest");

				return true;
			}

			
			if (args[0].equalsIgnoreCase("carga")) {

				if(!cofres.containChest(args[1])){
					sender.sendMessage("No existe Chest");
					return true;
				}
					
				cofres.getChest(args[1]).crear();
				sender.sendMessage("Chest cargado");

				return true;
			}
			sender.sendMessage(help());

			return true;
		}

		return false;
	}
	@Override
	public void cargar(Almacen nbt) {
		Almacen alm=nbt.getAlmacen("APIC");
		cofres.cargar(alm);
		nbt.setAlmacen(alm.getName(), alm);
	}
	@Override
	public void guardar(Almacen nbt) {
		Almacen alm=nbt.getAlmacen("APIC");
		cofres.guardar(alm);
		nbt.setAlmacen(alm.getName(), alm);

	}
}
