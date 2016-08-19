package com.earthman.app.adapter;

import java.util.List;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.bean.CircleListInfo;
import com.earthman.app.enums.CircleHandle;
import com.earthman.app.listener.CommonDialogListener;
import com.earthman.app.listener.EditTextDialogListener;
import com.earthman.app.ui.activity.circle.CircleCreateActivity;
import com.earthman.app.widget.BaseViewHolder;
import com.earthman.app.widget.DialogCommon;
import com.earthman.app.widget.DialogEditText;

/**
 * @Author：Vinci
 * @Version：地球人
 * @Date：2016-3-26 下午6:40:50
 * @Decription 创建圈子适配器
 */
public class CircleCreateAdapter extends BaseAdapter{

	private CircleCreateActivity mActivity;
	private List<CircleListInfo> mCircleList;

	public CircleCreateAdapter(CircleCreateActivity activity,List<CircleListInfo> circleList) {
		super();
		this.mActivity=activity;
		this.mCircleList = circleList;
	}
	
	@Override
	public int getCount() {
		return mCircleList.size();
	}

	@Override
	public Object getItem(int position) {
		return mCircleList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
			convertView = LayoutInflater.from(mActivity).inflate(R.layout.circle_create_item, null);
		}
		CircleListInfo info = mCircleList.get(position);
		TextView tvTitle = BaseViewHolder.get(convertView, R.id.tv_title);
		Button btnEdit = BaseViewHolder.get(convertView, R.id.btn_edit);
		Button btnDelete = BaseViewHolder.get(convertView, R.id.btn_delete);

		tvTitle.setText(info.getName());
		btnEdit.setOnClickListener(new ItemClick(position));
		btnDelete.setOnClickListener(new ItemClick(position));

		return convertView;
	}

	public class ItemClick implements OnClickListener {
		
		private int mPosition;
		
		public ItemClick(int position) {
			super();
			this.mPosition = position;
		}

		@Override
		public void onClick(View v) {
			final CircleListInfo info = mCircleList.get(mPosition);
			mActivity.mPosition=mPosition;
			switch (v.getId()) {
			case R.id.btn_edit:
				DialogEditText createDialog = new DialogEditText(mActivity,mActivity.getString(R.string.circle_edit),mActivity.getString(R.string.please_enter_circle_name),info.getName(),new EditTextDialogListener(){

					@Override
					public void onLeftButtonClick(EditText etContent) {
						
					}

					@Override
					public void onRightButtonClick(EditText etContent) {
//						Toast.makeText(mActivity,etContent.getText().toString(),1).show();
						mActivity.operationCircle(CircleHandle.EDIT, info.getName(),etContent.getText().toString());//编辑圈子
					}
				});
				createDialog.show();
				break;
			case R.id.btn_delete:
				DialogCommon commonDialog=new DialogCommon(mActivity,"",mActivity.getString(R.string.circle_delete_confirm),mActivity.getString(R.string.delete),new CommonDialogListener(){

					@Override
					public void onRightButtonClick() {
						mActivity.operationCircle(CircleHandle.DELETE, info.getName(),"");//删除圈子
					}
					
				});
				commonDialog.show();
				break;
			}
		}
		
	}	
	
}
