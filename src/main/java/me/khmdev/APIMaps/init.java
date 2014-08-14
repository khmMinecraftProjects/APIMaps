package me.khmdev.APIMaps;

import me.khmdev.APIMaps.Chest.APIC;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class init extends JavaPlugin{
	private APIM apim;
	private APIC apic;

	public void onEnable() {
		if (!hasPluging("APIBase")||
				!hasPluging("APIAuxiliar")) {
			getLogger().severe(
					getName()
							+ " se desactivo debido ha que no encontro la API");
			setEnabled(false);
			return;
		}
		apim=new APIM(this);
		apic=new APIC();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {

		if (apim.onCommand(sender, cmd, label, args)) {
			return true;
		}
		
		if (apic.onCommand(sender, cmd, label, args)) {
			return true;
		}

		
		return false;
	}


	private static boolean hasPluging(String s) {
		try {
			return Bukkit.getPluginManager().getPlugin(s).isEnabled();
		} catch (Exception e) {

		}
		return false;
	}
	public void onDisable() {
		apim.onDisable();
	}

}
