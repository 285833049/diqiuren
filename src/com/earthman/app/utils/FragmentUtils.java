package com.earthman.app.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

public class FragmentUtils {

	//显示Fragment
	public static void commit(FragmentActivity activity, int layoutId, Fragment fragment) {
		FragmentTransaction fragmentTransaction = activity.getSupportFragmentManager().beginTransaction();
		fragmentTransaction.replace(layoutId, fragment);
		fragmentTransaction.commit();
	}

}
