package com.earthman.app.nim.uikit.contact.core.provider;

import java.util.ArrayList;
import java.util.List;

import com.earthman.app.nim.uikit.contact.core.item.AbsContactItem;
import com.earthman.app.nim.uikit.contact.core.item.ItemTypes;
import com.earthman.app.nim.uikit.contact.core.query.IContactDataProvider;
import com.earthman.app.nim.uikit.contact.core.query.TextQuery;

public class ContactDataProvider implements IContactDataProvider {

    private int[] itemTypes;

    public ContactDataProvider(int... itemTypes) {
        this.itemTypes = itemTypes;
    }

    @Override
    public List<AbsContactItem> provide(TextQuery query) {
        List<AbsContactItem> data = new ArrayList<AbsContactItem>();

        for (int itemType : itemTypes) {
            data.addAll(provide(itemType, query));
        }

        return data;
    }

    private final List<AbsContactItem> provide(int itemType, TextQuery query) {
        switch (itemType) {
            case ItemTypes.FRIEND:
                return UserDataProvider.provide(query);
            case ItemTypes.TEAM:
            case ItemTypes.TEAMS.ADVANCED_TEAM:
            case ItemTypes.TEAMS.NORMAL_TEAM:
                return TeamDataProvider.provide(query, itemType);
            default:
                return new ArrayList<AbsContactItem>();
        }
    }
}
