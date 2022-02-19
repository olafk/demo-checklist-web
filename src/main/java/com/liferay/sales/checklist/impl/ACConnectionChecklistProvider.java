package com.liferay.sales.checklist.impl;

import com.liferay.analytics.message.sender.client.AnalyticsMessageSenderClient;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.sales.checklist.api.BaseChecklistProviderImpl;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component( 
		service = ChecklistProvider.class 
)
public class ACConnectionChecklistProvider extends BaseChecklistProviderImpl implements ChecklistProvider {

	private static final String MSG = "analytics-cloud";

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		try {
			analyticsMessageSenderClient.validateConnection(themeDisplay.getCompanyId());
			return create(true, themeDisplay.getLocale(), null, MSG);
		} catch (Exception e) {
			return create(false, themeDisplay.getLocale(), null, MSG, 
					e.getClass().getName() + " " + e.getMessage());
		}
	}

	@Reference
	AnalyticsMessageSenderClient analyticsMessageSenderClient;
}
