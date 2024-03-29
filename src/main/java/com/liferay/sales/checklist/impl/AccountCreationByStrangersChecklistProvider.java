package com.liferay.sales.checklist.impl;

import com.liferay.portal.kernel.model.Company;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.PrefsPropsUtil;
import com.liferay.portal.kernel.util.PropsKeys;
import com.liferay.portal.util.PropsUtil;
import com.liferay.portal.util.PropsValues;
import com.liferay.sales.checklist.api.BaseChecklistProviderImpl;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import javax.portlet.PortletPreferences;

import org.osgi.service.component.annotations.Component;


@Component( 
		service = ChecklistProvider.class 
)

public class AccountCreationByStrangersChecklistProvider extends BaseChecklistProviderImpl implements ChecklistProvider {

	private static final String LINK = "/group/control_panel/manage?p_p_id=com_liferay_configuration_admin_web_portlet_InstanceSettingsPortlet&_com_liferay_configuration_admin_web_portlet_InstanceSettingsPortlet_mvcRenderCommandName=%2Fconfiguration_admin%2Fview_configuration_screen&_com_liferay_configuration_admin_web_portlet_InstanceSettingsPortlet_configurationScreenKey=general-authentication";
	private static final String MSG = "strangers-can-create-accounts";

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		PortletPreferences preferences = PrefsPropsUtil.getPreferences(
				themeDisplay.getCompanyId());

		boolean state = ! _getPrefsPropsBoolean(
				preferences, themeDisplay.getCompany(), PropsKeys.COMPANY_SECURITY_STRANGERS,
				PropsValues.COMPANY_SECURITY_STRANGERS);
		
		return create(state, themeDisplay.getLocale(), LINK, MSG, "company.security.strangers"); 
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
}
