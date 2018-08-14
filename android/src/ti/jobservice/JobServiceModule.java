/*
 * Copyright (c) 2018. Innerstrength Ltd. All Rights Reserved.
 * Licensed under the terms of the MIT Public License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.jobservice;

import org.appcelerator.kroll.KrollModule;
import org.appcelerator.kroll.annotations.Kroll;

import org.appcelerator.titanium.TiApplication;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.common.TiConfig;


@Kroll.module(name="Jobs", id="ti.jobservice")
public class JobServiceModule extends KrollModule
{
	// Standard Debugging variables
	private static final String LCAT = "JobServiceModule";
	private static final boolean DBG = TiConfig.LOGD;
	private static JobServiceModule instance = null;

	// You can define constants with @Kroll.constant, for example:
	// @Kroll.constant public static final String EXTERNAL_NAME = value;
	public JobServiceModule()
	{
		super();
		instance = this;
	}

	public static JobServiceModule getInstance()
	{
		return instance;
	}


	@Kroll.onAppCreate
	public static void onAppCreate(TiApplication app)
	{
		Log.d(LCAT, "inside onAppCreate");
		// put module init code that needs to run when the application is created
	}

	// Methods
	@Kroll.method
	public JobIntentServiceProxy getCurrentJobIntentService()
	{
		return TiBaseJobIntentService.current;
	}

}
