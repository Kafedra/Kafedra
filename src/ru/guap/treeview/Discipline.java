package ru.guap.treeview;

import java.util.HashMap;
import java.util.LinkedList;

public class Discipline {

	public String name;
	public int id;
	
	private HashMap<Integer, GroupStream> streams = new HashMap<>();
	private GroupStream zeroStream;
	
	public Discipline(String discName, int discId) {
		this.name = discName;
		this.id = discId;
	}
	
	public HashMap<Integer, GroupStream> getSteams() {
		return this.streams;
	}
	
	public void addStream(GroupStream s) {
		this.streams.put(s.getId(), s);
	}
	
	public void setZeroStream(GroupStream s) {
		this.zeroStream = s;
	}
	
	public GroupStream getZeroStream() {
		return this.zeroStream;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Discipline) {
			Discipline disc = (Discipline) other;
			
			return this.id == disc.id && this.name.equals(other);
		} else {
			return false;
		}
	}
}
