package com.earthman.app.adapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
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
import com.charlie.lee.androidcommon.http.RequestManager;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.BaseResponse;
import com.earthman.app.bean.CommentResponse.CommentResult;
import com.earthman.app.bean.DynamicResponse;
import com.earthman.app.bean.DynamicResponse.DynamicContent;
import com.earthman.app.bean.DynamicResponse.DynamicContent.CommentContent;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.listener.CommonDialogListener;
import com.earthman.app.nim.location.activity.AMapActivity;
import com.earthman.app.ui.activity.dynamic.DynamicTransferActivity;
import com.earthman.app.ui.activity.dynamic.ImageViewPage;
import com.earthman.app.ui.activity.dynamic.InPutActivity;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.earthman.app.ui.fragment.main.DynamicFragment;
import com.earthman.app.utils.AndroidUtils;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.DialogCommon;
import com.earthman.app.widget.MyGridView;
import com.earthman.app.widget.MyToast;

import de.greenrobot.event.EventBus;

/**
 * @Title: MyDynamicAdapter
 * @Description:
 * @Company: 地球人
 * @author ERIC
 * @date 2016年3月13日
 */

public class MyDynamicAdapter extends BaseAdapter implements OnItemClickListener {

	private Context context;
	private ArrayList<DynamicContent> dynamicList;
	private int index;
	private int commentindex;
	private int commentindex2;
	private ListView listView;
	private int mFirstItem;// 保存第一个可见条目的position
	private int mFirstItemTop;// 保存第一个可见条目的顶部位置
	private boolean isDown;// 是否向下滑动

	public MyDynamicAdapter(Context context, ListView listView, ArrayList<DynamicContent> dynamicList) {
		this.context = context;
		this.listView = listView;
		this.dynamicList = dynamicList;
		infoPreferences = new UserInfoPreferences(context);
		imageLoader = new YwbImageLoader();
		EventBus.getDefault().register(this);
		// listview进入动画
		animation = AnimationUtils.loadAnimation(context, R.anim.listview_item_bottom_in_animation);
		listView.setOnScrollListener(myLVScrollListener);
	}

	@Override
	public int getCount() {
		return dynamicList.size();
	}

