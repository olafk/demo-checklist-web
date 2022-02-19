package com.liferay.sales.checklist.api;

public class ChecklistItem {
	public ChecklistItem(boolean resolved, String message, String link) {
		this.resolved = resolved;
		this.link = link;
		this.message = message;
	}
	
	public boolean isResolved() {
		return resolved;
	}
	public String getMessage() {
		return message;
	}
	public String getLink() {
		return link;
	}

	private final boolean resolved;
	private final String message;
	private String link;
	
}
