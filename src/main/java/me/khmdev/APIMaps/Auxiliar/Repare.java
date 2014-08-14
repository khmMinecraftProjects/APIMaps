package me.khmdev.APIMaps.Auxiliar;

import me.khmdev.APIMaps.APIM;

import org.bukkit.Bukkit;
import org.bukkit.Effect;
//import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.scheduler.BukkitRunnable;

public class Repare extends BukkitRunnable {
	int[] blocks;
	byte[] data;
	Zona zona;
	Map m;
	double minX, maxX, minY, maxY, minZ, maxZ;
	private byte dato=0;
	int i,  type;
	World world;
	APIM api;
	CommandSender sender;
	
	long timeO=500,ini;
    
	public Repare(Map map, APIM plug, CommandSender send) {
		m=map;
		map.setReparando(true);
		blocks = map.getBlock();
		data = map.getData();
		zona=map.getZona();
		if(zona!=null){
			setVariables();
		}
		world = map.getWorld();
		i = 0;
		m.setX((int) minX);
		m.setY((int) minY);
		m.setZ((int) minZ);
		api = plug;
		sender = send;
		type = -1;
	}
	public void setVariables() {
		minX = zona.minX();
		maxX = zona.maxX();
		minY = zona.minY();
		maxY = zona.maxY();
		minZ = zona.minZ();
		maxZ = zona.maxZ();
		m.setX((int) minX);
		m.setY((int) minY);
		m.setZ((int) minZ);
	}

	public Repare(Location loc,Map map, APIM plug, CommandSender send) {
		m=map;
		map.setReparando(true);
		blocks = map.getBlock();
		data = map.getData();
		zona=map.getZona();
		if(zona!=null){
			setVariables(loc);
		}
		world = map.getWorld();
		i = 0;
		api = plug;
		sender = send;
		type = -1;
	}
	public void setVariables(Location l) {
		minX = l.getX();
		maxX = l.getX()+(zona.maxX()-zona.minX());
		minY = l.getY();
		maxY = l.getY()+(zona.maxY()-zona.minY());
		minZ = l.getZ();
		maxZ = l.getZ()+(zona.maxZ()-zona.minZ());
		m.setX((int) minX);
		m.setY((int) minY);
		m.setZ((int) minZ);
		
	}
	
	public Repare(int x,byte d, Map map, APIM ap, CommandSender send) {
		dato=d;
		type = x;
		blocks = null;
		data = null;
		zona=map.getZona();
		world = map.getWorld();
		i = 0;
		m=map;
		if(zona!=null){
			setVariables();
		}
		api = ap;
		sender = send;

	}



	@SuppressWarnings("deprecation")
	public void run() {
		if(zona==null){
			m.setReparando(false);
			if(sender!=null){sender.sendMessage("Error mapa null");}
			//Bukkit.getScheduler().cancelTask(id);
			try {
				Bukkit.getScheduler().cancelTask(id);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
		ini = System.currentTimeMillis();
		m.setReparando(true);
		Location loc;
		Block ant;
		if (type == -1) {
			while (m.getX() <= maxX) {
				while (m.getY() <= maxY) {
					while (m.getZ() <= maxZ) {
						
						if(i>=blocks.length){
							m.setReparando(false);
							Bukkit.getScheduler().cancelTask(id);
							return;
						}
						if ((ini+timeO)-System.currentTimeMillis()<0) {
							return;
						}
						
						loc = new Location(world, m.getX(), m.getY(), m.getZ());
						ant = world.getBlockAt(loc);

						if (!ConstantesMaps.exceptions.contains(blocks[i])&&
								!ConstantesMaps.chests.contains(blocks[i])
									){
							
							if (ant.getTypeId() != blocks[i]
									|| ant.getData() != data[i]) {
			

								ant.setTypeId(blocks[i]);
								ant.setData(data[i]);
								world.playEffect(loc, Effect.STEP_SOUND,
										blocks[i]);
							}

						}
						i++;

						m.setZ(m.getZ()+1);
					}m.setZ((int) minZ);
					m.setY(m.getY()+1);
				}m.setY((int) minY);
				m.setX(m.getX()+1);
			}
			m.getAChests().spawnAll();

		} else {
			if (ConstantesMaps.exceptions.contains(type)) {
				sender.sendMessage("Id no permitido");
				m.setReparando(false);
				Bukkit.getScheduler().cancelTask(id);
				return;
			}
			while (m.getX() <= maxX) {
				while (m.getY() <= maxY) {
					while (m.getZ() <= maxZ) {

						if ((ini+timeO)-System.currentTimeMillis()<0) {
							return;
						}
						loc = new Location(world, m.getX(), m.getY(), m.getZ());
						ant = world.getBlockAt(loc);

						if (ant.getTypeId() != type
								||ant.getData()!=dato) {
							ant.setTypeId(type);
							ant.setData(dato);
							world.playEffect(loc, Effect.STEP_SOUND, type);
						}
						i++;
						m.setZ(m.getZ()+1);
					}m.setZ((int) minZ);
					m.setY(m.getY()+1);
				}m.setY((int) minY);
				m.setX(m.getX()+1);
			}

		}
		

		m.setReparando(false);
		if(sender!=null){sender.sendMessage("Reparado");}
		Bukkit.getScheduler().cancelTask(id);

	}
	
	int id;

	public void setId(int i) {
		id = i;
	}

}
