package com.liferay.sales.checklist.impl;

import aQute.bnd.annotation.metatype.Meta;

@Meta.OCD(
	    id = "com.liferay.sales.checklist.impl.ChecklistConfiguration"
	    , localization = "content/Language"
	    , name = "config-sales-checklist-name"
	    , description = "config-sales-checklist-description"
	)
public interface ChecklistConfiguration {
	/**
	 * Claim to be of the configured version. This default value is manually set to the 
	 * corresponding github release.
	 * @return
	 */
	@Meta.AD(
            deflt = "1.1.5",
            description = "config-updated-version-check-description",
            name = "config-updated-version-check-name",
            required = false
        )

	public String updatedVersionCheck();
	
	@Meta.AD(
            deflt = "0",
            description = "config-show-when-more-than-description",
            name = "config-show-when-more-than-name",
            required = false
        )

	public int showWhenMoreThan();
}


