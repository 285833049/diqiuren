package com.earthman.app.contactsave;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.charlie.lee.androidcommon.http.RequestManager;
import com.charlie.lee.androidcommon.http.request.FastJsonRequest;
import com.earthman.app.R;
import com.earthman.app.base.BaseActivity;
import com.earthman.app.base.UserInfoPreferences;
import com.earthman.app.bean.ContactURLBean;
import com.earthman.app.utils.HttpUrlConstants;
import com.earthman.app.utils.LogUtil;
import com.earthman.app.utils.NetStatusHandUtil;
import com.earthman.app.utils.UpLoadFileUtils;
import com.earthman.app.widget.DialogOK;
import com.earthman.app.widget.MyLoadingDialog;
import com.earthman.app.widget.MyToast;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;

/**
 * @Author：ERIC
 * @Version：地球人
 * @Date：2016年3月25日 上午10:03:04
 * @Decription
 */
public class ContactUpAndDown {
	private final static int SHOW_PROGRESS_DIALOG = 0;// 显示进度条
	private final static int SHOW_PROGRESS_DIALOG_RECOVER = 3;// 显示下载恢复进度
	private final static int DISMISS_PROGRESS_DIALOG_SAVE = 1;// 隐藏备份进度条
	private final static int DISMISS_PROGRESS_DIALOG_BACKUP = 4;// 隐藏备份进度条
	private final static int REFRESH_PROGRESS_DIALOG = 2;// 刷新进度条
	private Context context;
	private String token;
	private ContactHandler mContactHandler;
	
	private ContentResolver contentResolver;
	private  List<ContactInfo> allConatcts;//本地联系人信息
	private List<ContactInfo> allConatcts_recover; //服务器下载文件联系人信息
	private UserInfoPreferences userInfoPreferences;
	private String userID;
	private DialogOK myDialog;
	
	private MyLoadingDialog myLoadingDialog;
	private MyLoadingDialog myLoadingDialog2;

