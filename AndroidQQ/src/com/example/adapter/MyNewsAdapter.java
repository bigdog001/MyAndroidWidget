package com.example.adapter;

import java.util.List;

import org.w3c.dom.Text;

import com.example.myqq.R;
import com.example.smart.SmartImageView;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView.FindListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class MyNewsAdapter extends BaseAdapter{
	private List<com.example.bean.UserInfoWrapper.UserInfo> infos;
	private Context context;
	
	public MyNewsAdapter(Context context,List<com.example.bean.UserInfoWrapper.UserInfo> userInfo) {
		this.context=context;
		this.infos=userInfo;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return infos.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View mView;
		if(convertView!=null){
			mView=convertView;
		}else {
			mView=View.inflate(context, R.layout.tab_listview_news, null);
		}
		SmartImageView smartImageView= (SmartImageView) mView.findViewById(R.id.smartImageView);
		TextView name=(TextView) mView.findViewById(R.id.name);
		TextView signature=(TextView) mView.findViewById(R.id.signature);

		com.example.bean.UserInfoWrapper.UserInfo userInfo=infos.get(position);
		//1.请求的URL地址，2.显示请求失败的图片。3.正在请求的图片
		smartImageView.setImageUrl(userInfo.getHead(),R.drawable.image1,R.drawable.touxiang);
		name.setText(userInfo.getName());
		signature.setText(userInfo.getSignature());

		return mView;
	}


}
