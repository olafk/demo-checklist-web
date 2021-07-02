package com.liferay.sales.checklist.controlmenu;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.language.LanguageUtil;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.ResourceBundleUtil;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.product.navigation.control.menu.BaseJSPProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.ProductNavigationControlMenuEntry;
import com.liferay.product.navigation.control.menu.constants.ProductNavigationControlMenuCategoryKeys;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;
import com.liferay.sales.checklist.portlet.ChecklistChecker;

import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.osgi.service.component.annotations.Component;
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
		boolean allResolved = true;
		for (ChecklistItem checklistItem : checklist) {
			allResolved &= checklistItem.isResolved();
		}
		return !allResolved;
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
	
	private ChecklistChecker checker = new ChecklistChecker();
}
