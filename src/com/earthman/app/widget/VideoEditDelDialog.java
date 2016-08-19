package com.earthman.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.earthman.app.R;
import com.earthman.app.utils.AndroidUtils;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-4-6 下午5:45:40
 * @Decription
 */
public class VideoEditDelDialog extends Dialog{

	private Context context; 
	private Button btn_edit;
	private Button btn_delete;
	private View.OnClickListener onClickListener;
	
	/**
	 * 创建一个新的实例 VideoEditDelDialog.
	 *
	 * @param context
	 */
	public VideoEditDelDialog(Context context, View.OnClickListener onClickListener) {
		super(context, R.style.upomp_bypay_MyDialog);
		this.context = context;
		this.onClickListener = onClickListener;
		initView();
		
	}
	
	private void initView(){
		View view = LayoutInflater.from(context).inflate(R.layout.video_edit_del, null);
		setContentView(view);
		WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (AndroidUtils.getDeviceWidth(context) - AndroidUtils.dip2px(context, 100));
		btn_edit = (Button) view.findViewById(R.id.btn_edit);
		btn_delete = (Button) view.findViewById(R.id.btn_delete);
		btn_edit.setOnClickListener(onClickListener);
		btn_delete.setOnClickListener(onClickListener);
	}

}
