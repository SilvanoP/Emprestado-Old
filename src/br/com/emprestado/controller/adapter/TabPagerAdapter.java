package br.com.emprestado.controller.adapter;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import br.com.emprestado.view.LentListActivity;

public class TabPagerAdapter extends FragmentPagerAdapter {

	public TabPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	@Override
	public Fragment getItem(int index) {
		LentListActivity fragment = new LentListActivity();
        Bundle args = new Bundle();
        switch (index) {
		case 1:
			args.putString("TabTag", "emprestei");
			fragment.setArguments(args);
			break;
		case 2:
			args.putString("TabTag", "peguei");
			fragment.setArguments(args);
			break;
		default:
			args.putString("TabTag", "todos");
			fragment.setArguments(args);
			break;
		}
		return fragment;
	}

	@Override
	public int getCount() {
		return 3;
	}

}
