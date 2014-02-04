package br.com.emprestado.controller.services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmSetter extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Intent i = new Intent(context, SetAlarmService.class);
		i.setAction(SetAlarmService.CREATE_ALL);
		context.startService(i);
	}

}
