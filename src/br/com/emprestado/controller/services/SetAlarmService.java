package br.com.emprestado.controller.services;

import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import br.com.emprestado.controller.AlarmApplication;
import br.com.emprestado.model.Item;
import br.com.emprestado.model.Person;

public class SetAlarmService extends IntentService {

	public static final String CREATE = "CREATE";
	public static final String CANCEL = "CANCEL";
	public static final String CREATE_ALL = "CREATE_ALL";

	private IntentFilter matcher;
	private Item item;

	public SetAlarmService() {
		super("SetAlarmService");

		matcher = new IntentFilter();
		matcher.addAction(CREATE);
		matcher.addAction(CANCEL);
		matcher.addAction(CREATE_ALL);
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		String action = intent.getAction();
		String notificationId = intent.getStringExtra("notificationId");
		item = (Item) intent.getSerializableExtra("item");

		if (matcher.matchAction(action))
			execute(action, notificationId);

	}

	private void execute(String action, String notificationId) {
		AlarmManager am = (AlarmManager) getSystemService(Context.ALARM_SERVICE);

		if (CREATE_ALL.equals(action)) {
			List<Item> itens = AlarmApplication.service.getAllItens();
			Calendar today = Calendar.getInstance();

			for (Item i : itens) {
				Calendar cal = i.getCalendar();
				if (cal != null && cal.get(Calendar.YEAR) == today.get(Calendar.YEAR)
						&& cal.get(Calendar.DAY_OF_YEAR) >= today.get(Calendar.DAY_OF_YEAR)) {
					createAlarm(i, am);
				} else
					am.cancel(createPedingIntent(i));
			}
		} else if (CREATE.equals(action) && item != null) {
			createAlarm(item, am);
		} else if (CANCEL.equals(action) && item != null)
			am.cancel(createPedingIntent(item));
	}

	private void createAlarm(Item i, AlarmManager am) {
		Calendar cal = i.getCalendar();
		if (cal != null) {
			cal.add(Calendar.HOUR_OF_DAY, 10);
			PendingIntent pi = createPedingIntent(i);
			am.cancel(pi);
			am.set(AlarmManager.RTC_WAKEUP, cal.getTimeInMillis(), pi);
		}
	}

	private PendingIntent createPedingIntent(Item i) {
		Person person = AlarmApplication.service.getPersonByItem(i);

		Intent intent = new Intent(this, AlarmReceiver.class);
		intent.putExtra("id", i.getId());
		intent.putExtra("item", i);
		String verb = "";
		if (i.getIsMine())
			verb = "emprestado para";
		else
			verb = "emprestado por";
		if (person != null)
			intent.putExtra("msg", i.getDescription() + " " + verb + " " + person.getName());
		else
			intent.putExtra("msg", i.getDescription());

		PendingIntent pi = PendingIntent.getBroadcast(this, 0, intent,
				PendingIntent.FLAG_UPDATE_CURRENT);
		return pi;
	}

}
