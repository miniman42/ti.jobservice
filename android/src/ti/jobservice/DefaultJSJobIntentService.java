/*
 * Copyright (c) 2018. Innerstrength Ltd. All Rights Reserved.
 * Licensed under the terms of the MIT Public License
 * Please see the LICENSE included with this distribution for details.
 */

package ti.jobservice;

import android.content.Context;
import android.content.Intent;

public final class DefaultJSJobIntentService extends TiJSJobIntentService {

    /**
     * Convenience method for enqueuing work in to this service.
     */
    public static void enqueueWork(Context context, Intent work) {
        enqueueWork(context, DefaultJSJobIntentService.class,JOB_ID, work);
    }

    public DefaultJSJobIntentService() {
        super("defaultJSJobIntentService.js");
    }
}
