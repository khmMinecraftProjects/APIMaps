package me.khmdev.APIMaps.lang;

import java.util.Locale;
import java.util.ResourceBundle;

public class Lang {
	public static ResourceBundle lang=ResourceBundle.getBundle(
			"me.khmdev.APIMaps.lang.txt",
			new Locale("es", "ES"));;

	public static String get(String s){
		return lang.getString(s);
	}

}
