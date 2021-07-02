package com.liferay.sales.checklist.api;

import com.liferay.portal.kernel.theme.ThemeDisplay;

import org.osgi.annotation.versioning.ProviderType;

@ProviderType
public interface ChecklistProvider {
	ChecklistItem check(ThemeDisplay themeDisplay);
}
