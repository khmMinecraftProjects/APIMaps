package me.khmdev.APIMaps.Auxiliar;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.block.Action;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CustomItem;
import me.khmdev.APIBase.Almacenes.LocAlmacen;

public class ItemSelector extends CustomItem {
	public ItemSelector() {
		item = new ItemStack(Material.STICK);
		ItemMeta m = item.getItemMeta();
		m.setDisplayName("01001010101");
		item.setItemMeta(m);
	}

	@Override
	public void execute(InventoryClickEvent event) {

	}

	@Override
	public void execute(PlayerInteractEvent event) {
		if (!event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
			return;
		}
		Player player = event.getPlayer();

		if (!event.hasBlock()) {
			player.sendMessage("No hay bloque");
			return;
		}
		Location loc = event.getClickedBlock().getLocation();

		Location a = LocAlmacen.cargar(player,"LocA"), 
				b = LocAlmacen.cargar(player,"LocB");

		if (a == null && b == null) {
			LocAlmacen.guardar(player,loc,"LocA");
			player.sendMessage("Set A");
		} else if (b == null) {
			LocAlmacen.guardar(player,loc,"LocB");
			player.sendMessage("Set B");

		} else {

			LocAlmacen.guardar(player,loc,"LocA");
			LocAlmacen.guardar(player,null,"LocB");
			
			player.sendMessage("Reset A y b, Set A");

		}

		event.setCancelled(true);

	}


}
