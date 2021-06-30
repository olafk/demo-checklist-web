package com.liferay.sales.checklist;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.model.PasswordPolicy;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.service.PasswordPolicyLocalServiceUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.CountSearchRequest;
import com.liferay.portal.search.engine.adapter.search.CountSearchResponse;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.portal.util.PropsValues;

import java.util.ArrayList;
import java.util.List;

/**
 * This class implements all the checks executed by this plugin.
 * As an exercise, it could be extended to implement each check as a separate 
 * (and runtime-extensible) service, but that just wasn't worth the time
 * for the first release. 
 * 
 * @author Olaf Kock
 *
 */

public class DemoChecklistUtil {

	private SearchEngineAdapter searchEngineAdapter;
	private IndexNameBuilder indexNameBuilder;

	public DemoChecklistUtil(SearchEngineAdapter searchEngineAdapter, IndexNameBuilder indexNameBuilder) {
		this.searchEngineAdapter = searchEngineAdapter;
		this.indexNameBuilder = indexNameBuilder;
	}

	public List<ChecklistItem> getChecklist(ThemeDisplay themeDisplay) {
		List<ChecklistItem> result = new ArrayList<ChecklistItem>();
		
		result.add(checkIndex(themeDisplay));
		result.add(checkVirtualHost(themeDisplay));
		result.add(checkSessionExtension(themeDisplay));
		result.add(checkCompanyMailLogin(themeDisplay));
		result.add(checkPasswordPolicy(themeDisplay));
		return result;
	}
	
	private ChecklistItem checkIndex(ThemeDisplay themeDisplay) {
		try {
			CountSearchRequest countSearchRequest = new CountSearchRequest();
			countSearchRequest.setIndexNames(indexNameBuilder.getIndexName(themeDisplay.getCompanyId()));
			TermQuery termQuery = new TermQueryImpl(
				Field.ENTRY_CLASS_NAME, User.class.getName());
	
			countSearchRequest.setQuery(termQuery);
			CountSearchResponse countSearchResponse =
				searchEngineAdapter.execute(countSearchRequest);
			Long count = Long.valueOf(countSearchResponse.getCount());
			boolean exists = count > 0;
			ChecklistItem result = new ChecklistItem(exists, "index", 
					"/group/control_panel/manage?p_p_id=com_liferay_portal_search_admin_web_portlet_SearchAdminPortlet&_com_liferay_portal_search_admin_web_portlet_SearchAdminPortlet_tabs1=index-actions", 
					""+count);
			return result;
		} catch(Exception e) {
			ChecklistItem result = new ChecklistItem(false, "index", 
					"/group/control_panel/manage?p_p_id=com_liferay_portal_search_admin_web_portlet_SearchAdminPortlet&_com_liferay_portal_search_admin_web_portlet_SearchAdminPortlet_tabs1=index-actions", 
					e.getMessage());
			e.printStackTrace();
			return result;
		}
	}
	
	private ChecklistItem checkVirtualHost(ThemeDisplay themeDisplay) {
		String virtualHostname = themeDisplay.getCompany().getVirtualHostname();
		String host = themeDisplay.getRequest().getHeader("Host");
		if(host.indexOf(':')>0) {
			host = host.substring(0, host.indexOf(':'));
		}
		ChecklistItem result = new ChecklistItem(virtualHostname.equals(host), "virtualhost",
				"/group/control_panel/manage?p_p_id=com_liferay_configuration_admin_web_portlet_InstanceSettingsPortlet&_com_liferay_configuration_admin_web_portlet_InstanceSettingsPortlet_mvcRenderCommandName=%2Fconfiguration_admin%2Fview_configuration_screen&_com_liferay_configuration_admin_web_portlet_InstanceSettingsPortlet_configurationScreenKey=general", 
				new String[] {host, virtualHostname});
		return result;
	}
	
	private ChecklistItem checkSessionExtension(ThemeDisplay themeDisplay) {
		boolean autoextend = PropsValues.SESSION_TIMEOUT_AUTO_EXTEND;
		return new ChecklistItem(autoextend, "session-extension", "https://docs.liferay.com/portal/7.3-latest/propertiesdoc/portal.properties.html#Session", "session.timeout.auto.extend");
	}
	
	private ChecklistItem checkCompanyMailLogin(ThemeDisplay themeDisplay) {
		boolean prepopulate = PropsValues.COMPANY_LOGIN_PREPOPULATE_DOMAIN;
		return new ChecklistItem(!prepopulate, "login-prepopulate", "https://docs.liferay.com/portal/7.3-latest/propertiesdoc/portal.properties.html#Company", "company.login.prepopulate.domain");
	}
	
	private ChecklistItem checkPasswordPolicy(ThemeDisplay themeDisplay) {
		try {
			PasswordPolicy passwordPolicy = PasswordPolicyLocalServiceUtil.getDefaultPasswordPolicy(themeDisplay.getCompanyId());
			boolean changeRequired = passwordPolicy.getChangeRequired();
			return new ChecklistItem(!changeRequired, "password-policy-change-required", "/group/guest/~/control_panel/manage?p_p_id=com_liferay_password_policies_admin_web_portlet_PasswordPoliciesAdminPortlet&p_p_lifecycle=0&_com_liferay_password_policies_admin_web_portlet_PasswordPoliciesAdminPortlet_mvcPath=%2Fedit_password_policy.jsp&_com_liferay_password_policies_admin_web_portlet_PasswordPoliciesAdminPortlet_passwordPolicyId=" + passwordPolicy.getPasswordPolicyId());
		} catch (PortalException e) {
			return new ChecklistItem(false, "password-policy-change-required", "/group/guest/~/control_panel/manage?p_p_id=com_liferay_password_policies_admin_web_portlet_PasswordPoliciesAdminPortlet", e.getClass() + " " + e.getMessage());
		}
	}
}
