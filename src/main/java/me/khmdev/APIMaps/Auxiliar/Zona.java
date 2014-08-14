package me.khmdev.APIMaps.Auxiliar;

import java.util.Iterator;

import me.khmdev.APIBase.Almacenes.Almacen;
import me.khmdev.APIBase.Almacenes.Datos;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

public class Zona implements Datos {
	protected Location a, b;
	protected World w;
	protected int x, y, z;
	protected byte security = 0;
	
	public Zona(World ww) {
		w=ww;a=null;b=null;
	}

	public Zona(Location aa, Location bb) {
		
		a = aa;
		b = bb;
		a = new Location(aa.getWorld(), Math.min(aa.getX(), bb.getX()),
				Math.min(aa.getY(), bb.getY()), Math.min(aa.getZ(), bb.getZ()));
		b = new Location(aa.getWorld(), Math.max(aa.getX(), bb.getX()),
				Math.max(aa.getY(), bb.getY()), Math.max(aa.getZ(), bb.getZ()));
		x = (int) (b.getX() - a.getX());
		y = (int) (b.getY() - a.getY());
		z = (int) (b.getZ() - a.getZ());
	}

	public int getNum() {
		return (x + 5) * (y + 5) * (z + 5);
	}

	public Zona(Location aa, int xx, int yy, int zz) {
		a = aa;
		b=a.clone().add(xx, yy, zz);
		x = xx;
		y = yy;
		z = zz;

	}

	public Zona() {
	}

	public Location getLocA() {
		return a;
	}

	public Location getLocB() {
		return b;
	}

	public int minX() {
		return (int) a.getX();
	}

	public int maxX() {
		return minX() + x;
	}

	public int minY() {
		return (int) a.getY();
	}

	public int maxY() {
		return minY() + y;
	}

	public int minZ() {
		return (int) a.getZ();
	}

	public int maxZ() {
		return minZ() + z;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getZ() {
		return z;
	}

	public String toString() {
		return "X: " + minX() + "-" + maxX() + " Y: " + minY() + "-" + maxY()
				+ " Z: " + minZ() + "-" + maxZ() + ",securiry:" + security;
	}

	public World getWorld() {
		return w;
	}

	@Override
	public void cargar(Almacen nbt) {
		if (nbt == null) {
			return;
		}

		security = nbt.getByte("nivel");
		String wd = nbt.getString("w");
		w = Bukkit.getServer().getWorld(wd);

		int[] vecA = nbt.getIntArray("A");
		int[] vecB = nbt.getIntArray("B");
		if ((vecA != null&&vecA.length>2) && (vecB != null&&vecB.length>2)
				&& w != null) {
			a = new Location(w, vecA[0], vecA[1], vecA[2]);
			b = new Location(w, vecB[0], vecB[1], vecB[2]);
			x = (int) (b.getX() - a.getX());
			y = (int) (b.getY() - a.getY());
			z = (int) (b.getZ() - a.getZ());
		}

	}

	@Override
	public void guardar(Almacen nbt) {
		if (nbt == null) {
			return;
		}
		if(this==null){return;}
		nbt.setByte("nivel", security);
		
		if(w!=null&&w.getName()!=null){
			nbt.setString("w", w.getName());
		}
		if (a != null) {
			int[] vecA = { (int) a.getX(), (int) a.getY(), (int) a.getZ() };
			nbt.setIntArray("A", vecA);
		}

		if (b != null) {
			int[] vecB = { (int) b.getX(), (int) b.getY(), (int) b.getZ() };
			nbt.setIntArray("B", vecB);
		}
	}

	public boolean esta(Location loc) {

		return (a==null||b==null)?
				w!=null&&loc.getWorld().equals(w):
				(minX() <= loc.getX() && loc.getX() <= maxX()
				&& minY() <= loc.getY() && loc.getY() <= maxY()
				&& minZ() <= loc.getZ() && loc.getZ() <= maxZ());
	}

	public byte getPerms() {
		return security;
	}

	public void setPerms(byte o) {
		security = o;
	}

	public void clearItems() {
		Location radio = new Location(a.getWorld(), (b.getX() - a.getX()) / 2,
				(b.getY() - a.getY()) / 2, (b.getZ() - a.getZ()) / 2), medio = new Location(
				a.getWorld(), a.getX() + radio.getX(), a.getY() + radio.getY(),
				a.getZ() + radio.getZ());

		@SuppressWarnings("deprecation")
		LivingEntity ent = a.getWorld().spawnCreature(medio, EntityType.BAT);
		Iterator<Entity> list = ent.getNearbyEntities(Math.abs(radio.getX())+100,
				Math.abs(radio.getY())+100, Math.abs(radio.getZ())+100).iterator();
		while (list.hasNext()) {
			Entity is = list.next();
			if (!(is instanceof Player)) {
				is.remove();
			}
		}
		ent.remove();
	}

}
