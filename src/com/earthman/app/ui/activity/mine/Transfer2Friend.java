package com.earthman.app.ui.activity.mine;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.MyFriendListAdapter;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.bean.FriendList;
import com.earthman.app.bean.FriendList.FriendListResult;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.sortListView.CharacterParser;
import com.earthman.app.widget.sortListView.ClearEditText;
import com.earthman.app.widget.sortListView.PinyinComparator;
import com.earthman.app.widget.sortListView.SideBar;
import com.earthman.app.widget.sortListView.SideBar.OnTouchingLetterChangedListener;
import com.earthman.app.widget.sortListView.SortModel;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ContentView;
import com.lidroid.xutils.view.annotation.ViewInject;

/**
 * @Title: ZhuanZhuang2FriendActivity
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月7日
 */
@ContentView(R.layout.zhuanzhuang2friend)
public class Transfer2Friend extends BaseActivity {
	// -----------title-------------------------
	@ViewInject(R.id.tv_left)
	TextView mTvTitleLeft;
	@ViewInject(R.id.btn_back)
	Button mBtnTitleLeft;
	@ViewInject(R.id.btn_right)
	Button mBtnTitleRight;
	private ArrayList<FriendListResult> friendlist;
	private SideBar sideBar;
	private TextView dialog;
	private MyFriendListAdapter adapter;

	@Override
	protected void handclick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;

		default:
			break;
		}
	}

	@Override
	protected void initData() {

	}

	@SuppressLint("InflateParams")
	@Override
	protected void initView() {
		// TODO Auto-generated method stub
		ViewUtils.inject(this);
		mTvTitleLeft.setText(getString(R.string.zhuanzhang2friendtitle));
		mBtnTitleLeft.setOnClickListener(this);
		mLvSort = (ListView) findViewById(R.id.friend_list_1);
		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);
		doGetFriendList();
	}

	@Override
	protected void setAttribute() {

	}

	/**
	 * 请求好友列表
	 */
	private void doGetFriendList() {
		// /{userid}/{token}
		showLoading();
		ArrayList<Object> list = new ArrayList<Object>();

		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		String getUrl = HttpUrlConstants.getUrl(Transfer2Friend.this, HttpUrlConstants.FRIEND_LIST, list);
		LogUtil.i("获取到好友列表", getUrl);
		executeRequest(new FastJsonRequest<FriendList>(Method.GET, getUrl, FriendList.class, new Response.Listener<FriendList>() {
			@Override
			public void onResponse(FriendList response) {
				dismissLoading();
				if ("000000".equals(response.getCode())) {
					LogUtil.i("获取到好友列表", response.toString());
					friendlist = response.getResult();
					initViews();
				} else {
					NetStatusHandUtil.getInstance().handStatus(Transfer2Friend.this, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	/**
	 * 汉字转换成拼音的类
	 */
	private CharacterParser characterParser;
	private List<SortModel> SourceDateList;

	/**
	 * 根据拼音来排列ListView里面的数据类
	 */
	private PinyinComparator pinyinComparator;
	private ListView mLvSort;

	/**
	 * 
	 * initViews 初始化排序的相关参数
	 * @param convertView
	 * void
	 * @exception
	 */
	private void initViews() {
		// 实例化汉字转拼音类
		characterParser = CharacterParser.getInstance();

		pinyinComparator = new PinyinComparator();

		sideBar = (SideBar) findViewById(R.id.sidrbar);
		dialog = (TextView) findViewById(R.id.dialog);
		sideBar.setTextView(dialog);

		// 设置右侧触摸监听
		sideBar.setOnTouchingLetterChangedListener(new OnTouchingLetterChangedListener() {

			@Override
			public void onTouchingLetterChanged(String s) {
				// 该字母首次出现的位置
				int position = adapter.getPositionForSection(s.charAt(0));
				if (position != -1) {
					mLvSort.setSelection(position);
				}

			}
		});

		mLvSort.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				Intent zhuanzhangintent = new Intent(Transfer2Friend.this, TransferMoneyActivity.class);
//				zhuanzhangintent.putExtra("otherAccount", ((SortModel)adapter.getItem(position)).getCardId();
//				zhuanzhangintent.putExtra("userid", ((SortModel) adapter.getItem(position)).getUserid());
				zhuanzhangintent.putExtra("otherAccount", ((SortModel) adapter.getItem(position)).getCardId());
//				zhuanzhangintent.putExtra("icon", ((SortModel) adapter.getItem(position)).getPahonecode());
//				zhuanzhangintent.putExtra("nick", ((SortModel) adapter.getItem(position)).getName());
				zhuanzhangintent.putExtra("pageType", "0");// 0-->好友列表选择
				startActivity(zhuanzhangintent);
				finish();
			}
		});

		if (friendlist != null) {
			SourceDateList = filledData(friendlist);
		}

		// 根据a-z进行排序源数据
		Collections.sort(SourceDateList, pinyinComparator);
		adapter = new MyFriendListAdapter(Transfer2Friend.this, SourceDateList);
		mLvSort.setAdapter(adapter);

		ClearEditText mClearEditText = (ClearEditText) findViewById(R.id.filter_edit);

		// 根据输入框输入值的改变来过滤搜索
		mClearEditText.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
				// 当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
				filterData(s.toString());
			}

			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {

			}

			@Override
			public void afterTextChanged(Editable s) {
			}
		});
	}

	/**
	 * 为ListView填充数据
	 * 
	 * @param date
	 * @return
	 */
	private List<SortModel> filledData(ArrayList<FriendListResult> date) {
		List<SortModel> mSortList = new ArrayList<SortModel>();

		for (int i = 0; i < date.size(); i++) {
			SortModel sortModel = new SortModel();
			sortModel.setName(date.get(i).getNice());
			sortModel.setPahonecode(date.get(i).getAvatar());
			sortModel.setUserid(date.get(i).getId());
			sortModel.setCardId(date.get(i).getCardId());
			String remarks = date.get(i).getRemarks();
			sortModel.setRemarks(remarks);
			String name = null;
			if(!TextUtils.isEmpty(remarks)){
				name = remarks;
			}else{
				name = date.get(i).getNice();
			}
			sortModel.setName(name);
			// 汉字转换成拼音
			String pinyin = characterParser.getSelling(name);
			String sortString = pinyin.substring(0, 1).toUpperCase();

			// 正则表达式，判断首字母是否是英文字母
			if (sortString.matches("[A-Z]")) {
				sortModel.setSortLetters(sortString.toUpperCase());
			} else {
				sortModel.setSortLetters("#");
			}

			mSortList.add(sortModel);
		}
		return mSortList;
	}

	/**
	 * 根据输入框中的值来过滤数据并更新ListView
	 * 
	 * @param filterStr
	 */
	private void filterData(String filterStr) {
		List<SortModel> filterDateList = new ArrayList<SortModel>();

		if (TextUtils.isEmpty(filterStr)) {
			filterDateList = SourceDateList;
		} else {
			filterDateList.clear();
			for (SortModel sortModel : SourceDateList) {
				String name = sortModel.getName();
				if (name.indexOf(filterStr.toString()) != -1 || characterParser.getSelling(name).startsWith(filterStr.toString())) {
					filterDateList.add(sortModel);
				}
			}
		}

		// 根据a-z进行排序
		Collections.sort(filterDateList, pinyinComparator);
		adapter.updateListView(filterDateList);
	}

}
