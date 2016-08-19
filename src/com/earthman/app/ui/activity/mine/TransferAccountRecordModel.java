package com.earthman.app.ui.activity.mine;

import java.util.List;

import com.earthman.app.bean.BaseResponse;

public class TransferAccountRecordModel extends BaseResponse{
	
	private List<TransferAccountRecordItem> result;

	public List<TransferAccountRecordItem> getResult() {
		return result;
	}

	public void setResult(List<TransferAccountRecordItem> result) {
		this.result = result;
	}
	
}
