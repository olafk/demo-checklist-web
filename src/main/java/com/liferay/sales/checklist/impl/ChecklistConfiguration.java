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
            deflt = "1.1.3",
            description = "config-updated-version-check-description",
            name = "config-updated-version-check-name",
            required = false
        )

	public String updatedVersionCheck();
	
	@Meta.AD(
            deflt = "false",
            description = "config-show-always-description",
            name = "config-show-always-name",
            required = false
        )

	public boolean showAlways();

}


