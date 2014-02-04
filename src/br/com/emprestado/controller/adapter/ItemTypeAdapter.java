package br.com.emprestado.controller.adapter;

import java.lang.reflect.Field;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import br.com.emprestado.R;
import br.com.emprestado.model.ItensType;

public class ItemTypeAdapter extends ArrayAdapter<ItensType> {

	private Context context;
	private ItensType[] types;

	public ItemTypeAdapter(Context context, int resource, ItensType[] objects) {
		super(context, resource, objects);

		this.context = context;
		this.types = objects;
	}

	@Override
	public int getCount() {
		return types.length;
	}

	@Override
	public ItensType getItem(int position) {
		return types[position];
	}

	@Override
	public long getItemId(int position) {
		return types[position].id;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView typeView = new TextView(context);
		Integer res = null;
		try {
			if (position >= 0) {
				Field f = R.string.class.getField(types[position].name());
				res = (Integer) f.get(null);
			}
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		
		typeView.setTextSize(15f);

		if (res != null)
			typeView.setText(res);
		else
			typeView.setText("Tipo");

		return typeView;
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getView(position, convertView, parent);
	}

}
