package com.example.start;

import com.example.myqq.R;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class MyqqStartActivity extends Activity{
	private Button startbutton;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.start_activity);
		startbutton=(Button) findViewById(R.id.startbutton);
		startbutton.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent=new Intent();
				intent.setClass(MyqqStartActivity.this, PagerActivity.class);
				startActivity(intent);
				finish();
			}
		});
	}
}
