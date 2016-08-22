package com.habitrpg.android.habitica.old.callbacks;

import com.magicmicky.habitrpgwrapper.lib.models.HabitRPGUser;
import com.magicmicky.habitrpgwrapper.lib.models.Items;

import rx.functions.Action1;

public class ItemsCallback implements Action1<Items> {

    private final HabitRPGUserCallback.OnUserReceived mCallback;

    private HabitRPGUser user;

    public ItemsCallback(HabitRPGUserCallback.OnUserReceived callback, HabitRPGUser user) {
        this.mCallback = callback;
        this.user = user;
    }

    @Override
    public void call(Items items) {
        this.user.setItems(items);
        this.user.async().save();
        mCallback.onUserReceived(this.user);
    }
}
