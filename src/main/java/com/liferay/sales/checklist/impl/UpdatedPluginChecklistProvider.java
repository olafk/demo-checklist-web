package com.liferay.sales.checklist.impl;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.HttpUtil;
import com.liferay.sales.checklist.api.BaseChecklistProviderImpl;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;
import com.liferay.sales.checklist.impl.dto.Release;

import java.util.Date;
import java.util.Map;

import org.osgi.framework.Version;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

/**
 * Using the github release api to determine latest version 
 * This "current version" assumption can be configured: The configuration's default value is manually set 
 * upon release to the corresponding github version. When configuration is saved, this might go out of date. 
 * Configuration is used to give the user an option to override the check for update for a particular version.
 * E.g. if you're running version 1.0.4 and you're not interested in running version 1.0.5: Just configure your
 * current instance to be 1.0.5 and you'll only be notified once 1.0.6, 1.1 or 2.0 is out. Set to a much 
 * larger value to disable for the foreseeable future.
 * 
 * @author Olaf Kock
 */

@Component(
		configurationPid = "com.liferay.sales.checklist.impl.ChecklistConfiguration",
		service = ChecklistProvider.class
	)
public class UpdatedPluginChecklistProvider extends BaseChecklistProviderImpl implements ChecklistProvider {

	private static final String MSG = "updated-version-available";
	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		Date now = new Date();
		long oneHourAgo = (now.getTime()-(60L*60L*1000L));
		if(lastResult != null && lastCheck.getTime() > oneHourAgo) {
			// only check every hour: Remote connections can be costly (paid in time)
			log.info("skipping updated version check for demo checklist within one hour past the last check");
			return lastResult;
		}

		try {
			String json = null;
			try {
				String location = "https://api.github.com/repos/olafk/demo-checklist-web/releases";
				json = HttpUtil.URLtoString(location);
				log.info("read " + json.length() + " release information from " + location);
			} catch (Exception e) {
				String msg = e.getClass().getName() + " " + e.getMessage();
				return create(false, themeDisplay.getLocale(), LINK, MSG, msg, msg); 
			}
			Release[] releases = null;
			releases = JSONFactoryUtil.looseDeserialize(json, Release[].class);
			Version maxReleasedVersion = new Version(0,0,0);
			for (int i = 0; i < releases.length; i++) {
				Version githubVersion = new Version(releases[i].tag_name);
				
				if(githubVersion.compareTo(maxReleasedVersion) > 0)
					maxReleasedVersion = githubVersion;
			}
			boolean result = maxReleasedVersion.compareTo(new Version(config.updatedVersionCheck())) <= 0;
			lastResult = create(result,themeDisplay.getLocale(), LINK, MSG, maxReleasedVersion.toString(), config.updatedVersionCheck());
			lastCheck = now;
			return lastResult;
		} catch (Exception e) {
			return create(false, themeDisplay.getLocale(), LINK, MSG, e.getClass().getName() + " " + e.getMessage(), config.updatedVersionCheck());
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
		lastCheck = new Date(0);
		lastResult = null;
	}
	
	private Date lastCheck;
	private ChecklistItem lastResult;
	private volatile ChecklistConfiguration config;

	private static final Log log = LogFactoryUtil.getLog(UpdatedPluginChecklistProvider.class);
	public static final String LINK = "/group/control_panel/manage?p_p_id=com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet&p_p_lifecycle=0&p_p_state=maximized&p_p_mode=view&_com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet_factoryPid=com.liferay.sales.checklist.impl.ChecklistConfiguration&_com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet_mvcRenderCommandName=%2Fconfiguration_admin%2Fedit_configuration&_com_liferay_configuration_admin_web_portlet_SystemSettingsPortlet_pid=com.liferay.sales.checklist.impl.ChecklistConfiguration";

}
