package me.khmdev.APIMaps.Auxiliar;

import java.util.ArrayList;
import java.util.List;

public class ConstantesMaps {
	@SuppressWarnings("serial")
	public static List<Integer> exceptions = new ArrayList<Integer>() {
		{
			add(130);
			add(323);
			add(63);
			add(68);

		}
	};
	@SuppressWarnings("serial")
	public static List<Integer> chests = new ArrayList<Integer>() {
		{
			add(54);
			add(146);
		}
	};
	public static final byte BREAK_BLOCKS = 1, INTERACT_BLOCKS = 2,
			ATTACK_ENTITYS = 4, EXPLOSIONS = 8, BURN_BLOCKS = 16,
			PLACE_BLOCKS = 32, USE_PISTONES = 64;
}
