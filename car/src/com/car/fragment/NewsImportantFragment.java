package com.car.fragment;

import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONObject;
import com.car.activity.R;
import com.car.adapter.NewsHeadAdapter;
import com.car.adapter.NewsItemAdapter;
import com.car.application.LocalApplication;
import com.car.entity.NewsItem;
import com.car.util.ConstantsUtil;
import com.car.util.DisplayUtil;
import com.car.util.JListKit;
import com.car.view.ProgressWheel;
import com.car.view.ToastMaker;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest.HttpMethod;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnScroll;
import com.lidroid.xutils.view.annotation.event.OnScrollStateChanged;

/**
 * 资讯-要闻
 * 
 * @author blue
 */
public class NewsImportantFragment extends BaseFragment
{
	@ViewInject(R.id.ptr)
	PtrFrameLayout ptr;
	@ViewInject(R.id.pw)
	ProgressWheel pw;
	@ViewInject(R.id.news_important_lv)
	ListView news_important_lv;

	// 数据源
	private List<NewsItem> datas = JListKit.newArrayList();
	// 适配器
	private NewsItemAdapter adapter;

	// 加载布局
	private LinearLayout loading_llyt;

	// 是否为最后一行
	private boolean isLastRow = false;
	// 是否还有更多数据
	private boolean isMore = true;
	// 是否正在加载数据
	private boolean isLoading = false;

	private int pageIndex = 0;
	private int pageSize = 20;

	// 新闻组图
	private FrameLayout news_head_view;
	// viewpager
	private ViewPager news_head_vp;
	private TextView news_head_tv;
	private TextView news_head_tv1;
	private TextView news_head_tv2;
	private TextView news_head_tv3;
	private TextView news_head_tv4;
	private TextView news_head_tv5;
	// 指示器
	private List<TextView> textViewList = JListKit.newArrayList();;
	// 推荐新闻数据源
	private List<NewsItem> headList = JListKit.newArrayList();
	// 推荐新闻适配器
	private NewsHeadAdapter headAdapter;

	// 自动轮播定时器
	private ScheduledExecutorService scheduledExecutorService;
	// 当前图片的索引号
	private int currentItem = 0;

	@Override
	protected int getLayoutId()
	{
		return R.layout.fragment_important_main;
	}

	@Override
	protected void initParams()
	{
		// 设置显示文字信息
		pw.setText("loading");
		// 开始旋转加载
		pw.spin();

		// 底部布局
		loading_llyt = (LinearLayout) getLayoutInflater(null).inflate(R.layout.listview_loading_view, null);
		// 头部布局
		news_head_view = (FrameLayout) getLayoutInflater(null).inflate(R.layout.news_head_view, null);
		news_head_vp = (ViewPager) news_head_view.findViewById(R.id.news_head_vp);
		news_head_tv = (TextView) news_head_view.findViewById(R.id.news_head_tv);
		// 初始化指示器
		news_head_tv1 = (TextView) news_head_view.findViewById(R.id.news_head_tv1);
		news_head_tv2 = (TextView) news_head_view.findViewById(R.id.news_head_tv2);
		news_head_tv3 = (TextView) news_head_view.findViewById(R.id.news_head_tv3);
		news_head_tv4 = (TextView) news_head_view.findViewById(R.id.news_head_tv4);
		news_head_tv5 = (TextView) news_head_view.findViewById(R.id.news_head_tv5);
		textViewList.add(news_head_tv1);
		textViewList.add(news_head_tv2);
		textViewList.add(news_head_tv3);
		textViewList.add(news_head_tv4);
		textViewList.add(news_head_tv5);

		// 初始化推荐新闻
		headAdapter = new NewsHeadAdapter(context, headList);
		news_head_vp.setAdapter(headAdapter);
		news_head_vp.setOnPageChangeListener(new NewsPageChangeListener());

		// 初始化列表
		adapter = new NewsItemAdapter(context, datas, news_important_lv);
		// 增加头部显示布局
		news_important_lv.addHeaderView(news_head_view);
		// 增加底部加载布局
		news_important_lv.addFooterView(loading_llyt);
		// 绑定适配器
		news_important_lv.setAdapter(adapter);

		initPtr();
		// 加载数据
		loadHeadData();
		loadListData();
	}

