package com.geeklub.smartmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class AlarmReceiver extends BroadcastReceiver {
	 public final String MYTAG = "Vass";  
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.e(MYTAG,"起床了，闪讯管家");  
		Intent in = new Intent();  
		in.setClass(context, MainActivity.class);  
		in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);  
		context.startActivity(in);  

	}

}
