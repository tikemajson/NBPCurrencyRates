package nbpcurrencyrates.database.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import nbpcurrencyrates.service.CodeTable;
import nbpcurrencyrates.service.CountryTable;

public class CountryTableAddService {
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");
	
	public void addCountryToCurrency(String code, String countryName) {
		CountryTableGetService countryTableGetService = new CountryTableGetService();
		if(!countryTableGetService.findCountry(code)) {
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			EntityTransaction entityTransaction = null;
			CodeTable currencyCode = new CodeTableGetService().getCode(code);
			try {
				entityTransaction = entityManager.getTransaction();
				entityTransaction.begin();
				CountryTable countryTable = new CountryTable();
				countryTable.setCid(currencyCode.getId());
				countryTable.setName(countryName);
				entityManager.persist(countryTable);
				entityTransaction.commit();
			} catch (Exception e) {
				throw new RuntimeException(e);
			} finally {
				entityManager.close();
			}
		}
	}
}
