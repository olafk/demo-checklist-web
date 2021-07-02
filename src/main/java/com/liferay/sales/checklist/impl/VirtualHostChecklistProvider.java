package com.liferay.sales.checklist.impl;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import org.osgi.service.component.annotations.Component;

@Component
public class VirtualHostChecklistProvider implements ChecklistProvider {

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
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

}
