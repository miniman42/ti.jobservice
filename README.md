# Android JobIntentService - Titanium Module
Use the native JobIntentService Titanium.

## Requirements
- [x] Titanium SDK 7.3.0+


## Example Code to Schedule a Job From within a Module

```js
TiApplication tiapp=TiApplication.getInstance();
Intent intent = new Intent(tiapp, DefaultJSJobIntentService.class);
intent.putExtra("data", jsonData);
DefaultJSJobIntentService.enqueueWork(tiapp,intent);
```

## Example defaultJSJobIntentService.js (must be included in app/assets/android)

```js
(function(service) {
    // get the intent associated with the executing JobIntentService
    var serviceIntent = service.getIntent()

    //process intent if required...
    // e.g. var data = JSON.parse(serviceIntent.getStringExtra('data'));

    var timer;
    var done=function(){
        if (timer) clearTimeout(timer);
        timer=undefined;
        service.stop();
    };

    //set a timeout to ensure we actually "end" the job as we want to play nice
    //with system resources.
    //NOTE: max duration of a JobIntentService execution is ~10Mins on android 8.x
    timer=setTimeout(done,60000); //set as required

    //Do any necessary processing... i.e the JS Job to be done
    //it's a good idea to provide a callback to ensure the job service is
    //stopped when complete.
    require('mymodule').dowork(done);

})(require('ti.jobservice').getCurrentJobIntentService());
```


## Build
```js
cd android
appc run -p android --build-only
```

## Legal

This module is Copyright (c) 2018 - Present by Innerstrength Ltd. All Rights Reserved.
