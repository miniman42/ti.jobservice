//see: defaultJSJobIntentService.js

//NOTE: scheduling a job can be done in any android java module that includes
// this module as a dependency.
// e.g.
// 		TiApplication tiapp=TiApplication.getInstance();
// 		Intent intent = new Intent(tiapp, DefaultJSJobIntentService.class);
// 		intent.putExtra("data", jsonData);
// 		DefaultJSJobIntentService.enqueueWork(tiapp,intent);

// The enqueued work will be executed by the JobScheduler as soon as possible/allowed by the system.
// This will then fire up and run the code in defaultJSJobIntentService.js :)
// This is a useful pattern to follow if looking to support api 26+ where starting
// services is disallowed unless under whitelist conditions.  Happy coding!
