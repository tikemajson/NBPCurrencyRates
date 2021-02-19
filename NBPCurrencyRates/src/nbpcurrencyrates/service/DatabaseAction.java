package nbpcurrencyrates.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;

import nbpcurrencyrates.exception.NoResultDatabaseException;

public class DatabaseAction {
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");
	
	public List<CurrencyTable> getMinAndMaxValueInPeriod(String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		String query = "SELECT currency FROM CurrencyTable currency WHERE LOWER(currency.code) = LOWER(:CurrencyCode) AND (currency.mid = (SELECT MIN(currency.mid) FROM CurrencyTable currency) OR currency.mid = (SELECT MAX(currency.mid) FROM CurrencyTable currency))";
		TypedQuery<CurrencyTable> typedQuery = entityManager.createQuery(query, CurrencyTable.class);
		typedQuery.setParameter("CurrencyCode", code);
		try {
			List<CurrencyTable> currencyTable = typedQuery.getResultList();
			if(currencyTable != null) {
				return currencyTable;
			} else {
				throw new NoResultDatabaseException("No result.");
			}
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("Table is empty.");
		}
	}
	
	public boolean findCountry(String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		String query = "SELECT country FROM CountryTable country WHERE LOWER(country.code) = LOWER(:CurrencyCode)";
		TypedQuery<CountryTable> typedQuery = entityManager.createQuery(query, CountryTable.class);	
		typedQuery.setParameter("CurrencyCode", code);
		List<CountryTable> countryTable = null;
		try {
			countryTable = typedQuery.getResultList();
			if(countryTable.size() > 0) {
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
	
	public void AddCountryListToCurrency(long id, String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		CurrencyCode currencyCode = CurrencyCode.getByCode(code);
		List<CountryCode> countryList = currencyCode.getCountryList();
		for (CountryCode country : countryList) {
			CountryTable countryDataBase = new CountryTable();
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			countryDataBase.setCode(code);
			countryDataBase.setCountry(country.getName());
			entityManager.persist(countryDataBase);
			entityTransaction.commit();
		}
		entityManager.close();
	}
	
	public void addOneCountryToCurrency(long id, String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		CountryTable countryDataBase = new CountryTable();
		CurrencyCode currencyCode = CurrencyCode.getByCode(code);
		CountryCode countryCode = currencyCode.getCountryList().get(0);
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			countryDataBase.setCode(code);
			countryDataBase.setCountry(countryCode.getName());
			entityManager.persist(countryDataBase);
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			entityManager.close();
		}
	}
	
	public void addCurrency(String name, String code, double mid, Date date) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			CurrencyTable currencyDateBase = new CurrencyTable();
			currencyDateBase.setName(name);
			currencyDateBase.setCode(code);
			currencyDateBase.setMid(mid);
			currencyDateBase.setDate(date);
			entityManager.persist(currencyDateBase);
			entityTransaction.commit();
		} catch (Exception e){
			if(entityTransaction != null) {
				entityTransaction.rollback();
			} else {
				throw new RuntimeException(e);
			}
		} finally {
			entityManager.close();
		}
	}
	
	public void deleteCurrency(long id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		CurrencyTable currencyDateBase = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			currencyDateBase = entityManager.find(CurrencyTable.class, id);
			entityManager.remove(currencyDateBase);
			entityManager.persist(currencyDateBase);
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			entityManager.close();
		}
	}
	
	public void changeName(String name, long id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		CurrencyTable currencyDateBase = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			currencyDateBase = entityManager.find(CurrencyTable.class, id);
			currencyDateBase.setName(name);
			entityManager.persist(currencyDateBase);
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			entityManager.close();
		}
	}
	
	public void changeCode(String newCode, long id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		CurrencyTable currencyDateBase = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			currencyDateBase = entityManager.find(CurrencyTable.class, id);
			currencyDateBase.setCode(newCode);
			entityManager.persist(currencyDateBase);
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException();
		} finally {
			entityManager.close();
		}
	}
	
	public void changeMid(double mid, long id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		CurrencyTable currencyDateBase = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			currencyDateBase = entityManager.find(CurrencyTable.class, id);
			currencyDateBase.setMid(mid);
			entityManager.persist(currencyDateBase);
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			entityManager.close();
		}
	}
	
	public void changeCountry(String code, String country) {
		
	}
	
	public void changeDate(Date date, long id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		CurrencyTable currencyDateBase = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			currencyDateBase = entityManager.find(CurrencyTable.class, id);
			currencyDateBase.setDate(date);
			entityManager.persist(currencyDateBase);
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			entityManager.close();
		}
	}
}
