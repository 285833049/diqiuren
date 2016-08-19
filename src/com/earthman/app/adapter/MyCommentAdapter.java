package com.earthman.app.adapter;

import java.util.List;

import android.content.Context;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.DynamicResponse;
import com.earthman.app.bean.DynamicResponse.DynamicContent.CommentContent;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月30日 下午6:33:50
 * @Decription
 */
public class MyCommentAdapter extends BaseAdapter {

	private Context context;
	private List<CommentContent> commentLsit;

	public MyCommentAdapter(Context context, List<DynamicResponse.DynamicContent.CommentContent> commentLsit) {
		this.context = context;
		this.commentLsit = commentLsit;
		// commentList.add("张无忌吧下啊");
		// commentList.add("好的，新奥尔良鸡腿");
		// commentList.add("再来一份大的鸡腿");
		// commentList.add("吃饱了！");
		// commentList.add("再来五分");
		// commentList.add("张无忌吧下啊");
		// commentList.add("好的，新奥尔良鸡腿");
		// commentList.add("再来一份大的鸡腿");
		// commentList.add("吃饱了！");
		// commentList.add("再来五分");
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return commentLsit.size();
	}

	public void setData(CommentContent comment) {
		commentLsit.add(commentLsit.size(), comment);
		notifyDataSetChanged();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}

	public void setData(String comentName) {
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		CommentViewHolder holder = null;
		if (convertView == null) {
			holder = new CommentViewHolder();
			convertView = LayoutInflater.from(context).inflate(R.layout.comment_item, null);
			holder.comment = (TextView) convertView.findViewById(R.id.tv_comment);
			convertView.setTag(holder);
		}
		holder = (CommentViewHolder) convertView.getTag();
		// holder.comment.setText(Html.fromHtml(String.format(context.getString(R.string.comment_to_person),
		// "张无忌","谢逊："+commentList.get(position))));
		// if (condition) {
		//
		// }
		// String commentHtml = getCommentHtml(commentLsit.get(position).gets,
		// commentLsit.get(position).getsTitle(),
		// commentLsit.get(position).getsTitle());
		// holder.comment.setText(Html.fromHtml(commentHtml));
		String reply = null;
		if (!TextUtils.isEmpty(commentLsit.get(position).getReplyRemarks())) {
			reply = commentLsit.get(position).getReplyRemarks();
		} else {
			reply = commentLsit.get(position).getReplyNice();
		}
		String pReply = null;
		if (!TextUtils.isEmpty(commentLsit.get(position).getPReplyRemarks())) {
			pReply = commentLsit.get(position).getPReplyRemarks();
		} else {
			pReply = commentLsit.get(position).getPReplyNice();
		}
		String commentHtml = getCommentHtml(reply, pReply, commentLsit.get(position).getContent());
		holder.comment.setText(Html.fromHtml(commentHtml));
		return convertView;
	}

	class CommentViewHolder {
		TextView comment;
	}

	/**
	 * 
	 * getCommentHtml(将评论转化成html字符串，显示不同颜色)
	 * 
	 * @param nickName_1
	 * @param nickName_2
	 *            被回复人的名字，可以为空 若为空则是对动态的回复
	 * @param commentstr
	 * @return
	 *         String
	 * @exception
	 */
	private String getCommentHtml(String nickName_1, String nickName_2, String commentstr) {
		String str = "";
		if (TextUtils.isEmpty(nickName_1) || TextUtils.isEmpty(commentstr)) {
			return str;
		}
		StringBuffer buffer = new StringBuffer();
		buffer.append("<font color=\"#16B7E4\">").append(nickName_1).append("</font>");
		if (!TextUtils.isEmpty(nickName_2)) {
			buffer.append("<font color=\"#756F6F\">").append("\b").append(context.getString(R.string.comment_reply)).append("\b").append("</font>");
			buffer.append("<font color=\"#16B7E4\">").append(nickName_2).append("</font>");
		}
		buffer.append("<font color=\"#756F6F\">").append(":\b" + commentstr).append("</font>");

		return buffer.toString();

	}

}
