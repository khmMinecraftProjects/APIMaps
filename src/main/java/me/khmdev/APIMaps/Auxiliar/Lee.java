package me.khmdev.APIMaps.Auxiliar;



import me.khmdev.APIMaps.APIM;
import me.khmdev.APIMaps.Chest.Cofre;

import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class Lee extends BukkitRunnable {
	int[] blocks;
	byte[] data;
	Zona zona;

	double minX, maxX, minY, maxY, minZ, maxZ;
	int i,n;
	World world;
	APIM pluging;
	String name;
	Map m;
	CommandSender sender;

	long timeO=500,ini;


	public Lee(Map map, APIM plug, String txt, CommandSender send) {
		
		blocks = map.getBlock();
		data = map.getData();
		zona = map.getZona();
		setVariables();
		world = map.getWorld();
		i = 0;
		pluging = plug;
		name = txt;
		m = map;
		m.setX((int) minX);
		m.setY((int) minY);
		m.setZ((int) minZ);
		sender = send;
		n = (int) ((Math.abs((minX - maxX)) + 1)
				* (Math.abs((minY - maxY)) + 1) * (Math.abs((minZ - maxZ)) + 1));
		blocks = new int[n];
		data = new byte[n];
	}

	public void setVariables() {
		minX = zona.minX();
		maxX = zona.maxX();
		minY = zona.minY();
		maxY = zona.maxY();
		minZ = zona.minZ();
		maxZ = zona.maxZ();
	}

	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		ini = System.currentTimeMillis();

		while (m.getX() <= maxX) {

			while (m.getY() <= maxY) {

				while (m.getZ() <= maxZ) {


					if ((ini+timeO)-System.currentTimeMillis()<0) {
						return;
					}

					Block block = world
							.getBlockAt(m.getX(), m.getY(), m.getZ());
					blocks[i] = block.getTypeId();
					data[i] = block.getData();
					if(ConstantesMaps.chests.contains(blocks[i])){
						Chest c=(Chest) block.getState();
						m.addChest(i+"", new Cofre(block.getType().name(), data[i], c.getInventory()
								, block.getLocation()));
					}
					i++;
					m.setZ(m.getZ() + 1);
				}
				m.setZ((int) minZ);
				m.setY(m.getY() + 1);
			}
			m.setY((int) minY);
			m.setX(m.getX() + 1);
		}
		m.setBlock(blocks);
		m.setData(data);
		pluging.setMap(m, name);
		sender.sendMessage("Guardado");
		Bukkit.getScheduler().cancelTask(id);

	}
	int id;

	public void setId(int idd) {
		id = idd;
	}

}
