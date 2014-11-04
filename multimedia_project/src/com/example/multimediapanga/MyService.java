package com.example.multimediapanga;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class MyService extends Service {
	//static MyService myService;
	public static boolean eventPassed = false;
	public int num = 0;
	//IncomingCallReceiver incomingCallReceiver = new IncomingCallReceiver();
	final Timer timer = new Timer();

	@Override
	public void onCreate() {
		//myService = this;
		Toast.makeText(getBaseContext(), "Service Started", Toast.LENGTH_SHORT)
				.show();
		Log.d("MyService", "Service: OnCreate() where eventPassed = "
				+ eventPassed);
		super.onCreate();
	}

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

//	public static MyService getInstance() {
	//	return myService;
//	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {

		// Log.w("MyService", "Service: OnStartCommand()");
		
		Bundle b = intent.getExtras();
		final int _id = b.getInt("_id");
		final long EventStartTime = b.getLong("EventStartTime");
		final long EventEndTime = b.getLong("EventEndTime");
		final boolean done_with_event = b.getBoolean("event_passed");
		eventPassed = done_with_event;
		// MyThread myThread = new MyThread(EventStartTime, EventEndTime, _id);

		final Handler handler = new Handler();
		
		TimerTask doAsynchronousTask = new TimerTask() {
			@Override
			public void run() {
				handler.post(new Runnable() {
					public void run() {
						try {
							
							Log.w("MyService", "Inside Thread!");
							long currentTime = System.currentTimeMillis();

							putPhoneOnSilent(EventStartTime, EventEndTime,
									currentTime, _id);
							

						} catch (Exception e) {

						}
					}
				});
			}
		};
		timer.schedule(doAsynchronousTask, 0, 20000);

		/*
		 * final Handler handler = new Handler(); Runnable runnable = null;
		 * 
		 * if(eventPassed == false){ runnable = new Runnable() {
		 * 
		 * @Override public void run() {
		 * 
		 * Log.w("MyService", "Inside Thread!"); long currentTime =
		 * System.currentTimeMillis();
		 * 
		 * putPhoneOnSilent(EventStartTime, EventEndTime, currentTime, _id);
		 * handler.removeCallbacks(this); handler.postDelayed(this, 10000); } };
		 * 
		 * handler.removeCallbacks(runnable); handler.postDelayed(runnable,
		 * 10000); }else{handler.removeCallbacks(runnable);}
		 */
		Log.i("MyService", "ByeBYEService: OnStartCommand()");
		return Service.START_REDELIVER_INTENT;
	}

	public void startUpdateService() {

		// Log.d("MyService", "Service: in StartService() beginning");
		Intent i = new Intent(MyService.this, UpdateService.class);
		startService(i);
		// Log.i("MyService", "Service: in StartService() end");
	}

	public void putPhoneOnSilent(long EventStartTime, long EventEndTime,
			long timeToCompare, int _id) {
		AudioManager audiomanage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);

		Log.e("TIME", "_id = " + _id);
		Log.d("TIME", "MyService Start time = "
				+ millisToTimestamp(EventStartTime));
		Log.v("TIME", "MyService End time = " + millisToTimestamp(EventEndTime));
		Log.i("TIME", "Current Time = " + millisToTimestamp(timeToCompare));

		if ((timeToCompare >= EventStartTime)
				&& (timeToCompare <= EventEndTime)
				&& (audiomanage.getRingerMode() != AudioManager.RINGER_MODE_SILENT)) {

			audiomanage.setRingerMode(AudioManager.RINGER_MODE_SILENT);
			Toast.makeText(getApplicationContext(), "Phone on Silent!!",
					Toast.LENGTH_LONG).show();
			//IntentFilter filter = new IntentFilter();
			//filter.addAction("android.intent.action.PHONE_STATE");
			
			Log.v("MyService", "Phone ON silent");
		}

		else if ((timeToCompare > EventStartTime)
				&& (timeToCompare > EventEndTime)
				&& (audiomanage.getRingerMode() == AudioManager.RINGER_MODE_SILENT)) {
			Log.v("MyService", "Phone Removed from silent");
			audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
			Toast.makeText(getApplicationContext(),
					"Phone Removed from silent!", Toast.LENGTH_LONG).show();
			eventPassed = true;
			// timer.cancel();
			// registerMyReceiver();
			startUpdateService();
			stopSelf();
			

		}
	}


	public void listenPhoneState() {
		TelephonyManager manager;
		PhoneState phoneStateListener;
		phoneStateListener = new PhoneState(this);// Creating the Object of
													// PhoneStateMonitor (User
													// Defined Subclass of
													// android.telephony.PhoneStateListener)
		manager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);
	}

	public String millisToTimestamp(long millis) {
		Date date = new Date(millis); // *1000 is to convert minutes to
										// milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); // the
																			// format
																			// of
																			// date

		String formattedDate = sdf.format(date);
		return formattedDate;
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		// handler.removeCallbacks(t);
		timer.cancel();
		// registerMyReceiver();
		// unregisterReceiver(incomingCallReceiver);
		Toast.makeText(getBaseContext(), "Service STOPPED!!",
				Toast.LENGTH_SHORT).show();
		Log.e("MyService", "Service: OnDestroy()");
		super.onDestroy();
	}

}