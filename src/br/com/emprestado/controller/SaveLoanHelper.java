package br.com.emprestado.controller;

import java.util.Calendar;
import android.app.Activity;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import br.com.emprestado.R;
import br.com.emprestado.controller.adapter.ItemTypeAdapter;
import br.com.emprestado.model.Item;
import br.com.emprestado.model.ItensType;
import br.com.emprestado.model.Person;

public class SaveLoanHelper {

	private Item item;
	private Person person;
	private Calendar deliveryDate;
	private ItemTypeAdapter adapter;

	private EditText itemDesc;
	private Spinner itemTypeSpinner;
	private CheckBox ownCheck;
	private TextView dateText;
	private EditText personNameText;
	private EditText personEmailText;
	private EditText personPhoneText;

	public SaveLoanHelper(Activity activity) {
		super();

		item = new Item();
		person = new Person();

		itemDesc = (EditText) activity.findViewById(R.id.edit_item_desc);
		itemTypeSpinner = (Spinner) activity.findViewById(R.id.spinner_type);
		ownCheck = (CheckBox) activity.findViewById(R.id.check_own);
		dateText = (TextView) activity.findViewById(R.id.text_date_format);
		personNameText = (EditText) activity.findViewById(R.id.edit_person_name);
		personEmailText = (EditText) activity.findViewById(R.id.edit_person_email);
		personPhoneText = (EditText) activity.findViewById(R.id.edit_person_phone);

		ItensType types[] = ItensType.values();
		adapter = new ItemTypeAdapter(activity, android.R.layout.simple_spinner_item, types);
		itemTypeSpinner.setAdapter(adapter);
	}

	public Item getItemFromForm() {
		item.setDescription(itemDesc.getText().toString());
		item.setIsMine(!ownCheck.isChecked());
		if (itemTypeSpinner.getSelectedItem() instanceof ItensType)
			item.setType((ItensType) itemTypeSpinner.getSelectedItem());
		if (deliveryDate != null)
			item.setCalendar(deliveryDate);
		if (person.getId() != null)
			item.setPersonId(person.getId());
		return item;
	}

	public Person getPersonFromForm() {
		person.setName(personNameText.getText().toString());
		person.setEmail(personEmailText.getText().toString());
		person.setPhone(personPhoneText.getText().toString());

		return person;
	}

	public void populateAllViews(Item i, Person p) {
		this.item = i;

		itemDesc.setText(item.getDescription());
		itemTypeSpinner.setSelection(adapter.getPosition(item.getType()));
		deliveryDate = item.getCalendar();
		setDateText();
		if (!item.getIsMine())
			ownCheck.setChecked(true);

		if (p != null)
			populatePersonViews(p);
		
	}
	
	public void fillOwnCheck(boolean borrowed) {
		ownCheck.setChecked(borrowed);
	}

	public void populatePersonViews(Person p) {
		this.person = p;

		if (person.getName() != null)
			personNameText.setText(person.getName());
		if (person.getEmail() != null)
			personEmailText.setText(person.getEmail());
		if (person.getPhone() != null)
			personPhoneText.setText(person.getPhone());
	}

	public void setDateText() {
		if (deliveryDate == null) {
			dateText.setText(R.string.no_date);
		} else {
			int month = deliveryDate.get(Calendar.MONTH) + 1;

			String date_format = deliveryDate.get(Calendar.DAY_OF_MONTH) + "/" + month + "/"
					+ deliveryDate.get(Calendar.YEAR) + "  ";
			dateText.setText(date_format);
		}
	}

	public void setDateText(int year, int month, int day) {
		if (deliveryDate == null)
			deliveryDate = Calendar.getInstance();
		deliveryDate.set(Calendar.YEAR, year);
		deliveryDate.set(Calendar.MONTH, month);
		deliveryDate.set(Calendar.DAY_OF_MONTH, day);
		setDateText();
	}
}
