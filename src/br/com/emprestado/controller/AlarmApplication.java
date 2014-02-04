package br.com.emprestado.controller;

import android.app.Application;

public class AlarmApplication extends Application {

	public static ItemPersonService service; 
	
	@Override
	public void onCreate() {
		super.onCreate();
		
		service = new ItemPersonService(this);
	}
}
