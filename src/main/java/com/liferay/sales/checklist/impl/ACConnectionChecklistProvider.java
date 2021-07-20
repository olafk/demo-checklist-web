package com.liferay.sales.checklist.impl;

import com.liferay.analytics.message.sender.client.AnalyticsMessageSenderClient;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class ACConnectionChecklistProvider implements ChecklistProvider {

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		try {
			analyticsMessageSenderClient.validateConnection(themeDisplay.getCompanyId());
			return new ChecklistItem(true, "analytics-cloud", null);
		} catch (Exception e) {
			return new ChecklistItem(false, "analytics-cloud", null, e.getClass().getName() + " " + e.getMessage());
		}
	}

	@Reference
	AnalyticsMessageSenderClient analyticsMessageSenderClient;
}
