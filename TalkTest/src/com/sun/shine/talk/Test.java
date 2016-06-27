package com.sun.shine.talk;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class Test extends Activity {
	/** Called when the activity is first created. */
	ListView list;
	List<Talk> talkList;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		init();
		list = (ListView) findViewById(R.id.list);
		MyAdapter myAdapter = new MyAdapter();
		myAdapter.setList(talkList);
		list.setAdapter(myAdapter);

	}

	void init() {
		talkList = new ArrayList<Talk>();
		String str = "xxx";
		for (int i = 0; i < 10; i++) {
			if (i > 5) {
				str = "xxx";
			}
			str = str + str;
			Talk talk = new Talk();
			if (i % 2 == 0) {

				talk.isLeft = true;
				talk.text = "left" + str;
			} else {
				talk.text = "right" + str;
			}
			talkList.add(talk);
		}
	}

	class MyAdapter extends BaseAdapter {

		List<Talk> list;

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return list.size();
		}

		public void setList(List<Talk> talkList) {
			// TODO Auto-generated method stub
			this.list = talkList;
		}

		@Override
		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			Talk talk = list.get(position);
			if (convertView == null) {
				LayoutInflater inflater = LayoutInflater.from(Test.this);

				convertView = inflater.inflate(R.layout.talk, null);

			}
			TextView text = (TextView) convertView.findViewById(R.id.talk);

			text.setText(talk.text);
			LinearLayout parent1 = (LinearLayout) convertView
					.findViewById(R.id.parent1);
			LinearLayout parent2 = (LinearLayout) convertView
					.findViewById(R.id.parent2);
			if (talk.isLeft) {

				text.setBackgroundResource(R.drawable.bg_recieved_msg);
				parent1.setGravity(android.view.Gravity.LEFT);
				parent2.setGravity(android.view.Gravity.LEFT);

			} else {
				text.setBackgroundResource(R.drawable.bg_sent_msg);
				parent1.setGravity(android.view.Gravity.RIGHT);
				parent2.setGravity(android.view.Gravity.RIGHT);
			}
			return convertView;
		}

	}

	class Talk {
		String text = "";
		boolean isLeft = false;

	}
}