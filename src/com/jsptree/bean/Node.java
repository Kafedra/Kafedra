
package com.jsptree.bean;

import java.util.ArrayList;
import java.util.List;

public class Node {
	// Current node id
	private String nodeId;
	// Current node name
	private String nodeName;
	// Status of node ( checked or not checked )
	private int isSelected;
	
	private boolean isAppointed;
	
	// Each node has list of children
	private ArrayList<Node> childrens = new ArrayList<>();	
	// This will hold exceptions if occurred.
	private String errorDescription;
	
	private boolean isLoadNode; // leaf node

	public Node(String id) {
		this.nodeId = id;
	}
	
	public Node(String id, String name) {
		this.nodeId = id;
		this.nodeName = name;
	}	
	
	public String getNodeId() {
		return nodeId;
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
}


