package com.earthman.app.ui.activity.circle;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Point;
import android.os.Handler;
import android.os.Message;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.DeadFlowerAdapter;
import com.earthman.app.adapter.DeadWishesAdapter;
import com.earthman.app.adapter.TributeRecordAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.DeadCategoryInfo;
import com.earthman.app.bean.DeadModel;
import com.earthman.app.listener.ActivityResultListener;
import com.earthman.app.listener.OnFlowerPayListener;
import com.earthman.app.receiver.PaySuccessReceiver;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.utils.PayFactory;
import com.earthman.app.widget.CircleImageView;
import com.earthman.app.widget.DialogPayFlower;
import com.earthman.app.widget.DrawFlowers;
import com.earthman.app.widget.HorizontalListView;
import com.earthman.app.widget.MyListView;
import com.earthman.app.widget.PayPwdDialog;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-24 下午7:14:44
 * @Decription 逝者页面
 */
@ContentView(R.layout.dead_activity)
public class DeadActivity extends BaseActivity {

	@ViewInject(R.id.btn_back)
	private Button mBtnBack;
 	@ViewInject(R.id.tv_title)
	private TextView mTvTitle;
	@ViewInject(R.id.sv_parent_layout)
	private ScrollView mSvParentLayout;
	@ViewInject(R.id.drawFlowers)
	private DrawFlowers mDrawFlowers;
	@ViewInject(R.id.iv_head)
	private CircleImageView mIvHead;
	// --------------------------------------------------------//写寄语
	@ViewInject(R.id.rl_add_wishes)
	private RelativeLayout mRlAddWishes;
	// --------------------------------------------------------//祭祀商铺列表
	private List<DeadCategoryInfo> mCategoryList;
	private DeadCategoryInfo mCurrCategoryInfo;
	@ViewInject(R.id.lv_flowers)
	private HorizontalListView mLvFlower;
	private DeadFlowerAdapter mFlowerAdapter;
	// --------------------------------------------------------//敬献记录详情
	@ViewInject(R.id.rl_tribute_record)
	private RelativeLayout mRlTributeRecord;// 敬献记录详情
	// --------------------------------------------------------//敬献记录列表
	@ViewInject(R.id.lv_tribute_record)
	private MyListView mLvTributeRecord;
	private TributeRecordAdapter mTributeRecordAdapter;
	// --------------------------------------------------------//寄语详情
	@ViewInject(R.id.rl_wishes)
	private RelativeLayout mRlWishes;
	// --------------------------------------------------------//寄语列表
	@ViewInject(R.id.lv_wishes)
	private MyListView mLvWishes;
	private DeadWishesAdapter mWishessAdapter;
	// ---------------------------
	private PaySuccessReceiver mPaySuccessReceiver;
	private ActivityResultListener mListener;// 支付类型回调

	private String friendsuserid;

	@Override
	protected void handclick(View v) {
		Intent intent = getIntent();
		switch (v.getId()) {
		case R.id.rl_add_wishes:
			startActivity(intent.setClass(this, AddWishesksActivity.class));//写寄语
			break;
		case R.id.rl_tribute_record:
			startActivity(intent.setClass(this, TributeRecordActivity.class));// 敬献记录
			break;
		case R.id.rl_wishes:
			startActivity(intent.setClass(this, WishesActivity.class));// 寄语
			break;
		}
	}

	
	@Override
	protected void initData() {
		friendsuserid = getIntent().getStringExtra("friendsuserid");
		mPaySuccessReceiver = new PaySuccessReceiver();
		mPaySuccessReceiver.setHandler(handler);
		registerReceiver(mPaySuccessReceiver, new IntentFilter(Constants.PAY_SUCCESS_ACTION));
		// ----------------------
		getDeadDate();
	}

	private Handler handler = new Handler() {
		public void handleMessage(Message msg) {
			if (msg.what > 0) {// 支付成功
				getDeadDate();// 刷新页面
			}
		};
	};

	@OnClick(R.id.btn_back)
	private void onBackClick(View v) {
		this.finish();
	}

