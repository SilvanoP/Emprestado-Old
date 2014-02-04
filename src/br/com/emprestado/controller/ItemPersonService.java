package br.com.emprestado.controller;

import java.util.List;
import java.util.Map;

import android.content.Context;
import br.com.emprestado.dao.ItemDAO;
import br.com.emprestado.dao.PersonDAO;
import br.com.emprestado.model.Item;
import br.com.emprestado.model.Person;

public class ItemPersonService {

	ItemDAO itemDAO;
	PersonDAO personDAO;

	public ItemPersonService(Context context) {
		itemDAO = new ItemDAO(context);
		personDAO = new PersonDAO(context);
	}

	public List<Item> getAllItens() {
		List<Item> itens = itemDAO.getAll();
		return itens;
	}

	public List<Item> getItensByCriteria(Map<String, String> criteria) {
		List<Item> itens = itemDAO.getByCriteria(criteria);
		itemDAO.close();
		return itens;
	}

	public List<Person> getAllPeople() {
		List<Person> people = personDAO.getAll();
		personDAO.close();
		return people;
	}

	public Person getPersonByItem(Item i) {
		if (i == null)
			return null;

		Long id = i.getPersonId();
		if (id == null)
			return null;
		Person p = personDAO.getById(id);

		personDAO.close();
		return p;
	}

	public void save(Item item, Person person) {
		if (item.getId() == null) {
			Long personId = null;
			if (person != null)
				personId = personDAO.save(person);

			item.setPersonId(personId);
			itemDAO.save(item);

		} else {
			if (person.getId() == null) {
				Long personId = personDAO.save(person);
				item.setPersonId(personId);
			} else
				personDAO.update(person);
			itemDAO.update(item);
		}
		personDAO.close();
		itemDAO.close();
	}

	public void saveItem(Item item) {
		save(item, null);
	}

	public void remove(Item item) {
		if (item.getPersonId() != null) {
			Long personId = item.getPersonId();
			personDAO.removeById(personId);
			personDAO.close();
		}
		itemDAO.remove(item);
		itemDAO.close();
	}
}
