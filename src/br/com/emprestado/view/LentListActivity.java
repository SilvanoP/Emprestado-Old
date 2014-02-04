package br.com.emprestado.view;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import br.com.emprestado.R;
import br.com.emprestado.controller.ItemPersonService;
import br.com.emprestado.controller.adapter.ItemListArrayAdapter;
import br.com.emprestado.model.Item;

public class LentListActivity extends Fragment {
	
	private ListView lentList;
	private ItemListArrayAdapter itemAdapter;
	private ItemPersonService service;
	private Item selectedItem;
	private String tag;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		service = new ItemPersonService(getActivity());
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.activity_lent_list, container);
		Bundle args = getArguments();
		if (args != null)
			tag = getArguments().getString("TabTag");
		else
			tag = "";
		lentList = (ListView) view.findViewById(R.id.list_item);
		refreshList();
		
		registerForContextMenu(lentList);
		
		lentList.setOnItemClickListener(new OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> adapter, View view, int position, long id) {
				selectedItem = (Item) lentList.getItemAtPosition(position);
				Intent intent = new Intent(getActivity(), SaveLoanActivity.class);
				intent.putExtra("SelectedItem", selectedItem);
				startActivity(intent);
			}
		});
		
		lentList.setOnItemLongClickListener(new OnItemLongClickListener() {
			@Override
			public boolean onItemLongClick(AdapterView<?> adapter, View view, int position, long id) {
				selectedItem = (Item) lentList.getItemAtPosition(position);
				return false;
			}
		});
		
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		refreshList();
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getActivity().getMenuInflater().inflate(R.menu.lent_list_context, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_remove:
			service.remove(selectedItem);
			refreshList();
			break;
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}
	
	public void refreshList() {
		Map<String, String> criteria = new Hashtable<String, String>();
		
		if (tag.equals("emprestei")){
			criteria.put("mine", "1"); // 1 means true
			List<Item> itens = service.getItensByCriteria(criteria);
			itemAdapter = new ItemListArrayAdapter(getActivity(), service, itens);
		} else if(tag.equals("peguei")) {
			criteria.put("mine", "0"); // 0 means false
			List<Item> itens = service.getItensByCriteria(criteria);
			itemAdapter = new ItemListArrayAdapter(getActivity(), service, itens);
		} else
			itemAdapter = new ItemListArrayAdapter(getActivity(), service);
		
		lentList.setAdapter(itemAdapter);
	}

}
