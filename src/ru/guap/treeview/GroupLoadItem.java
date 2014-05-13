package ru.guap.treeview;

public class GroupLoadItem {
	public String name;
	public int streamId;
	public String kindLoad;
	public int id;
	public boolean isAppointed;
	public int teacherId;
	
	public int valueCO, valueEP, valueCF, valueG;
	public String nameDisc;
	
	public GroupLoadItem(String aName, int aStreamId, String aKindLoad, int aId, boolean aIsAppointed, int aTeacherId) {
		this.name = aName;
		this.streamId = aStreamId;
		this.kindLoad = aKindLoad;
		this.id = aId;
		this.isAppointed = aIsAppointed;
		this.teacherId = aTeacherId;
	}
}
