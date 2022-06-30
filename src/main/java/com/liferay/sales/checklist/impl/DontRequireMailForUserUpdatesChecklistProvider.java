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

public class DontRequireMailForUserUpdatesChecklistProvider extends BaseChecklistProviderImpl
		implements ChecklistProvider {

	private static final String LINK = "https://docs.liferay.com/portal/7.4-latest/propertiesdoc/portal.properties.html#Company";
	private static final String MSG = "user-change-needs-to-be-mailvalidated";

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		return create(! PropsValues.COMPANY_SECURITY_STRANGERS_VERIFY, 
				themeDisplay.getLocale(), LINK, MSG); 
	}

}
