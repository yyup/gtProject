package com.gt.wl.model;

import org.joyone.model.IPdTreeModel;

/**
 * 树结点对象
 * @author liuyj
 * 
 */
public class Node implements java.io.Serializable, IPdTreeModel {
	private String nodeId;
	private String name;
	private String parentId;
	private boolean isLast;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public boolean isLast() {
		return isLast;
	}

	public void setLast(boolean isLast) {
		this.isLast = isLast;
	}

}
