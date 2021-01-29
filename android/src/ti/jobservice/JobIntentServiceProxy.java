/*
 * Copyright (c) 2018. Innerstrength Ltd. All Rights Reserved.
 * Licensed under the terms of the MIT Public License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.jobservice;

import org.appcelerator.kroll.KrollProxy;
import org.appcelerator.kroll.annotations.Kroll;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.titanium.proxy.IntentProxy;

import android.content.Intent;
import java.lang.reflect.Method;

import androidx.core.app.JobIntentService;

@Kroll.proxy

public class JobIntentServiceProxy extends KrollProxy
{
	private JobIntentService service;
	private int serviceInstanceId;
	private IntentProxy intentProxy;

	private static final String TAG = "TiJobIntentServiceProxy";

	public JobIntentServiceProxy()
	{
	}

	/**
	 * For when a service started via onHandleWork creates a proxy when it starts running
	 */
	public JobIntentServiceProxy(JobIntentService service, Intent intent, int serviceInstanceId)
	{
		this.service = service;
		setIntent(intent);
		this.serviceInstanceId = serviceInstanceId;
	}

	// clang-format off
	@Kroll.method
	@Kroll.getProperty
	public int getServiceInstanceId()
	// clang-format on
	{
		return serviceInstanceId;
	}

	// clang-format off
	@Kroll.method
	@Kroll.getProperty
	public IntentProxy getIntent()
	// clang-format on
	{
		return intentProxy;
	}

	public void setIntent(Intent intent)
	{
		setIntent(new IntentProxy(intent));
	}

	/**
	 * Sets the IntentProxy.
	 * @param intentProxy the proxy to set.
	 */
	public void setIntent(IntentProxy intentProxy)
	{
		this.intentProxy = intentProxy;
	}


	@Kroll.method
	public void stop()
	{
		Log.w(TAG, "Stopping service", Log.DEBUG_MODE);
		if (service != null) {
            if (service instanceof TiBaseJobIntentService) {
                ((TiBaseJobIntentService) service).stop(this);
            } else {
                service.stopSelf();
            }
		}
	}

	@Override
	public void release()
	{
		super.release();
		Log.d(TAG, "Nullifying wrapped service", Log.DEBUG_MODE);
		this.service = null;
	}

	@Override
	public String getApiName()
	{
		return "Ti.Android.JobIntentService";
	}

}
