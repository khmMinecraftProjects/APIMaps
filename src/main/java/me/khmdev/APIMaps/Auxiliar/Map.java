package me.khmdev.APIMaps.Auxiliar;

import java.util.LinkedList;
import java.util.List;

import me.khmdev.APIBase.Almacenes.Almacen;
import me.khmdev.APIBase.Almacenes.Datos;
import me.khmdev.APIMaps.APIM;
import me.khmdev.APIMaps.Chest.AlmacenChest;
import me.khmdev.APIMaps.Chest.Cofre;

import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.World;

public class Map implements Datos {

	private Zona zona;
	byte[] data;
	int[] blocks;
	String world, name;
	private int x, y, z;
	private boolean reparando;
	private AlmacenChest chests = new AlmacenChest();

	public Map() {
		setReparando(false);
	}

	public boolean isReparando() {
		return reparando;
	}

	public void setReparando(boolean reparando) {
		if (reparando) {
			removeItems();

		}
		this.reparando = reparando;
	}

	public void setBlock(int[] block) {
		blocks = block;
	}

	public void setData(byte[] dat) {
		data = dat;
	}

	public int[] getBlock() {
		return blocks;
	}

	public byte[] getData() {
		return data;
	}

	public void setWorld(String id2) {
		world = id2;
	}

	public World getWorld() {
		return Bukkit.getServer().getWorld(world);
	}

	public void setName(String id2) {
		name = id2;
	}

	public String getName() {
		return name;
	}

	public void iniciarIt() {

	}

	@Override
	public void guardar(Almacen nbt) {

		if (zona != null) {
			nbt.setByte("nivel", zona.getPerms());

			int[] vecA = { zona.minX(), zona.minY(), zona.minZ() };
			int[] vecB = { zona.maxX(), zona.maxY(), zona.maxZ() };

			nbt.setIntArray("A", vecA);
			nbt.setIntArray("B", vecB);
		}
		if (getWorld() != null) {
			nbt.setString("World", getWorld().getName());
		} else {
			if (zona != null) {
				nbt.setString("World", zona.getWorld().getName());
			}
		}

		nbt.setByteArray("Data", getData());
		nbt.setIntArray("Blocks", getBlock());
		chests.guardar(nbt);
	}

	@Override
	public void cargar(Almacen nbt) {

		setWorld(nbt.getString("World"));

		int[] vecA = nbt.getIntArray("A");

		int[] vecB = nbt.getIntArray("B");

		setName(nbt.getName());

		if (vecA.length > 0 && vecB.length > 0) {
			zona = new Zona( new Location(getWorld(), vecA[0], vecA[1],
					vecA[2]), new Location(getWorld(), vecB[0], vecB[1],
					vecB[2]));
			
		}
		setData(nbt.getByteArray("Data"));

		setBlock(nbt.getIntArray("Blocks"));


		chests.cargar(nbt);
	}

	public Zona getZona() {
		return zona;
	}

	public void setZona(Zona zona) {
		this.zona = zona;
	}

	public String toString() {

		return name + ": " + zona;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getZ() {
		return z;
	}

	public void setZ(int z) {
		this.z = z;
	}

	public AlmacenChest getAChests() {
		return chests;
	}

	public void addChest(String s, Cofre c) {
		chests.addChest(s, c);
	}

	public void removeItems() {
		zona.clearItems();
	}

	public void asegurarMargenes() {
		Zona[] cubo=new Zona[6];
		cubo[0]=new Zona(new Location(getWorld(), 
				zona.getLocA().getX(),
				zona.getLocA().getY(), 
				zona.getLocA().getZ()),
				new Location(getWorld(), 
						zona.getLocA().getX()+zona.getX(),
						zona.getLocA().getY()+zona.getY(), 
						zona.getLocA().getZ()));
		cubo[1]=new Zona(new Location(getWorld(), 
				zona.getLocA().getX(),
				zona.getLocA().getY(), 
				zona.getLocA().getZ()),
				new Location(getWorld(), 
						zona.getLocA().getX()+zona.getX(),
						zona.getLocA().getY(), 
						zona.getLocA().getZ()+zona.getZ()));
		
		cubo[2]=new Zona(new Location(getWorld(), 
				zona.getLocA().getX(),
				zona.getLocA().getY(), 
				zona.getLocA().getZ()),
				new Location(getWorld(), 
						zona.getLocA().getX(),
						zona.getLocA().getY()+zona.getY(), 
						zona.getLocA().getZ()+zona.getZ()));
		
		cubo[3]=new Zona(new Location(getWorld(), 
				zona.getLocB().getX(),
				zona.getLocB().getY(), 
				zona.getLocB().getZ()),
				new Location(getWorld(), 
						zona.getLocB().getX()-zona.getX(),
						zona.getLocB().getY()-zona.getY(), 
						zona.getLocB().getZ()));
		cubo[4]=new Zona(new Location(getWorld(), 
				zona.getLocB().getX(),
				zona.getLocB().getY(), 
				zona.getLocB().getZ()),
				new Location(getWorld(), 
						zona.getLocB().getX()-zona.getX(),
						zona.getLocB().getY(), 
						zona.getLocB().getZ()-zona.getZ()));
		cubo[5]=new Zona(new Location(getWorld(), 
				zona.getLocB().getX(),
				zona.getLocB().getY(), 
				zona.getLocB().getZ()),
				new Location(getWorld(), 
						zona.getLocB().getX(),
						zona.getLocB().getY()-zona.getY(), 
						zona.getLocB().getZ()-zona.getZ()));
		for (int i = 0; i < cubo.length; i++) {
			asegurar(cubo[i], "C"+i);
		}
	}
	
	public void desAsegurarMargenes() {
		for (int i = 0; i <6; i++) {
			desAsegurar( "C"+i);
		}
	}

	private void asegurar(Zona z,String c){
		z.setPerms((byte) 99);
		APIM.getInstance().AsegurarZona(name+"_"+c, z);
	}
	private void desAsegurar(String c){
		APIM.getInstance().EliminaZS(name+"_"+c);
	}

	public List<Chunk> getChunks() {
		List<Chunk> c=new LinkedList<>();
		Location l=zona.getLocA(),
				mx=zona.getLocB();
		for (int i = (int) l.getX(); i < mx.getX(); i++) {
			for (int o = (int) l.getX(); o < mx.getX(); o++) {
				c.add(l.clone().add(i, 0, o).getChunk());
			}
		}

		return c;
	}
}
