package br.com.emprestado.view;

import java.util.Calendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract.CommonDataKinds.Email;
import android.provider.ContactsContract.CommonDataKinds.Phone;
import android.provider.ContactsContract.Contacts;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Toast;
import br.com.emprestado.R;
import br.com.emprestado.controller.ItemPersonService;
import br.com.emprestado.controller.SaveLoanHelper;
import br.com.emprestado.controller.services.SetAlarmService;
import br.com.emprestado.model.Item;
import br.com.emprestado.model.Person;

public class SaveLoanActivity extends ActionBarActivity {

	private final int CONTACT_PICKER_RESULT = 1001;

	private Item item;
	private Person person;

	private ItemPersonService service;
	private SaveLoanHelper helper;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_save_loan);

		Intent intent = this.getIntent();
		item = (Item) intent.getSerializableExtra("SelectedItem");
		boolean borrowed = (boolean) intent.getBooleanExtra("borrowed", false);

		showActionBar();

		helper = new SaveLoanHelper(this);
		service = new ItemPersonService(this);

		if (item != null) {
			person = service.getPersonByItem(item);
			helper.populateAllViews(item, person);
		} else {
			person = new Person();
			helper.fillOwnCheck(borrowed);
			helper.setDateText();
		}

	}

	public void showActionBar() {
		LayoutInflater inflator = (LayoutInflater) this
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		View v = inflator.inflate(R.layout.action_bar_save_loan, null);

		Button saveButton = (Button) v.findViewById(R.id.actionbar_button_save);
		OnClickListener saveListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				save();
			}
		};
		saveButton.setOnClickListener(saveListener);

		View divider = v.findViewById(R.id.actionbar_divider);
		Button removeButton = (Button) v.findViewById(R.id.actionbar_button_remove);

		if (this.item != null && this.item.getId() != null) {
			OnClickListener removeListener = new OnClickListener() {
				@Override
				public void onClick(View v) {
					remove();
				}
			};
			removeButton.setOnClickListener(removeListener);
		} else {
			divider.setVisibility(View.INVISIBLE);
			removeButton.setVisibility(View.INVISIBLE);
		}

		ActionBar bar = getSupportActionBar();
		bar.setDisplayHomeAsUpEnabled(false);
		bar.setDisplayShowHomeEnabled(false);
		bar.setDisplayShowCustomEnabled(true);
		bar.setDisplayShowTitleEnabled(false);
		bar.setCustomView(v);
	}

	public void save() {
		item = helper.getItemFromForm();
		person = helper.getPersonFromForm();

		// Verify if the date inserted is before today
		// If it is, warn and return, so a valid date can be inserted
		Calendar today = Calendar.getInstance();
		Calendar cal = item.getCalendar();
		if (cal != null)
			if (cal.get(Calendar.YEAR) < today.get(Calendar.YEAR)
					|| (cal.get(Calendar.YEAR) == today.get(Calendar.YEAR) && cal
							.get(Calendar.DAY_OF_YEAR) < today.get(Calendar.DAY_OF_YEAR))) {
				Toast.makeText(this, "Datas anteriores a de hoje não são aceitas, favor corrigir!",
						Toast.LENGTH_LONG).show();
				return;
			}
		// Verify if the descripion or person name are blank
		// If it is ,warn and return, since those a required fields
		if (item.getDescription() == null || item.getDescription().equals("")) {
			Toast.makeText(this, "Por favor adicione uma descrição!", Toast.LENGTH_LONG).show();
			return;
		} else if (person.getName() == null || person.getName().equals("")) {
			Toast.makeText(this, "Por favor adicione o nome do contato!", Toast.LENGTH_LONG).show();
			return;
		}

		service.save(item, person);

		Intent i = new Intent(this, SetAlarmService.class);
		i.setAction(SetAlarmService.CREATE);
		i.putExtra("item", item);
		this.startService(i);

		finish();
	}

	private void remove() {
		if (item != null && item.getId() != null) {
			service.remove(item);

			finish();
		} else
			Toast.makeText(this, "Item não existente", Toast.LENGTH_SHORT).show();
	}

	/**
	 * Executed when the user click on the textview related to date The textview
	 * call can be seen on the layout file
	 * 
	 * @param view
	 */
	public void onShowDateDialog(View view) {
		FragmentManager fm = getSupportFragmentManager();
		DatePickerFragment dpf = new DatePickerFragment(new OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				helper.setDateText(year, monthOfYear, dayOfMonth);
			}
		});
		dpf.show(fm, "date_picker");
	}

	/**
	 * Executed when the user click on the button seach contacts The button call
	 * can be seen on the layout file
	 * 
	 * @param view
	 */
	public void onShowContactPicker(View view) {
		Intent intent = new Intent(Intent.ACTION_PICK, Contacts.CONTENT_URI);
		startActivityForResult(intent, CONTACT_PICKER_RESULT);
	}

	/**
	 * Get the contact selected by the user and assigns it's details to the
	 * field related.
	 */
	@Override
	protected void onActivityResult(int reqCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			switch (reqCode) {
			case CONTACT_PICKER_RESULT:
				ContentResolver cr = getContentResolver();
				Uri contactData = data.getData();
				Cursor c = cr.query(contactData, null, null, null, null);
				if (c.moveToFirst()) {
					String id = c.getString(c.getColumnIndex(Contacts._ID));
					String name = c.getString(c.getColumnIndex(Contacts.DISPLAY_NAME));
					String email = "";
					String phone = "";
					String hasPhone = c.getString(c.getColumnIndex(Contacts.HAS_PHONE_NUMBER));
					if (hasPhone != null && hasPhone.equals("1")) {
						Cursor phones = cr.query(Phone.CONTENT_URI, null, Phone.CONTACT_ID + " = "
								+ id, null, null);
						if (phones != null && phones.getCount() > 0) {
							phones.moveToFirst();
							Integer phoneIndex = phones.getColumnIndex(Phone.NUMBER);
							if (!phones.isNull(phoneIndex))
								phone = phones.getString(phones.getColumnIndex(Phone.NUMBER));
						}
						phones.close();
					}
					Cursor emailC = cr.query(Email.CONTENT_URI, null,
							Email.CONTACT_ID + " = " + id, null, null);
					if (emailC != null) {
						emailC.moveToFirst();
						Integer emailIndex = emailC.getColumnIndex(Email.DATA);

						if (emailIndex != null && emailC.getCount() >= emailIndex)
							if (!emailC.isNull(emailIndex))
								email = emailC.getString(emailIndex);
						emailC.close();
					}

					person.setName(name);
					person.setEmail(email);
					person.setPhone(phone);
					helper.populatePersonViews(person);
				}
				c.close();
				break;

			default:
				break;
			}
		} else
			Log.w("DEBUG", "Result not ok!");
	}

}
