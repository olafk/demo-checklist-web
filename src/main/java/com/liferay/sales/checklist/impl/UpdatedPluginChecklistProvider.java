package com.liferay.sales.checklist.impl;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;
import com.liferay.sales.checklist.impl.dto.Release;

import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;


@Component(
		configurationPid = "com.liferay.sales.checklist.impl.ChecklistConfiguration"
	)
public class UpdatedPluginChecklistProvider implements ChecklistProvider {

	private static final String CP_URL = "/group/control_panel/manage?p_p_id=com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&_com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet_factoryPid=com.liferay.sales.checklist.impl.ChecklistConfiguration&_com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet_mvcRenderCommandName=%2Fconfiguration_admin%2Fedit_configuration&_com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet_pid=com.liferay.sales.checklist.impl.ChecklistConfiguration";

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		try {
			String json = null;
			try {
				String location = "https://api.github.com/repos/olafk/demo-checklist-web/releases";
				json = HttpUtil.URLtoString(location);
				log.info("read " + json.length() + " release information from " + location);
			} catch (Exception e) {
				return new ChecklistItem(false, "updated-version-available", e.getClass().getName() + " " + e.getMessage());
			}
			Release[] releases = null;
			releases = JSONFactoryUtil.looseDeserialize(json, Release[].class);
			String maxReleasedVersion = "";
			for (int i = 0; i < releases.length; i++) {
				if(releases[i].tag_name.compareTo(maxReleasedVersion) > 0)
					maxReleasedVersion = releases[i].tag_name;
			}
			boolean result = maxReleasedVersion.compareTo(config.updatedVersionCheck()) <= 0;
			return new ChecklistItem(result, "updated-version-available", CP_URL, maxReleasedVersion, config.updatedVersionCheck());
		} catch (Exception e) {
			return new ChecklistItem(false, "updated-version-available", CP_URL, e.getClass().getName() + " " + e.getMessage(), config.updatedVersionCheck() );
		}
	}

	@Reference
	protected void setConfigurationProvider(ConfigurationProvider configurationProvider) {
	    // configuration update will actually be handled in the @Modified event,
		// which will only be triggered in case we have a @Reference to the 
		// ConfigurationProvider
	}
	
	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		config = ConfigurableUtil.createConfigurable(ChecklistConfiguration.class, 
				properties);
	}

	private volatile ChecklistConfiguration config;

	public static final Log log = LogFactoryUtil.getLog(UpdatedPluginChecklistProvider.class);
}
