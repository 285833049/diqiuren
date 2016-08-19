package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;

import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.TransactionListAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.BillListResponse;
import com.earthman.app.bean.BillListResponse.BillResult;
import com.earthman.app.pulltoreflesh.XListView;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;

/**
 * 交易记录
 * 
 * @author xiexianyong
 * @Date2016-03-06
 * 
 */
@ContentView(R.layout.transaction_record_list)
public class TransactionRecordListActivity extends BaseActivity {
	@ViewInject(R.id.jy_list)
	private XListView jy_list;
	private TransactionListAdapter adapter;
	@ViewInject(R.id.btn_back)
	private Button btn_back;
	@ViewInject(R.id.tv_left)
	private TextView tv_left;

	@Override
	protected void handclick(View v) {

	}

	@Override
	protected void initData() {
		adapter = new TransactionListAdapter(this);
		doGetQueryBill();
	}

	/**
	 * doGetQueryBill(查询最近交易记录)
	 * void
	 * @exception
	 */
	private void doGetQueryBill() {
		ArrayList<Object>list=new ArrayList<Object>();
        list.add(preferences.getUserID());
        list.add(preferences.getUserToken());
        String loginUrl = HttpUrlConstants.getUrl(this, HttpUrlConstants.QUERY_BILL, list);
        executeRequest(new FastJsonRequest<BillListResponse>(Method.GET, loginUrl, BillListResponse.class, new Response.Listener<BillListResponse>(){
            @Override
            public void onResponse(BillListResponse response) {
                if ("000000".equals(response.getCode())) {
                    //登陆成功,保存数据
                	 ArrayList<BillResult> result = response.getResult();
					adapter.setData(result);
                	jy_list.setAdapter(adapter);
                }else {
                	NetStatusHandUtil.getInstance().handStatus(TransactionRecordListActivity.this, response.getCode(),response.getMessage());                  
                }
            }},getErrorListener()));
	}

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);
		jy_list.setAutoLoadEnable(false);
		jy_list.setPullLoadEnable(false);
		jy_list.setPullRefreshEnable(false);

	}

	@Override
	protected void setAttribute() {
		tv_left.setText(R.string.transaction_record);
	}

}
