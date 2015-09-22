package org.hibernate.search.genericjpa.sample;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.Properties;
import java.util.Scanner;

import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.Query;

import org.hibernate.search.genericjpa.JPASearchFactoryController;
import org.hibernate.search.genericjpa.Setup;
import org.hibernate.search.genericjpa.sample.entities.Author;
import org.hibernate.search.genericjpa.sample.entities.Book;
import org.hibernate.search.jpa.FullTextEntityManager;

/**
 * Created by Martin on 22.09.2015.
 */
public class Main {

	public static void main(String[] args) throws InterruptedException {
		EntityManagerFactory emf = Persistence.createEntityManagerFactory( "EclipseLink_MySQL" );
		EntityManager em = emf.createEntityManager();
		Properties properties = new Properties();
		try (InputStream is = Main.class.getResourceAsStream( "/META-INF/hsearch.properties" )) {
			properties.load( is );
		}
		catch (IOException e) {
			throw new RuntimeException( e );
		}
		JPASearchFactoryController searchController = Setup.createSearchFactoryController( emf, properties );


		FullTextEntityManager fem = searchController.getFullTextEntityManager( em );
		fem.createIndexer( Book.class ).startAndWait();

		while ( true ) {
			try {
				Scanner sc = new Scanner( System.in );
				System.out.println( "insert, query, purgeAll or exit?" );
				String insertOrQuery = sc.nextLine();
				if ( insertOrQuery.equalsIgnoreCase( "insert" ) ) {
					em.getTransaction().begin();
					Book book = new Book();
					System.out.println( "Insert title:" );
					book.setTitle( sc.nextLine() );
					System.out.println( "Insert summary:" );
					book.setSummary( sc.nextLine() );
					System.out.println( "Insert genre:" );
					book.setGenre( sc.nextLine() );
					System.out.println( "Insert ISBN" );
					book.setIsbn( sc.nextLine() );

					System.out.println( "set-up Book. Now setting up Author." );

					Author author = new Author();
					System.out.println( "Insert country" );
					author.setCountry( sc.nextLine() );
					System.out.println( "Insert first name" );
					author.setFirstName( sc.nextLine() );
					System.out.println( "Insert last name" );
					author.setLastName( sc.nextLine() );

					book.setAuthors( new HashSet<>( Collections.singletonList( author ) ) );
					author.setBooks( new HashSet<>( Collections.singletonList( book ) ) );
					fem.persist( book );
					em.getTransaction().commit();
				}
				else if ( insertOrQuery.equalsIgnoreCase( "query" ) ) {
					System.out.println( "Insert query:" );
					QueryParser queryParser = new QueryParser( "title", new StandardAnalyzer() );
					Query query = queryParser.parse( sc.nextLine() );
					System.out.println( fem.createFullTextQuery( query, Book.class ).getResultList() );
				}
				else if ( insertOrQuery.equalsIgnoreCase( "purgeAll" ) ) {
					em.getTransaction().begin();
					em.createQuery( "DELETE FROM Book" ).executeUpdate();
					em.createQuery( "DELETE FROM Author" ).executeUpdate();
					em.getTransaction().commit();
					System.out.println("purged everything!");
				}
				else if ( insertOrQuery.equalsIgnoreCase( "exit" ) ) {
					break;
				}
				else {
					System.err.println( "wrong input!" );
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}

		fem.close();
		searchController.close();
		emf.close();
	}

	private void persistBook(EntityManager em, Book book) {

	}

}
