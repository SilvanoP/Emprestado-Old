package br.com.emprestado.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import br.com.emprestado.model.Person;

public class PersonDAO extends BaseDAO<Person> {
	
	private String columns[] = {"id", "name", "phone", "email"}; 
	
	public PersonDAO(Context context) {
		super(context, null, 1, "PERSON");
	}

	public List<Person> getAll() {
		List<Person> people = new ArrayList<Person>();
		
		Cursor c = getReadableDatabase().query(table, columns, null, null, null, null, null);
		
		while (c.moveToNext()) {
			Person p = get(c);
			people.add(p);
		}
		c.close();
		return people;
	}

	@Override
	public Person getById(Long id) {
		String selection = "id = ?";
		String selectionArgs[] = {id.toString()};
		
		Cursor c = getReadableDatabase().query(table, columns, selection, selectionArgs, null, null, null);
		
		Person p = get(c);
		c.close();
		
		return p;
	}

	@Override
	public Long save(Person person) {
		ContentValues values = new ContentValues();
		values.put(columns[1], person.getName());
		values.put(columns[2], person.getPhone());
		values.put(columns[3], person.getEmail());

		return getWritableDatabase().insert(table, null, values);
	}
	
	public void update(Person person) {
		ContentValues values = new ContentValues();
		values.put(columns[1], person.getName());
		values.put(columns[2], person.getPhone());
		values.put(columns[3], person.getEmail());
		
		getWritableDatabase().update(table, values, "id = ?", new String[]{person.getId().toString()});
	}

	@Override
	public void remove(Person person) {
		getWritableDatabase().delete(table, "id = ?", new String[]{person.getId().toString()});
	}
	
	@Override
	public void removeById(Long id) {
		getWritableDatabase().delete(table, "id = ?", new String[]{id.toString()});
	}
	
	private Person get(Cursor c) {
		if(!c.moveToFirst())
			return null;
		
		Person p = new Person();
		p.setId(c.getLong(0));
		p.setName(c.getString(1));
		p.setPhone(c.getString(2));
		p.setEmail(c.getString(3));
		
		return p;
	}

}
