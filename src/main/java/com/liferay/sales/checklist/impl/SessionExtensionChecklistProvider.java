package com.liferay.sales.checklist.impl;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.util.PropsValues;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import org.osgi.service.component.annotations.Component;


@Component
public class SessionExtensionChecklistProvider implements ChecklistProvider {

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		boolean autoextend = PropsValues.SESSION_TIMEOUT_AUTO_EXTEND;
		return new ChecklistItem(autoextend, "session-extension", "https://docs.liferay.com/portal/7.3-latest/propertiesdoc/portal.properties.html#Session", "session.timeout.auto.extend");
	}

}
