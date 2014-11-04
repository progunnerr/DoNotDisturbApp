package com.example.multimediapanga;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import com.multimedia_project.R;

public class ActivityB extends Activity {
	TelephonyManager manager;
    PhoneState phoneStateListener;
	/** Called when the activity is first created. */
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_b);
        phoneStateListener=new PhoneState(this);//Creating the Object of PhoneStateMonitor (User Defined Subclass of android.telephony.PhoneStateListener)
        manager=(TelephonyManager)getSystemService(Context.TELEPHONY_SERVICE);//Getting the TelephonyManager Instance
    }
	
	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		super.onResume();
		manager.listen(phoneStateListener, PhoneStateListener.LISTEN_CALL_STATE);//Registering the Listner(PhoneStateMonitor) with the TelephonyManager using PhoneStateListener.LISTEN_CALL_STATE constant
	}
	
	//
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		manager.listen(phoneStateListener, PhoneStateListener.LISTEN_NONE);//Removing the Listner(PhoneStateMonitor) with the TelephonyManager using PhoneStateListener.LISTEN_NONE constant
	}
 }

