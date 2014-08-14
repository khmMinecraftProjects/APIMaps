package me.khmdev.APIMaps.Chest;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import me.khmdev.APIBase.Almacenes.Almacen;
import me.khmdev.APIBase.Almacenes.Datos;

public class AlmacenChest implements Datos{
	private HashMap<String, Cofre> cofres=new HashMap<>();
	public AlmacenChest(){
		
	}
	
	public void addChest(String s,Cofre c){
		cofres.put(s,c);
	}
	
	public void removeChest(String s){
		cofres.remove(s);
	}
	
	public boolean containChest(String s){
		return cofres.containsKey(s);
	}
	
	public Cofre getChest(String s){
		return cofres.get(s);
	}
	
	public void spawnAll(){
		Iterator<Entry<String, Cofre>> it=cofres.entrySet().iterator();
		while(it.hasNext()){
			it.next().getValue().crear();
		}
	}
	
	@Override
	public void cargar(Almacen nbt) {
		Iterator<Almacen> it=nbt.getAlmacenes().iterator();
		while(it.hasNext()){
			Almacen next=it.next();
			Cofre c=new Cofre();
			c.cargar(next);
			cofres.put(next.getName(), c);
		}
		
	}
	@Override
	public void guardar(Almacen nbt) {
		Iterator<Entry<String, Cofre>> it=cofres.entrySet().iterator();
		while(it.hasNext()){
			Entry<String, Cofre> next=it.next();
			nbt.escribir(next.getValue(), next.getKey());
		}
	}
}
