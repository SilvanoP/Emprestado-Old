package br.com.emprestado.controller;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBar.TabListener;
import br.com.emprestado.R;
import br.com.emprestado.view.LentListActivity;

public class TabController<T extends Fragment> implements TabListener {

	private final Activity mContext;
	private Fragment mFrag;
	private String tag;

	/**
	 * Constructor used each time a new tab is created.
	 */
	public TabController(Activity activity, String mTag) {
		mContext = activity;
		tag = mTag;
	}

	/* The following are each of the ActionBar.TabListener callbacks */

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		if (mFrag == null) {
			// If not, instantiate and add it to the activity
			Bundle args = new Bundle();
			args.putString("TabTag", tag);
			mFrag = Fragment.instantiate(mContext, LentListActivity.class.getName(), args);
			//Add the fragment into the correct content
			if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
				ft.add(R.id.action_bar_activity_content, mFrag, tag);
			else
				ft.add(android.R.id.content, mFrag, tag);
		} else {
			// If it exists, simply attach it in order to show it
			ft.attach(mFrag);
		}

		//mPager.setCurrentItem(tab.getPosition());
	}

	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (mFrag != null) {
	           // Detach the fragment, because another one is being attached
	           ft.detach(mFrag);
	       }
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// User selected the already selected tab. Usually do nothing.
	}

}
