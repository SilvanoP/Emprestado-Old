package br.com.emprestado.dao;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.emprestado.model.Item;
import br.com.emprestado.model.ItensType;

public class ItemDAO extends BaseDAO<Item> {
	
	String[] columns = { "id", "type", "description", "personId", "mine", "date" };

	public ItemDAO(Context context) {
		super(context, null, 1, "ITEM");
	}

	public List<Item> getAll() {
		List<Item> itens = new ArrayList<Item>();

		Cursor c = getReadableDatabase().query(table, columns, null, null, null, null, null);

		while (c.moveToNext()) {
			Item i = get(c);
			itens.add(i);
		}
		c.close();
		return itens;
	}

	@Override
	public Item getById(Long id) {
		String selection = "id = ?";
		String selectionArgs[] = {id.toString()};
		
		Cursor c = getReadableDatabase().query(table, columns, selection, selectionArgs, null, null, null);
		
		Item i = get(c);
		c.close();
		
		return i;
	}
	
	public List<Item> getByCriteria(Map<String, String> criteria) {
		List<Item> itens = new ArrayList<Item>();
		String selection = "";
		String selectionArgs[] = new String[criteria.size()];
		int argIndex = 0;
		for (String column : criteria.keySet()) {
			selection += column + " = ?";
			selectionArgs[argIndex] = criteria.get(column);
			argIndex++;
			if (argIndex < criteria.size())
				selection += " and ";
		}
		selection.substring(0, selection.length()-5);
		
		Cursor c = getReadableDatabase().query(table, columns, selection, selectionArgs, null, null, null);
		
		while (c.moveToNext()) {
			Item i = get(c);
			itens.add(i);
		}
		c.close();
		
		return itens;
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public Long save(Item item) {
		ContentValues values = new ContentValues();
		values.put(columns[1], item.getType().id);
		values.put(columns[2], item.getDescription());
		values.put(columns[3], item.getPersonId());
		values.put(columns[4], item.getIsMine());
		if (item.getCalendar() != null)
			values.put(columns[5], new SimpleDateFormat("yyyyMMdd").format(item.getCalendar().getTime()));
		
		return getWritableDatabase().insert(table, null, values);
	}

	@SuppressLint("SimpleDateFormat")
	@Override
	public void update(Item item) {
		ContentValues values = new ContentValues();
		values.put(columns[1], item.getType().id);
		values.put(columns[2], item.getDescription());
		values.put(columns[3], item.getPersonId());
		values.put(columns[4], item.getIsMine());
		if (item.getCalendar() != null)
			values.put(columns[5], new SimpleDateFormat("yyyyMMdd").format(item.getCalendar().getTime()));
		
		getWritableDatabase().update(table, values, "id = ?", new String[]{item.getId().toString()});
	}

	@Override
	public void remove(Item item) {
		getWritableDatabase().delete(table, "id = ?", new String[]{item.getId().toString()});
	}
	
	@Override
	public void removeById(Long id) {
		getWritableDatabase().delete(table, "id = ?", new String[]{id.toString()});
		
	}
	
	@SuppressLint("SimpleDateFormat")
	private Item get(Cursor c) {
		Item i = new Item();
		i.setId(c.getLong(0));
		Integer type = c.getInt(1);
		ItensType itemType = ItensType.values()[type-1];
		i.setType(itemType);
		i.setDescription(c.getString(2));
		i.setPersonId(c.getLong(3));
		Integer mine = c.getInt(4);
		if (mine == 1)
			i.setIsMine(true);
		else
			i.setIsMine(false);
		String date = c.getString(5);
		Calendar cal = Calendar.getInstance();
		if (date != null && !date.equals("")){
			try {
				cal.setTime(new SimpleDateFormat("yyyyMMdd").parse(date));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			i.setCalendar(cal);
		}
		
		return i;
	}

}
