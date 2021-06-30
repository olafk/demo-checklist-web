package com.liferay.sales.checklist.portlet;

import com.liferay.portal.kernel.portlet.bridges.mvc.MVCPortlet;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.kernel.util.WebKeys;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.sales.checklist.ChecklistItem;
import com.liferay.sales.checklist.DemoChecklistUtil;
import com.liferay.sales.checklist.constants.DemoChecklistPortletKeys;

import java.io.IOException;
import java.util.List;

import javax.portlet.Portlet;
import javax.portlet.PortletException;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * @author olaf
 */
@Component(
	immediate = true,
	property = {
		"com.liferay.portlet.display-category=category.sample",
		"com.liferay.portlet.header-portlet-css=/css/main.css",
		"com.liferay.portlet.instanceable=false", 
		"com.liferay.portlet.add-default-resource=true",
		"javax.portlet.display-name=DemoChecklist",
		"javax.portlet.init-param.template-path=/",
		"javax.portlet.init-param.view-template=/view.jsp",
		"javax.portlet.name=" + DemoChecklistPortletKeys.DEMOCHECKLIST,
		"javax.portlet.resource-bundle=content.Language",
		"javax.portlet.security-role-ref=power-user,user"
	},
	service = Portlet.class 
)
public class DemoChecklistPortlet extends MVCPortlet {
	
	@Override
	public void doView(RenderRequest renderRequest, RenderResponse renderResponse)
			throws IOException, PortletException {
		ThemeDisplay themeDisplay = (ThemeDisplay) renderRequest.getAttribute(WebKeys.THEME_DISPLAY);

		List<ChecklistItem> checklist = demoChecklistUtil.getChecklist(themeDisplay);
		renderRequest.setAttribute("checklist", checklist);
		
		super.doView(renderRequest, renderResponse);
	}
	
	@Reference
	public void setSearchEngineAdapter(SearchEngineAdapter searchEngineAdapter) {
		this.searchEngineAdapter = searchEngineAdapter;
		initDemoChecklistUtil();
	}
	
	@Reference
	public void setIndexNameBuilder(IndexNameBuilder indexNameBuilder) {
		this.indexNameBuilder = indexNameBuilder;
		initDemoChecklistUtil();
	}

	private void initDemoChecklistUtil() {
		if(this.searchEngineAdapter != null && this.indexNameBuilder != null) {
			this.demoChecklistUtil = new DemoChecklistUtil(searchEngineAdapter, indexNameBuilder);
		}
	}
	
	private IndexNameBuilder indexNameBuilder;
	private SearchEngineAdapter searchEngineAdapter;
	
	DemoChecklistUtil demoChecklistUtil;
	
}