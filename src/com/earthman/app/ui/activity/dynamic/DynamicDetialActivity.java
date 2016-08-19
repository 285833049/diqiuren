package com.earthman.app.ui.activity.dynamic;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.android.volley.AuthFailureError;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.Response.Listener;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.MyPicGrideAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.bean.DynamicResponse.DynamicContent;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.earthman.app.ui.fragment.DynamicDetialFragment;
import com.earthman.app.ui.fragment.main.DynamicFragment;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyGridView;
import com.earthman.app.widget.MyToast;
import com.earthman.app.widget.viewpagerindicator.TabPageIndicator;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Title: DynamicDetialActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月13日
 */
@ContentView(R.layout.activity_dynamic_detial)
public class DynamicDetialActivity extends BaseActivity {

	@ViewInject(R.id.btn_back)
	Button mBtnRight;
	@ViewInject(R.id.tv_left)
	private TextView mTvTitleLeft;
	// -----------------------------
	@ViewInject(R.id.dynamic_images)
	MyGridView mGVImages;
	@ViewInject(R.id.dynamic_detial_viewpage)
	ViewPager mViewPagePriser;
	@ViewInject(R.id.viewpage_indecator)
	TabPageIndicator mVPI;
	// 点赞分享评论
	@ViewInject(R.id.ll_dianzan)
	LinearLayout mLlPraised;
	@ViewInject(R.id.dynamicReplay)
	LinearLayout mLlReply;
	@ViewInject(R.id.dynamicShare)
	LinearLayout mLlShare;
	@ViewInject(R.id.dianzan_iv_h)
	ImageView mIvPraised;
	// --------------------动态内容-------------
	@ViewInject(R.id.v_user_icon)
	ImageView mIvUserIcon;
	@ViewInject(R.id.tv_user_nick)
	TextView mTvUserNick;
	@ViewInject(R.id.creadtime)
	TextView mTvCreadTime;
	@ViewInject(R.id.content)
	TextView mTvContent;

	private DynamicContent dynamicContent;
	private static String[] VpiTitle;
	ArrayList<String> imageList;
	private YwbImageLoader imageLoader;
	
	
	//------------------转发动态内容-------------
	@ViewInject(R.id.tv_location)
	TextView mTvLoction;
	
	@ViewInject(R.id.rl_transfer)
	RelativeLayout mRltransfer;
	
	@ViewInject(R.id.tv_transfer_nickname)
	TextView mTvTransferNickname;
	
	@ViewInject(R.id.tv_transfer_content)
	TextView mTvTtransferContent;
	
