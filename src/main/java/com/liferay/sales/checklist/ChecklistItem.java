package com.liferay.sales.checklist;

public class ChecklistItem {
	public ChecklistItem(boolean resolved, String name, String link, String ... info) {
		this.resolved = resolved;
		this.name = name;
		this.info = info;
		this.link = link;
	}
	
	public boolean isResolved() {
		return resolved;
	}
	public String getName() {
		return name;
	}
	public String[] getInfo() {
		return info;
	}
	public String getLink() {
		return link;
	}

	private final boolean resolved;
	private final String name;
	private final String[] info;
	private String link;
	
}
