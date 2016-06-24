package com.example.myqq;

import java.util.Iterator;
import java.util.List;

import com.easemob.EMEventListener;
import com.easemob.EMNotifierEvent;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMMessage;
import com.easemob.util.EMLog;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

public class MyApplicaton extends Application {
	private  EMEventListener eventListener;
	@Override
	public void onCreate() {

		int pid = android.os.Process.myPid();
		String processAppName = getAppName(pid);
		Log.d("main", "process app name : " + processAppName);

		if (processAppName == null
				|| !processAppName
						.equalsIgnoreCase(this.getPackageName())) {
			// 则此application::onCreate 是被service 调用的，直接返回
			return;
		}
		EMChat.getInstance().init(getApplicationContext());
		/**
		 * debugMode == true 时为打开，sdk 会在log里输入调试信息
		 * 
		 * @param debugMode
		 *            在做代码混淆的时候需要设置成false
		 */
		EMChat.getInstance().setDebugMode(true);// 在做打包混淆时，要关闭debug模式，如果未被关闭，则会出现程序无法运行问题
		initLis();
	}

	/**
	 * check the application process name if process name is not qualified, then
	 * we think it is a service process and we will not init SDK
	 * 
	 * @param pID
	 * @return
	 */
	private String getAppName(int pID) {
		String processName = null;
		ActivityManager am = (ActivityManager)
				getSystemService(Context.ACTIVITY_SERVICE);
		List l = am.getRunningAppProcesses();
		Iterator i = l.iterator();
		PackageManager pm = this.getPackageManager();
		while (i.hasNext()) {
			ActivityManager.RunningAppProcessInfo info = (ActivityManager.RunningAppProcessInfo) (i
					.next());
			try {
				if (info.pid == pID) {
					CharSequence c = pm.getApplicationLabel(pm
							.getApplicationInfo(info.processName,
									PackageManager.GET_META_DATA));
					processName = info.processName;
					return processName;
				}
			} catch (Exception e) {
				// Log.d("Process", "Error>> :"+ e.toString());
			}
		}
		return processName;
	}
	/*
	 * 注册消息监听
	 * */
	private void initLis(){
		 eventListener = new EMEventListener() {
            @Override
            public void onEvent(EMNotifierEvent event) {
            	
                switch (event.getEvent()) {
                case EventNewMessage:
                {
                	 EMMessage message = (EMMessage)event.getData();
                	 Log.d("main",message.getBody().toString());
                    break;
                }
                case EventNewCMDMessage:
                {
                    break;
                }
                // add other events in case you are interested in
                default:
                    break;
                }
                
            }
        };
        EMChatManager.getInstance().registerEventListener(eventListener);
	}
}
