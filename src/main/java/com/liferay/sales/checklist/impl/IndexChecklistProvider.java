package com.liferay.sales.checklist.impl;

import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.search.Field;
import com.liferay.portal.kernel.search.TermQuery;
import com.liferay.portal.kernel.search.generic.TermQueryImpl;
import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.portal.search.engine.adapter.SearchEngineAdapter;
import com.liferay.portal.search.engine.adapter.search.CountSearchRequest;
import com.liferay.portal.search.engine.adapter.search.CountSearchResponse;
import com.liferay.portal.search.index.IndexNameBuilder;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component
public class IndexChecklistProvider implements ChecklistProvider {

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
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
	
	@Reference
	SearchEngineAdapter searchEngineAdapter;

	@Reference
	IndexNameBuilder indexNameBuilder;

}
