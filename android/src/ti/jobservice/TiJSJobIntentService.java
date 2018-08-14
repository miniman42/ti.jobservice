/*
 * Copyright (c) 2018. Innerstrength Ltd. All Rights Reserved.
 * Licensed under the terms of the MIT Public License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.jobservice;

import org.appcelerator.kroll.KrollDict;
import org.appcelerator.kroll.KrollRuntime;
import org.appcelerator.kroll.common.Log;
import org.appcelerator.kroll.util.KrollAssetHelper;
import org.appcelerator.titanium.TiC;
import ti.modules.titanium.android.AndroidModule;

import android.content.Context;
import android.content.Intent;

public class TiJSJobIntentService extends TiBaseJobIntentService
{
    /**
     * Unique job ID for this service.
     */
    protected static final int JOB_ID = 1234;

    protected String url = null;
    protected boolean stopRequested = false;

	private static final String TAG = "TiJSJobIntentService";

    static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, TiJSJobIntentService.class,JOB_ID, work);
    }

	public TiJSJobIntentService(String url)
	{
		super();
		this.url = url;
	}


    @Override
    protected void onHandleWork(Intent intent) {
        Log.d(TAG, "onHandleWork", Log.DEBUG_MODE);
        finalizeUrl(intent);
        JobIntentServiceProxy proxy = createProxy(intent);
        current=proxy;
        start(proxy);
        //Allow continued execution until JS calls stop or we are stopped by the system.
        while (!isStopped() && !stopRequested){
            try {
                //allow 30 seconds for work to complete...
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                Log.w(TAG,"Interrupted while sleeping - Job Stopped!",Log.DEBUG_MODE);
            }
        }
        Log.d(TAG,"onHandleWork complete",Log.DEBUG_MODE);
    }

    @Override
    protected JobIntentServiceProxy createProxy(Intent intent)
    {
        finalizeUrl(intent);
        int lastSlash = url.lastIndexOf('/');
        String baseUrl = url.substring(0, lastSlash + 1);
        if (baseUrl.length() == 0) {
            baseUrl = null;
        }
        serviceProxy = new JobIntentServiceProxy(this, intent, proxyCounter.incrementAndGet());
        return serviceProxy;
    }

    protected void start(JobIntentServiceProxy proxy)
    {
        proxy.fireEvent(TiC.EVENT_START, new KrollDict());
        executeServiceCode(proxy);
    }

    @Override
    public void stop(JobIntentServiceProxy proxy)
	{
        //inform work handler that we wish to end execution
    	stopRequested=true;
        //stop the service
        stopSelf();
	}

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "All work complete");
    }

	protected void executeServiceCode(JobIntentServiceProxy proxy)
	{
		String fullUrl = url;
		if (!fullUrl.contains("://") && !fullUrl.startsWith("/") && proxy.getCreationUrl().baseUrl != null) {
			fullUrl = proxy.getCreationUrl().baseUrl + fullUrl;
		}
		if (Log.isDebugModeEnabled()) {
			if (url != fullUrl) {
				Log.d(TAG, "Eval JS Service:" + url + " (" + fullUrl + ")");
			} else {
				Log.d(TAG, "Eval JS Service:" + url);
			}
		}
		if (fullUrl.startsWith(TiC.URL_APP_PREFIX)) {
			fullUrl = fullUrl.replaceAll("app:/", "Resources");

		} else if (fullUrl.startsWith(TiC.URL_ANDROID_ASSET_RESOURCES)) {
			fullUrl = fullUrl.replaceAll("file:///android_asset/", "");
		}
		proxy.fireEvent(TiC.EVENT_RESUME, new KrollDict());
		KrollRuntime.getInstance().runModule(KrollAssetHelper.readAsset(fullUrl), fullUrl, proxy);
		proxy.fireEvent(TiC.EVENT_PAUSE, new KrollDict());
		proxy.fireEvent(TiC.EVENT_STOP, new KrollDict()); // this basic JS Service class only runs once.
	}

    private void finalizeUrl(Intent intent)
    {
        if (url == null) {
            if (intent != null && intent.getDataString() != null) {
                url = intent.getDataString();
            } else {
                throw new IllegalStateException("Service url required.");
            }
        }
    }

}
