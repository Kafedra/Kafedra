package ru.guap.treeview;

import java.util.ArrayList;
import java.util.HashSet;

public class GroupStream {
	public boolean isZeroStream;
	private ArrayList<GroupLoadItem> items;
	private int streamId;
	
	private static final String[] groupsEndings = { "группа", "группы", "групп" };
	
	private boolean isMultiGroup;
	
	public GroupStream(boolean aIsZeroStream, int aStreamId) {
		this.isZeroStream = aIsZeroStream;
		this.items = new ArrayList<>();
		this.streamId = aStreamId;
	}

	public int getId() {
		return this.streamId;
	}
	public ArrayList<GroupLoadItem> getItems() {
		return this.items;
	}

	public void addGroupItem(GroupLoadItem item) {
		this.items.add(item);
	}
	
	@Override
	public String toString() {
		if (!isZeroStream) {
			return "Поток: " + getGroupStringList();
		} else {
			int groupsCount = countUniqueGroups();
			return "Не в потоке: " + groupsCount + " " + groupsPluralEnding(groupsCount);
		}
	}
	
	public String getGroupStringList() {
		StringBuilder s = new StringBuilder();
		
		for (int i = 0; i < items.size(); i++) {
			GroupLoadItem item = items.get(i);

			s.append(item.name);

			if (i != items.size() - 1) {	
				s.append(", ");
			}
		}

		return s.toString();	
	}
	
	public int countUniqueGroups() {
		HashSet<String> groupsSet = new HashSet<>();
		for (GroupLoadItem item : this.items) {
			if (!groupsSet.contains(item.name)) {
				groupsSet.add(item.name);
			}
		}
			
		return groupsSet.size();
	}
	
	public static String groupsPluralEnding(int count) {
		int i = count % 100;

		if (i >= 11 && i <= 19) {
			return groupsEndings[2];
		} else {
			i %= 10;

			switch (i) {
				case 1: 
					return groupsEndings[0];
					
				case 2:
				case 3:
				case 4:
					return groupsEndings[1];
				default:
					return groupsEndings[2];
			}
		}
	}
	
	public boolean isMultiGroup() {
		return this.isMultiGroup;
	}
	
	public void setMultiGroup(boolean multi) {
		this.isMultiGroup = true;
	}
}
