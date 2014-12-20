package me.khmdev.APIMaps;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import me.khmdev.APIAuxiliar.Inventory.CustomInventorys.CItems;
import me.khmdev.APIBase.Almacenes.Almacen;
import me.khmdev.APIBase.Almacenes.Central;
import me.khmdev.APIBase.Almacenes.Datos;
import me.khmdev.APIBase.Almacenes.local.LocAlmacen;
import me.khmdev.APIBase.Auxiliar.Auxiliar;
import me.khmdev.APIMaps.Auxiliar.ItemSelector;
import me.khmdev.APIMaps.Auxiliar.Lee;
import me.khmdev.APIMaps.Auxiliar.ListenMap;
import me.khmdev.APIMaps.Auxiliar.Map;
import me.khmdev.APIMaps.Auxiliar.Mapas;
import me.khmdev.APIMaps.Auxiliar.Repare;
import me.khmdev.APIMaps.Auxiliar.Zona;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class APIM implements Datos {

	private Mapas mapas;
	private HashMap<String, Zona> seguras;
	private JavaPlugin plugin;
	private ItemSelector selector;
	private static Central central;
	private static APIM instance;
	public APIM(JavaPlugin plu) {
		plugin=plu;
		getPluging().getServer().getPluginManager()
				.registerEvents(new ListenMap(this), getPluging());

		selector = new ItemSelector();
		CItems.addItem(selector);
		seguras = new HashMap<String, Zona>();
		instance=this;
		central=new Central(plu);
		central.insertar(this);

	}

	public static APIM getInstance(){
		return instance;
	}
	public boolean onCommand(CommandSender sender, Command cmd, String label,
			String[] args) {
		if (!cmd.getName().equalsIgnoreCase("apim")) {
			return false;
		}
		if (args.length == 0) {
			sender.sendMessage(help());
			return true;
		}
		if (args[0].equalsIgnoreCase("set")) {
			if (args.length > 1) {
				set(args[1], sender);
			} else {
				set("", sender);
			}

			return true;
		}
		if (args[0].equalsIgnoreCase("guardar")) {

			if (args.length > 1) {
				guarda(args[1], sender);
			} else {
				guarda("", sender);
			}
			return true;
		}
		if (args[0].equalsIgnoreCase("cargar")) {
			if (args.length > 1) {
				carga(args[1], sender);
			} else {
				carga("", sender);
			}

			return true;
		}
		if (args[0].equalsIgnoreCase("asegurar")) {
			if (args.length > 2) {
				asegura(args[1],args[2], sender);
			} else {
				asegura("","", sender);
			}

			return true;
		}
		if (args[0].equalsIgnoreCase("asegurarMap")) {
			if (args.length > 2) {
				aseguraMap(args[1],args[2], sender);
			} else {
				aseguraMap("","", sender);
			}

			return true;
		}
		if (args[0].equalsIgnoreCase("asegurarWorld")) {
			if (args.length > 2) {
				aseguraWorld(args[1],args[2], sender);
			} else {
				aseguraWorld("","", sender);
			}

			return true;
		}
		if (args[0].equalsIgnoreCase("representar")) {
			if (args.length > 1) {
				representar(args[1], sender);
			} else {
				representar("", sender);
			}

			return true;
		}
		if (args[0].equalsIgnoreCase("sobreescribir")) {
			if (args.length > 1) {
				sobreescribir(args[1], sender);
			} else {
				sobreescribir("", sender);
			}

			return true;
		}
		if (args[0].equalsIgnoreCase("DesAsegurar")) {
			if (args.length > 1) {
				if (EliminaZS(args[1])) {
					sender.sendMessage("Zona eliminada");
				} else {
					sender.sendMessage("Zona NO eliminada");

				}
			} else {
				if (EliminaZS(args[1])) {
					sender.sendMessage("Zona eliminada");
				} else {
					sender.sendMessage("Zona NO eliminada");

				}
			}

			return true;
		}
		if (args[0].equalsIgnoreCase("get")) {
			getIt(sender);

			return true;
		}
		sender.sendMessage(help());
		return true;
	}

	private void aseguraWorld(String string, String name,
			CommandSender sender) {
		if (name == "") {
			sender.sendMessage("No se ha introducido nombre de la zona");
			return;
		}
		World m;
		
		
		if ((m = Bukkit.getWorld(name)) != null) {
			byte s=0;
			try{
				s=(byte) Integer.parseInt(string,2);
			}catch(Exception e){
				s=-1;
			}
			if(s<=0||s>258){
				sender.sendMessage("Nivel de seguridad incorrecto");
				return;
			}
			Zona z=new Zona(m);
			z.setPerms(s);

			AsegurarZona(name, z);
			sender.sendMessage("Zona " + name + " asegurada con nivel "+s);
		} else {
			sender.sendMessage("No existe mapa " + name);

		}
	}

	private void representar(String name, CommandSender sender) {

		if (name == "") {
			sender.sendMessage("No se ha introducido nombre de la zona");
			return;
		}
		if (sender.getName() == "CONSOLE") {
			return;
		}

		Player pl = sender instanceof Player?(Player)sender:null;

		Location a = LocAlmacen.cargar(pl, "LocA");

		if (a == null) {
			sender.sendMessage("No se ha introducido las coordenadas");
			return;
		}

		Map mapa = getMap(name);
		if (mapa == null) {
			sender.sendMessage("No existe mapeado");
			return;
		}
		cargaMapa(a, mapa, sender);
	}

	private void cargaMapa(Location a, Map mapa, CommandSender sender) {

		Repare run = new Repare(a, mapa, this, sender);

		int idd = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPluging(),
				run, 0L, 20L);
		run.setId(idd);
		if (sender != null) {
			sender.sendMessage("Cargando...");
		}

	}

	private void sobreescribir(String name, CommandSender sender) {

		if (name == "") {
			sender.sendMessage("No se ha introducido nombre de la zona");
			return;
		}
		Map m = getMap(name);
		if (m == null) {
			sender.sendMessage("No existe mapa");
			return;
		}

		guardaMapa(name, m.getZona(), sender);
		sender.sendMessage("Mapa " + name + " sobreescrito");

	}

	public String help() {
		String s = "";
		s += "/APIM (Comando)\n";
		s += "     get\n";
		s += "     asegurar     (zona) (seguridad)\n";
		s += "     asegurarMap  (mapa) (seguridad)\n";
		s += "     DesAsegurar  (zona)\n";
		s += "     guardar      (mapa)\n";
		s += "     cargar       (mapa)\n";
		s += "     representar  (mapa)\n";
		s += "     sobreescribir(mapa)\n";

		return s;
	}

	public boolean estaSeguro(String s) {
		return seguras.containsKey(s);
	}

	public Map getMap(String s) {
		return mapas.getMap(s);
	}

	public boolean containsMap(String s) {
		return mapas.contain(s);
	}

	public void setMap(Map m, String s) {
		mapas.setMap(s, m);
	}

	public void AsegurarZona(String name, Zona zona) {
		seguras.put(name, zona);
	}

	public boolean EliminaZS(String name) {
		if(seguras.containsKey(name)){
			seguras.get(name).setPerms((byte)0);
			seguras.remove(name);
			return true;
		}
		return false;
	}

	public void guardaMapa(String name, Zona zona, CommandSender sender) {
		Map mapa = getMap(name);
		mapa = new Map();
		mapa.setZona(zona);

		mapa.setName(name);
		String id =sender instanceof Player?((Player)sender)
				.getWorld().getName():null;
		mapa.setWorld(id);

		Lee run = new Lee(mapa, this, name, sender);
		int idd = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPluging(),
				run, 0L, 20L);
		run.setId(idd);

		if (sender != null) {
			sender.sendMessage("Guardando...");
		}
	}

	public void guarda(String name, CommandSender sender) {

		if (name == "") {
			sender.sendMessage("No se ha introducido nombre de la zona");
			return;
		}
		if (sender.getName() == "CONSOLE") {
			return;
		}

		Player pl = sender instanceof Player?(Player)sender:null;

		Location a = LocAlmacen.cargar(pl, "LocA");
		Location b = LocAlmacen.cargar(pl, "LocB");

		if (a == null || b == null) {
			sender.sendMessage("No se ha introducido las coordenadas");
			return;
		}
		guardaMapa(name, new Zona(a, b), sender);

	}

	public void asegura(String name, String args, CommandSender sender) {

		if (name == "") {
			sender.sendMessage("No se ha introducido nombre de la zona");
			return;
		}
		if (sender.getName() == "CONSOLE") {
			return;
		}
		byte s=0;
		try{
			s=(byte) Integer.parseInt(args,2);
		}catch(Exception e){
			s=-1;
		}
		if(s<0||s>258){
			sender.sendMessage("Nivel de seguridad incorrecto");
			return;
		}
		Player pl = sender instanceof Player?(Player)sender:null;

		Location a = LocAlmacen.cargar(pl, "LocA");
		Location b = LocAlmacen.cargar(pl, "LocB");

		if (a == null || b == null) {
			sender.sendMessage("No se ha introducido las coordenadas");
			return;
		}
		Zona z=new Zona(a, b);
		z.setPerms(s);
		AsegurarZona(name, z);
		
		

		sender.sendMessage("Zona " + name + " asegurada");

	}

	public void aseguraMap(String name, String string, CommandSender sender) {
		if (name == "") {
			sender.sendMessage("No se ha introducido nombre de la zona");
			return;
		}
		Map m;
		
		
		if ((m = getMap(name)) != null) {
			byte s=0;
			try{
				s=(byte) Integer.parseInt(string,2);
			}catch(Exception e){
				s=-1;
			}
			if(s<=0||s>258){
				sender.sendMessage("Nivel de seguridad incorrecto");
				return;
			}
			Zona z=m.getZona();
			z.setPerms(s);
			m.setZona(z);
			AsegurarZona(name, m.getZona());
			sender.sendMessage("Zona " + name + " asegurada con nivel "+s);
		} else {
			sender.sendMessage("No existe mapa " + name);

		}

	}

	public void cargaMapa(Map m, CommandSender sender) {

		Repare run = new Repare(m, this, sender);

		int idd = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPluging(),
				run, 0L, 20L);
		run.setId(idd);
		if (sender != null) {
			sender.sendMessage("Cargando...");
		}

	}

	public void carga(String name, CommandSender sender) {

		if (name == "") {
			sender.sendMessage("No se ha introducido nombre de la zona");
			return;
		}
		Map mapa = getMap(name);
		if (mapa == null) {
			sender.sendMessage("No existe mapeado");
			return;
		}
		cargaMapa(mapa, sender);
	}

	public void getIt(CommandSender sender) {
		if (sender.getName() != "CONSOLE") {
			Player pl = sender instanceof Player?(Player)sender:null;
			pl.getInventory().setItemInHand(selector.getItem());
		}
	}

	public void setMapa(int bl,byte d, Map m, CommandSender sender) {
		Repare run = new Repare(bl,d, m, this, sender);
		int idd = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPluging(),
				run, 0L, 20L);
		run.setId(idd);

		if (sender != null) {
			sender.sendMessage("Cargando...");
		}
	}

	public void set(String bl, CommandSender sender) {
		if (bl == "") {
			sender.sendMessage("No se ha introducido bloque");
			return;
		}
		if (sender.getName() == "CONSOLE") {
			return;
		}
		int i=Auxiliar.getNatural(Auxiliar.getSeparate(bl, 0,':'),-1);
		byte d=(byte) 
				Auxiliar.getNatural(Auxiliar.getSeparate(bl, 1,':'),0);

		Player pl = sender instanceof Player?(Player)sender:null;

		Location a = LocAlmacen.cargar(pl, "LocA");
		Location b = LocAlmacen.cargar(pl, "LocB");
		if (a == null || b == null||i==-1) {
			sender.sendMessage("No se ha introducido las coordenadas");
			return;
		}
		Map mapa = new Map();
		mapa.setZona(new Zona(a, b));

		mapa.setWorld(a.getWorld().getName());


		Repare run = new Repare(i,d, mapa, this, sender);
		int idd = Bukkit.getScheduler().scheduleSyncRepeatingTask(getPluging(),
				run, 0L, 20L);
		run.setId(idd);

	}

	public JavaPlugin getPluging() {
		return plugin;
	}

	@Override
	public void cargar(Almacen nbt) {
		mapas = new Mapas();
		nbt.leer(mapas, "Mapas");

		Almacen Zonas = nbt.getAlmacen("Securirty");
		Iterator<Almacen> it = Zonas.getAlmacenes().iterator();
		Zona a;
		while (it.hasNext()) {
			Almacen al = it.next();
			a = new Zona();
			a.cargar(al);
			seguras.put(al.getName(), a);

		}

	}

	@Override
	public void guardar(Almacen nbt) {
		nbt.escribir(mapas, "Mapas");

		Almacen Zonas = nbt.getAlmacen("Securirty");
		Iterator<Entry<String, Zona>> it = seguras.entrySet().iterator();

		while (it.hasNext()) {
			Entry<String, Zona> next = it.next();
			if (next.getValue() != null) {
				Zonas.escribir(next.getValue(), next.getKey());

			}
		}
		nbt.setAlmacen("Securirty", Zonas);

	}

	public boolean estaSeguro(Location location, byte... perms) {
		Iterator<Entry<String, Zona>> it = seguras.entrySet().iterator();
		while (it.hasNext()) {
			Zona next = it.next().getValue();
			if (next != null) {
				if (next.esta(location)) {
					byte flags = next.getPerms();
					

					for (byte p : perms) {

						if ((flags & p) == p) {
							return true;
						}
					}
					return false;
				}
			}
		}
		return false;
	}
	public void onDisable() {
		central.guardar();
	}
}
