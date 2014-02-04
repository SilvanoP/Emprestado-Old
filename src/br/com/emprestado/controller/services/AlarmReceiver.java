package br.com.emprestado.controller.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import br.com.emprestado.R;
import br.com.emprestado.model.Item;
import br.com.emprestado.view.SaveLoanActivity;

public class AlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		long id = intent.getLongExtra("id", 0);
		String msg = intent.getStringExtra("msg");
		Item item = (Item) intent.getSerializableExtra("item");
		
		Intent newIntent = new Intent(context, SaveLoanActivity.class);
		newIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
		if (item != null) {
			Log.i("INFO", "Item encontrado");
			newIntent.putExtra("SelectedItem", item);
		}
		PendingIntent pi = PendingIntent.getActivity(context, 0, newIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		
		Notification not = new NotificationCompat.Builder(context)
		.setContentTitle("Data de devolução atingida")
		.setContentText(msg)
		.setContentIntent(pi)
		.setSmallIcon(R.drawable.ic_launcher)
		.setAutoCancel(true)
		//.addAction(R.drawable.ic_launcher, "Adiar", pi)
		.build();
		
		NotificationManager nm = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		nm.notify((int)id, not);
	}

}
