package com.liferay.sales.checklist.impl;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.util.PropsValues;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import org.osgi.service.component.annotations.Component;

@Component
public class MailLoginChecklistProvider implements ChecklistProvider {

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		boolean prepopulate = PropsValues.COMPANY_LOGIN_PREPOPULATE_DOMAIN;
		String authType = PropsValues.COMPANY_SECURITY_AUTH_TYPE;
		prepopulate &= "emailAddress".equals(authType);
		return new ChecklistItem(!prepopulate, "login-prepopulate", "https://docs.liferay.com/portal/7.3-latest/propertiesdoc/portal.properties.html#Company", "company.login.prepopulate.domain");
	}

}
