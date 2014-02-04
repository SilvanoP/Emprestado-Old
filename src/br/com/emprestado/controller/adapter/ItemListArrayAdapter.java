package br.com.emprestado.controller.adapter;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import br.com.emprestado.R;
import br.com.emprestado.controller.ItemPersonService;
import br.com.emprestado.model.Item;
import br.com.emprestado.model.Person;

public class ItemListArrayAdapter extends BaseAdapter {

	private final Activity activity;
	private final ItemPersonService service;
	private final List<Item> itens;

	public ItemListArrayAdapter(Activity activity, ItemPersonService service) {
		super();
		this.activity = activity;
		this.itens = service.getAllItens();
		this.service = service;

		Collections.sort(itens);
	}

	public ItemListArrayAdapter(Activity activity, ItemPersonService service, List<Item> itens) {
		super();
		this.activity = activity;
		this.itens = itens;
		this.service = service;

		Collections.sort(itens);
	}

	@Override
	public int getCount() {
		return itens.size();
	}

	@Override
	public Object getItem(int prosition) {
		return itens.get(prosition);
	}

	@Override
	public long getItemId(int position) {
		return itens.get(position).getId();
	}

	@Override
	public View getView(int position, View contentView, ViewGroup parent) {
		View view = activity.getLayoutInflater().inflate(R.layout.list_item, null);
		Item item = itens.get(position);
		Person person = service.getPersonByItem(item);
		String personName = "";
		if (person != null) {
			if (item.getIsMine())
				personName = "Para: ";
			else
				personName = "Por: ";
			personName += person.getName();
		}

		ImageView image = (ImageView) view.findViewById(R.id.image_item_pic);
		TextView item_name = (TextView) view.findViewById(R.id.text_item_desc);
		TextView person_name = (TextView) view.findViewById(R.id.text_person_name);
		TextView deliver_date = (TextView) view.findViewById(R.id.text_list_limit_date);

		Bitmap bm;
		switch (item.getType()) {
		case BOOK:
			bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.book_itens);
			break;
		case MONEY:
			bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.money_itens);
			break;
		case BLURAY:
		case CD:
		case DVD:
			bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.cd_itens);
			break;
		case OTHER:
			bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.other_itens);
			break;
		default:
			bm = BitmapFactory.decodeResource(activity.getResources(), R.drawable.other_itens);
		}

		// Fills the delivery date field based on its value
		Calendar deliveryDate = item.getCalendar();
		String dateFormat = "";
		if (deliveryDate != null) {
			Calendar today = Calendar.getInstance();
			if (deliveryDate.get(Calendar.YEAR) == today.get(Calendar.YEAR)
					&& deliveryDate.get(Calendar.DAY_OF_YEAR) == today.get(Calendar.DAY_OF_YEAR))
				deliver_date.setText(R.string.date_today);
			else {
				int month = deliveryDate.get(Calendar.MONTH) + 1;
				dateFormat = deliveryDate.get(Calendar.DAY_OF_MONTH) + "/" + month + "/"
						+ deliveryDate.get(Calendar.YEAR);
				deliver_date.setText(dateFormat);
				
				if (deliveryDate.before(today))
					deliver_date.setTextColor(Color.RED);
			}
		} else
			deliver_date.setText(R.string.no_date_list);

		image.setImageBitmap(bm);
		item_name.setText(item.getDescription());
		person_name.setText(personName);

		return view;
	}

}
