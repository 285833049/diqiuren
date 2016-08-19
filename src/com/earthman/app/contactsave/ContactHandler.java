package com.earthman.app.contactsave;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.app.Activity;
import android.content.ContentProviderOperation;
import android.content.ContentProviderResult;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.net.Uri;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Organization;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.CommonDataKinds.StructuredName;
import android.provider.ContactsContract.CommonDataKinds.StructuredPostal;
import android.provider.ContactsContract.Data;
import android.provider.ContactsContract.RawContacts;
import android.text.TextUtils;
import android.util.Log;

import com.earthman.app.contactsave.ContactInfo.OrganizationInfo;
import com.earthman.app.contactsave.ContactInfo.PhoneInfo;
import com.earthman.app.contactsave.provider.Contacts;
import com.earthman.app.contactsave.syncml.VDataBuilder;
import com.earthman.app.contactsave.syncml.VNode;
import com.earthman.app.contactsave.syncml.vcard.ContactStruct;
import com.earthman.app.contactsave.syncml.vcard.ContactStruct.ContactMethod;
import com.earthman.app.contactsave.syncml.vcard.ContactStruct.OrganizationData;
import com.earthman.app.contactsave.syncml.vcard.ContactStruct.PhoneData;
import com.earthman.app.contactsave.syncml.vcard.VCardComposer;
import com.earthman.app.contactsave.syncml.vcard.VCardException;
import com.earthman.app.contactsave.syncml.vcard.VCardParser;

/**
 * 联系人 备份/还原操作
 * 
 * @author
 * 
 */
public class ContactHandler {
	private static String TAG = "ContactHandler";
	private static ContactHandler instance_ = new ContactHandler();

	/** 获取实例 */
	public static ContactHandler getInstance() {
		return instance_;
	}

	/**
	 * 获取联系人contact_id和名字，用于快速显示
	 * 
	 * @param context
	 * @return
	 */
	public List<ContactInfo> getAllDisplayName(Activity context,
			ContentResolver cr) {
		List<ContactInfo> infoList = new ArrayList<ContactInfo>();
		Cursor cursorContact = cr
				.query(ContactsContract.Contacts.CONTENT_URI,
						new String[] {
								android.provider.ContactsContract.Contacts._ID,
								android.provider.ContactsContract.Contacts.HAS_PHONE_NUMBER,
								android.provider.ContactsContract.Contacts.DISPLAY_NAME },
						android.provider.ContactsContract.Contacts.IN_VISIBLE_GROUP
								+ "=1", null, null);
		/* 把全部info信息读出来，放入mapInfos,Contacts._ID为key */
		while (cursorContact.moveToNext()) {
			int id = Integer
					.valueOf(cursorContact.getString(cursorContact
							.getColumnIndex(android.provider.ContactsContract.Contacts._ID)));
			int flagHasPhone = Integer
					.valueOf(cursorContact.getString(cursorContact
							.getColumnIndex(android.provider.ContactsContract.Contacts.HAS_PHONE_NUMBER)));
			String displayName = cursorContact
					.getString(cursorContact
							.getColumnIndex(android.provider.ContactsContract.Contacts.DISPLAY_NAME));
			Log.i("wu0wu", "id=" + id + ",flagHasPhone=" + flagHasPhone
					+ ",displayName=" + displayName);
			ContactInfo cantact = new ContactInfo(displayName);
			cantact.setId(id);
			if (flagHasPhone == 1) {
				cantact.setHasPhoneNumber(true);
			} else {
				cantact.setHasPhoneNumber(false);
			}
			infoList.add(cantact);
		}
		return infoList;
	}

