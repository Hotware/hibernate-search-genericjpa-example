package org.hibernate.search.genericjpa.sample.entities;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;

import org.hibernate.search.annotations.ContainedIn;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.genericjpa.annotations.IdColumn;
import org.hibernate.search.genericjpa.annotations.IdInfo;
import org.hibernate.search.genericjpa.annotations.InIndex;
import org.hibernate.search.genericjpa.annotations.UpdateInfo;
import org.hibernate.search.genericjpa.db.ColumnType;

/**
 * Created by Martin on 25.07.2015.
 */
@Entity
@Table(name = "Author")
@InIndex
@UpdateInfo(tableName = "Author", idInfos = @IdInfo(columns = @IdColumn(column = "authorId", columnType = ColumnType.LONG)))
public class Author {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "authorId", columnDefinition = "BIGINT(64) NOT NULL")
	private Long authorId;

	@Column(name = "firstName", columnDefinition = "VARCHAR(50) NOT NULL")
	@Field
	private String firstName;

	@Column(name = "lastName", columnDefinition = "VARCHAR(50) NOT NULL")
	@Field
	private String lastName;

	@Column(name = "country", columnDefinition = "VARCHAR(100) NOT NULL")
	@Field
	private String country;

	@ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH, CascadeType.PERSIST, CascadeType.REFRESH})
	@JoinTable(name = "Author_Book", joinColumns = @JoinColumn(name = "authorFk", referencedColumnName = "authorId", columnDefinition = "BIGINT(64) NOT NULL"),
			inverseJoinColumns = @JoinColumn(name = "bookFk", referencedColumnName = "isbn", columnDefinition = "VARCHAR(100) NOT NULL"))
	@UpdateInfo(tableName = "Author_Book", idInfos = {
			@IdInfo(entity = Author.class, columns = @IdColumn(column = "authorFk", columnType = ColumnType.LONG, columnDefinition = "BIGINT(64) NOT NULL")),
			@IdInfo(entity = Book.class, columns = @IdColumn(column = "bookFk", columnType = ColumnType.STRING, columnDefinition = "VARCHAR(100) NOT NULL"))
	})
	@ContainedIn
	private Set<Book> books;

	public Long getAuthorId() {
		return authorId;
	}

	public void setAuthorId(Long authorId) {
		this.authorId = authorId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public Set<Book> getBooks() {
		return books;
	}

	public void setBooks(Set<Book> books) {
		this.books = books;
	}

	@Override
	public String toString() {
		return "Author{" +
				"authorId=" + authorId +
				", firstName='" + firstName + '\'' +
				", lastName='" + lastName + '\'' +
				", country='" + country + '\'' +
				'}';
	}
}