	@ViewInject(R.id.transfer_dynamic_images)
	GridView  mTransferDynamicImages;
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.ll_dianzan:
			doPostPraise(dynamicContent.getHasPraised(), dynamicContent.getId(), dynamicContent.getUserId());
			dynamicContent
					.setPraiseAmount(dynamicContent.getHasPraised().equals("0") ? dynamicContent.getPraiseAmount() + 1 : dynamicContent.getPraiseAmount() - 1);
			dynamicContent.setHasPraised(dynamicContent.getHasPraised().equals("0") ? "1" : "0");
			praised();
			break;
		case R.id.dynamicReplay:
			Intent intent = new Intent(this, InPutActivity.class);
			intent.putExtra("articleId", dynamicContent.getId());
			intent.putExtra("authorId", dynamicContent.getUserId());
			intent.putExtra("type", "1");
			startActivity(intent);
			break;
		case R.id.dynamicShare:
			Intent shareIntent= new Intent(DynamicDetialActivity.this, DynamicTransferActivity.class);
			shareIntent.putExtra("dynamicContent", dynamicContent);
			startActivity(shareIntent);
			break;
		default:
			break;
		}
	}

	private void praised() {
		Drawable redDrawable = getResources().getDrawable(R.drawable.dianzanh);
		Drawable grayDrawable = getResources().getDrawable(R.drawable.dianzanb);
		redDrawable.setBounds(0, 0, 52, 52);
		grayDrawable.setBounds(0, 0, 52, 52);
		if ("0".equals(dynamicContent.getHasPraised())) {
			mIvPraised.setImageDrawable(grayDrawable);
		} else if ("1".equals(dynamicContent.getHasPraised())) {
			mIvPraised.setImageDrawable(redDrawable);
		}
		// mTvZanNum.setText(dynamicContent.getPraiseAmount() + "");// 设置点赞数量
	}

	// 点赞请求
	private void doPostPraise(String type, String articleId, String userId) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(type);
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.PRAISE_DYNAMIC, list);
		JSONObject jsonRequest = new JSONObject();
		try {
			jsonRequest.put("articleId", articleId);
			jsonRequest.put("articleUserId", userId);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		Listener<JSONObject> listener = new Listener<JSONObject>() {
			@Override
			public void onResponse(JSONObject response) {
				BaseResponse response2 = JSON.parseObject(response.toString(), BaseResponse.class);
				LogUtil.i("注册返回结果", response.toString());
				if ("000000".equals(response2.getCode())) {
					// MyToast.makeText(context, response2.getMessage(),
					// Toast.LENGTH_LONG).show();
				} else {
					NetStatusHandUtil.getInstance().handStatus(DynamicDetialActivity.this, response2.getCode(), response2.getMessage());
				}
			}
		};
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, loginUrl, jsonRequest, listener, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				LogUtil.e("网络请求错误", error.toString());
				MyToast.makeText(DynamicDetialActivity.this, R.string.server_error, Toast.LENGTH_LONG).show();

			}
		}) {

			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("Content-Type", "application/json");
				return map;
			}
		};
		executeRequest(jsonObjectRequest);
	}

	@Override
	protected void initData() {
		width_grid = (int) ((AndroidUtils.getDeviceWidth(this)));
		if (getIntent().getExtras() != null) {
			dynamicContent = (DynamicContent) getIntent().getSerializableExtra("dynamicdetial");
		}
		VpiTitle = getResources().getStringArray(R.array.dynamic_commentandpraiser);
		imageLoader = new YwbImageLoader();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#initView()
	 */
	@Override
	protected void initView() {
		ViewUtils.inject(this);

		mTvTitleLeft.setText(R.string.dynamic_detial);
		mBtnRight.setOnClickListener(this);
		mLlPraised.setOnClickListener(this);
		mLlReply.setOnClickListener(this);
		mLlShare.setOnClickListener(this);
		if (dynamicContent.getImgs().length() != 0 && dynamicContent.getImgs().contains(",")) {
			imageList = new ArrayList<String>(Arrays.asList(dynamicContent.getImgs().split(",")));
		} else if (dynamicContent.getImgs().length() != 0 && !dynamicContent.getImgs().contains(",")) {
			imageList = new ArrayList<String>();
			imageList.add(dynamicContent.getImgs());
		} else {
			imageList = new ArrayList<String>();
		}
		DynamicFragment.setNumColumns(mGVImages, imageList.size());
		// 设置内容
		mTvContent.setText(dynamicContent.getTitle());
		mTvCreadTime.setText(dynamicContent.getCreatedAt());
		if(!TextUtils.isEmpty(dynamicContent.getRemarks())){
			mTvUserNick.setText(dynamicContent.getRemarks());
		}else{
			mTvUserNick.setText(dynamicContent.getNice());
		}		
		imageLoader.loadImage(dynamicContent.getAvatar(), mIvUserIcon,R.drawable.user_avatar);
		mIvUserIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PeronalDetialActivity.startSee(DynamicDetialActivity.this,dynamicContent.getUserId());
			}
		});
		
		mGVImages.setSelector(new ColorDrawable(Color.TRANSPARENT));// 取消GridView中Item选中时默认的背景色
		//mGVImages.setAdapter(myGridViewAdapter);
		mGVImages.setAdapter(new MyPicGrideAdapter(DynamicDetialActivity.this, imageList));
		FragmentPagerAdapter adapter = new GoogleMusicAdapter(getSupportFragmentManager());
		mViewPagePriser.setAdapter(adapter);
		mVPI.setViewPager(mViewPagePriser);
		mGVImages.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent intent = new Intent(DynamicDetialActivity.this, ImageViewPage.class);
				intent.putExtra("pics", dynamicContent.getImgs());
				intent.putExtra("position", position);
				startActivity(intent);
			}
		});
		
		
		if(dynamicContent.getArticlesBase() != null){//当前动态是转发过来的动态
			mRltransfer.setVisibility(View.VISIBLE);
			String remarks=dynamicContent.getArticlesBase().getRemarks();
			String nice=dynamicContent.getArticlesBase().getNice();
			String title=dynamicContent.getArticlesBase().getTitle();
			//如果remarks 和nice同时为null 且title包含已被删除 则此转发动态被删除
			if(TextUtils.isEmpty(remarks)&&TextUtils.isEmpty(nice))
			{
				String tvContent="<font color=\"#FF0000\">"+title.replace("[del.此", "").replace(" .del]", "")+"</font>";
				mTvTtransferContent.setText(Html.fromHtml(tvContent));//设置内容
			}else{
				
				if(!TextUtils.isEmpty(remarks)){
					mTvTransferNickname.setText(remarks+":");// 设置昵称
				}else{
					mTvTransferNickname.setText(nice+":");// 设置昵称
				}
				if(!TextUtils.isEmpty(title)){
					mTvTtransferContent.setText(title);//设置内容
				}else{
					mTvTtransferContent.setText(R.string.publish_mood);//设置内容
				}
				mTvTransferNickname.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						PeronalDetialActivity.startSee(DynamicDetialActivity.this,String.valueOf(dynamicContent.getArticlesBase().getUserId()));
					}
				});
				ArrayList<String> imgList = new ArrayList<String>();
				final String imgs = dynamicContent.getArticlesBase().getImgs();
				if (!TextUtils.isEmpty(imgs) && imgs.contains(",")) {
					imgList.addAll(Arrays.asList(imgs.split(",")));
				} else if (!TextUtils.isEmpty(imgs) && !imgs.contains(",")) {
					imgList.add(imgs);
				} 
				if (imgList.size() == 0) {
					mTransferDynamicImages.setVisibility(View.GONE);
				} else {
					mTransferDynamicImages.setVisibility(View.VISIBLE);
					DynamicFragment.setNumColumns(mTransferDynamicImages, imgList.size());
	
					mTransferDynamicImages.setSelector(new ColorDrawable(Color.TRANSPARENT));// 取消GridView中Item选中时默认的背景色
					mTransferDynamicImages.setAdapter(new MyPicGrideAdapter(DynamicDetialActivity.this, imgList));
					mTransferDynamicImages.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Intent intent = new Intent(DynamicDetialActivity.this, ImageViewPage.class);
							intent.putExtra("pics", imgs);
							intent.putExtra("position", position);
							startActivity(intent);
						}
					});
				}
			}
		}else{
			mRltransfer.setVisibility(View.GONE);
		}
		
		
		if(TextUtils.isEmpty(dynamicContent.getLocation())){//不显示位置
			mTvLoction.setVisibility(View.GONE);
		}else{
			mTvLoction.setVisibility(View.VISIBLE);
			mTvLoction.setText(dynamicContent.getLocation());
			if(dynamicContent.getLocation().contains(".")){
				mTvLoction.setTextColor(getResources().getColor(R.color.tab_text_hover));
			}else{
				mTvLoction.setTextColor(getResources().getColor(R.color.black3));
			}
		}
		
		new myAsyncTask().execute();// 模拟网络请求去再次计算高度
		mVPI.setOnPageChangeListener(new OnPageChangeListener() {
			@Override
			public void onPageSelected(int position) {
				// 页面切换后重置ViewPager高度
				resetViewPagerHeight(position);
				switch (position) {
				case 0:
					break;
				case 1:
					break;
				}
			}

			@Override
			public void onPageScrolled(int postion, float arg1, int arg2) {
			}

			@Override
			public void onPageScrollStateChanged(int arg0) {
			}
		});

	}

	public class myAsyncTask extends AsyncTask<Void, Void, Void> {
		@Override
		protected Void doInBackground(Void... params) {
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {
			resetViewPagerHeight(0);
		}
	}

	/**
	 * 重新设置viewPager高度
	 * 
	 * @param position
	 */
	public void resetViewPagerHeight(int position) {

		View child = mViewPagePriser.getChildAt(position);
		if (child != null) {
			child.measure(0, 0);
			int h = child.getMeasuredHeight();
			LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) mViewPagePriser.getLayoutParams();
			params.height = h + 20;
			mViewPagePriser.setLayoutParams(params);
		}
	}

	Runnable runnable = new Runnable() {
		public void run() {
			try {
				Thread.sleep(300);
				resetViewPagerHeight(0);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	};

	class GoogleMusicAdapter extends FragmentPagerAdapter {
		public GoogleMusicAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Fragment fragment = new DynamicDetialFragment(DynamicDetialActivity.this);
			Bundle args = new Bundle();
			args.putString("arg", VpiTitle[position]);
			args.putString("articleId", dynamicContent.getId());
			args.putString("userid", dynamicContent.getUserId());

			args.putSerializable("dynamicComment", (Serializable) dynamicContent.getArticlesComments());
			fragment.setArguments(args);
			return fragment;
		}

		@Override
		public CharSequence getPageTitle(int position) {
			return VpiTitle[position % VpiTitle.length];
		}

		@Override
		public int getCount() {
			return VpiTitle.length;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.ywb.earthman.app.base.BaseActivity#setAttribute()
	 */
	@Override
	protected void setAttribute() {
		praised();
		runnable.run();
	}

	private int width_grid;

	public class MyGrideViewHolder {
		ImageView imageview;
	}

	/**
	 * 图片Gridview的适配器
	 */
	private BaseAdapter myGridViewAdapter = new BaseAdapter() {

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MyGrideViewHolder gridviewholder = null;
			if (convertView == null) {
				gridviewholder = new MyGrideViewHolder();
				convertView = LayoutInflater.from(DynamicDetialActivity.this).inflate(R.layout.item_published_grida, null);
				gridviewholder.imageview = (ImageView) convertView.findViewById(R.id.item_grida_image);
				FrameLayout.LayoutParams llLayout=(FrameLayout.LayoutParams) gridviewholder.imageview.getLayoutParams();
				DynamicFragment.setImageWH(gridviewholder.imageview, imageList.size(), (int)AndroidUtils.getDeviceWidth(DynamicDetialActivity.this), llLayout);
				convertView.setTag(gridviewholder);

			}
			gridviewholder = (MyGrideViewHolder) convertView.getTag();
			imageLoader.loadImage(imageList.get(position), gridviewholder.imageview,R.drawable.pictures_no);
			return convertView;
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public Object getItem(int position) {
			return null;
		}

		@Override
		public int getCount() {
			return imageList.size();
		}
	};
}