	@Override
	protected void initView() {
		ViewUtils.inject(this);

		mRlAddWishes.setOnClickListener(this);
		mRlTributeRecord.setOnClickListener(this);
		mRlWishes.setOnClickListener(this);
		// -------------------------
		mLvFlower.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int position, long id) {

				if (0 == new UserInfoPreferences(DeadActivity.this).getIsSetPwd()) {
					showSetPayPwdAlert();
				} else {
					mCurrCategoryInfo = mCategoryList.get(position);
					DialogPayFlower dialogPayFlower = new DialogPayFlower(DeadActivity.this, mCategoryList.get(position), new OnFlowerPayListener() {
						@Override
						public void onSurePayClick(int payType, String password, int flowerNum, String flowerTotalMonay) {
							new PayFactory(DeadActivity.this, PayFactory.CASE_JINGXIAN, payType, flowerTotalMonay).genJingxuanRequest(friendsuserid, flowerNum,
									String.valueOf(mCurrCategoryInfo.getId()), password);
						}
					});
					mListener = dialogPayFlower;
					dialogPayFlower.show();
				}
			}
		});
		// -------------------------测试
		// ArrayList<Integer> flowerList = new ArrayList<Integer>();
		// flowerList.add(1);
		// flowerList.add(1);
		// flowerList.add(1);
		// flowerList.add(1);
		// flowerList.add(1);
		// flowerList.add(1);
		// flowerList.add(1);
		// flowerList.add(1);
		// flowerList.add(1);
		// flowerList.add(1);
		// flowerList.add(1);
		// flowerList.add(1);
		// flowerList.add(1);
		// flowerList.add(1);
		// DisplayMetrics dm = new DisplayMetrics();
		// getWindowManager().getDefaultDisplay().getMetrics(dm);
		// //---- ----------------------
		// mDrawFlowers.setFlowersList(new Point((dm.widthPixels -
		// DrawFlowers.dip2px(this, 60)) / 2, 100), flowerList);

	}

	private void getDeadDate() {
		myLoadingDialog.show();
		UserInfoPreferences userinfo = new UserInfoPreferences(this);
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userinfo.getUserID());
		list.add(userinfo.getUserToken());
		list.add(friendsuserid);
		String url = HttpUrlConstants.getUrl(this, HttpUrlConstants.GET_DEAD_INFO, list);
		executeRequest(new FastJsonRequest<DeadModel>(Method.GET, url, DeadModel.class, new Response.Listener<DeadModel>() {

			@Override
			public void onResponse(DeadModel response) {
				myLoadingDialog.dismiss();
				if ("000000".endsWith(response.getCode())) {
					DisplayMetrics dm = new DisplayMetrics();
					getWindowManager().getDefaultDisplay().getMetrics(dm);
					mDrawFlowers.setFlowersList(new Point((dm.widthPixels - DrawFlowers.dip2px(DeadActivity.this, 60)) / 2, 100), response.getResult()
							.getSurroundList());
					mTvTitle.setText(response.getResult().getDeadInfoBase().getNice());
					ImageLoader.getInstance().displayImage(response.getResult().getDeadInfoBase().getAvatar(), mIvHead);
					// -------------------------------
					mLvFlower.setAdapter(mFlowerAdapter = new DeadFlowerAdapter(mCategoryList = response.getResult().getCategoryList()));
					mLvTributeRecord.setAdapter(mTributeRecordAdapter = new TributeRecordAdapter(response.getResult().getFewPresented()));
					mLvWishes.setAdapter(mWishessAdapter = new DeadWishesAdapter(response.getResult().getFewWishes()));
					// -------------------------------置顶
					mSvParentLayout.post(new Runnable() {
						public void run() {
							mSvParentLayout.scrollTo(0, 0);
						}
					});
				} else {
					NetStatusHandUtil.getInstance().handStatus(DeadActivity.this, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	@Override
	protected void setAttribute() {
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == Activity.RESULT_OK) {
			switch (requestCode) {
			case PayPwdDialog.CHANGE_PAYTYPE:
				if (mListener != null) {
					mListener.onActivityResult(requestCode, resultCode, data);
				}
				break;
			}
		}
	}
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mPaySuccessReceiver);
		
	}
}
