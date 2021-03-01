package nbpcurrencyrates.database.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import nbpcurrencyrates.exception.NoResultDatabaseException;
import nbpcurrencyrates.service.CodeTable;
import nbpcurrencyrates.service.CountryTable;

public class CountryTableGetService {
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");
	
	public List<CountryTable> getCountryWithTwoOrMoreCurrency() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createNamedQuery("CountryTable.getCountryWithTwoOrMoreCurrency");
		List<Object[]> objects;
		List<CountryTable> countryTable = new ArrayList<>();
		try {
			objects = query.getResultList();
			for (Object[] obj : objects) {
				String countryName = obj[0].toString();
				CountryTable country = new CountryTable();
				country.setName(countryName);
				countryTable.add(country);
			}
			return countryTable;
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} finally {
			entityManager.close();
		}
	}
	
	public CountryTable getCountry(String code, String name) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CodeTable currencyCode = new CodeTableGetService().getCode(code);
		Query query = entityManager.createNamedQuery("CountryTable.getCountry").setParameter("currencyId", currencyCode.getId()).setParameter("countryName", name).setMaxResults(1);
		CountryTable country = null;
		try {
			country = (CountryTable) query.getSingleResult();
			return country;
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} finally {
			entityManager.close();
		}
	}
	
	public List<CountryTable> getCountryList(String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CodeTable currencyCode = new CodeTableGetService().getCode(code);
		Query query = entityManager.createNamedQuery("CountryTable.getCountryList").setParameter("currencyId", currencyCode.getId());
		List<CountryTable> countryList;
		try {
			countryList = query.getResultList();
			if(countryList.size() > 0) {
				return countryList;
			} else {
				throw new NoResultDatabaseException("No result.");
			}
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} finally {
			entityManager.close();
		}
	}

	public boolean findCountry(String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CodeTable currencyCode = new CodeTableGetService().getCode(code);
		Query query = entityManager.createNamedQuery("CountryTable.getCountryList").setParameter("currencyId", currencyCode.getId()).setMaxResults(1);
		CountryTable countryTable;
		try {
			countryTable = (CountryTable) query.getSingleResult();
			if(countryTable != null) {
				return true;
			} else {
				return false;
			}
		} catch (NoResultException e) {
			return false;
		} finally {
			entityManager.close();
		}
	}
}
