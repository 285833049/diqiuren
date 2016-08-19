package com.earthman.app.nim.uikit.contact.core.viewholder;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.nim.session.SessionHelper;
import com.earthman.app.nim.uikit.NimUIKit;
import com.earthman.app.nim.uikit.common.ui.imageview.HeadImageView;
import com.earthman.app.nim.uikit.contact.core.item.ContactItem;
import com.earthman.app.nim.uikit.contact.core.model.ContactDataAdapter;
import com.earthman.app.nim.uikit.contact.core.model.IContact;
import com.earthman.app.ui.activity.mine.PeronalDetialActivity;

public class FriendsHolder extends AbsContactViewHolder<ContactItem> implements OnClickListener {

	private IContact contact;
	private ContactItem item;
	private HeadImageView ivHead;
	private TextView tvFriendName;
	private TextView tvFriendGroup;
	private Button btnSendMessage;
	private RelativeLayout rlItemLayout;

	@Override
	public void refresh(ContactDataAdapter adapter, int position, final ContactItem item) {
		// contact info
		this.contact = item.getContact();
		this.item = item;
		if (contact.getContactType() == IContact.Type.Friend) {
			ivHead.loadBuddyAvatar(contact.getContactId());
		} else {
			ivHead.setImageBitmap(NimUIKit.getUserInfoProvider().getTeamIcon(contact.getContactId()));
		}
		tvFriendName.setText(contact.getDisplayName());
		rlItemLayout.setOnClickListener(this);
		btnSendMessage.setOnClickListener(this);
	}

	@Override
	public View inflate(LayoutInflater inflater) {
		View view = inflater.inflate(R.layout.nim_friends_item, null);

		rlItemLayout = (RelativeLayout) view.findViewById(R.id.rl_item_layout);
		ivHead = (HeadImageView) view.findViewById(R.id.contacts_item_head);
		tvFriendName = (TextView) view.findViewById(R.id.tv_friend_name);
		tvFriendGroup = (TextView) view.findViewById(R.id.tv_friend_group);
		btnSendMessage = (Button) view.findViewById(R.id.btn_send_message);

		return view;
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.rl_item_layout:// 点击头像
			System.out.println("--------------head click");
			// if (contact.getContactType() == IContact.Type.Friend) {
			// if (NimUIKit.getContactEventListener() != null) {
			// NimUIKit.getContactEventListener().onAvatarClick(context,
			// item.getContact().getContactId());
			// }

			Intent intent = new Intent(context, PeronalDetialActivity.class);
			intent.putExtra("friendsuserid", item.getContact().getContactId());
			intent.putExtra("remarks", contact.getDisplayName());
			context.startActivity(intent);

			break;
		case R.id.btn_send_message:// 发消息
			System.out.println("--------------send message click");
			SessionHelper.startP2PSession(context, item.getContact().getContactId());
			break;
		}
	}
}
