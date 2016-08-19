package com.earthman.app.ui.activity.news;

import java.util.ArrayList;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebSettings;
import android.webkit.WebSettings.LayoutAlgorithm;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.NewsDetailModel;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.ProgressWebView;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * @Author：LiuYueMing
 * @Owner：地球人
 * @Date：2016-4-29 下午4:49:08
 * @Decription 企业新闻详情
 */
@ContentView(R.layout.news_detail_activity)
public class NewsDetailActivity extends BaseActivity {

	@ViewInject(R.id.btn_back)
	private Button mBtnBack;
	@ViewInject(R.id.tv_left)
	private TextView mTvTitleLeft;

	@ViewInject(R.id.webView)
	private ProgressWebView mWebView;
	private WebSettings mWebSettings;

	private int mNewsType;

	@Override
	protected void initData() {
		mNewsType = getIntent().getIntExtra("type", 0);
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
		mWebSettings.setDisplayZoomControls(false);// 设定缩放控件隐藏
		mWebSettings.setLayoutAlgorithm(LayoutAlgorithm.SINGLE_COLUMN);// 静止横向滚动
		mWebSettings.setDefaultTextEncodingName("UTF-8");
		// -------------------------------------------------缩放
		// mWebSettings.setSupportZoom(true);
		// mWebSettings.setBuiltInZoomControls(true);
		// --------------------------------------------------大页面自动适应屏幕
		// mWebSettings.setUseWideViewPort(true);
		// mWebSettings.setLoadWithOverviewMode(true);
		// --------------------------
		mWebView.requestFocus();// 请求焦点
		// mWebView.loadUrl("http://player.youku.com/player.php/sid/XMTQ0NTM0OTg2OA==/v.swf");
		loadNewsDetail();
	}

	@Override
	protected void setAttribute() {
		mTvTitleLeft.setText(getIntent().getStringExtra("title"));

		// Intent intent = getIntent();
		// mTvNewsTitle.setText(intent.getStringExtra("title"));
		// mTvNewsTime.setText(new
		// SimpleDateFormat("yyyy-MM-dd HH:mm").format(new
		// Date(intent.getLongExtra("time", 0))));
		// // mTvNewsFrom.setText(intent.getStringExtra("from"));
		// new YwbImageLoader().loadImage(intent.getStringExtra("img"),
		// mIvNewsImage);
		// //
		// mTvNewsContent.setText(Html.fromHtml(intent.getStringExtra("content")));
		//
		// mTvNewsContent.setText(Html.fromHtml(intent.getStringExtra("from")));
	}

	private void loadNewsDetail() {
		myLoadingDialog.show();
		UserInfoPreferences userInfo = new UserInfoPreferences(this);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfo.getUserID());
		list.add(userInfo.getUserToken());
		list.add(mNewsType);
		list.add(getIntent().getIntExtra("id", 0));
		String urlString = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_NEWS_DETAILS, list);
		executeRequest(new FastJsonRequest<NewsDetailModel>(Method.GET, urlString, NewsDetailModel.class, new Response.Listener<NewsDetailModel>() {
			@Override
			public void onResponse(NewsDetailModel response) {
				myLoadingDialog.dismiss();
				if ("000000".endsWith(response.getCode())) {
					mWebView.setDownloadListener(new DownloadListener() {
						@Override
						public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
							if (url != null && url.startsWith("http://"))
								startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
						}
					});
					
					StringBuilder sbURL = new StringBuilder("http://www.dqr2015.com/news");
					sbURL.append(mNewsType == 1 ? "_2/" : "/");
					sbURL.append(response.getResult().getId());
					
					mWebView.loadUrl(sbURL.toString());
				} else {
					NetStatusHandUtil.getInstance().handStatus(NewsDetailActivity.this, response.getCode(), response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	@OnClick(value = { R.id.btn_back })
	@Override
	protected void handclick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		}
	}

}
