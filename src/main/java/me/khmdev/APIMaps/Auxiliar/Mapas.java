package me.khmdev.APIMaps.Auxiliar;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import me.khmdev.APIBase.Almacenes.Almacen;
import me.khmdev.APIBase.Almacenes.Datos;


public class Mapas implements Datos {
	private HashMap<String, Map> mapas;
	public Mapas() {
		mapas = new HashMap<String, Map>();
	}

	public Map getMap(String name) {
		return mapas.get(name);
	}

	public void setMap(String name, Map map) {
		mapas.put(name, map);
	}
	public boolean contain(String name) {

		return mapas.containsKey(name);
	}
	@Override
	public void guardar(Almacen nbt) {
		nbt.clear();
		Iterator<Entry<String, Map>> it = mapas.entrySet().iterator();

		while (it.hasNext()) {
			
			Entry<String, Map> next = it.next();

			nbt.escribir(next.getValue(), next.getKey());
		}

	}

	@Override
	public void cargar(Almacen nbt) {
		Iterator<Almacen> it = nbt.getAlmacenes().iterator();
		while (it.hasNext()) {

			Almacen n = (Almacen) it.next();
			Map m = new Map();
			m.cargar(n);
			mapas.put(n.getName(), m);
		}
	}




}
