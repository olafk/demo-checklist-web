package com.liferay.sales.checklist.portlet;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import java.util.LinkedList;
import java.util.List;

public class ChecklistChecker {

	public List<ChecklistItem> check(ThemeDisplay themeDisplay) {
		LinkedList<ChecklistItem> result = new LinkedList<ChecklistItem>();
		for (ChecklistProvider provider : providers) {
			try {
				result.add(provider.check(themeDisplay));
			} catch (Exception e) {
				result.add(new ChecklistItem(false, "failed-checklist-item", null, 
						e.getClass().getName() + " " + e.getMessage() + " " + " from " + provider.getClass().getName()));
			}
		}
		return result;
	}
	
	public void doRegister(ChecklistProvider checklistProvider) {
		providers.add(checklistProvider);
	}

	public void doUnRegister(ChecklistProvider checklistProvider) {
		providers.remove(checklistProvider);
	}
	
	private List<ChecklistProvider> providers = new LinkedList<ChecklistProvider>();

}
