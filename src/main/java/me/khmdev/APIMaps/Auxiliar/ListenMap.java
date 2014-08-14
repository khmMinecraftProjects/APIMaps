package me.khmdev.APIMaps.Auxiliar;

import java.util.List;

import me.khmdev.APIMaps.APIM;

import org.bukkit.Location;

import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockBurnEvent;
import org.bukkit.event.block.BlockPistonExtendEvent;
import org.bukkit.event.block.BlockPistonRetractEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class ListenMap implements Listener {
	APIM pluging;

	public ListenMap(APIM plug) {
		pluging = plug;
	}

	@EventHandler
	public void Asegurar(EntityDamageEvent event) {
		if(event.getEntity() instanceof Player
				&&((Player)event.getEntity())
				.hasPermission("apim.Damange")){
			return;
		}
		byte flag = ConstantesMaps.ATTACK_ENTITYS;
		if (pluging.estaSeguro(event.getEntity().getLocation(), flag)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void Asegurar(BlockPlaceEvent event) {
		if(event.getPlayer().hasPermission("apim.PlaceBlock")){
			return;
		}
		byte flag = ConstantesMaps.PLACE_BLOCKS;
		if (pluging.estaSeguro(event.getBlock().getLocation(), flag)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void Asegurar(BlockBurnEvent event) {

		byte flag = ConstantesMaps.BURN_BLOCKS;
		if (pluging.estaSeguro(event.getBlock().getLocation(), flag)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void Asegurar(BlockPistonExtendEvent event) {
		BlockFace s = event.getDirection();

		Location a = event.getBlock().getLocation(), loc = new Location(
				a.getWorld(), a.getX() + s.getModX(), a.getY() + s.getModY(),
				a.getZ() + s.getModZ());

		byte flag = ConstantesMaps.USE_PISTONES;

		if (pluging.estaSeguro(loc, flag)) {
			event.setCancelled(true);
		}
	}


	@EventHandler
	public void Asegurar(BlockPistonRetractEvent event) {
		BlockFace s = event.getDirection();

		Location a = event.getBlock().getLocation(), loc = new Location(
				a.getWorld(), a.getX() + s.getModX() * 2, a.getY()
						+ s.getModY() * 2, a.getZ() + s.getModZ() * 2);
		byte flag = ConstantesMaps.USE_PISTONES;

		if (pluging.estaSeguro(loc, flag)) {
			event.setCancelled(true);
		}
	}

	@EventHandler
	public void Asegurar(EntityExplodeEvent event) {
		List<Block> List = event.blockList();
		byte flag = ConstantesMaps.BREAK_BLOCKS, flag2 = ConstantesMaps.EXPLOSIONS;

		for (int i = 0; i < List.size(); i++) {
			if (pluging.estaSeguro(List.get(i).getLocation(), flag2)) {
				event.setCancelled(true);
				return;
			}
			if (pluging.estaSeguro(List.get(i).getLocation(), flag)) {
				List.remove(i);
				i--;
			}
		}
	}

	@EventHandler
	public void Asegurar(BlockBreakEvent event) {
		if(event.getPlayer().hasPermission("apim.BreakBlocks")){
			return;
		}
		
		byte flag = ConstantesMaps.BREAK_BLOCKS;

		if (pluging.estaSeguro(event.getBlock().getLocation(), flag)) {
			event.setCancelled(true);
		}

	}
	@EventHandler
	public void Asegurar(PlayerInteractEvent event) {
		if(event.getPlayer().hasPermission("apim.InteractBlock")){
			return;
		}
		byte flag = ConstantesMaps.INTERACT_BLOCKS;
		
		if (event.getAction() == Action.RIGHT_CLICK_BLOCK
				||event.getAction() == Action.PHYSICAL) {
			if (pluging.estaSeguro(event.getClickedBlock().getLocation(), flag)) {
				event.setCancelled(true);
			}
		}

	}

	

}
