package br.com.emprestado.dao;

import java.io.Serializable;
import java.util.List;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public abstract class BaseDAO<T extends Serializable> extends SQLiteOpenHelper {
	
	private static final String DATABASE = "EMPRESTADO";
	
	protected String table;
	protected Context context;
	
	public BaseDAO(Context context, CursorFactory factory, int version, String table) {
		super(context, DATABASE, factory, version);
		this.context = context; 
		this.table = table;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		String createItem = "CREATE TABLE ITEM (id INTEGER PRIMARY KEY, type INTEGER, description TEXT, " +
				"personId INTEGER, mine INTEGER, date TEXT ); \n";
		String createPerson = "CREATE TABLE PERSON (id INTEGER PRIMARY KEY, name TEXT, phone TEXT, " +
				"email TEXT );";
		db.execSQL(createItem);
		db.execSQL(createPerson);
	}
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		if (oldVersion < newVersion) {
			// Include new table config if needed
		}
	}
	
	public abstract List<T> getAll();
	public abstract T getById(Long id);
	public abstract Long save(T t);
	public abstract void update(T t);
	public abstract void remove(T t);
	public abstract void removeById(Long id);
}