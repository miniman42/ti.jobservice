/*global Ti: true, require: true */
(function(service) {
    // If using this module without modification or extension, this file
    // (OR a modified version of it) must be placed in app/assets/android of
    // the target titanium Application. It must be called defaultJSJobIntentService.js
    // to be picked up and loaded by the DefaultJSJobIntentService

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
