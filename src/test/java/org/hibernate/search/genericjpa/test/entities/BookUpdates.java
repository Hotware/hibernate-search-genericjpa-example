package org.hibernate.search.genericjpa.test.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.search.genericjpa.annotations.Event;
import org.hibernate.search.genericjpa.annotations.IdFor;
import org.hibernate.search.genericjpa.annotations.Updates;

/**
 * Created by Martin on 24.06.2015.
 */
@Entity(name = "BookUpdates")
@Updates(originalTableName = "Book", tableName = "BookUpdates")
public class BookUpdates {

	@Id
	@Column(name = "id")
	private Long id;

	@Column(name = "bookId")
	@IdFor(entityClass = Book.class, columns = "bookId", columnsInOriginal = "name")
	private String bookId;

	@Column(name = "eventCase")
	@Event(column = "eventCase")
	private Integer eventCase;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getBookId() {
		return bookId;
	}

	public void setBookId(String bookId) {
		this.bookId = bookId;
	}

	public Integer getEventCase() {
		return eventCase;
	}

	public void setEventCase(Integer eventCase) {
		this.eventCase = eventCase;
	}
}
