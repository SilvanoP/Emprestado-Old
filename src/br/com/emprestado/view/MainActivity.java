package br.com.emprestado.view;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBar.Tab;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;
import br.com.emprestado.R;
import br.com.emprestado.controller.TabController;

public class MainActivity extends ActionBarActivity {

	private ActionBar actionBar;
	//private ViewPager mViewPager;
	//private TabPagerAdapter mAdapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		actionBar = getSupportActionBar();	
		actionBar.setHomeButtonEnabled(false);
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.ICE_CREAM_SANDWICH)
			actionBar.setLogo(R.drawable.ic_launcher_bar);
		
		Tab tab = actionBar.newTab().setText(R.string.all_tab)
				.setTabListener(new TabController<LentListActivity>(this, "todos"));
		actionBar.addTab(tab);
		tab = actionBar.newTab().setText(R.string.lent_tab)
				.setTabListener(new TabController<LentListActivity>(this, "emprestei"));
		actionBar.addTab(tab,false);
		tab = actionBar.newTab().setText(R.string.borrowed_tab)
				.setTabListener(new TabController<LentListActivity>(this, "peguei"));
		actionBar.addTab(tab,false);
		
		actionBar.setSelectedNavigationItem(0);

	}

	@Override
	protected void onResume() {
		super.onResume();

		actionBar.setSelectedNavigationItem(0); // Always goes to ALL tab
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.lent_list, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.item_add:
			Intent intent = new Intent(this, SaveLoanActivity.class);
			if (actionBar.getSelectedTab().getPosition() == 2)
				intent.putExtra("borrowed", true);
			startActivity(intent);
			break;
		default:
			break;
		}
		return false;
	}
}
