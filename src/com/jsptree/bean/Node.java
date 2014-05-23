
package com.jsptree.bean;

import java.util.ArrayList;
import java.util.List;

import ru.guap.treeview.GroupLoadItem;
import ru.guap.treeview.GroupStream;

public class Node {
	// Current node id
	private String nodeId;
	// Current node name
	private String nodeName;
	// Status of node ( checked or not checked )
	private int isSelected;
	
	private boolean isAppointed;
	
	private boolean isMultiNode;
	private int streamId;
	
	private int discId;
	
	// Each node has list of children
	private ArrayList<Node> childrens = new ArrayList<>();	
	// This will hold exceptions if occurred.
	private String errorDescription;
	
	private boolean isLoadNode; // leaf node

	private GroupLoadItem item;
	private GroupStream stream;
	
	public Node(String id) {
		this.nodeId = id;
	}
	
	public Node(String id, String name) {
		this.nodeId = id;
		this.nodeName = name;
	}	
	
	public boolean isMultiNode() {
		return isMultiNode;
	}

	public GroupLoadItem getItem() {
		return item;
	}

	public void setItem(GroupLoadItem item) {
		this.item = item;
	}

	public void setMultiNode(boolean isMultiNode) {
		this.isMultiNode = isMultiNode;
	}

	public int getStreamId() {
		return streamId;
	}

	public void setStreamId(int streamId) {
		this.streamId = streamId;
	}

	public int getDiscId() {
		return discId;
	}

	public void setDiscId(int discId) {
		this.discId = discId;
	}

	public String getNodeId() {
		if (!this.isMultiNode) {
			return nodeId;
		} else {
			return new Integer(this.discId + 23 * this.streamId).toString();
		}
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getNodeName() {
		return nodeName;
	}

	public void setNodeName(String nodeName) {
		this.nodeName = nodeName;
	}

	public ArrayList<Node> getChildren() {
		return childrens;
	}

	public void setChildren(ArrayList<Node> childrens) {
		this.childrens = childrens;
	}
	
	public void addChildNode(Node child) {
		this.childrens.add(child);
	}

	public int getIsSelected() {
		return isSelected;
	}

	public void setIsSelected(int isSelected) {
		this.isSelected = isSelected;
	}

	public String getErrorDescription() {
		return errorDescription;
	}

	public void setErrorDescription(String errorDescription) {
		this.errorDescription = errorDescription;
	}
	
	public boolean isLoadNode() {
		return isLoadNode;
	}
	
	public void setLoadNode(boolean load) {
		this.isLoadNode = load;
	}
	
	public void setAppointed(boolean isAppointed) {
		this.isAppointed = isAppointed;
	}
	
	public boolean isAppointed() {
		return this.isAppointed;
	}

	public GroupStream getStream() {
		return stream;
	}

	public void setStream(GroupStream stream) {
		this.stream = stream;
	}
}


