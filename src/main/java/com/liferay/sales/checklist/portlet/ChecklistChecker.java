package com.liferay.sales.checklist.portlet;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class ChecklistChecker {

	private static final String MSG = "failed-checklist-item";

	public List<ChecklistItem> check(ThemeDisplay themeDisplay) {
		LinkedList<ChecklistItem> result = new LinkedList<ChecklistItem>();
		for (ChecklistProvider provider : providers) {
			try {
				ChecklistItem item = provider.check(themeDisplay);
				if(item==null) {
					item = new ChecklistItem(false, "null " + provider.getClass().getName(), null);
				}
				result.add(item);
			} catch (Exception e) {
				result.add(new ChecklistItem(
						false, 
						lookupMessage(themeDisplay.getLocale(), false, MSG, e.getClass().getName() + " " + e.getMessage() + " " + " from " + provider.getClass().getName() ), 
						null 
						)
				);
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
	
	protected String lookupMessage(Locale locale, boolean state, String key, Object... info) {
		String prefix = state ? "message-" : "missing-";
		ResourceBundle bundle = ResourceBundleUtil.getBundle(locale, this.getClass().getClassLoader());
		return ResourceBundleUtil.getString(bundle, prefix+key, info);
	}
}
