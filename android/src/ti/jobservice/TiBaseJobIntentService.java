/*
 * Copyright (c) 2018. Innerstrength Ltd. All Rights Reserved.
 * Licensed under the terms of the MIT Public License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.jobservice;

import java.util.concurrent.atomic.AtomicInteger;

import org.appcelerator.kroll.KrollRuntime;
import org.appcelerator.titanium.TiC;
import org.appcelerator.kroll.common.Log;

import android.content.Intent;
import android.content.Context;

import android.support.v4.app.JobIntentService;

/**
 * The base class for Titanium Job Intent services. To learn more about JobIntentServices, see the
 * <a href="https://developer.android.com/reference/android/support/v4/app/JobIntentService.html">Android JobIntentService documentation</a>.
 */
public class TiBaseJobIntentService extends JobIntentService
{
	private static final String TAG = "TiBaseJobIntentService";
	protected AtomicInteger proxyCounter = new AtomicInteger();
	protected JobIntentServiceProxy serviceProxy;

    protected static JobIntentServiceProxy current=null;

	/**
	 * Creates and returns a service proxy, also increments the instance id.
	 * Each service proxy has a unique instance id.
	 * @param intent the intent used to create the proxy.
	 * @return service proxy
	 */
	protected JobIntentServiceProxy createProxy(Intent intent)
	{
		serviceProxy = new JobIntentServiceProxy(this, intent, proxyCounter.incrementAndGet());
		return serviceProxy;
	}


    @Override
    protected void onHandleWork(Intent intent) {
        // meant to be overridden
    }


    /**
	 * Implementing subclasses should use this method to stop work currently executing in the onHandleWork method.
	 * @param proxy the ServiceProxy.
	 */
	public void stop(JobIntentServiceProxy proxy)
	{
		// meant to be overridden
	}

	/**
	 * Implementing subclasses should use this method to release the proxy.
	 * @param proxy the proxy to release.
	 */
	public void unbindProxy(JobIntentServiceProxy proxy)
	{
		// meant to be overridden
	}

	/**
	 * @return next service instance id.
	 */
	public int nextServiceInstanceId()
	{
		return proxyCounter.incrementAndGet();
	}

	@Override
	public void onCreate()
	{
		super.onCreate();
		KrollRuntime.incrementServiceReceiverRefCount();
	}

	@Override
	public void onDestroy()
	{
		super.onDestroy();
		KrollRuntime.decrementServiceReceiverRefCount();
        current=null;
	}

	@Override
	public void onTaskRemoved(Intent rootIntent)
	{
		if (Log.isDebugModeEnabled()) {
			Log.d(TAG, "The task that comes from the service's application has been removed.");
		}
		serviceProxy.fireSyncEvent(TiC.EVENT_TASK_REMOVED, null);
	}
}
