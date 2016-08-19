package com.earthman.app.widget;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.earthman.app.R;
import com.earthman.app.utils.AndroidUtils;

/** 
* @Title: MyDialog
* @Description: 
* @Company: 地球人
* @author    ERIC
* @date       2016年3月7日
*/

public class DialogOK extends Dialog implements android.view.View.OnClickListener {

	private Context context;
	private String content;
	private String title;
	private Button commit;

	/**
	 * @param context
	 */
	public DialogOK(Context context,String title,String content) {
		super(context, R.style.upomp_bypay_MyDialog);
		this.context=context;
		this.title=title;
		this.content=content;
		initView();
	}

	public View getView(){
		return commit;
	}
	/**
	 * 
	 */
	private void initView() {
		setContentView(R.layout.dialog_ok);
		commit = (Button) findViewById(R.id.dialog_simple_commit);
		TextView dialog_content = (TextView) findViewById(R.id.dialog_content);
		TextView dialog_title = (TextView) findViewById(R.id.dialog_title);
		dialog_content.setText((CharSequence) content);
		dialog_title.setText((CharSequence) title);
		android.view.WindowManager.LayoutParams params = getWindow().getAttributes();
		params.width = (int) (AndroidUtils.getDeviceWidth(context) - AndroidUtils.dip2px(context, 100));
	}

	/* (non-Javadoc)
	 * @see android.view.View.OnClickListener#onClick(android.view.View)
	 */
	@Override
	public void onClick(View v) {
		if (v.getId()==R.id.dialog_simple_commit) {
			dismiss();
		}
	}
}
