package com.earthman.app.ui.activity.friend;

import java.util.ArrayList;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.QuanziListResponse;
import com.earthman.app.bean.QuanziListResponse.quanziLiseResult;
import com.earthman.app.pulltoreflesh.XListView;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月24日 上午10:24:20
 * @Decription
 */
@ContentView(R.layout.activity_choise_quanzi)
public class QuanziChoiseActivity extends BaseActivity {
	@ViewInject(R.id.quanzi_list)
	XListView mCicleLv;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	private MyListQuanziAdapter myListQuanziAdapter;
	private ArrayList<quanziLiseResult> result;
	@Override
	protected void handclick(View v) {
		if (v.getId() == R.id.btn_back) {
			finish();
		}
	}

	/*
	 * @see com.earthman.app.base.BaseActivity#initData()
	 */
	@Override
	protected void initData() {

	}

	/**
	 * doGetQuanZiList(获取圈子列表)
	 * void
	 * @exception
	 */
	private void doGetQuanZiList() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.MY_GROUPS, list);
		executeRequest(new FastJsonRequest<QuanziListResponse>(Method.GET, loginUrl, QuanziListResponse.class, new Response.Listener<QuanziListResponse>() {

			@Override
			public void onResponse(QuanziListResponse response) {
				if ("000000".equals(response.getCode())) {
					result = response.getResult();
					myListQuanziAdapter = new MyListQuanziAdapter(QuanziChoiseActivity.this, result);
					mCicleLv.setAdapter(myListQuanziAdapter);
				} else {
					NetStatusHandUtil.getInstance().handStatus(QuanziChoiseActivity.this, response.getCode(),response.getMessage());					
				}
			}
		}, getErrorListener()));
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		mBtnTitleLeft.setOnClickListener(this);
		mTvTitleLeft.setText(R.string.quanzichoise);
		mCicleLv.setPullLoadEnable(false);
		mCicleLv.setPullRefreshEnable(false);

		doGetQuanZiList();
		mCicleLv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent putExtra = new Intent();
				putExtra.putExtra("quanzitype", result.get(position - 1).getName());
				putExtra.putExtra("quanziid", result.get(position - 1).getId());
				setResult(0x0012, putExtra);
				QuanziChoiseActivity.this.finish();

			}
		});
	}

	private class MyListQuanziAdapter extends BaseAdapter {
		private ArrayList<quanziLiseResult> result;
		private Context context;
		private TextView tv1;

		public MyListQuanziAdapter(Context context, ArrayList<quanziLiseResult> result) {
			this.context = context;
			this.result = result;
		}

		/*
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount() {
			return result.size();
		}

		/*
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public Object getItem(int position) {
			return null;
		}

		/*
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int position) {
			return 0;
		}

		/*
		 * @see android.widget.Adapter#getView(int, android.view.View,
		 * android.view.ViewGroup)
		 */
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if (convertView == null) {
				convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_1, null);
				tv1 = (TextView) convertView.findViewById(android.R.id.text1);
				tv1.setTextSize(13);
			}
			tv1.setText(result.get(position).getName());
			return convertView;
		}

	}

	@Override
	protected void setAttribute() {
	}

}
