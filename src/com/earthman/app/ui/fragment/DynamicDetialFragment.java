package com.earthman.app.ui.fragment;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.adapter.MyCommentAdapter;
import com.earthman.app.base.BaseFragment;
import com.earthman.app.bean.CommentResponse.CommentResult;
import com.earthman.app.bean.DynamicPrasied;
import com.earthman.app.bean.DynamicPrasied.UserList;
import com.earthman.app.bean.DynamicResponse;
import com.earthman.app.bean.DynamicResponse.DynamicContent.CommentContent;
import com.earthman.app.imageloader.YwbImageLoader;
import com.earthman.app.ui.activity.dynamic.InPutActivity;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.widget.MyGridView;

import de.greenrobot.event.EventBus;

@SuppressLint("ValidFragment")
public class DynamicDetialFragment extends BaseFragment {

	class imageViewHolder {
		ImageView iv_usericon;
	}

	private Context context;

	public DynamicDetialFragment(Context context) {
		this.context = context;
		EventBus.getDefault().register(this);
	}

	// 评论后的刷新
	public void onEvent(CommentResult event) {
		if ("1".equals(event.getType())) {
			CommentContent commentdetial = new DynamicResponse().new DynamicContent().new CommentContent();
			commentdetial.setReplyNice(preferences.getUsernice());
			commentdetial.setContent(event.getContent());
			comments.add(comments.size(), commentdetial);
			myCommentAdapter.notifyDataSetChanged();
		} else if ("2".equals(event.getType())) {
			CommentContent commentdetial = new DynamicResponse().new DynamicContent().new CommentContent();
			commentdetial.setReplyNice(preferences.getUsernice());
			commentdetial.setContent(event.getContent());
			commentdetial.setPReplyNice(comments.get(event.getPosition()).getReplyNice());
			comments.add(event.getPosition() + 1, commentdetial);
			myCommentAdapter.notifyDataSetChanged();
		}
	}

	/**
	 * doGetPrasied获取点赞列表
	 * void
	 * @exception
	 */
	private void doGetPrasied() {
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(preferences.getUserID());
		list.add(preferences.getUserToken());
		list.add(articleId);
		String loginUrl = HttpUrlConstants.getUrl(activity, HttpUrlConstants.DYNAMIC_PRAISED, list);
		executeRequest(new FastJsonRequest<DynamicPrasied>(Method.GET, loginUrl, DynamicPrasied.class, new Response.Listener<DynamicPrasied>() {
			@Override
			public void onResponse(DynamicPrasied response) {
				if ("000000".equals(response.getCode())) {
					arrayList.clear();
					arrayList.addAll(response.getResult());
					mDianZnaAdapter.notifyDataSetChanged();
				} else {
					NetStatusHandUtil.getInstance().handStatus(activity, response.getCode(),response.getMessage());
				}
			}
		}, getErrorListener()));
	}