	/**
	 * 
	 * saveContact(保存联系人到本地文件)
	 * void
	 * @exception
	 */
	public ContactUpAndDown(final Context context) {
		this.context = context;
		contentResolver = context.getContentResolver();
		mContactHandler = ContactHandler.getInstance();
		//LogUtil.d("EarthMan", "allConatcts" + allConatcts.size());
		userInfoPreferences = new UserInfoPreferences(context);
		userID = userInfoPreferences.getUserID();
		token = userInfoPreferences.getUserToken();
		myDialog = new DialogOK(context, context.getString(R.string.dialog_title), context.getString(R.string.backupok));
		myDialog.getView().setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				myDialog.dismiss();
			}
		});
	}



	// handler
	private Handler mHandler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			
			switch (msg.what) {
			case SHOW_PROGRESS_DIALOG:
				myLoadingDialog = new MyLoadingDialog(context, context.getString(R.string.backuping));
				myLoadingDialog.show();
				break;
			case DISMISS_PROGRESS_DIALOG_SAVE:
				String path = FileNameSelector.getRootPath(context);
				UpLoadFileUtils.doUploadImg(context, path + ".vcf", userID, token, mHandler);
				break;
			case DISMISS_PROGRESS_DIALOG_BACKUP:
				myLoadingDialog2.dismiss();
				final DialogOK myDialog2 = new DialogOK(context, context.getString(R.string.dialog_title), context.getString(R.string.recoverok));
				myDialog2.getView().setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View v) {
						myDialog2.dismiss();
					}
				});
				myDialog2.show();
				break;
			case SHOW_PROGRESS_DIALOG_RECOVER:
				break;
			case 0x99://通讯录VCF文件上传成功
				myLoadingDialog.dismiss();
				myDialog.show();
				break;
			case 0x100://通讯录VCF文件上传失败
				myLoadingDialog.dismiss();
				break;
			default:
				break;
			}
		}
	};
	

	/**
	 * 
	 * saveContact(保存文件到本地)
	 * void
	 * @exception
	 */
	public void saveContact() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				mHandler.sendEmptyMessage(SHOW_PROGRESS_DIALOG);
				allConatcts = mContactHandler.getAllDisplayName((Activity) context, contentResolver);
				List<ContactInfo> selectedContact = new ArrayList<ContactInfo>();
				//int count = 0;
				int allSise = allConatcts.size();
				for (int i = 0; i < allSise; i++) {
					if (allConatcts.get(i).isSelected) {
						/*Log.i("wu0wu", allConatcts.get(i).getName());
						Message msg = new Message();
						msg.arg1 = count * 100 / allSise;
						msg.what = REFRESH_PROGRESS_DIALOG;
						msg.obj = allConatcts.get(i).getName();
						mHandler.sendMessage(msg);*/
						selectedContact.add(refreshContactInfo(allConatcts.get(i)));
						//count++;
					}
				}
				mContactHandler.backupContacts((Activity) context, selectedContact);
				Message msg = new Message();
				msg.what = DISMISS_PROGRESS_DIALOG_SAVE;
				mHandler.sendMessage(msg);
			}
		}).start();
	}

	private ContactInfo refreshContactInfo(ContactInfo contact) {
		return mContactHandler.getContactInfo((Activity) context, contact, contentResolver);
	}

	/**
	 * 
	 * downContact(从服务器下载联系人vcf文件)
	 * void
	 * @exception
	 */
	public  void downContact(String url) {
		String path=FileNameSelector.getRootPath(context);
		HttpUtils httpUtils = new HttpUtils();
		httpUtils.download(url, path+"download.vcf",new RequestCallBack<File>() {
			@Override
			public void onStart() {
				super.onStart();
			}

			/*
			 * @see
			 * com.lidroid.xutils.http.callback.RequestCallBack#onLoading(long,
			 * long, boolean)
			 */
			@Override
			public void onLoading(long total, long current, boolean isUploading) {
				// TODO Auto-generated method stub
				super.onLoading(total, current, isUploading);
			}

			@Override
			public void onSuccess(ResponseInfo<File> responseInfo) {
				insertContect(FileNameSelector.getRootPath(context));
			}

			@Override
			public void onFailure(HttpException arg0, String arg1) {
				myLoadingDialog2.dismiss();
				MyToast.makeText(context, context.getString(R.string.recoverokfail), Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 
	 * insertContect(插入联系人)
	 * void
	 * @exception
	 */
	public  void insertContect(final String path) {
		if (TextUtils.isEmpty(path)) {
			myLoadingDialog2.dismiss();
			MyToast.makeText(context, context.getString(R.string.sdcardtips), Toast.LENGTH_SHORT).show();
			return;
		}
		
			new Thread(new Runnable() {
				@Override
				public void run() {
					try {
						allConatcts_recover = mContactHandler.restoreContacts(path+"download.vcf");
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					mContactHandler.addContacts((Activity) context,allConatcts_recover);
					mHandler.sendEmptyMessage(DISMISS_PROGRESS_DIALOG_BACKUP);
				}
			}).start();
	}
	
	/**
	 * doGetContactDownloadURl(这里用一句话描述这个方法的作用)
	 * void
	 * @exception
	*/
	public void doGetContactDownloadURl() {
		myLoadingDialog2 = new MyLoadingDialog(context, "正在恢复联系人...");
		myLoadingDialog2.show();
		ArrayList<Object> list = new ArrayList<Object>();
		list.add(userID);
		list.add(token);
		String loginUrl = HttpUrlConstants.getUrl(context, HttpUrlConstants.CONTACTBACKUP, list);
		FastJsonRequest<ContactURLBean> request=new FastJsonRequest<ContactURLBean>(Method.GET, loginUrl, ContactURLBean.class, new Response.Listener<ContactURLBean>() {

			@Override
			public void onResponse(ContactURLBean response) {
				if ("000000".equals(response.getCode())) {
					downContact(response.getResult().getFilePath());
				} else {
					myLoadingDialog2.dismiss();
					NetStatusHandUtil.getInstance().handStatus(context, response.getCode(),response.getMessage());
				}
			}

		}, new Response.ErrorListener() {
			@Override
			public void onErrorResponse(VolleyError error) {
				myLoadingDialog2.dismiss();
				MyToast.makeText(context, R.string.server_error, Toast.LENGTH_LONG).show();
			}
		});
		
		RequestManager.getInstance().addRequest(request, this);
	}

}
