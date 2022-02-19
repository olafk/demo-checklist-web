package com.liferay.sales.checklist.impl;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PasswordPolicy;
import com.liferay.portal.kernel.service.PasswordPolicyLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.sales.checklist.api.BaseChecklistProviderImpl;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component( 
		service = ChecklistProvider.class 
)
public class PasswordPolicyChecklistProvider extends BaseChecklistProviderImpl implements ChecklistProvider {

	private static final String LINK = "/group/control_panel/manage?p_p_id=com_liferay_password_policies_admin_web_portlet_PasswordPoliciesAdminPortlet";
	private static final String MSG = "password-policy-change-required";
	private static final String LINK_PARAM = "&p_p_lifecycle=0&_com_liferay_password_policies_admin_web_portlet_PasswordPoliciesAdminPortlet_mvcPath=%2Fedit_password_policy.jsp&_com_liferay_password_policies_admin_web_portlet_PasswordPoliciesAdminPortlet_passwordPolicyId=";

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		try {
			PasswordPolicy passwordPolicy = passwordPolicyLocalService.getDefaultPasswordPolicy(themeDisplay.getCompanyId());
			boolean changeRequired = passwordPolicy.getChangeRequired();
			return create(!changeRequired, themeDisplay.getLocale(), 
					LINK + LINK_PARAM + passwordPolicy.getPasswordPolicyId(),
					MSG);
		} catch (PortalException e) {
			return create(false, themeDisplay.getLocale(), LINK, MSG, e.getClass() + " " + e.getMessage()); 
		}
	}

	@Reference
	PasswordPolicyLocalService passwordPolicyLocalService;
}
