package ru.guap.treeview;

public class GroupLoadItem {
	private String name;
	private int streamId;
	private String kindLoad;
	private int id;
	private boolean isAppointed;
	private int teacherId;
	
	public int valueCO, valueEP, valueCF, valueG;
	public String nameDisc;
	
	public GroupLoadItem(String aName, int aStreamId, String aKindLoad, int aId, boolean aIsAppointed, int aTeacherId) {
		this.setName(aName);
		this.setStreamId(aStreamId);
		this.setKindLoad(aKindLoad);
		this.setId(aId);
		this.setAppointed(aIsAppointed);
		this.setTeacherId(aTeacherId);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getStreamId() {
		return streamId;
	}

	public void setStreamId(int streamId) {
		this.streamId = streamId;
	}

	public String getKindLoad() {
		return kindLoad;
	}

	public void setKindLoad(String kindLoad) {
		this.kindLoad = kindLoad;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isAppointed() {
		return isAppointed;
	}

	public void setAppointed(boolean isAppointed) {
		this.isAppointed = isAppointed;
	}

	public int getTeacherId() {
		return teacherId;
	}

	public void setTeacherId(int teacherId) {
		this.teacherId = teacherId;
	}
}
