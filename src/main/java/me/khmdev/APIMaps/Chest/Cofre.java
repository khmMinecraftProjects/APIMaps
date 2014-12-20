package me.khmdev.APIMaps.Chest;

import me.khmdev.APIAuxiliar.Inventory.InventoryNBT;
import me.khmdev.APIBase.Almacenes.Almacen;
import me.khmdev.APIBase.Almacenes.Datos;
import me.khmdev.APIBase.Almacenes.local.LocAlmacen;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class Cofre implements Datos {
	private String material;
	private byte data;
	private Inventory inventory;
	private Location loc;

	public Cofre(String d, byte b, Inventory inv, Location l) {
		material = d;
		inventory = Bukkit.createInventory(inv.getHolder(), inv.getSize());
		inventory.setContents(inv.getContents());
		loc = l;
		data = b;
	}

	public Cofre() {
	}

	@SuppressWarnings("deprecation")
	public void crear() {

		Block b = loc.getBlock();
		b.setType(Material.getMaterial(material));
		b.setData(data);

		if (b.getType() != Material.CHEST
				&& b.getType() != Material.TRAPPED_CHEST) {
			return;
		}

		Chest c = (Chest) b.getState();

		c.getInventory().clear();
		ItemStack[] it = inventory.getContents();
		ItemStack[] aux=
				c.getInventory().getContents();
		for (int i = 0; i < it.length; i++) {

			if (it[i] != null) {
				if(c.getInventory().getSize()>i){
					aux=c.getInventory().getContents();
					aux[i]=(it[i]);
					c.getInventory().setContents(aux);
				}else{

					c.getInventory().addItem(it[i]);

				}
			}
		}

		c.update();
	}

	@Override
	public void cargar(Almacen nbt) {
		InventoryNBT inv = new InventoryNBT(nbt.getName());
		inv.initInventory(nbt);
		ItemStack[] stack = inv.getInventory();

		inventory = Bukkit.getServer().createInventory(null, stack.length,
				nbt.getName());
		inventory.setContents(stack);
		
		material = nbt.getString("Material");
		data = nbt.getByte("Data");
		loc = LocAlmacen.cargar(nbt, "Loc");
	}

	@Override
	public void guardar(Almacen nbt) {
		InventoryNBT inv = new InventoryNBT(nbt.getName(),
				inventory.getContents());
		inv.saveInventory(nbt);
		nbt.setString("Material", material);
		nbt.setByte("Data", data);
		LocAlmacen.guardar(nbt, loc, "Loc");

	}
}
