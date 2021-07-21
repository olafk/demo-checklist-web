package com.liferay.sales.checklist.impl;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.util.PropsValues;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import org.osgi.service.component.annotations.Component;


@Component
public class AccountCreationByStrangersChecklistProvider implements ChecklistProvider {

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		boolean accountCreationByStrangers = PropsValues.COMPANY_SECURITY_STRANGERS;
		return new ChecklistItem(!accountCreationByStrangers, "strangers-can-create-accounts", "https://docs.liferay.com/portal/7.3-latest/propertiesdoc/portal.properties.html#Company", "company.security.strangers");
	}

}
