package com.liferay.sales.checklist.impl;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.util.PropsValues;
import com.liferay.sales.checklist.api.BaseChecklistProviderImpl;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import org.osgi.service.component.annotations.Component;


@Component( 
		service = ChecklistProvider.class 
)
public class SessionExtensionChecklistProvider extends BaseChecklistProviderImpl implements ChecklistProvider {

	private static final String LINK = "https://docs.liferay.com/portal/7.3-latest/propertiesdoc/portal.properties.html#Session";
	private static final String MSG = "session-extension";

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		boolean autoextend = PropsValues.SESSION_TIMEOUT_AUTO_EXTEND;
		return create(autoextend, themeDisplay.getLocale(), LINK, MSG, "session.timeout.auto.extend");
	}

}
