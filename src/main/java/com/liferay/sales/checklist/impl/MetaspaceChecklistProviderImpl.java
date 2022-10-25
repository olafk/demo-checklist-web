package com.liferay.sales.checklist.impl;

import com.liferay.portal.kernel.theme.ThemeDisplay;
import com.liferay.sales.checklist.api.BaseChecklistProviderImpl;
import com.liferay.sales.checklist.api.ChecklistItem;
import com.liferay.sales.checklist.api.ChecklistProvider;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;

import org.osgi.service.component.annotations.Component;

@Component( 
		service = ChecklistProvider.class 
)

public class MetaspaceChecklistProviderImpl extends BaseChecklistProviderImpl implements ChecklistProvider {
	private static final String LINK = "https://liferay.slack.com/archives/CKSFZMYAY/p1644913190462979";
	private static final String MSG = "max-metaspace-must-be-above-768m";

	@Override
	public ChecklistItem check(ThemeDisplay themeDisplay) {
		for (MemoryPoolMXBean memoryMXBean : ManagementFactory.getMemoryPoolMXBeans()) {
		    if ("Metaspace".equals(memoryMXBean.getName())) {
		            long maxMetaspace = memoryMXBean.getUsage().getMax();
		            return create(maxMetaspace>=768*1024*1024, themeDisplay.getLocale(), LINK, MSG, maxMetaspace);
		    }
		}
		
        return create(false, themeDisplay.getLocale(), LINK, MSG, "undetected");
	}
}
