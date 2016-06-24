package com.example.download;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import org.json.JSONObject;

import android.app.Application;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v4.graphics.BitmapCompat;
import android.support.v4.util.LruCache;
import android.widget.ImageView;
import android.widget.ListView;

import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response.Listener;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageCache;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.adapter.MyNewsAdapter;
import com.example.bean.UserInfoWrapper;
import com.example.myqq.R;
import com.example.myqq.R.id;
import com.google.gson.Gson;



public class httpDownloader extends AsyncTask<String, Void, String>{
	private URL url;
	private HttpURLConnection con;
	private String result;
	private ListView listView;
	private Context context;
	private ImageView imageView;
	public httpDownloader(Context context,ListView listView) {
		this.context=context;
		this.listView=listView;
	}
	public void setResult(String result){
		this.result=result;
	}
	public String getResult(){
		return result;
	}
	
	@Override
	protected String doInBackground(String... params) {
		StringBuffer sb=new StringBuffer();
		String line=null;
		BufferedReader buffer=null;
		try {
			//创建一个URL
			url=new URL("http://192.168.1.166:8080/users/userinfos.xml");

			//建立一个http连接
			con=(HttpURLConnection) url.openConnection();

			//io流读取数据
			buffer=new BufferedReader(new InputStreamReader(con.getInputStream()));

			while ((line=buffer.readLine())!=null) {
				sb.append(line);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}finally{

			try {
				buffer.close();

			} catch (Exception e2) {
				e2.printStackTrace();
			}
		}	
		return sb.toString();
	}
	@Override
	public void onPostExecute(String result) {
		try {
			Gson gson=new Gson();
			UserInfoWrapper wrrap = new Gson().fromJson(result, UserInfoWrapper.class);
			MyNewsAdapter adapter=new MyNewsAdapter(context, wrrap.userInfo);
			listView.setAdapter(adapter);
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}

}