	/**
	 * 获取联系人信息
	 * 
	 * @param context
	 * @return
	 */
	public ContactInfo getContactInfo(Activity context,
			ContactInfo contactInfo, ContentResolver cr) {
		String contactId = String.valueOf(contactInfo.getId());
		Cursor c = null;
		c = cr.query(Data.CONTENT_URI, null, Data.CONTACT_ID + "=?",
				new String[] { contactId }, null);
		if (c == null) {
			return null;
		}
		// 设置联系人电话信息
		if (contactInfo.isHasPhoneNumber()) {
			List<ContactInfo.PhoneInfo> phoneNumberList = getPhoneInfos(c,
					contactId, context);
			if (!phoneNumberList.isEmpty()) {
				contactInfo.setPhones(phoneNumberList);
			}
		}

		// 设置email信息
		List<ContactInfo.EmailInfo> emailInfoList = getEmailInfo(c, contactId,
				context);
		if (!emailInfoList.isEmpty()) {
			contactInfo.setEmail(emailInfoList);
		}
		// 设置地址信息
		List<ContactInfo.PostalInfo> postalInfoList = getPostalInfo(c,
				contactId, context);
		if (!postalInfoList.isEmpty()) {
			contactInfo.setPostal(postalInfoList);
		}
		// // 设置公司信息
		List<ContactInfo.OrganizationInfo> organizationInfoList = getOrganizationInfo(
				c, contactId, context);
		if (!organizationInfoList.isEmpty()) {
			contactInfo.setOrganization(organizationInfoList);
		}
		c.close();
		return contactInfo;
	}

	private List<ContactInfo.PhoneInfo> getPhoneInfos(final Cursor c,
			String id, Context context) {
		List<ContactInfo.PhoneInfo> phoneNumberList = new ArrayList<ContactInfo.PhoneInfo>();
		Cursor phonesCursor = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=" + id,
				null, null);

		if (phonesCursor != null) {
			while (phonesCursor.moveToNext()) {
				// 遍历所有电话号码
				String phoneNumber = phonesCursor
						.getString(phonesCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
				// 对应的联系人类型
				int type = phonesCursor
						.getInt(phonesCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.TYPE));
				String label = phonesCursor
						.getString(phonesCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Phone.LABEL));
				// 初始化联系人电话信息
				ContactInfo.PhoneInfo phoneInfo = new ContactInfo.PhoneInfo();
				phoneInfo.type = type;
				phoneInfo.number = phoneNumber;
				phoneInfo.label = label;
				Log.i("wu0wu1", "id=" + id);
				Log.i("wu0wu1", "label=" + phoneInfo.label);
				Log.i("wu0wu1", "type=" + type);
				Log.i("wu0wu1", "number=" + phoneNumber);
				phoneNumberList.add(phoneInfo);
			}

