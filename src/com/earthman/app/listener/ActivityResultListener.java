package com.earthman.app.listener;

import android.content.Intent;

/**
 * 作者：Zhou
 * 日期：2016-3-10 下午2:53:24
 * 描述：（支付类型回调接口）
 */
public interface ActivityResultListener {
	public void onActivityResult(int requestCode, int resultCode, Intent data);
}
