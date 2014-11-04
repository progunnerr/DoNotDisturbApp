package com.example.multimediapanga;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import com.multimedia_project.R;

public class SplashScreen extends Activity {

	MediaPlayer mp;

	// Splash screen timer
	private static int SPLASH_TIME_OUT = 3000;// time in miliseconds

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);

		/* Gets Music file from the resources */
		mp = MediaPlayer.create(this, R.raw.godfather_main);
		mp.start();

		// This method will be executed once the timer is over
		new Handler().postDelayed(new Runnable() {

			@Override
			public void run() {
				// Start main activity
				Intent i = new Intent(SplashScreen.this, MainActivity.class);
				startActivity(i);

			}
		}, SPLASH_TIME_OUT);
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		mp.release();
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		mp.release();
		super.onDestroy();
	}

}