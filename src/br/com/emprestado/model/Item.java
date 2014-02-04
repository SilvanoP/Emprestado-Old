package br.com.emprestado.model;

import java.io.Serializable;
import java.util.Calendar;

public class Item implements Serializable, Comparable<Item> {

	private static final long serialVersionUID = 7565546631196080386L;

	private Long id;
	private ItensType type;
	private String description;
	private Integer quantity;
	private Long personId;
	private Boolean isMine;
	private Calendar calendar;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public ItensType getType() {
		if (type == null)
			return ItensType.OTHER;
		return type;
	}

	public void setType(ItensType type) {
		this.type = type;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Integer getQuantity() {
		return quantity;
	}

	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}

	public Long getPersonId() {
		return personId;
	}

	public void setPersonId(Long personId) {
		this.personId = personId;
	}

	public Boolean getIsMine() {
		return isMine;
	}

	public void setIsMine(Boolean isMine) {
		this.isMine = isMine;
	}

	public Calendar getCalendar() {
		return calendar;
	}

	public void setCalendar(Calendar calendar) {
		this.calendar = calendar;
	}

	@Override
	public String toString() {
		return description;
	}

	@Override
	public int compareTo(Item another) {
		if (calendar == null && another.getCalendar() != null)
			return 1;
		else if (calendar != null && another.getCalendar() == null)
			return -1;
		else if (calendar == null && another.getCalendar() == null)
			return 0;
		else if (calendar.before(another.getCalendar()))
			return -1;
		else if (calendar.after(another.getCalendar()))
			return 1;
		return 0;
	}
}
