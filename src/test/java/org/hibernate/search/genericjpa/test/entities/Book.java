/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.genericjpa.test.entities;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import java.util.Set;

import org.hibernate.search.annotations.DocumentId;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Index;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.hibernate.search.genericjpa.annotations.IdColumn;
import org.hibernate.search.genericjpa.annotations.IdInfo;
import org.hibernate.search.genericjpa.annotations.InIndex;
import org.hibernate.search.genericjpa.annotations.UpdateInfo;
import org.hibernate.search.genericjpa.db.events.ColumnType;

/**
 * Created by Martin on 24.06.2015.
 */
@Entity
@Table(name = "Book")
@InIndex
@Indexed
@UpdateInfo(tableName = "Book", idInfos = @IdInfo(columns = @IdColumn(column = "isbn", columnType = ColumnType.STRING)))
public class Book {

	@Id
	@DocumentId
	@Column(name = "isbn", columnDefinition = "VARCHAR(100) NOT NULL")
	private String isbn;

	@Column(name = "title", columnDefinition = "VARCHAR(100) NOT NULL")
	@Field(store = Store.YES, index = Index.YES, name = "nameStored")
	private String title;

	@Column(name = "genre", columnDefinition = "VARCHAR(100) NOT NULL")
	@Field
	private String genre;

	@Lob
	@Column(name = "summary", columnDefinition = "BLOB NOT NULL")
	@Field
	private String summary;

	@ManyToMany(mappedBy = "books", cascade = {
			CascadeType.MERGE,
			CascadeType.DETACH,
			CascadeType.PERSIST,
			CascadeType.REFRESH
	})
	@IndexedEmbedded(includeEmbeddedObjectId = true)
	private Set<Author> authors;

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getGenre() {
		return genre;
	}

	public void setGenre(String genre) {
		this.genre = genre;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getIsbn() {
		return isbn;
	}

	public Set<Author> getAuthors() {
		return authors;
	}

	public void setAuthors(Set<Author> authors) {
		this.authors = authors;
	}
}
