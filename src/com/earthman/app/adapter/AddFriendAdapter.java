package com.earthman.app.adapter;

import java.util.ArrayList;

import android.app.Activity;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.charlie.lee.androidcommon.http.RequestManager;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.bean.FindFriendResponse.FindFriendResult;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.utils.Constants;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.BaseViewHolder;
import com.earthman.app.widget.MyToast;

/**
 * 作者：Zhou
 * 日期：2016-2-29 下午5:41:43
 * 描述：（）
 */
public class AddFriendAdapter extends BaseAdapter {

	private Activity activity;
	private String keyword;
	private ArrayList<FindFriendResult> result;
	private FindFriendResult friend;
	private UserInfoPreferences userInfoPreferences;
	private YwbImageLoader ywbImageLoader;

	public AddFriendAdapter(Activity activity) {
		this.activity = activity;
		userInfoPreferences = new UserInfoPreferences(activity);
		ywbImageLoader = new YwbImageLoader();
	}

	@Override
	public int getCount() {
		return result.size();
	}

	/**
	 * 添加数据
	 */
	public void setData(ArrayList<FindFriendResult> result, String keyword) {
		this.result = result;
		this.keyword = keyword;
	}

	@Override
	public Object getItem(int position) {
		return result.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(activity).inflate(R.layout.friend_item, null);
		}
		ImageView ivHead = BaseViewHolder.get(convertView, R.id.iv_head);
		TextView tvNickname = BaseViewHolder.get(convertView, R.id.tv_nickname);
		Button btnAddFriend = BaseViewHolder.get(convertView, R.id.btn_add);
		TextView tvAdded = BaseViewHolder.get(convertView, R.id.tv_added);

		btnAddFriend.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doGetAddFriend(position);// 添加好友
			}
		});
		friend = result.get(position);

		ywbImageLoader.loadImage(friend.getAvatar(), ivHead, R.drawable.user_avatar);
		String html = getKeyWordHtml(friend.getNice(), keyword);
		tvNickname.setText(TextUtils.isEmpty(html) ? friend.getNice() : Html.fromHtml(html));
		if (friend.getIsFrined().equals("0")) {// 非好友显示加好友
			btnAddFriend.setVisibility(View.VISIBLE);
			tvAdded.setVisibility(View.GONE);
		} else {
			btnAddFriend.setVisibility(View.GONE);
			tvAdded.setVisibility(View.VISIBLE);
		}

//		convertView.setOnClickListener(new View.OnClickListener() {
//
//			@Override
//			public void onClick(View v) {
//				Intent intent = new Intent(v.getContext(), PeronalDetialActivity.class);
//				intent.putExtra("friendsuserid", friend.getUniqueid());
//				start((Activity) v.getContext(), intent);
//			}
//
//			public void start(Activity srcActivity, Intent intent) {
//				srcActivity.startActivity(intent);
//			}
//		});
		return convertView;
	}

	// 添加好友请求
	private void doGetAddFriend(int position) {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userInfoPreferences.getUserID());
		list.add(result.get(position).getUniqueid());
		list.add(userInfoPreferences.getUserToken());
		list.add(Constants.ADD_FRIEND_TO_SOMEONE);
		String loginUrl = HttpUrlConstants.getUrl(activity, HttpUrlConstants.ADD_FRIEND, list);

		FastJsonRequest<BaseResponse> fastJsonRequest = new FastJsonRequest<BaseResponse>(Method.GET, loginUrl, BaseResponse.class,
				new Response.Listener<BaseResponse>() {
					@Override
					public void onResponse(BaseResponse response) {
						if ("000000".equals(response.getCode())) {
							MyToast.makeText(activity, R.string.add_friend2, Toast.LENGTH_LONG).show();
							activity.finish();
						} else {
							NetStatusHandUtil.getInstance().handStatus(activity, response.getCode(), response.getMessage());
						}
					}
				}, new Response.ErrorListener() {
					@Override
					public void onErrorResponse(VolleyError error) {
						MyToast.makeText(activity, R.string.error_request_inform, Toast.LENGTH_LONG).show();
					}
				});
		RequestManager.getInstance().addRequest(fastJsonRequest, activity);
	}

	private String getKeyWordHtml(String nickName, String keyword) {
		String str = "";
		if (TextUtils.isEmpty(nickName) || TextUtils.isEmpty(keyword)) {
			return str;
		}
		if (nickName.contains(keyword)) {
			int nicLength = nickName.length();
			int keyLength = keyword.length();
			ArrayList<Integer> indexs = new ArrayList<Integer>();// 关键字在字符串中的索引
			int fromIndex = 0;
			while (fromIndex < nicLength && nickName.indexOf(keyword, fromIndex) > -1) {
				int index = nickName.indexOf(keyword, fromIndex);
				indexs.add(index);
				fromIndex = index + keyword.length();
			}
			StringBuffer stringBuffer = new StringBuffer();
			int beginIndex = 0;
			for (int i = 0; i < indexs.size(); i++) {
				int index = indexs.get(i);
				if (beginIndex < index) {
					String first = nickName.subSequence(beginIndex, index).toString();
					stringBuffer.append("<font color=\"#8f8f8f\">").append(first).append("</font>");
				}
				stringBuffer.append("<font color=\"#16b7e4\">").append(keyword).append("</font>");
				if (i == indexs.size() - 1 && indexs.get(i) + keyLength < nicLength) {
					String last = nickName.substring(indexs.get(i) + keyword.length()).toString();
					stringBuffer.append("<font color=\"#8f8f8f\">").append(last).append("</font>");
				}
				beginIndex = index + keyword.length();
			}
			str = stringBuffer.toString();
		}
		return str;
	}
}
