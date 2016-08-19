package com.earthman.app.nim.uikit.contact.core.query;

import java.util.List;

import com.earthman.app.nim.uikit.contact.core.item.AbsContactItem;

/**
 * 通讯录数据源提供者接口
 * Created by huangjun on 2015/4/2.
 */
public interface IContactDataProvider {
    public List<AbsContactItem> provide(TextQuery query);
}
