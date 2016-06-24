package com.car.activity;

import java.util.HashMap;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;

import com.alibaba.fastjson.JSONArray;
import com.car.application.LocalApplication;
import com.car.cache.AsyncImageLoader;
import com.car.entity.NewsImageItem;
import com.car.util.ConstantsUtil;
import com.car.view.ToastMaker;
import com.car.view.photoview.PhotoView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 资讯图解详情
 * 
 * @author blue
 * 
 */
public class NewsPictureActivity extends BaseActivity
{
	@ViewInject(R.id.car_picture_iv_back)
	ImageView car_picture_iv_back;
	@ViewInject(R.id.car_picture_iv_share)
	ImageView car_picture_iv_share;
	@ViewInject(R.id.car_picture_tv_index)
	TextView car_picture_tv_index;
	@ViewInject(R.id.car_picture_vp)
	ViewPager car_picture_vp;

	private static final int MSG_AUTH_CANCEL = 2;
	private static final int MSG_AUTH_ERROR = 3;
	private static final int MSG_AUTH_COMPLETE = 4;

	// 获取的数据
	private List<NewsImageItem> dataList;

	// 分享对话框
	private Dialog shareDialog;

	// 静态方法启动activity
	public static void startActivity(Context context, String datas)
	{
		Intent intent = new Intent(context, NewsPictureActivity.class);
		intent.putExtra("datas", datas);
		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		dataList = JSONArray.parseArray(getIntent().getStringExtra("datas"), NewsImageItem.class);
		super.onCreate(savedInstanceState);
	}

	@Override
	protected int getLayoutId()
	{
		return R.layout.news_picture_main;
	}

	@Override
	protected void initParams()
	{
		car_picture_tv_index.setText("1/" + dataList.size());
		// 绑定适配器
		car_picture_vp.setAdapter(new ViewPagerAdapter());
		car_picture_vp.setOnPageChangeListener(new ViewPagerChangeListener());
		car_picture_vp.setCurrentItem(0);
	}

	// 控件点击事件
	@OnClick({ R.id.car_picture_iv_back, R.id.car_picture_iv_share })
	public void viewOnClick(View view)
	{
		switch (view.getId()) {
		case R.id.car_picture_iv_back:

			finish();

			break;

		case R.id.car_picture_iv_share:

			// 弹出分享对话框
			showDialog();

			break;

		default:
			break;
		}
	}

	@SuppressLint("InflateParams")
	private void showDialog()
	{
		if (shareDialog == null)
		{
			View view = getLayoutInflater().inflate(R.layout.share_dialog_main, null);
			// 新浪微博
			LinearLayout share_llyt_sina = (LinearLayout) view.findViewById(R.id.share_llyt_sina);

			// 新浪微博
			share_llyt_sina.setOnClickListener(new OnClickListener()
			{

				@Override
				public void onClick(View v)
				{
					ShareParams sp = new ShareParams();
					sp.setText("我分享了一张图片，大家快来看！");
					sp.setImageUrl(ConstantsUtil.IMAGE_URL + dataList.get(0).url);

					Platform weibo = ShareSDK.getPlatform(SinaWeibo.NAME);
					// 设置分享事件回调
					weibo.setPlatformActionListener(new ShareListener());
					// 执行图文分享
					weibo.share(sp);

					shareDialog.dismiss();
					ToastMaker.showShortToast("正在分享，请稍候");
				}
			});

			shareDialog = new Dialog(this, R.style.DialogNoTitleStyleTranslucentBg);
			shareDialog.setContentView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
			Window window = shareDialog.getWindow();
			// 设置显示动画
			window.setWindowAnimations(R.style.BottomMenuAnim);
			WindowManager.LayoutParams wl = window.getAttributes();
			wl.x = 0;
			wl.y = getWindowManager().getDefaultDisplay().getHeight();
			// 以下这两句是为了保证可以水平满屏
			wl.width = ViewGroup.LayoutParams.MATCH_PARENT;
			wl.height = ViewGroup.LayoutParams.WRAP_CONTENT;

			// 设置显示位置
			shareDialog.onWindowAttributesChanged(wl);
			// 设置点击外面关闭对话框
			shareDialog.setCanceledOnTouchOutside(true);
		}
		shareDialog.show();
	}

	// 分享回调接口
	private class ShareListener implements PlatformActionListener
	{

		@Override
		public void onCancel(Platform arg0, int arg1)
		{
			// 判断当前是否是分享结果
			if (arg1 == Platform.ACTION_SHARE)
			{
				shareHandler.sendEmptyMessage(MSG_AUTH_CANCEL);
			}
		}

		@Override
		public void onComplete(Platform arg0, int arg1, HashMap<String, Object> arg2)
		{
			// 判断当前是否是分享结果
			if (arg1 == Platform.ACTION_SHARE)
			{
				shareHandler.sendEmptyMessage(MSG_AUTH_COMPLETE);
			}
		}

		@Override
		public void onError(Platform arg0, int arg1, Throwable arg2)
		{
			// 判断当前是否是分享结果
			if (arg1 == Platform.ACTION_SHARE)
			{
				shareHandler.sendEmptyMessage(MSG_AUTH_ERROR);
			}
		}

	}

	@SuppressLint("HandlerLeak")
	private Handler shareHandler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what) {
			// 授权取消
			case MSG_AUTH_CANCEL:

				ToastMaker.showShortToast("分享取消");

				break;
			// 授权成功
			case MSG_AUTH_COMPLETE:

				ToastMaker.showShortToast("分享成功");

				break;
			// 授权失败
			case MSG_AUTH_ERROR:

				ToastMaker.showShortToast("分享失败，请先安装第三方客户端");

				break;

			default:
				break;
			}

		}
	};

	// 查看大图viewpager适配器
	private class ViewPagerAdapter extends PagerAdapter
	{

		@SuppressLint("InflateParams")
		@Override
		public Object instantiateItem(ViewGroup container, final int position)
		{
			View view = getLayoutInflater().inflate(R.layout.news_picture_item, null);
			PhotoView picture_iv_item = (PhotoView) view.findViewById(R.id.picture_iv_item);
			// 给imageview设置一个tag，保证异步加载图片时不会乱序
			picture_iv_item.setTag(ConstantsUtil.IMAGE_URL + dataList.get(position).url);
			// 开启异步加载图片,显示图片宽度为screenW
			AsyncImageLoader.getInstance(NewsPictureActivity.this).loadBitmaps(view, picture_iv_item, ConstantsUtil.IMAGE_URL + dataList.get(position).url, LocalApplication.getInstance().screenW, 0);
			container.addView(view);

			return view;
		}

		@Override
		public int getCount()
		{
			return dataList.size();
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1)
		{
			return arg0 == arg1;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			View view = (View) object;
			container.removeView(view);
		}

	}

	// viewpager切换监听器
	private class ViewPagerChangeListener implements OnPageChangeListener
	{
		@Override
		public void onPageScrollStateChanged(int arg0)
		{
		}

		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2)
		{
		}

		@Override
		public void onPageSelected(int arg0)
		{
			car_picture_tv_index.setText((arg0 + 1) + "/" + dataList.size());
		}

	}
}
