package com.example.multimediapanga;
//author Hartej Singh Grewal/7757992
import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Vibrator;
import android.provider.CalendarContract;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.Switch;
import android.widget.TextView;
import com.multimedia_project.R;
//Hello
public class MainActivity extends Activity {
	Intent launchCalendarIntent = null,  intent = null;
	Button launchCal;

	TextView tv1, tv2, tv3, tv4;

	Switch onOffSwitch;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_b);

		launchCal = (Button) findViewById(R.id.button1);
		tv1 = (TextView) findViewById(R.id.textView1);
		tv2 = (TextView) findViewById(R.id.textView2);
		tv3 = (TextView) findViewById(R.id.textView3);
		tv4 = (TextView) findViewById(R.id.textView7);
		onOffSwitch = (Switch) findViewById(R.id.switch1);
		
		intent = new Intent(MainActivity.this, MyService.class);
		launchCalendarIntent = new Intent(Intent.ACTION_VIEW);
		
		onOffSwitch.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {

				Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);

				if (isChecked == true) {
				
					v.vibrate(200);
					
					final long currentTimeInMIlls = System.currentTimeMillis();
					String currentTime = Long.toString(currentTimeInMIlls);
					String[] projection = { CalendarContract.Events.DTSTART,
							CalendarContract.Events.TITLE,
							CalendarContract.Events.DTEND,
							CalendarContract.Events._ID };
					String[] selection_args = { currentTime, currentTime };

					// Get a Cursor over the Events Provider.
					Cursor cursor = getContentResolver().query(
							CalendarContract.Events.CONTENT_URI, projection,
							"DTSTART >= ? OR DTEND >= ?", selection_args,
							"DTSTART ASC");

					
					// Get the index of the columns.
					int nameIdx = cursor
							.getColumnIndexOrThrow(CalendarContract.Events.TITLE);
					int unique_id = cursor
							.getColumnIndexOrThrow(CalendarContract.Events._ID);
					int idIdx = cursor
							.getColumnIndexOrThrow(CalendarContract.Events.DTSTART);
					int dt_end = cursor
							.getColumnIndexOrThrow(CalendarContract.Events.DTEND);

					
					cursor.moveToFirst();//this is the upcoming/ongoing event
					// Extract the name.
					final String name = cursor.getString(nameIdx);
					// Extract the unique ID.
					final int _id = cursor.getInt(unique_id);
					//Extract the start time of event.
					final long event_start = cursor.getLong(idIdx);
					//Extract the end time of event
					final long event_end = cursor.getLong(dt_end);

					// Close the Cursor.
					cursor.close();
					
					
					tv1.setText(name);

					tv2.setText(millisToTimestamp(event_start));

					tv3.setText(millisToTimestamp(currentTimeInMIlls));

					tv4.setText(millisToTimestamp(event_end));


					
					Bundle b = new Bundle();
					b.putLong("EventStartTime", event_start);
					b.putLong("EventEndTime", event_end);
					b.putInt("_id", _id);
					b.putBoolean("event_passed", false);
					intent.putExtras(b);
					stopService(intent);
					startService(intent);

				} else {
					v.vibrate(200);
					
					tv1.setText("--");
					tv2.setText("--");
					tv3.setText("--");
					tv4.setText("--");
					
					AudioManager audiomanage = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
					audiomanage.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
					
					stopService(intent);

				}

			}
		});

		launchCal.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				
				launchCalendarIntent.setData(Uri.parse("content://com.android.calendar/time"));
				startActivity(launchCalendarIntent);
			}
		});

		/*
		 * startButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent ii = new
		 * Intent(MainActivity.this, MyService.class); Bundle b = new Bundle();
		 * b.putLong("EventStartTime", millis); b.putLong("EventEndTime",
		 * event_end); ii.putExtras(b); startService(ii);
		 * 
		 * } });
		 * 
		 * stopButton.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) { Intent i = new
		 * Intent(MainActivity.this, MyService.class); stopService(i);
		 * 
		 * } });
		 * 
		 * send.setOnClickListener(new OnClickListener() {
		 * 
		 * @Override public void onClick(View v) {
		 * 
		 * intent = new Intent("my.action.string"); intent.putExtra("label",
		 * "FUCK"); sendBroadcast(intent);
		 * 
		 * } });
		 */

		Log.d("LIFECYCLE", "onCreate() was called");

	}

	public void doSomething(View v) {
		Log.d("ORIENTATION", "Button was clicked!!");
	}


	public String millisToTimestamp(long millis) {
		Date date = new Date(millis); // *1000 is to convert minutes to
										// milliseconds
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"); // the
																			// format
																			// of
																			// your
																			// date
		String formattedDate = sdf.format(date);
		return formattedDate;
	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {
		// TODO Auto-generated method stub
		super.onConfigurationChanged(newConfig);
		if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE)
			Log.d("ORIENTATION", "Orientaion Changed to LANDSCAPE");
		else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT)
			Log.e("ORIENTATION", "Orientaion Changed to POTRAIT");
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		super.onStart();
		Log.i("LIFECYCLE", "onStart() was called");
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		Log.w("LIFECYCLE", "onResume() was called");
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		Log.d("LIFECYCLE", "onPause() was called");

	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		super.onStop();
		Log.v("LIFECYCLE", "onStop() was called");
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
		Log.e("LIFECYCLE", "onRestart() was called");
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		Log.e("LIFECYCLE", "onDestroy() was called");
	}

	/*
	 * @Override public boolean onCreateOptionsMenu(Menu menu) { // Inflate the
	 * menu; this adds items to the action bar if it is present.
	 * getMenuInflater().inflate(R.menu.main, menu); return true; }
	 */

}
