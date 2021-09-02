package com.liferay.sales.checklist.impl;

import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.util.PrefsPropsUtil;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;

@Component
public class MailLoginChecklistProvider implements ChecklistProvider {

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		PortletPreferences preferences = PrefsPropsUtil.getPreferences(
				themeDisplay.getCompanyId(), true);

		boolean prepopulate = _getPrefsPropsBoolean(
				preferences, themeDisplay.getCompany(), PropsKeys.COMPANY_LOGIN_PREPOPULATE_DOMAIN,
				PropsValues.COMPANY_LOGIN_PREPOPULATE_DOMAIN);;
		String authType = _getPrefsPropsString(
				preferences, themeDisplay.getCompany(), PropsKeys.COMPANY_SECURITY_AUTH_TYPE,
				PropsValues.COMPANY_SECURITY_AUTH_TYPE);
		prepopulate &= "emailAddress".equals(authType);
		
		return new ChecklistItem(!prepopulate, "login-prepopulate", "/group/control_panel/manage?p_p_id=com_liferay_configuration_admin_web_portlet_InstanceSettingsPortlet&_com_liferay_configuration_admin_web_portlet_InstanceSettingsPortlet_mvcRenderCommandName=%2Fconfiguration_admin%2Fview_configuration_screen&_com_liferay_configuration_admin_web_portlet_InstanceSettingsPortlet_configurationScreenKey=general-authentication", "company.login.prepopulate.domain");
	}
	
	private static boolean _getPrefsPropsBoolean(
		PortletPreferences portletPreferences, Company company, String name,
		boolean defaultValue) {

		String value = portletPreferences.getValue(
			name, PropsUtil.get(company, name));

		if (value != null) {
			return GetterUtil.getBoolean(value);
		}

		return defaultValue;
	}

	private static String _getPrefsPropsString(
		PortletPreferences portletPreferences, Company company, String name,
		String defaultValue) {

		String value = portletPreferences.getValue(
			name, PropsUtil.get(company, name));

		if (value != null) {
			return value;
		}

		return defaultValue;
	}
}
