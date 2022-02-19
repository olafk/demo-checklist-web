package com.liferay.sales.checklist.api;

import com.liferay.portal.kernel.util.ResourceBundleUtil;

import java.util.Locale;
import java.util.ResourceBundle;

public abstract class BaseChecklistProviderImpl implements ChecklistProvider {

	public String lookupMessage(Locale locale, boolean state, String key, Object... info) {
		String prefix = state ? "message-" : "missing-";
		ResourceBundle bundle = ResourceBundleUtil.getBundle(locale, this.getClass().getClassLoader());
		return ResourceBundleUtil.getString(bundle, prefix+key, info);
	}
	
	public ChecklistItem create(boolean state, Locale locale, String link, String msgKey, Object... info) {
		String prefix = state ? "message-" : "missing-";
		ResourceBundle bundle = ResourceBundleUtil.getBundle(locale, this.getClass().getClassLoader());
		String message = ResourceBundleUtil.getString(bundle, prefix+msgKey, info);
		return new ChecklistItem(state, message, link);
	}
}
