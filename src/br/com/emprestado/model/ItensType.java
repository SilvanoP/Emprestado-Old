package br.com.emprestado.model;

public enum ItensType {
	BOOK(1),
	CD(2),
	DVD(3),
	BLURAY(4),
	MONEY(5),
	OTHER(6);
	
	public final long id;
	
	ItensType(long id) {
		this.id = id;
	}
}
