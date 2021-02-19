package nbpcurrencyrates.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import nbpcurrencyrates.exception.NoResultDatabaseException;

public class DatabaseReader1 implements RateService{
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");
	
	public CurrencyTable getCurrencyFromDatabase(String code, Date date) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		String query = "SELECT currency FROM CurrencyTable currency WHERE LOWER(currency.code) = LOWER(:CurrencyCode) AND currency.date = :CurrencyDate";
		TypedQuery<CurrencyTable> typedQuery = entityManager.createQuery(query, CurrencyTable.class);
		typedQuery.setParameter("CurrencyCode", code);
		typedQuery.setParameter("CurrencyDate", date);
		CurrencyTable currencyDateBase = null;
		try {
			currencyDateBase = typedQuery.getSingleResult();
			return currencyDateBase;
		} catch (NoResultException e) {
			return currencyDateBase;
		} finally {
			entityManager.close();
		}
	}
	
	public List<CurrencyTable> getAllCurrency() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		String query = "SELECT c FROM CurrencyTable c";
		TypedQuery<CurrencyTable> typedQuery = entityManager.createQuery(query, CurrencyTable.class);
		List<CurrencyTable> currencyList;
		try {
			currencyList = typedQuery.getResultList();
			return currencyList;
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("Table is empty.");
		} finally {
			entityManager.close();
		}
	}

	@Override
	public Currency getCurrency(String code, Date date) {
		CurrencyTable currencyDateBase = getCurrencyFromDatabase(code, date);
		Currency currency = null;
		if(currencyDateBase != null) {
			currency = new Currency(currencyDateBase.getName(), currencyDateBase.getCode(), currencyDateBase.getMid(), currencyDateBase.getDate());
			return currency;
		}
		return currency;
	}
}
