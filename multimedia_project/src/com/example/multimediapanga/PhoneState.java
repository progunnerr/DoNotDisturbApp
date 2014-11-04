package com.example.multimediapanga;

import android.content.Context;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.widget.Toast;

public class PhoneState extends PhoneStateListener {
	Context context;
	public static boolean ring = false;
	public static boolean callReceived = false;
	//public static String callerPhoneNumber;
	
	public PhoneState(Context context) {
		super();
		// TODO Auto-generated constructor stub
		this.context=context;
	}
	
	public void onCallStateChanged(int state, String incomingNumber) {
		// TODO Auto-generated method stub
		super.onCallStateChanged(state, incomingNumber);
		
		//Toast.makeText(context, "Phone State - "+state+" Incoming Number - "+incomingNumber, Toast.LENGTH_LONG).show();//Giving the Message that Phone State Changed
		//Checking The phone state  
		switch(state)
		{
		
		case TelephonyManager.CALL_STATE_RINGING:  //Phone is Ringing
			Toast.makeText(context, "Phone State is RINGING", Toast.LENGTH_LONG).show();
			ring = true;
			break;
		case TelephonyManager.CALL_STATE_OFFHOOK:  //Call is Received
			Toast.makeText(context, "Call State is OFFHOOK",Toast.LENGTH_LONG).show();
			callReceived = true;
			break;
		case TelephonyManager.CALL_STATE_IDLE:    //Phone is in Idle State
			if (ring == true && callReceived == false) {
				Toast.makeText(context,"It was A MISSED CALL from : " + incomingNumber,Toast.LENGTH_LONG).show();
				  }
			//Toast.makeText(context, "Phone State is IDLE", Toast.LENGTH_LONG).show();
			break;
			
		}
		
		

			// If phone was ringing(ring=true) and not
			// received(callReceived=false) , then it is a missed call
			
	}
}
