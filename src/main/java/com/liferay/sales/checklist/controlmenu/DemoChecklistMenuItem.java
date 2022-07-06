package com.liferay.sales.checklist.controlmenu;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.module.configuration.ConfigurationProvider;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.product.navigation.control.menu.BaseJSPProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.ProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.constants.ProductNavigationControlMenuCategoryKeys;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;
import com.liferay.sales.checklist.impl.ChecklistConfiguration;
import com.liferay.sales.checklist.portlet.ChecklistChecker;

import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * This ControlMenuEntry is only shown when "something is wrong" in the 
 * configuration that could be improved.
 * 
 * @author Olaf Kock
 */

@Component(
		immediate = true,
		configurationPid = "com.liferay.sales.checklist.impl.ChecklistConfiguration",
		property = {
			"product.navigation.control.menu.category.key=" + ProductNavigationControlMenuCategoryKeys.USER,
			"product.navigation.control.menu.entry.order:Integer=1"
		},
		service = ProductNavigationControlMenuEntry.class
	)
public class DemoChecklistMenuItem 	
  extends BaseJSPProductNavigationControlMenuEntry
  implements ProductNavigationControlMenuEntry {

	@Override
	public String getIconJspPath() {
		return "/menu.jsp";
	}
	
	@Override
	public String getLabel(Locale locale) {
		ResourceBundle resourceBundle = ResourceBundleUtil.getBundle(
				"content.Language", locale, getClass());
		return LanguageUtil.get(resourceBundle, "inconsistencies-in-demo-configuration");
	}

	@Override
	public String getURL(HttpServletRequest httpServletRequest) {
		return "/";
	}
	
	@Override
	public boolean isShow(HttpServletRequest request) throws PortalException {
		ThemeDisplay themeDisplay = (ThemeDisplay) request.getAttribute(WebKeys.THEME_DISPLAY);
		List<ChecklistItem> checklist = checker.check(themeDisplay);
		int unresolved = 0;
		for (ChecklistItem checklistItem : checklist) {
			if(!checklistItem.isResolved()) {
				unresolved++;
			}
		}
		request.setAttribute("checklist-all-resolved", unresolved==0); // determines symbol when shown: checkmark or warning sign
		return (unresolved > config.showWhenMoreThan()); // determines if anything is shown at all
	}
	
	@Override
	@Reference(
		target = "(osgi.web.symbolicname=com.liferay.sales.checklist)",
		unbind = "-"
	)
	public void setServletContext(ServletContext servletContext) {
		super.setServletContext(servletContext);
	}
	
	@Reference( 
			cardinality = ReferenceCardinality.MULTIPLE,
		    policyOption = ReferencePolicyOption.GREEDY,
		    unbind = "doUnRegister" 
	)
	void doRegister(ChecklistProvider checklistProvider) {
		checker.doRegister(checklistProvider);
	}
	
	void doUnRegister(ChecklistProvider checklistProvider) {
		checker.doUnRegister(checklistProvider);
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

	private ChecklistChecker checker = new ChecklistChecker();
}
