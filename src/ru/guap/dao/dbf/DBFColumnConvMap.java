package ru.guap.dao.dbf;

import java.util.HashMap;

public class DBFColumnConvMap {
    public static HashMap<String, String> map = new HashMap<>();
    
    static {
	map.put("FORM", "Form");
	map.put("NSEM", "Nsem");
	map.put("OKCO", "OKCO");
	map.put("SPZ", "Spec");
	map.put("FAK", "Fac");
	map.put("RSPE", "CodeSpec");
	map.put("GROUP", "Group");
	map.put("STUDG", "StudG");
	map.put("STUDKNA", "StudCO");
	map.put("STUDKIN", "StudCF");
	map.put("KODDISC", "CodeDisc");
	map.put("RDISC", "NameDisc");
	map.put("NPOT", "Nstream");
	map.put("VIDNAGR", "KindLoad");
	map.put("ZNUP", "ValueEP");
	map.put("ZNGB", "ValueG");
	map.put("ZNKONTN", "ValueCO");
	map.put("ZNKONTI", "ValueCF");
	map.put("RKAF1", "Nkaf");
	map.put("DFAK", "Nfac");
	map.put("DKAF", "NKafFor");
    }
}
