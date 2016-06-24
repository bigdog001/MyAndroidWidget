package com.example.myqq;

import com.easemob.EMCallBack;
import com.easemob.chat.EMChat;
import com.easemob.chat.EMChatManager;
import com.easemob.chat.EMConversation;
import com.easemob.chat.EMGroupManager;
import com.easemob.chat.EMMessage;
import com.easemob.chat.TextMessageBody;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class MyQQNewsMessge extends Activity{
	private EditText ed_text;
	private Button send;
	private EMConversation conversation;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myqq_messge_activity);
		ed_text=(EditText) findViewById(R.id.ed_text);
		send=(Button) findViewById(R.id.send);
		
		

		EMChatManager.getInstance().login("123", "123", new EMCallBack() {// 回调
			@Override
			public void onSuccess() {
				EMGroupManager.getInstance().loadAllGroups();
				EMChatManager.getInstance().loadAllConversations();
				Log.d("main", "登陆聊天服务器成功！");


				EMChat.getInstance().setAppInited();
			}

			@Override
			public void onProgress(int progress, String status) {

			}

			@Override
			public void onError(int code, String message) {
				Log.d("main", "登陆聊天服务器失败！");
			}
		});
		
		send.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(conversation==null)
					conversation = EMChatManager.getInstance().getConversation("1234");
				EMMessage message = EMMessage.createSendMessage(EMMessage.Type.TXT);
				TextMessageBody txtBody = new TextMessageBody("发送的内容");
				// 设置消息body
				message.addBody(txtBody);
				// 设置要发给谁,用户username或者群聊groupid
				message.setReceipt("1234");
				// 把messgage加到conversation中
				conversation.addMessage(message);
				// 通知adapter有消息变动，adapter会根据加入的这条message显示消息和调用sdk的发送方法
				//发送消息
				EMChatManager.getInstance().sendMessage(message, new EMCallBack(){

					@Override
					public void onError(int arg0, String arg1) {
						Log.d("main", "发送失败");
					}

					@Override
					public void onProgress(int arg0, String arg1) {
						Log.d("main", "发送中..");
					}

					@Override
					public void onSuccess() {
							Log.d("main", "发送成功");
					}});
			}
		});
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//unregisterReceiver(msgReceiver);
	}
}