	ArrayList<UserList> arrayList = new ArrayList<DynamicPrasied.UserList>();
	/**
	 * 点赞的adapter
	 */
	private BaseAdapter mDianZnaAdapter = new BaseAdapter() {
		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			imageViewHolder holder = null;
			if (convertView == null) {
				holder = new imageViewHolder();
				convertView = LayoutInflater.from(context).inflate(R.layout.image_item, null);
				holder.iv_usericon = (ImageView) convertView.findViewById(R.id.iv_user_icon);
				convertView.setTag(holder);
			}
			holder = (imageViewHolder) convertView.getTag();
			imageLoader.loadImage(arrayList.get(position).getValue(), holder.iv_usericon, R.drawable.user_avatar);
			convertView.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(v.getContext(), PeronalDetialActivity.class);
					intent.putExtra("friendsuserid", arrayList.get(position).getId());
					startActivity(intent);
					
				}
			});
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
			return arrayList.size();
		}
	};
	/**
	 * 评论的adapter
	 */
	// private BaseAdapter myCommentAdapter = new BaseAdapter() {
	//
	// @Override
	// public int getCount() {
	// // TODO Auto-generated method stub
	// return comments.size();
	// }
	//
	// @Override
	// public Object getItem(int position) {
	// // TODO Auto-generated method stub
	// return null;
	// }
	//
	// @Override
	// public long getItemId(int position) {
	// // TODO Auto-generated method stub
	// return 0;
	// }
	//
	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// CommentViewHolder holder = null;
	// if (convertView == null) {
	// holder = new CommentViewHolder();
	// convertView = LayoutInflater.from(context).inflate(R.layout.comment_item,
	// null);
	// holder.comment = (TextView) convertView.findViewById(R.id.tv_comment);
	// convertView.setTag(holder);
	// }
	// holder = (CommentViewHolder) convertView.getTag();
	// if (position % 2 == 1) {
	// holder.comment.setText(Html.fromHtml(String.format(context.getString(R.string.comment_to_person),
	// "张无忌", ":你在这里做什么")));
	// } else {
	// holder.comment.setText(Html.fromHtml(String.format(context.getString(R.string.comment_to_dynamic),
	// "王中王", "谢逊", "我受够你了")));
	// }
	// return convertView;
	// }
	// };
	private int inedx;
	private YwbImageLoader imageLoader;
	private String articleId;
	private List<CommentContent> comments;
	private String authorId;
	private MyCommentAdapter myCommentAdapter;

	class CommentViewHolder {
		TextView comment;
	}

	/*
	 * @see android.support.v4.app.Fragment#onViewCreated(android.view.View,
	 * android.os.Bundle)
	 */

	/*
	 * @see com.earthman.app.base.BaseFragment#createView()
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		imageLoader = new YwbImageLoader();
		View contextView = LayoutInflater.from(context).inflate(R.layout.fragment_item, container, false);
		ListView mListView = (ListView) contextView.findViewById(R.id.lv_comment_detial);
		MyGridView myGridView = (MyGridView) contextView.findViewById(R.id.dianzan_gv);
		myGridView.setSelector(new ColorDrawable(Color.TRANSPARENT));//取消GridView中Item选中时默认的背景色  
		Bundle mBundle = getArguments();
		String title = mBundle.getString("arg");
		articleId = mBundle.getString("articleId");
		authorId = mBundle.getString("userid");
		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				inedx = position;
				Intent intent = new Intent(context, InPutActivity.class);
				intent.putExtra("articleId", articleId);
				intent.putExtra("authorId", authorId);
				intent.putExtra("replyId", preferences.getUserID());
				intent.putExtra("parentId", comments.get(position).getCommentId());
				intent.putExtra("type", "2");
				intent.putExtra("position", position);
				context.startActivity(intent);
			}
		});
		comments = new ArrayList<DynamicResponse.DynamicContent.CommentContent>((List<CommentContent>) mBundle.getSerializable("dynamicComment"));
		if ("评论".equals(title)) {
			mListView.setVisibility(View.VISIBLE);
			myGridView.setVisibility(View.GONE);
			myCommentAdapter = new MyCommentAdapter(context, comments);
			mListView.setAdapter(myCommentAdapter);
		}
		if ("点赞".equals(title)) {
			myGridView.setVisibility(View.VISIBLE);
			mListView.setVisibility(View.GONE);
			myGridView.setAdapter(mDianZnaAdapter);
			doGetPrasied();
		}
		return contextView;
	}

	/*
	 * @see com.earthman.app.base.BaseFragment#createView()
	 */
	@Override
	protected View createView() {
		return null;
	}

	/*
	 * @see com.earthman.app.base.BaseFragment#initData()
	 */
	@Override
	protected void initData() {

	}

	/*
	 * @see com.earthman.app.base.BaseFragment#initView(android.view.View)
	 */
	@Override
	protected void initView(View convertView) {

	}

	/*
	 * @see com.earthman.app.base.BaseFragment#setAttribute()
	 */
	@Override
	protected void setAttribute() {

	}

	/*
	 * @see com.earthman.app.base.BaseFragment#handclick(android.view.View)
	 */
	@Override
	protected void handclick(View v) {
		// TODO Auto-generated method stub

	}

	/*
	 * @see com.earthman.app.base.BaseFragment#onDestroy()
	 */
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		EventBus.getDefault().unregister(this);
	}
}