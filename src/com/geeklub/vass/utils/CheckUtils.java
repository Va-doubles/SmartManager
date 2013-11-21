package com.geeklub.vass.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;



public class CheckUtils {
	/**下次开启闪讯的时间间隔*/
	private static Long  Next_RESERT_TIME;
	public final static String MYACTION = "android.intent.action.STARTMYAPP";  

	/**
	 * 比较两个时间，如果当前时间大于密码截止时间，那么当前的密码就是无效的，返回0，表示要重新发送短信
	 * 如果是否，返回1,表示密码没有失效，不需要发送查询短信
	 * 如果是-1，表示
	 * @param current_time  当前的手机上的时间
	 * @param sms_time		闪讯密码的截止时间
	 * @return   
	 * 返回0，表示要重新发送短信
	 * 返回1,表示密码没有失效，不需要发送查询短信
	 * 返回-1，表示发生未知错误，
	 */

	public static int compare_date(Long current_time,String sms_time,Context context){
		java.text.DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		try{

			Date sms_date = df.parse(sms_time);

			//如果当前的时间大于密码的截止时间，返回0
			if(current_time>sms_date.getTime())
				return 0;
			else{
				//获取下次开启程序的时间间隔
				Next_RESERT_TIME = sms_date.getTime() + 60000-current_time;
				AlarmClock(context, Next_RESERT_TIME);
				return 1;
			}

		}catch(Exception e){
//			出现未知错误
			return -1;

		}


	}

	private static void AlarmClock(Context context,Long Next_Time) {
		AlarmManager am = (AlarmManager)context.getSystemService(Context.ALARM_SERVICE);  
		Intent intent = new Intent(MYACTION);  
		PendingIntent pi = PendingIntent.getBroadcast(context, 0, intent, 1);  
		am.set(AlarmManager.RTC_WAKEUP,System.currentTimeMillis()+Next_Time, pi);	
		System.out.println("闹钟"+Next_Time);
	}

	public static Long getNext_RESERT_TIME() {
		return Next_RESERT_TIME;
	}

	public static void setNext_RESERT_TIME(Long next_RESERT_TIME) {
		Next_RESERT_TIME = next_RESERT_TIME;
	}






}
