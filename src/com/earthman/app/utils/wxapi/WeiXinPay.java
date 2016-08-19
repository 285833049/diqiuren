package com.earthman.app.utils.wxapi;

import java.util.Map;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.earthman.app.R;
import com.earthman.app.utils.Constants;
import com.tencent.mm.sdk.modelpay.PayReq;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

/**
 * @Author：Zhou
 * @Version：地球人
 * @Date：2016-3-26 下午4:16:11
 * @Decription
 */
public class WeiXinPay {

	private static Context mContext;
	private static WeiXinPay weiXinPay;

	private WeiXinPay() {

	}

	public static WeiXinPay getInstance(Context context) {
		mContext = context.getApplicationContext();
		if (weiXinPay == null) {
			weiXinPay = new WeiXinPay();
		}
		return weiXinPay;
	}

	/**
	 * @param out_trade_no
	 * @param subject
	 * @param total_fee
	 * @param body
	 *            微信支付
	 *            dopostWxPay(这里用一句话描述这个方法的作用)
	 *            void
	 * 
	 * @exception
	 */

	private IWXAPI msgApi;
	private PayReq req;

	public void dopostWxPay(String body, String total_fee, String subject, String out_trade_no) {
		msgApi = WXAPIFactory.createWXAPI(mContext, null, true);
		req = new PayReq();
		// 将该app注册到微信
		msgApi.registerApp(Constants.WX_APPID);
		GetPrepayIdTask task = new GetPrepayIdTask(body, out_trade_no, total_fee);
		task.execute();
	}

	private class GetPrepayIdTask extends AsyncTask<Void, Void, String> {
		private ProgressDialog dialog;
		private String orderid;
		private String body;
		private String total_fee;

		public GetPrepayIdTask(String body, String out_trade_no, String total_fee) {
			this.orderid = out_trade_no;
			this.body = body;
			this.total_fee = total_fee;

		}

		@Override
		protected void onPreExecute() {
//			 dialog = ProgressDialog.show(mContext, "提示", "正在加载..");
		}

		@Override
		protected void onPostExecute(String result) {
			super.onPostExecute(result);
			if (dialog != null) {
				dialog.dismiss();
			}
			msgApi.registerApp(Constants.WX_APPID);
			msgApi.sendReq(req);
			// if (TextUtils.isEmpty(result)) {
			// return;
			// }

			//Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();

		}

		@Override
		protected void onCancelled() {
			super.onCancelled();
		}

		@Override
		protected String doInBackground(Void... params) {
			String url = String.format("https://api.mch.weixin.qq.com/pay/unifiedorder");
			String entity = WeiXinPayUtils.genProductArgs(body, orderid, total_fee);

			byte[] buf = Util.httpPost(url, entity);

			String content = new String(buf);

			Map<String, String> result = WeiXinPayUtils.decodeXml(content);

			if ("SUCCESS".equals(result.get("result_code"))) {
				// 生成预支付交易单成功
				req = WeiXinPayUtils.genPayReq(req, result);
				
			} else {
				return mContext.getString(R.string.genearate_order_fail);
			}
			return null;
		}
	}
}
