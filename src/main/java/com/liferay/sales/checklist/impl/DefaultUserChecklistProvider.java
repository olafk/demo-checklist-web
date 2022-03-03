package com.liferay.sales.checklist.impl;

import com.liferay.portal.kernel.exception.NoSuchUserException;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.pwd.PasswordEncryptorUtil;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.sales.checklist.api.BaseChecklistProviderImpl;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component( 
		service = ChecklistProvider.class 
)

public class DefaultUserChecklistProvider extends BaseChecklistProviderImpl implements ChecklistProvider {

	private static final String LINK = "/group/control_panel/manage?p_p_id=com_liferay_users_admin_web_portlet_UsersAdminPortlet";
	private static final String MSG = "default-account-with-default-password";

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		try {
			User user = userLocalService.getUserByEmailAddress(themeDisplay.getCompanyId(), "test@liferay.com");
			if(user != null) {
				String hashedPassword = PasswordEncryptorUtil.encrypt("test", user.getPassword());
				return create(! user.getPassword().equals(hashedPassword), themeDisplay.getLocale(), LINK, MSG);
			}
		} catch (NoSuchUserException e) {
			// ignore - this is great and exactly what we're after.
		} catch (PortalException e) {
			return create(themeDisplay.getLocale(), e);
		}
		return create(true, themeDisplay.getLocale(), LINK, MSG);
	}

	@Reference
	UserLocalService userLocalService;
	

}