	// listview滚动监听，添加item的进场动画
	private OnScrollListener myLVScrollListener = new OnScrollListener() {
		@Override
		public void onScrollStateChanged(AbsListView view, int scrollState) {

		}

		@Override
		public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
			View itemView = view.getChildAt(0);
			if (itemView == null) {
				return;
			}
			int top = itemView.getTop();
			isDown = firstVisibleItem > mFirstItem || mFirstItemTop > top;
			mFirstItem = firstVisibleItem;
			mFirstItemTop = top;
		}
	};

	// 评论后的刷新
	public void onEvent(CommentResult event) {
		if ("1".equals(event.getType())) {
			CommentContent commentdetial = new DynamicResponse().new DynamicContent().new CommentContent();
			commentdetial.setReplyNice(infoPreferences.getUsernice());
			commentdetial.setContent(event.getContent());
			dynamicList.get(index).getArticlesComments().add(dynamicList.get(index).getArticlesComments().size(), commentdetial);
			myCommentAdapter.notifyDataSetChanged();
		} else if ("2".equals(event.getType())) {
			CommentContent commentdetial = new DynamicResponse().new DynamicContent().new CommentContent();
			commentdetial.setReplyNice(infoPreferences.getUsernice());
			commentdetial.setContent(event.getContent());
			commentdetial.setPReplyNice(dynamicList.get(commentindex2).getArticlesComments().get(event.getPosition()).getReplyNice());
			dynamicList.get(commentindex2).getArticlesComments().add(event.getPosition() + 1, commentdetial);
			myCommentAdapter.notifyDataSetChanged();
		}
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		return dynamicList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@SuppressLint("NewApi")
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		LogUtil.i("getview", "getView" + position);
		final DynamicContent dynamicContent = dynamicList.get(position);
		ViewHolder viewholder = null;
		if (convertView == null) {
			viewholder = new ViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.dynamic_items, null);
			viewholder.mGvImags = (MyGridView) convertView.findViewById(R.id.dynamic_images);
			viewholder.mTvZanNum = (TextView) convertView.findViewById(R.id.like_dianzan);
			viewholder.mLlZan = (LinearLayout) convertView.findViewById(R.id.ll_priser);
			viewholder.mTvZanNum = (TextView) convertView.findViewById(R.id.like_dianzan);
			viewholder.zhuanfa = (TextView) convertView.findViewById(R.id.zhuanfa);
			viewholder.mTvCommuntNum = (TextView) convertView.findViewById(R.id.communt);
			viewholder.lv_comment = (ListView) convertView.findViewById(R.id.lv_comment);
			viewholder.mIvUserIcon = (ImageView) convertView.findViewById(R.id.user_icon);
			viewholder.mIvPriser = (ImageView) convertView.findViewById(R.id.iv_priser);
			viewholder.mTvUserNick = (TextView) convertView.findViewById(R.id.user_nick);
			viewholder.mTvDynamicContent = (TextView) convertView.findViewById(R.id.dynamic_content);
			viewholder.mTvDynamicDelete = (TextView) convertView.findViewById(R.id.dynamic_delete);
			viewholder.mTvDynamicCreadTime = (TextView) convertView.findViewById(R.id.creadtime);
			viewholder.mTvLoction = (TextView) convertView.findViewById(R.id.tv_location);
			viewholder.rl_transfer = (RelativeLayout) convertView.findViewById(R.id.rl_transfer);
			viewholder.tv_transfer_nickname = (TextView) convertView.findViewById(R.id.tv_transfer_nickname);
			viewholder.tv_transfer_content = (TextView) convertView.findViewById(R.id.tv_transfer_content);
			viewholder.transfer_dynamic_images = (GridView) convertView.findViewById(R.id.transfer_dynamic_images);
			convertView.setTag(viewholder);
		} else {
			viewholder = (ViewHolder) convertView.getTag();
			
			//防止重用时显示重复数据所以这里将不是每次都赋值的控件   全部重新初始化为null
			viewholder.mGvImags.setAdapter(null);
			viewholder.mTvZanNum.setText(null);
			//viewholder.mLlZan = (LinearLayout) convertView.findViewById(R.id.ll_priser);
			//viewholder.zhuanfa.setText(null);
			//viewholder.mTvCommuntNum.setText(null);
			viewholder.lv_comment.setAdapter(null);
			viewholder.mIvPriser.setImageDrawable(null);
			//viewholder.mTvDynamicDelete.setText(null);
			viewholder.mTvLoction.setText(null);
			//viewholder.rl_transfer = (RelativeLayout) convertView.findViewById(R.id.rl_transfer);
			viewholder.tv_transfer_nickname.setText(null);
			viewholder.tv_transfer_content.setText(null);
			viewholder.transfer_dynamic_images.setAdapter(null);
		}
		// 清除当前显示区域中所有item的动画
		for (int i = 0; i < listView.getChildCount(); i++) {
			View view = listView.getChildAt(i);
			view.clearAnimation();
		}
		// 然后给当前item添加上动画
		if (isDown) {
			convertView.startAnimation(animation);
		}

		
		
		// 设置动态的数据
		imageLoader.loadImage(dynamicContent.getAvatar(), viewholder.mIvUserIcon, R.drawable.user_avatar);// 设置头像
		//点击头像跳转到个人详情页面
		viewholder.mIvUserIcon.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				PeronalDetialActivity.startSee(context, dynamicContent.getUserId());
			}
		});
		if(!TextUtils.isEmpty(dynamicContent.getRemarks())){
			viewholder.mTvUserNick.setText(dynamicContent.getRemarks());// 设置昵称
		}else{
			viewholder.mTvUserNick.setText(dynamicContent.getNice());// 设置昵称
		}		
		viewholder.mTvDynamicContent.setText(dynamicContent.getTitle());// 设置动态的内容
		viewholder.mTvDynamicCreadTime.setText(dynamicContent.getCreatedAt());// 设置创建时间

		if(TextUtils.isEmpty(dynamicContent.getLocation())){//不显示位置
			viewholder.mTvLoction.setVisibility(View.GONE);
		}else{
			viewholder.mTvLoction.setVisibility(View.VISIBLE);
			viewholder.mTvLoction.setText(dynamicContent.getLocation());
			if(dynamicContent.getLocation().contains(".")){
				viewholder.mTvLoction.setTextColor(context.getResources().getColor(R.color.tab_text_hover));
			}else{
				viewholder.mTvLoction.setTextColor(context.getResources().getColor(R.color.black3));
			}
		}
		viewholder.mTvLoction.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {				
				if(dynamicContent.getLocation().contains(".")){
					Intent intent = new Intent(context, AMapActivity.class);//#
					intent.putExtra("latitude", dynamicContent.getLatitude());
					intent.putExtra("longitude", dynamicContent.getLongitude());
					intent.putExtra("address", dynamicContent.getLocation());
					context.startActivity(intent);
				}
				
			}
		});
		if(dynamicContent.getArticlesBase() != null){//当前动态是转发过来的动态
			viewholder.rl_transfer.setVisibility(View.VISIBLE);
			String remarks=dynamicContent.getArticlesBase().getRemarks();
			String nice=dynamicContent.getArticlesBase().getNice();
			String title=dynamicContent.getArticlesBase().getTitle();
			//如果remarks 和nice同时为null 且title包含已被删除 则此转发动态被删除
			if(TextUtils.isEmpty(remarks)&&TextUtils.isEmpty(nice))
			{
				//转发动态被删除 只显示在自己的动态中好友动态中不再显示
				//convertView.setVisibility(dynamicContent.getUserId().equals(infoPreferences.getUserID()) ? View.VISIBLE : View.GONE);
				String tvContent="<font color=\"#FF0000\">"+title.replace("[del.此", "").replace(" .del]", "")+"</font>";
				viewholder.tv_transfer_content.setText(Html.fromHtml(tvContent));//设置内容
			}else{
				
				if(!TextUtils.isEmpty(remarks)){
					viewholder.tv_transfer_nickname.setText(remarks+":");// 设置昵称
				}else{
					viewholder.tv_transfer_nickname.setText(nice+":");// 设置昵称
				}
				if(!TextUtils.isEmpty(title)){
					viewholder.tv_transfer_content.setText(title);//设置内容
				}else{
					viewholder.tv_transfer_content.setText(R.string.publish_mood);//设置内容
				}
				//点击转发昵称跳转到个人详情页面
				viewholder.tv_transfer_nickname.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						PeronalDetialActivity.startSee(context,String.valueOf(dynamicContent.getArticlesBase().getUserId()));
					}
				});
				
				
				ArrayList<String> imageList = new ArrayList<String>();
				final String imgs = dynamicContent.getArticlesBase().getImgs();
				if (!TextUtils.isEmpty(imgs) && imgs.contains(",")) {
					imageList.addAll(Arrays.asList(imgs.split(",")));
				} else if (!TextUtils.isEmpty(imgs) && !imgs.contains(",")) {
					imageList.add(imgs);
				} 
				if (imageList.size() == 0) {
					viewholder.transfer_dynamic_images.setVisibility(View.GONE);
				} else {
					viewholder.transfer_dynamic_images.setVisibility(View.VISIBLE);
					/*switch (imageList.size()) {
					case 1:
					case 2:
						viewholder.transfer_dynamic_images.setNumColumns(2);
						break;
					default:
						viewholder.transfer_dynamic_images.setNumColumns(3);
						break;
					}*/
					DynamicFragment.setNumColumns(viewholder.transfer_dynamic_images, imageList.size());
	
					viewholder.transfer_dynamic_images.setSelector(new ColorDrawable(Color.TRANSPARENT));// 取消GridView中Item选中时默认的背景色
					viewholder.transfer_dynamic_images.setAdapter(new MyPicGrideAdapter(context, imageList));
					viewholder.transfer_dynamic_images.setOnItemClickListener(new OnItemClickListener() {
						@Override
						public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
							Intent intent = new Intent(context, ImageViewPage.class);
							intent.putExtra("pics", imgs);
							intent.putExtra("position", position);
							context.startActivity(intent);
						}
					});
				 }
			}
		}else{//当前动态是自己发的动态
			viewholder.rl_transfer.setVisibility(View.GONE);
			ArrayList<String> imageList;
			if (dynamicContent.getImgs().length() != 0 && dynamicContent.getImgs().contains(",")) {
				imageList = new ArrayList<String>(Arrays.asList(dynamicContent.getImgs().split(",")));
			} else if (dynamicContent.getImgs().length() != 0 && !dynamicContent.getImgs().contains(",")) {
				imageList = new ArrayList<String>();
				imageList.add(dynamicContent.getImgs());
			} else {
				imageList = new ArrayList<String>();
			}
			if (imageList.size() == 0) {
				viewholder.mGvImags.setVisibility(View.GONE);
			} else {
				viewholder.mGvImags.setVisibility(View.VISIBLE);
				/*switch (imageList.size()) {
				case 1:
				case 2:
					viewholder.mGvImags.setNumColumns(2);
					break;
				default:
					viewholder.mGvImags.setNumColumns(3);
					break;
				}*/
				DynamicFragment.setNumColumns(viewholder.mGvImags, imageList.size());
				viewholder.mGvImags.setSelector(new ColorDrawable(Color.TRANSPARENT));// 取消GridView中Item选中时默认的背景色
				viewholder.mGvImags.setAdapter(new MyPicGrideAdapter(context, imageList));
				viewholder.mGvImags.setOnItemClickListener(new OnItemClickListener() {
					@Override
					public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
						Intent intent = new Intent(context, ImageViewPage.class);
						intent.putExtra("pics", dynamicContent.getImgs());
						intent.putExtra("position", position);
						context.startActivity(intent);
					}
				});
			}
		}
	
		
		// 点赞功能实现
		// Drawable redDrawable =
		// context.getResources().getDrawable(R.drawable.dianzanh);
		// Drawable grayDrawable =
		// context.getResources().getDrawable(R.drawable.dianzan);
		if ("0".equals(dynamicContent.getHasPraised())) {
			viewholder.mIvPriser.setBackgroundResource(R.drawable.dianzan);
		} else if ("1".equals(dynamicContent.getHasPraised())) {
			viewholder.mIvPriser.setBackgroundResource(R.drawable.dianzanh);
		}
		viewholder.mTvZanNum.setText(dynamicContent.getPraiseAmount() + "");// 设置点赞数量
		// 点赞的逻辑实现
		viewholder.mLlZan.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				doPostPraise(dynamicContent.getHasPraised(), dynamicContent.getId(), dynamicContent.getUserId());
				dynamicContent.setPraiseAmount(dynamicContent.getHasPraised().equals("0") ? dynamicContent.getPraiseAmount() + 1 : dynamicContent.getPraiseAmount() - 1);
				dynamicContent.setHasPraised(dynamicContent.getHasPraised().equals("0") ? "1" : "0");
				notifyDataSetChanged();
			}
		});
		// 转发功能实现
		viewholder.zhuanfa.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//MyToast.makeText(context, "转发功能在实现中", Toast.LENGTH_SHORT).show();
				Intent intent = new Intent(context, DynamicTransferActivity.class);
				intent.putExtra("dynamicContent", dynamicContent);
				context.startActivity(intent);
			}
		});
		// 评论的数目
		viewholder.mTvCommuntNum.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// EventBus.getDefault().post(new DynamicTypeBean("",position));
				// post方式提交
				// 包含参数：
				// articleId:动态id
				// replyId:被回复人id
				// content:回复的内容
				// parentId:被回复的回复id 恢复记录的id（如果是回复发布动态的人，则不传值或传null）
				// authorId:动态发布人id
				index = position;
				Intent intent = new Intent(context, InPutActivity.class);
				intent.putExtra("articleId", dynamicContent.getId());
				intent.putExtra("authorId", dynamicContent.getUserId());
				intent.putExtra("type", "1");
				context.startActivity(intent);
			}
		});
		viewholder.lv_comment.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
				if (infoPreferences.getUserID().equals(dynamicContent.getArticlesComments().get(position).getReplyUserId())) {
					new DialogCommon(context, context.getString(R.string.wenxin_inform), context.getString(R.string.delete_comment), "", new CommonDialogListener() {

						@Override
						public void onRightButtonClick() {
							// 删除此条评论
							MyToast.makeText(context, R.string.develop_inform, Toast.LENGTH_SHORT).show();
						}
					}).show();
				}
				return true;
			}
		});
		// 评论的显示
		myCommentAdapter = new MyCommentAdapter(context, dynamicContent.getArticlesComments());
		viewholder.lv_comment.setAdapter(myCommentAdapter);
		viewholder.lv_comment.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position1, long id) {
				commentindex = position1;
				commentindex2 = position;
				Intent intent = new Intent(context, InPutActivity.class);
				intent.putExtra("articleId", dynamicContent.getId());
				intent.putExtra("authorId", dynamicContent.getUserId());
				intent.putExtra("replyId", dynamicContent.getArticlesComments().get(position1).getReplyUserId());
				intent.putExtra("parentId", dynamicContent.getArticlesComments().get(position1).getCommentId());
				intent.putExtra("type", "2");
				intent.putExtra("position", position1);
				context.startActivity(intent);
			}
		});
		// 删除动态的操作
		viewholder.mTvDynamicDelete.setVisibility(dynamicContent.getUserId().equals(infoPreferences.getUserID()) ? View.VISIBLE : View.GONE);
		//		viewholder.mTvDynamicDelete.setVisibility(View.VISIBLE);
		viewholder.mTvDynamicDelete.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// 删除操作
				new DialogCommon(context, context.getString(R.string.wenxin_inform), context.getString(R.string.isdeletedynamic), "", new CommonDialogListener() {
					@Override
					public void onRightButtonClick() {
						dynamicList.remove(dynamicContent);
						doGetDeleteDynamic(dynamicContent.getId());
					}
				}).show();
			}
		});
		
		
		
		return convertView;
	}

	// 点赞请求
	private void doPostPraise(String type, String articleId, String userId) {
		if(!AndroidUtils.isNetworkAvailable(context)){
			return;
		}
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(infoPreferences.getUserID());
		list.add(infoPreferences.getUserToken());
		list.add(type);
		String loginUrl = HttpUrlConstants.getUrl(context, HttpUrlConstants.PRAISE_DYNAMIC, list);
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
					NetStatusHandUtil.getInstance().handStatus(context, response2.getCode(), response2.getMessage());
				}
			}
		};
		JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Method.POST, loginUrl, jsonRequest, listener, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				LogUtil.e("网络请求错误", error.toString());
				MyToast.makeText(context, R.string.server_error, Toast.LENGTH_LONG).show();
			}
		}) {
			@Override
			public Map<String, String> getHeaders() throws AuthFailureError {
				Map<String, String> map = new HashMap<String, String>();
				map.put("Content-Type", "application/json");
				return map;
			}

		};
		RequestManager.getInstance().addRequest(jsonObjectRequest, context);
	}

	// 删除我的动态
	private void doGetDeleteDynamic(String id) {
		if(!AndroidUtils.isNetworkAvailable(context)){
			return;
		}
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(infoPreferences.getUserID());
		list.add(infoPreferences.getUserToken());
		list.add(id);
		String loginUrl = HttpUrlConstants.getUrl(context, HttpUrlConstants.DELETE_DYNAMIC, list);
		FastJsonRequest<BaseResponse> fastJsonRequest = new FastJsonRequest<BaseResponse>(Method.GET, loginUrl, BaseResponse.class, new Response.Listener<BaseResponse>() {
			@Override
			public void onResponse(BaseResponse response) {
				if ("000000".equals(response.getCode())) {
					MyToast.makeText(context, R.string.dynamic_delete, Toast.LENGTH_LONG).show();
					notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(context, response.getCode(), response.getMessage());
				}
			}
		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				MyToast.makeText(context, R.string.server_error, Toast.LENGTH_LONG).show();
			}
		});
		RequestManager.getInstance().addRequest(fastJsonRequest, context);
	}

	private class ViewHolder {
		MyGridView mGvImags;
		TextView mTvCommuntNum;
		TextView mTvZanNum;
		TextView zhuanfa;
		LinearLayout mLlZan;
		ListView lv_comment;
		ImageView mIvUserIcon;
		ImageView mIvPriser;
		TextView mTvUserNick;
		TextView mTvDynamicContent;
		TextView mTvDynamicDelete;
		TextView mTvDynamicCreadTime;
		TextView mTvLoction;
		RelativeLayout rl_transfer;
		TextView tv_transfer_nickname;
		TextView tv_transfer_content;
		GridView transfer_dynamic_images;

	}

	private MyCommentAdapter myCommentAdapter;
	private YwbImageLoader imageLoader;
	private UserInfoPreferences infoPreferences;
	private Animation animation;

	/*
	 * @see
	 * android.widget.AdapterView.OnItemClickListener#onItemClick(android.widget
	 * .AdapterView, android.view.View, int, long)
	 */
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

	}

}