	// 初始化下拉刷新
	private void initPtr()
	{
		// header
		StoreHouseHeader header = new StoreHouseHeader(context);
		header.setLayoutParams(new PtrFrameLayout.LayoutParams(-1, -2));
		header.setPadding(0, DisplayUtil.dip2px(context, 15), 0, DisplayUtil.dip2px(context, 10));
		header.initWithString("CAR CAR");
		header.setTextColor(getResources().getColor(android.R.color.black));

		ptr.setHeaderView(header);
		ptr.addPtrUIHandler(header);
		ptr.setPtrHandler(new PtrHandler()
		{

			@Override
			public void onRefreshBegin(PtrFrameLayout frame)
			{
				frame.postDelayed(new Runnable()
				{
					@Override
					public void run()
					{
						ptr.refreshComplete();
					}
				}, 1800);
			}

			@Override
			public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header)
			{
				return PtrDefaultHandler.checkContentCanBePulledDown(frame, content, header);
			}
		});
	}

	// 加载头部数据
	private void loadHeadData()
	{
		LocalApplication.getInstance().httpUtils.send(HttpMethod.GET, ConstantsUtil.SERVER_URL + "getNewsHead", new RequestCallBack<String>()
		{

			@Override
			public void onFailure(HttpException arg0, String arg1)
			{
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0)
			{
				String list = JSONObject.parseObject(arg0.result).getString("list");
				List<NewsItem> tmp = JSONObject.parseArray(list, NewsItem.class);
				if (JListKit.isNotEmpty(tmp))
				{
					headList.addAll(tmp);
					headAdapter.refreshDatas(headList);
					// 初始选中项
					news_head_vp.setCurrentItem(headList.size() * 1000);
					news_head_tv.setText(headList.get(0).title);

					// 要闻推荐最多不超过5个，低于5个时指示器需要做相应调整
					if (tmp.size() < 5)
					{
						for (int i = 0; i < 5 - tmp.size(); i++)
						{
							textViewList.get(textViewList.size() - 1 - i).setVisibility(View.GONE);
						}
					}
				}
			}
		});
	}

	// 加载列表数据
	private void loadListData()
	{
		isLoading = true;
		pageIndex++;
		RequestParams params = new RequestParams();
		params.addBodyParameter("pageIndex", pageIndex + "");
		params.addBodyParameter("pageSize", pageSize + "");
		LocalApplication.getInstance().httpUtils.send(HttpMethod.POST, ConstantsUtil.SERVER_URL + "getNewsImportant", params, new RequestCallBack<String>()
		{

			@Override
			public void onFailure(HttpException arg0, String arg1)
			{
				pw.stopSpinning();
				pw.setVisibility(View.GONE);
				ToastMaker.showShortToast("请求失败，请检查网络后重试");
			}

			@Override
			public void onSuccess(ResponseInfo<String> arg0)
			{
				String list = JSONObject.parseObject(arg0.result).getString("list");
				List<NewsItem> tmp = JSONObject.parseArray(list, NewsItem.class);

				pw.stopSpinning();
				pw.setVisibility(View.GONE);
				news_important_lv.setVisibility(View.VISIBLE);

				if (JListKit.isNotEmpty(tmp))
				{
					if (pageIndex == 1)
					{
						// 移除底部加载布局
						if (tmp.size() < pageSize)
						{
							news_important_lv.removeFooterView(loading_llyt);
						}
					}
					datas.addAll(tmp);
					adapter.refreshDatas(datas);
				} else
				{
					isMore = false;
					news_important_lv.removeFooterView(loading_llyt);
					ToastMaker.showShortToast("已没有更多数据");
				}
				isLoading = false;
			}
		});
	}

	@OnScroll(R.id.news_important_lv)
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		if (firstVisibleItem + visibleItemCount == totalItemCount && totalItemCount > 0)
		{
			isLastRow = true;
		}
	}

	@OnScrollStateChanged(R.id.news_important_lv)
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{
		if (isLastRow && scrollState == OnScrollListener.SCROLL_STATE_IDLE)
		{
			if (!isLoading && isMore)
			{
				loadListData();
			}
			isLastRow = false;
		}
	}

	/**
	 * 当ViewPager中页面的状态发生改变时调用
	 * 
	 * @author blue
	 */
	private class NewsPageChangeListener implements OnPageChangeListener
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
			currentItem = arg0;
			news_head_tv.setText(headList.get(arg0 % headList.size()).title);
			for (int i = 0; i < headList.size(); i++)
			{
				if (i == arg0 % headList.size())
				{
					textViewList.get(i).setBackgroundColor(context.getResources().getColor(R.color.news_head_cl_choose));
				} else
				{
					textViewList.get(i).setBackgroundColor(context.getResources().getColor(R.color.news_head_cl_unchoose));
				}
			}
		}
	}

	@Override
	public void onStart()
	{
		super.onStart();
		scheduledExecutorService = Executors.newSingleThreadScheduledExecutor();
		// 当Activity显示出来后，每五秒钟切换一次图片显示
		scheduledExecutorService.scheduleAtFixedRate(new ScrollTask(), 5, 5, TimeUnit.SECONDS);
	}

	@Override
	public void onStop()
	{
		super.onStop();
		// 当Activity不可见的时候停止切换
		if (scheduledExecutorService != null)
		{
			scheduledExecutorService.shutdown();
		}
	}

	/**
	 * 换行切换任务
	 * 
	 * @author Administrator
	 * 
	 */
	private class ScrollTask implements Runnable
	{

		public void run()
		{
			synchronized (news_head_vp)
			{
				currentItem++;
				// 通过Handler切换图片
				handler.sendEmptyMessage(1);
			}
		}

	}

	private Handler handler = new Handler()
	{

		@Override
		public void handleMessage(Message msg)
		{
			switch (msg.what) {
			// 切换图片
			case 1:

				if (JListKit.isNotEmpty(headList))
				{
					news_head_vp.setCurrentItem(currentItem);
				}

				break;

			default:
				break;
			}
		}

	};

}
