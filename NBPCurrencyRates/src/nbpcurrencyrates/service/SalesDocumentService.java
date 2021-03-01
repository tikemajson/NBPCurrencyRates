package nbpcurrencyrates.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import nbpcurrencyrates.database.service.CodeTableGetService;
import nbpcurrencyrates.database.service.CountryTableGetService;
import nbpcurrencyrates.database.service.RateTableGetService;
import nbpcurrencyrates.readers.DatabaseReader;

public class SalesDocumentService {
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");
	
	public static void insert() {
		String url = "http://api.nbp.pl/api/exchangerates/rates/A/";
		String dateString = "2021-02-26";
		String dateStringTwo = "2021-02-05";
		String code = "EUR";
		Date date;
		Date dateTwo;
		
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
			dateTwo = new SimpleDateFormat("yyyy-MM-dd").parse(dateStringTwo);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
 
		CountryTableGetService countryTableGetService = new CountryTableGetService();
		CountryTable country = countryTableGetService.getCountry("CAD", "Canada");
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			country = entityManager.find(CountryTable.class, 12L);
			country.setName("Kanada");
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			entityManager.close();
		}
		
	}
	
	public static void main(String[] args) {
		insert();
	}
}
