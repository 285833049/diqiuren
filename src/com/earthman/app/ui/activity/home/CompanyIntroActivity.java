package com.earthman.app.ui.activity.home;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.CompanyIntroTypeAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.CompanyIntroItem;
import com.earthman.app.bean.NewsDetailModel;
import com.earthman.app.enums.CompanyIntroType;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @Author：Vinci
 * @Owner：地球人
 * @Date：2016-4-29 下午5:58:24
 * @Decription 公司介绍
 */
@ContentView(R.layout.company_intro_activity)
public class CompanyIntroActivity extends BaseActivity implements OnItemClickListener {

	@ViewInject(R.id.btn_back)
	private Button mBtnBack;
	@ViewInject(R.id.tv_left)
	private TextView mTvTitleLeft;
	@ViewInject(R.id.imageView)
	private ImageView mImageView;
	@ViewInject(R.id.webView)
	private WebView mWebView;
	private WebSettings mWebSettings;

	@ViewInject(R.id.gv_intro_type)
	private GridView mGvIntroType;
	private CompanyIntroTypeAdapter mAdapter;
	private List<CompanyIntroItem> mIntroList;
	
	

	@OnClick(value = { R.id.btn_back })
	@Override
	protected void handclick(View v) {
		if (v.getId() == R.id.btn_back) {
			finish();
		}
	}

	@Override
	protected void initData() {
		String[] introNamesArray = getResources().getStringArray(R.array.company_intro_names);
		mIntroList = new ArrayList<CompanyIntroItem>();
		mIntroList.add(new CompanyIntroItem(CompanyIntroType.INTRO, introNamesArray[0]));
		mIntroList.add(new CompanyIntroItem(CompanyIntroType.PLAN, introNamesArray[1]));
		mIntroList.add(new CompanyIntroItem(CompanyIntroType.LEADER, introNamesArray[2]));
		mIntroList.add(new CompanyIntroItem(CompanyIntroType.TEAM, introNamesArray[3]));
		mIntroList.add(new CompanyIntroItem(CompanyIntroType.VISION, introNamesArray[4]));
		mGvIntroType.setAdapter(mAdapter = new CompanyIntroTypeAdapter(mIntroList));
		mGvIntroType.setOnItemClickListener(this);
	}

	@Override
	protected void initView() {
		mWebView.getSettings().setDomStorageEnabled(true);
		mWebView.setDrawingCacheEnabled(true);
		mWebView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);// 防止因硬件加速引起的闪烁
		// -----------------------
		mWebSettings = mWebView.getSettings();
		mWebSettings.setAllowFileAccess(true);
		mWebSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
		mWebSettings.setAllowFileAccess(true);
		mWebSettings.setJavaScriptEnabled(true);
		mWebSettings.setDisplayZoomControls(false);//设定缩放控件隐藏
		mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);//静止横向滚动
		// -------------------------------------------------缩放
//		mWebSettings.setSupportZoom(true);
//		mWebSettings.setBuiltInZoomControls(true);
		// --------------------------------------------------大页面自动适应屏幕
//		mWebSettings.setUseWideViewPort(true);
//		mWebSettings.setLoadWithOverviewMode(true);
		// --------------------------
		mWebView.requestFocus();// 请求焦点
		
		loadIntro();
	}

	@Override
	protected void setAttribute() {
		mTvTitleLeft.setText(R.string.comany_introduce);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		mAdapter.setSelection(position);
		mAdapter.notifyDataSetChanged();
		loadIntro();
	}

	private void loadIntro() {
		myLoadingDialog.show();
		UserInfoPreferences userInfo = new UserInfoPreferences(this);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfo.getUserID());
		list.add(userInfo.getUserToken());
		list.add(mAdapter.getSelection()+1);
		String urlString = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_COMPANY_INTRO, list);
		executeRequest(new FastJsonRequest<NewsDetailModel>(Method.GET, urlString, NewsDetailModel.class, new Response.Listener<NewsDetailModel>() {
			@Override
			public void onResponse(NewsDetailModel response) {
				myLoadingDialog.dismiss();
				if ("000000".endsWith(response.getCode())) {
					ImageLoader.getInstance().displayImage(response.getResult().getImg(), mImageView);
					mWebView.loadDataWithBaseURL("", response.getResult().getContent(),"text/html", "UTF-8","");
				} else {
					NetStatusHandUtil.getInstance().handStatus(CompanyIntroActivity.this, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

}
