package com.example.multimediapanga;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

public class IncomingCallReceiver extends BroadcastReceiver {

	public static boolean ring = false;
	public static boolean callReceived = false;
	public static String callerPhoneNumber;
	public static String str;

	@Override
	public void onReceive(Context mContext, Intent intent) {
		
		String action = intent.getAction();
		Log.d("LIFECYCLE", "BroadcastReceiver: OnReceive()");

		//Toast.makeText(mContext,"called by Button",Toast.LENGTH_LONG).show();
		Intent service2 = new Intent(mContext, MyService.class);
		mContext.startService(service2);
		// Get the current Phone State
		String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);

		if (state == null)
			return;

		// If phone state "Rininging"
		if (state.equals(TelephonyManager.EXTRA_STATE_RINGING)) {
			ring = true;
			// Get the Caller's Phone Number
			Bundle bundle = intent.getExtras();
			callerPhoneNumber = bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
		}

		// If incoming call is received
		if (state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)) {
			callReceived = true;
		}

		// If phone is Idle
		if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)) {

			// If phone was ringing(ring=true) and not
			// received(callReceived=false) , then it is a missed call
			if (ring == true && callReceived == false) {
				/*if(action.equals("my.action.string")){
				     String str = intent.getExtras().getString("label");
				     Toast.makeText(mContext, "Received Intent Data: " + str, Toast.LENGTH_LONG).show();
				  }*/
								
				Toast.makeText(mContext,"It was A MISSED CALL from : " + callerPhoneNumber,Toast.LENGTH_LONG).show();
				
			}
		}
		
		
		

	}

}