/*
 * Hibernate Search, full-text search for your domain model
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.search.genericjpa.test.integration;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Properties;

import org.apache.lucene.search.MatchAllDocsQuery;

import org.hibernate.search.genericjpa.JPASearchFactoryController;
import org.hibernate.search.genericjpa.Setup;
import org.hibernate.search.genericjpa.sample.entities.Author;
import org.hibernate.search.genericjpa.sample.entities.Book;
import org.hibernate.search.genericjpa.util.Sleep;
import org.hibernate.search.jpa.FullTextEntityManager;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by Martin on 24.06.2015.
 */
public class IntegrationTest {

	private static final int BOOK_COUNT = 100;

	private EntityManagerFactory emf;
	private EntityManager em;
	private JPASearchFactoryController searchController;

	@Test
	public void testBasicIntegration() throws InterruptedException {
		Sleep.sleep(
				100_000, () -> BOOK_COUNT ==
						this.searchController.getFullTextEntityManager( this.em )
								.createFullTextQuery( new MatchAllDocsQuery(), Book.class )
								.getResultSize()
				, 100, ""
		);
	}

	@Before
	public void setup() {
		this.emf = Persistence.createEntityManagerFactory( "EclipseLink_MySQL" );
		this.em = this.emf.createEntityManager();
		Properties properties = new Properties();
		try (InputStream is = IntegrationTest.class.getResourceAsStream( "/META-INF/hsearch.properties" )) {
			properties.load( is );
		}
		catch (IOException e) {
			throw new RuntimeException( e );
		}
		this.searchController = Setup.createSearchFactoryController( this.emf, properties );


		FullTextEntityManager fem = this.searchController.getFullTextEntityManager( this.em );
		this.em.getTransaction().begin();
		for ( int i = 0; i < BOOK_COUNT; ++i ) {
			Author author = new Author();
			author.setFirstName( "Author" + i );
			author.setLastName( "LastName" + i );
			author.setCountry( "Country" + i );
			Book book = new Book();
			book.setIsbn( "ISBN" + i );
			book.setGenre( "Genre" + i );
			book.setSummary( "some kind of summary" + i );
			book.setTitle( "Title" + i );
			book.setAuthors( new HashSet<>( Arrays.asList( author ) ) );
			author.setBooks( new HashSet<>( Arrays.asList( book ) ) );
			this.em.persist( book );
		}
		this.em.getTransaction().commit();
		this.em.clear();
	}

	@After
	public void shutdown() {
		if ( this.searchController != null ) {
			this.searchController.close();
		}
		if ( this.em != null ) {
			this.em.close();
		}
		if ( this.emf != null ) {
			this.emf.close();
		}
	}

}
