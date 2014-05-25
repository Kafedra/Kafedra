package ru.guap.treeview;

public class GroupLoadItem {
	private String name;
	private int streamId;
	private String kindLoad;
	private int id;
	private boolean isAppointed;
	private int teacherId;
	
	private int valueCO;
	private int valueEP;
	private int valueCF;
	private int valueG;
	public String nameDisc;
	
	private String teacherName;
	
	public GroupLoadItem(String aName, int aStreamId, String aKindLoad, int aId, boolean aIsAppointed, int aTeacherId, String teacherName) {
		this.setName(aName);
		this.setStreamId(aStreamId);
		this.setKindLoad(aKindLoad);
		this.setId(aId);
		this.setAppointed(aIsAppointed);
		this.setTeacherId(aTeacherId);
		this.setTeacherName(teacherName);
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

	public int getValueG() {
		return valueG;
	}

	public void setValueG(int valueG) {
		this.valueG = valueG;
	}

	public int getValueCF() {
		return valueCF;
	}

	public void setValueCF(int valueCF) {
		this.valueCF = valueCF;
	}

	public int getValueEP() {
		return valueEP;
	}

	public void setValueEP(int valueEP) {
		this.valueEP = valueEP;
	}

	public int getValueCO() {
		return valueCO;
	}

	public void setValueCO(int valueCO) {
		this.valueCO = valueCO;
	}
	
	public int getValueC() {
		return this.valueCF + this.valueCO;
	}

	public String getTeacherName() {
		return teacherName;
	}

	public void setTeacherName(String teacherName) {
		this.teacherName = teacherName;
	}
}