			phonesCursor.close();
		}
		return phoneNumberList;
	}

	private List<ContactInfo.EmailInfo> getEmailInfo(final Cursor c, String id,
			Context context) {
		List<ContactInfo.EmailInfo> emailList = new ArrayList<ContactInfo.EmailInfo>();
		// 获得联系人的EMAIL
		Cursor emailCur = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.Email.CONTENT_URI, null,
				ContactsContract.CommonDataKinds.Email.CONTACT_ID + "=" + id,
				null, null);
		if (emailCur != null) {
			while (emailCur.moveToNext()) {
				// 遍历所有的email
				String email = emailCur
						.getString(emailCur
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA1));
				int type = emailCur
						.getInt(emailCur
								.getColumnIndex(ContactsContract.CommonDataKinds.Email.TYPE));

				// 初始化联系人邮箱信息
				ContactInfo.EmailInfo emailInfo = new ContactInfo.EmailInfo();
				emailInfo.type = type; // 设置邮箱类型
				emailInfo.email = email; // 设置邮箱地址
				emailList.add(emailInfo);
			}
		}
		emailCur.close();
		return emailList;
	}

	/* 地址 */
	private List<ContactInfo.PostalInfo> getPostalInfo(final Cursor c,
			String id, Context context) {
		List<ContactInfo.PostalInfo> postalList = new ArrayList<ContactInfo.PostalInfo>();

		Cursor postalCur = context.getContentResolver().query(
				ContactsContract.CommonDataKinds.StructuredPostal.CONTENT_URI,
				null,
				ContactsContract.CommonDataKinds.StructuredPostal.CONTACT_ID
						+ "=" + id, null, null);
		if (postalCur != null) {
			while (postalCur.moveToNext()) {
				String address = postalCur
						.getString(postalCur
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.FORMATTED_ADDRESS));
				int type = postalCur
						.getInt(postalCur
								.getColumnIndex(ContactsContract.CommonDataKinds.StructuredPostal.TYPE));

				ContactInfo.PostalInfo postalInfo = new ContactInfo.PostalInfo();
				postalInfo.type = type;
				postalInfo.address = address;
				postalList.add(postalInfo);
			}
		}
		postalCur.close();
		return postalList;
	}

	/* 公司 */
	private List<ContactInfo.OrganizationInfo> getOrganizationInfo(
			final Cursor c, String id, Context context) {
		List<ContactInfo.OrganizationInfo> organizationList = new ArrayList<ContactInfo.OrganizationInfo>();

		// 获取该联系人组织
		Cursor organizationsCursor = context.getContentResolver().query(
				Data.CONTENT_URI,
				new String[] { Data._ID, Organization.TYPE,
						Organization.COMPANY, Organization.TITLE },
				Data.CONTACT_ID + "=?" + " AND " + Data.MIMETYPE + "='"
						+ Organization.CONTENT_ITEM_TYPE + "'",
				new String[] { id }, null);
		if (organizationsCursor != null) {
			while (organizationsCursor.moveToNext()) {
				int type = organizationsCursor
						.getInt(organizationsCursor
								.getColumnIndex(ContactsContract.CommonDataKinds.Organization.TYPE));
				String company = organizationsCursor
						.getString(organizationsCursor
								.getColumnIndex(Organization.COMPANY));
				String jobDescription = organizationsCursor
						.getString(organizationsCursor
								.getColumnIndex(Organization.TITLE));
				ContactInfo.OrganizationInfo organizationInfo = new ContactInfo.OrganizationInfo();
				organizationInfo.type = type;
				organizationInfo.companyName = company;
				organizationInfo.jobDescription = jobDescription;
				organizationList.add(organizationInfo);
			}
		}
		organizationsCursor.close();
		return organizationList;
	}

	/**
	 * 备份联系人
	 */

	public String getDateFormate() {
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
		Date curDate = new Date(System.currentTimeMillis());// 获取当前时间
		return formatter.format(curDate);
	}

	/**
	 * 将联系人信息写入vcf文件
	 * 
	 * @param context
	 * @param infos
	 */
	public void backupContacts(Activity context, List<ContactInfo> infos) {
		if (infos == null) {
			return;
		}
		try {
			Log.i(TAG, "infos.size=" + infos.size());

			String path = FileNameSelector.getRootPath(context) + ".vcf";

			OutputStreamWriter writer = new OutputStreamWriter(
					new FileOutputStream(path), "UTF-8");

			VCardComposer composer = new VCardComposer();
			for (ContactInfo info : infos) {
				ContactStruct contact = new ContactStruct();
				contact.name = info.getName();
				List<ContactInfo.PhoneInfo> numberList = info.getPhones();
				for (ContactInfo.PhoneInfo phoneInfo : numberList) {
					contact.addPhone(phoneInfo.type, phoneInfo.number,
							phoneInfo.label, true);
				}
				List<ContactInfo.EmailInfo> emailList = info.getEmail();
				for (ContactInfo.EmailInfo emailInfo : emailList) {
					contact.addContactmethod(Contacts.KIND_EMAIL,
							emailInfo.type, emailInfo.email, null, true);
				}
				List<ContactInfo.PostalInfo> postalList = info.getPostal();
				for (ContactInfo.PostalInfo postal : postalList) {
					contact.addContactmethod(Contacts.KIND_POSTAL, postal.type,
							postal.address, null, true);
				}
				List<ContactInfo.OrganizationInfo> organizationList = info
						.getOrganization();
				for (ContactInfo.OrganizationInfo organization : organizationList) {
					Log.i(TAG, organization.type + ","
							+ organization.companyName + ","
							+ organization.jobDescription);
					contact.company = organization.companyName;
					// contact.addOrganization(organization.type,
					// organization.companyName,
					// organization.jobDescription, false);
				}
				String vcardString = composer.createVCard(contact,
						VCardComposer.VERSION_VCARD30_INT);
				writer.write(vcardString);
				writer.write("\n");
				Log.i(TAG, "vcardString=" + vcardString);
				writer.flush();
			}
			writer.close();

		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			Log.i(TAG, "UnsupportedEncodingException=" + e.toString());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			Log.i(TAG, "FileNotFoundException=" + e.toString());
		} catch (VCardException e) {
			e.printStackTrace();
			Log.i(TAG, "VCardException=" + e.toString());
		} catch (IOException e) {
			e.printStackTrace();
			Log.i(TAG, "IOException=" + e.toString());
		}

	}

	/**
	 * 获取vCard文件中的联系人信息
	 * 
	 * @return
	 */
	public List<ContactInfo> restoreContacts(String path) throws Exception {
		List<ContactInfo> contactInfoList = new ArrayList<ContactInfo>();

		VCardParser parse = new VCardParser();
		VDataBuilder builder = new VDataBuilder();

		BufferedReader reader = new BufferedReader(new InputStreamReader(
				new FileInputStream(path), "UTF-8"));

		StringBuffer vcardString=new StringBuffer("");
		String line;
		while ((line = reader.readLine()) != null) {
			vcardString.append(line + "\n");
		}
		reader.close();

		boolean parsed = parse.parse(vcardString.toString(), "UTF-8", builder);

		if (!parsed) {
			throw new VCardException("Could not parse vCard file: " + path);
		}

		List<VNode> pimContacts = builder.vNodeList;
		for (VNode contact : pimContacts) {
			ContactStruct contactStruct = ContactStruct
					.constructContactFromVNode(contact, 1);
			// 获取备份文件中的联系人电话信息
			List<PhoneData> vcfPhoneDataList = contactStruct.phoneList;
			List<ContactInfo.PhoneInfo> phoneInfoList = new ArrayList<ContactInfo.PhoneInfo>();
			if (null != vcfPhoneDataList && vcfPhoneDataList.size() != 0) {
				for (PhoneData phoneData : vcfPhoneDataList) {
					try{
						ContactInfo.PhoneInfo phoneInfo = new ContactInfo.PhoneInfo();
						phoneInfo.number = phoneData.data;
						phoneInfo.type = phoneData.type;
						phoneInfoList.add(phoneInfo);
					}catch(Exception e){
						e.printStackTrace();
						continue;//如果一个出错继续执行
					}
				}
			}
			// 获取备份文件中的联系人邮箱信息和地址
			List<ContactMethod> vcfContactmethodList = contactStruct.contactmethodList;
			List<ContactInfo.EmailInfo> emailInfoList = new ArrayList<ContactInfo.EmailInfo>();
			List<ContactInfo.PostalInfo> postalInfoList = new ArrayList<ContactInfo.PostalInfo>();

			if (null != vcfContactmethodList) {
				// 存在 Email 信息
				for (ContactMethod contactMethod : vcfContactmethodList) {
					try{
						if (Contacts.KIND_EMAIL == contactMethod.kind) {
							ContactInfo.EmailInfo emailInfo = new ContactInfo.EmailInfo();
							emailInfo.email = contactMethod.data;
							emailInfo.type = contactMethod.type;
							emailInfoList.add(emailInfo);
						}
					
						if (Contacts.KIND_POSTAL == contactMethod.kind) {
							ContactInfo.PostalInfo postalInfo = new ContactInfo.PostalInfo();
							postalInfo.address = contactMethod.data;
							postalInfo.type = contactMethod.type;
							postalInfoList.add(postalInfo);
						}
					}catch(Exception e){
						e.printStackTrace();
						continue;//如果一个出错继续执行
					}
				}
			}

			List<ContactInfo.OrganizationInfo> organizationInfoList = new ArrayList<ContactInfo.OrganizationInfo>();
			Log.i("wu0wu", "restore contactStruct.company="
					+ contactStruct.organizationList);
			List<OrganizationData> vcfOrganizationList = contactStruct.organizationList;
			// 获取备份文件中的联系人电话信息
			if (null != vcfOrganizationList) {
				for (OrganizationData organizationData : vcfOrganizationList) {
					try{
						OrganizationInfo organizationInfo = new ContactInfo.OrganizationInfo();
						organizationInfo.companyName = organizationData.companyName;
						organizationInfo.type = organizationData.type;
						organizationInfoList.add(organizationInfo);
						
					}catch(Exception e){
						e.printStackTrace();
						continue;//如果一个出错继续执行
					}
				}
			}
			ContactInfo info = new ContactInfo(contactStruct.name);
			if (emailInfoList.size() != 0) {
				info.setEmail(emailInfoList);
			}
			if (phoneInfoList.size() != 0) {
				info.setPhones(phoneInfoList);
			}
			if (postalInfoList.size() != 0) {
				info.setPostal(postalInfoList);
			}
			if (organizationInfoList.size() != 0) {
				info.setOrganization(organizationInfoList);
			}

			contactInfoList.add(info);
		}

		return contactInfoList;
	}

	/**
	 * 向手机中录入联系人信息
	 * 
	 * @param info
	 *            要录入的联系人信息
	 */
	public void addContacts(Activity context, ContactInfo info) {

		ContentResolver cr = context.getContentResolver();
		ContentValues values = new ContentValues();
		// 首先向RawContacts.CONTENT_URI执行一个空值插入，目的是获取系统返回的rawContactId
		Uri rawContactUri = context.getContentResolver().insert(
				RawContacts.CONTENT_URI, values);
		long rawContactId = ContentUris.parseId(rawContactUri);

		// 往data表入姓名数据
		values.clear();
		values.put(Data.RAW_CONTACT_ID, rawContactId);
		values.put(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE);
		values.put(StructuredName.GIVEN_NAME, info.getName());
		context.getContentResolver().insert(
				android.provider.ContactsContract.Data.CONTENT_URI, values);

		// 获取联系人电话信息
		List<ContactInfo.PhoneInfo> phoneList = info.getPhones();
		/** 录入联系电话 */
		for (ContactInfo.PhoneInfo phoneInfo : phoneList) {
			values.clear();
			values.put(
					android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
					rawContactId);
			values.put(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE);
			// 设置录入联系人电话信息
			values.put(Phone.NUMBER, phoneInfo.number);
			values.put(Phone.TYPE, phoneInfo.type);
			// 往data表入电话数据
			context.getContentResolver().insert(
					android.provider.ContactsContract.Data.CONTENT_URI, values);
		}
		// 获取联系人邮箱信息
		List<ContactInfo.EmailInfo> emailList = info.getEmail();

		/** 录入联系人邮箱信息 */
		for (ContactInfo.EmailInfo email : emailList) {
			values.clear();
			values.put(
					android.provider.ContactsContract.Contacts.Data.RAW_CONTACT_ID,
					rawContactId);
			values.put(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE);
			// 设置录入的邮箱信息
			values.put(Email.DATA, email.email);
			values.put(Email.TYPE, email.type);
			// 往data表入Email数据
			context.getContentResolver().insert(
					android.provider.ContactsContract.Data.CONTENT_URI, values);
		}

		for (ContactInfo.PostalInfo postal : info.getPostal()) {
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, StructuredPostal.CONTENT_ITEM_TYPE);
			values.put(StructuredPostal.DATA1, postal.address);
			values.put(StructuredPostal.TYPE, postal.type);
			Log.i("wu0wu", "insert postal " + postal.address + ","
					+ postal.type);
			Uri dataUri = cr.insert(Data.CONTENT_URI, values);
		}
		for (ContactInfo.OrganizationInfo organization : info.getOrganization()) {
			values.clear();
			values.put(Data.RAW_CONTACT_ID, rawContactId);
			values.put(Data.MIMETYPE, Organization.CONTENT_ITEM_TYPE);
			values.put(Organization.COMPANY, organization.companyName);
			Log.i("wu0wu", "insert organization.companyName "
					+ organization.companyName);
			values.put(Organization.TYPE, organization.type);
			Uri dataUri = cr.insert(Data.CONTENT_URI, values);
		}

	}

	/**
	 * 批量添加联系人信息
	 * 
	 * @param context
	 * @param infos
	 */
	public void addContacts(Activity context, List<ContactInfo> infos) {
		List<ContactInfo> locals = getAllPhoneContacts(context);
		ArrayList<ContentProviderOperation> ops = new ArrayList<ContentProviderOperation>();
		int rawContactInsertIndex = 0;
		
		//ContactInfo contact : infos
		for (int x=0;x<infos.size();x++) {
			ContactInfo contact=infos.get(x);
			if(TextUtils.isEmpty(contact.getName())||contact.getPhones().size() == 0)continue;//姓名为空或者号码为空直接不写入
			if (locals.size() > 0) {
				boolean duplicate = ClearDuplicateContacts(contact, locals,
						context);
				if (duplicate) {
					continue;
				}
			}
			rawContactInsertIndex = ops.size(); // 有了它才能给真正的实现批量添加

			ops.add(ContentProviderOperation.newInsert(RawContacts.CONTENT_URI)
					.withValue(RawContacts.ACCOUNT_TYPE, null)
					.withValue(RawContacts.ACCOUNT_NAME, null)
					.withYieldAllowed(true).build());

			// 添加姓名
			ops.add(ContentProviderOperation
					.newInsert(
							android.provider.ContactsContract.Data.CONTENT_URI)
					.withValueBackReference(Data.RAW_CONTACT_ID,
							rawContactInsertIndex)
					.withValue(Data.MIMETYPE, StructuredName.CONTENT_ITEM_TYPE)
					.withValue(StructuredName.DISPLAY_NAME, contact.getName())
					.withYieldAllowed(true).build());

			// 获取联系人电话信息
			List<ContactInfo.PhoneInfo> phoneList = contact.getPhones();
			/** 录入联系电话 */
			for (ContactInfo.PhoneInfo phoneInfo : phoneList) {
				// 添加号码
				ops.add(ContentProviderOperation
						.newInsert(
								android.provider.ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(Data.RAW_CONTACT_ID,
								rawContactInsertIndex)
						.withValue(Data.MIMETYPE, Phone.CONTENT_ITEM_TYPE)
						.withValue(Phone.NUMBER, phoneInfo.number)
						.withValue(Phone.TYPE, phoneInfo.type)
						.withYieldAllowed(true).build());
			}

			/** 录入联系人邮箱信息 */
			for (ContactInfo.EmailInfo email : contact.getEmail()) {
				ops.add(ContentProviderOperation
						.newInsert(
								android.provider.ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(Data.RAW_CONTACT_ID,
								rawContactInsertIndex)
						.withValue(Data.MIMETYPE, Email.CONTENT_ITEM_TYPE)
						.withValue(Email.DATA, email.email)
						.withValue(Email.TYPE, email.type)
						.withYieldAllowed(true).build());
			}

			for (ContactInfo.PostalInfo postal : contact.getPostal()) {
				ops.add(ContentProviderOperation
						.newInsert(
								android.provider.ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(Data.RAW_CONTACT_ID,
								rawContactInsertIndex)
						.withValue(Data.MIMETYPE,
								StructuredPostal.CONTENT_ITEM_TYPE)
						.withValue(StructuredPostal.DATA1, postal.address)
						.withValue(StructuredPostal.TYPE, postal.type)
						.withYieldAllowed(true).build());
			}

			for (ContactInfo.OrganizationInfo organization : contact
					.getOrganization()) {
				ops.add(ContentProviderOperation
						.newInsert(
								android.provider.ContactsContract.Data.CONTENT_URI)
						.withValueBackReference(Data.RAW_CONTACT_ID,
								rawContactInsertIndex)
						.withValue(Data.MIMETYPE,
								Organization.CONTENT_ITEM_TYPE)
						.withValue(Organization.COMPANY,
								organization.companyName)
						.withValue(Organization.TYPE, organization.type)
						.withYieldAllowed(true).build());
			}
			//一次事物最多500条数据
			if(x%500==0||x==infos.size()-1){
				if (ops != null) {
					// 真正添加
					try {
						//ContentProviderResult[] results = 
						context.getContentResolver()
								.applyBatch(ContactsContract.AUTHORITY, ops);
					} catch (RemoteException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					} catch (OperationApplicationException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				ops = new ArrayList<ContentProviderOperation>();
			}

		}

		
	}

	/**
	 * 向通讯录写入信息时 判断是否重复 1.先判断名字是否相同 2.判断电话号码是否相同
	 * 
	 * @param name
	 *            联系人姓名
	 * @return 是否重复
	 */
	public boolean ClearDuplicateContacts(ContactInfo netInfo,
			List<ContactInfo> locals, Activity context) {
		for (ContactInfo localPInfo : locals) {// 当前联系人信息与本地联系人信息一一对比
		try{
				if(TextUtils.isEmpty(localPInfo.getName())||localPInfo.getPhones().size() == 0)continue;
				// 先判断姓名是否相同,相同进行下一步判断
				if (localPInfo.getName().equals(netInfo.getName())) {
	
					List<PhoneInfo> phoneList = localPInfo.getPhones();
					// 备份数据没有号码时为重复
					/*if (netInfo.getPhones().size() == 0
							&& localPInfo.getPhones().size() == 0) {
						return true;
					}*/
					// 判断备份数据号码是否与本地号码一致
					int i = 0;
					for (PhoneInfo netPhone : netInfo.getPhones()) {
	
						for (PhoneInfo localPhone : phoneList) {
	
							if (netPhone.number.equals(localPhone.number)) {
								i++;
								break;
							}
	
						}
						if (i == netInfo.getPhones().size()) {// 当联系人的所有号码在本地都能找到对应的号码时
																// 说明重复
							return true;
						}
					}
	
				}
			}catch(Exception e){
				e.printStackTrace();
				continue;
			}
		}

		return false;

	}

	/**
	 * 获取所有拥有手机号的联系人
	 * 
	 * @param context
	 * @return
	 */
	public List<ContactInfo> getAllPhoneContacts(Context context) {
		List<ContactInfo> contactList = new ArrayList<ContactInfo>();
		ContentResolver cr = context.getContentResolver();
		String[] mContactsProjection = new String[] {
				ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
				ContactsContract.CommonDataKinds.Phone.NUMBER,
				ContactsContract.Contacts.DISPLAY_NAME };

		String oldContactsId = "";
		String newContactsId = "";
		String phoneNum;
		String name;

		List<PhoneInfo> phoneList = null;
		ContactInfo contactInfo = null;
		PhoneInfo phoneInfo = null;

		/*
		 * Cursor cursor=context.getContentResolver().query(
		 * ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,null, null,
		 * ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
		 */
		// 查询contacts表中的所有数据，联系人一个号码一条数据按照ID排序
		Cursor cursor = cr.query(Phone.CONTENT_URI, mContactsProjection, null,
				null, ContactsContract.CommonDataKinds.Phone.CONTACT_ID);
		if (cursor.getCount() > 0) {
			while (cursor.moveToNext()) {

				newContactsId = cursor.getString(0);
				phoneNum = cursor.getString(1);
				name = cursor.getString(2);

				if (!oldContactsId.equals(newContactsId)) {// 每一次新旧ID不同时
															// 表示已经换l一个联系人
					contactInfo = new ContactInfo(name);
					phoneList = new ArrayList<ContactInfo.PhoneInfo>();
					contactInfo.setPhones(phoneList);
					contactList.add(contactInfo);

				}
				contactInfo.setId(Integer.parseInt(newContactsId));

				phoneInfo = new PhoneInfo();
				phoneInfo.number = phoneNum;
				phoneList.add(phoneInfo);

				oldContactsId = newContactsId;
			}
		}
		cursor.close();
		return contactList;
	}

	/**
	 * 获取所有联系人姓名 号码资料
	 * @param context
	 * @return
	 */
	public List<ContactInfo> getAllPhone(Context context) {
		List<ContactInfo> contactList = getAllDisplayName((Activity) context,
				context.getContentResolver());
		int allSise = contactList.size();
		for (int i = 0; i < allSise; i++) {
			getContactPhoneInfo((Activity) context,
					contactList.get(i), context.getContentResolver());

		}
		return contactList;
	}
	
	
	/**
	 * 根据联系人ID获取联系人电话
	 * 
	 * @param context
	 * @return
	 */
	public ContactInfo getContactPhoneInfo(Activity context,
			ContactInfo contactInfo, ContentResolver cr) {
		String contactId = String.valueOf(contactInfo.getId());
		Cursor c = null;
		c = cr.query(Data.CONTENT_URI, null, Data.CONTACT_ID + "=?",
				new String[] { contactId }, null);
		if (c == null) {
			return null;
		}
		// 设置联系人电话信息
		if (contactInfo.isHasPhoneNumber()) {
			List<ContactInfo.PhoneInfo> phoneNumberList = getPhoneInfos(c,
					contactId, context);
			if (!phoneNumberList.isEmpty()) {
				contactInfo.setPhones(phoneNumberList);
			}
		}
		c.close();
		return contactInfo;
	}

	
}
