package com.geeklub.smartmanager;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.telephony.SmsManager;
import android.util.Log;
import android.widget.Toast;

import com.geeklub.vass.R;
import com.geeklub.vass.model.SmsInfo;
import com.geeklub.vass.utils.CheckUtils;
import com.geeklub.vass.utils.Regex;


public class MainActivity extends Activity {
	
	protected static final int USEFUL = 1 ;
	protected static final int UNUSEFUL = 0;
	private static final int UNKNOW = -1;
	protected static final int EMPTY = 2;
	



	private SmsManager smsManager ;



	Handler shutdown = new Handler(){
		public void handleMessage(Message msg){
			System.out.print("msg.what的值是"+msg.what);
			switch (msg.what) {

			case USEFUL:ShutDownProgress("密码有效，不用重新发送短信");

			break;

			case UNKNOW:ShutDownProgress("闪讯管家发生了错误");
			break;

			case UNUSEFUL:ShutDownProgress("密码无效，正在发送查询短信");
			break;
			
			case EMPTY:ShutDownProgress("当前没有密码，发送查询短信");
			break;



			default:
				break;
			}
		}

	};






	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		smsManager = SmsManager.getDefault();

		atuostart();

	}














	private void atuostart() {
		int isEffect = 2;
		if(smart()==null){
			Log.i("短信为空", "短信记录中还没有闪讯服务器端返回的短信");
			SendMessageTo_Server(MainActivity.this);

		}else{
			//获取当前的时间
			Long current_time = System.currentTimeMillis();
			System.out.println("接收到短信的时间是："+current_time);
			//			从短信的body中获取到密码的截止时间
			String sms_time = Regex.SelectDate(smart().getBody());
			System.out.println("密码的截止时间是"+sms_time);
			//			
			isEffect = CheckUtils.compare_date(current_time, sms_time, MainActivity.this);
			System.out.println("ISEffect的值是："+isEffect);
			//短信密码过期了，无效的密码，发送短信
			if(isEffect==0){
				Log.i("短信密码已过期，是无效的", "正在发送新的短信");
				SendMessageTo_Server(MainActivity.this);
			}


		}	


		Waitfor_Restart(MainActivity.this,isEffect);	

	}

















	/**
	 * 关闭程序
	 */
	protected void ShutDownProgress(String text) {
		MainActivity.this.finish();
		Toast.makeText(this, text, Toast.LENGTH_LONG).show();
	}















	/**
	 * 显示一个正在计算中的进度条
	 * 暂停3秒钟后告诉主线程关闭程序
	 * @param context  上下文
	 */
	private void Waitfor_Restart(Context context,final int isEffect) {
		//      显示进度对话框
		final ProgressDialog shutdown_dialog = ProgressDialog.show(context, "程序马上就要自动关闭...", "see you next time");


		new Thread(new Runnable() {

			@Override
			public void run() {

				try {
					Thread.sleep(3000);
					//					关闭进度对话框
					shutdown_dialog.dismiss();


					Message msg = shutdown.obtainMessage();
					msg.what = isEffect;
					shutdown.sendMessage(msg);

				} catch (InterruptedException e) {

					e.printStackTrace();
				}


			}
		}).start();
	}



	/**
	 * 重新发送短信
	 * 
	 */
	private void SendMessageTo_Server(final Context context) {
		//		Toast.makeText(context, "正在发送短信。。。", Toast.LENGTH_SHORT).show();
		Log.e("Resend", "发送短信中");

		new Thread(){
			public void run() {
				try{
					//发送查询短信
					smsManager.sendTextMessage("106593005", null, "MM", null, null);
					Log.i("Resend", "发送短信到106593005");
					Thread.sleep(9000);

					String sms_time = Regex.SelectDate(smart().getBody());
					Log.d("短信密码的截止时间是：", sms_time);

					Long current_time = System.currentTimeMillis();
					Log.d("收到短信的时间是：", current_time+"");



					CheckUtils.compare_date(current_time, sms_time, context);



				}catch(Exception e){
					e.printStackTrace();

				}

			};

		}.start();

	}



	/**
	 * 从短信记录中获取最新的闪讯的短信
	 * 如果有，则返回最新的一条短信
	 * 如果没有，就返回null
	 * 
	 * @return
	 */
	private SmsInfo smart(){
		Uri uri = Uri.parse("content://sms/");
		ContentResolver resolver = getContentResolver();
		Cursor cursor = resolver.query(uri, new String[]{"address","date","type","body"},"address=?", new String[]{"106593005"},"date desc");

		if(cursor.moveToFirst()){
			String address = cursor.getString(0);
			Long   date    = cursor.getLong(1);
			int    type    = cursor.getInt(2);
			String body    = cursor.getString(3);

			cursor.close();

			System.out.println("address:"+address);
			System.out.println("date:"+date);
			System.out.println("type:"+type);
			System.out.println("body:"+body);



			return new SmsInfo(address, date, type, body);
		}else{
			Log.i("SmsInfo", "目前还没有闪讯发来的短信");
			return null;
		}
	}


}





/*

	private void queryAppInfo() {
//获得已安装的应用程序信息 。可以通过getPackageManager()方法获得。
		PackageManager pm = this.getPackageManager();
		Intent mainIntent = new Intent(Intent.ACTION_MAIN);
		mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);
		List<ResolveInfo> resolveInfos = pm.queryIntentActivities(mainIntent,PackageManager.MATCH_DEFAULT_ONLY );
//		调用系统排序，根据name排序
//该排序很重要，否则只能显示系统应用，而不能列出第三方应用程序
		Collections.sort(resolveInfos, new ResolveInfo.DisplayNameComparator(pm));
		if(mlistAppInfo!=null){
			mlistAppInfo.clear();
			for(ResolveInfo reInfo : resolveInfos){
//				 获得应用程序的包名 
				String pkgName = reInfo.activityInfo.packageName;
//				获得该应用程序的启动Activity的名
				String activityName = reInfo.activityInfo.name;

				if()

//				为应用程序的启动Activity 准备Intent  
				Intent launchIntent = new Intent();
				launchIntent.setComponent(new ComponentName(pkgName,activityName));


			}

		}


	}

 */




