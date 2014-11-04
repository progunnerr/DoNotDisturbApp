package com.example.multimediapanga;

import java.text.SimpleDateFormat;
import java.util.Date;

import android.app.Service;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.CalendarContract;
import android.util.Log;
import android.widget.Toast;

public class UpdateService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub
		Log.w("MyService", "in Update Service: OnStartCommand()");
		Toast.makeText(getBaseContext(),"Update Started",Toast.LENGTH_SHORT).show();
		
		
		long currentTime = System.currentTimeMillis();
		long[] updatedTimes = cursorUpdate(Long.toString(currentTime));
		long event_start_time = updatedTimes[0];
		long event_stop_time = updatedTimes[1];
		long x = updatedTimes[2];
		int _id = (int) x;
		
		Intent i = new Intent(UpdateService.this, MyService.class);
		
		Bundle b = new Bundle();
		b.putLong("EventStartTime", event_start_time);
		b.putLong("EventEndTime", event_stop_time);
		b.putInt("_id", _id);
		b.putBoolean("event_passed", false);
		i.putExtras(b);
		
		startService(i);
		
		stopSelf();
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	public void startMyService(long event_start_time,long event_stop_time){
		
	}
	
	public long[] cursorUpdate(String hunTime){
		String[] projection = { CalendarContract.Events.DTSTART,
				CalendarContract.Events.TITLE, CalendarContract.Events.DTEND , CalendarContract.Events._ID};
		String[] selection_args = { hunTime, hunTime };

		// Get a Cursor over the Events Provider.
		Cursor cursor = getContentResolver().query(
				CalendarContract.Events.CONTENT_URI, projection,
				"DTSTART >= ? OR DTEND >= ?", selection_args, "DTSTART ASC");
		
		int idIdx = cursor
				.getColumnIndexOrThrow(CalendarContract.Events.DTSTART);
		int unique_id = cursor.getColumnIndexOrThrow(CalendarContract.Events._ID);
		int dt_end = cursor
				.getColumnIndexOrThrow(CalendarContract.Events.DTEND);
		
		cursor.moveToFirst();
		long start_time = cursor.getLong(idIdx);
		long end_time = cursor.getLong(dt_end);
		int _id =  cursor.getInt(unique_id);
		long u_id = _id;
		
		long[] updatedValues = {start_time, end_time,u_id};
		Log.e("TIME", "After update service_ID() = " + u_id);
		Log.d("TIME", "MyService Start time = " + millisToTimestamp(start_time));
		Log.d("TIME", "MyService Start time = " + millisToTimestamp(end_time));
		cursor.close();
		return updatedValues;
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
		Log.e("MyService", "in Update Service: onDestroy()");
		super.onDestroy();
	}
}
