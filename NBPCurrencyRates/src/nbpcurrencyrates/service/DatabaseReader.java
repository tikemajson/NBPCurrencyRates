package nbpcurrencyrates.service;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;

import nbpcurrencyrates.exception.NoResultDatabaseException;

public class DatabaseReader implements RateService{
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");
	
	public Currency findCurrency(String code, Date date) {
		RateContext rateContext = new RateContext();
		rateContext.set(new JSONReader());
		return rateContext.getCurrency(code, date);
	}
	
	public void setCountry(int currencyId, String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		CurrencyCode currencyCode = CurrencyCode.getByCode(code);
		List<CountryCode> countryList = currencyCode.getCountryList();
		CountryDataBase countryDataBase = null;
		try {
			System.out.println("Set country 1");
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			for (CountryCode countryCode : countryList) {
				countryDataBase = new CountryDataBase();
				countryDataBase.setCode(code);
				countryDataBase.setCountry(countryCode.getName());
				countryDataBase.setCurrencyID(currencyId);
				entityManager.persist(countryDataBase);
				entityTransaction.commit();
			}
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			entityManager.close();
		}
	}
	
	public List<CountryDataBase> findCountry(int currencyId, String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		String query = "SELECT country FROM CountryDataBase country WHERE country.currencyid = :CurrencyID";
		TypedQuery<CountryDataBase> typedQuery = entityManager.createQuery(query, CountryDataBase.class);
		typedQuery.setParameter("CurrencyID", currencyId);
		List<CountryDataBase> countryList;
		try {
			System.out.println("Find country 1");
			countryList = typedQuery.getResultList();
			return countryList;
		} catch (NoResultException e) {
			System.out.println("Find country 2");
			setCountry(currencyId, code);
			countryList = typedQuery.getResultList();
			return countryList;
		} finally {
			entityManager.close();
		}
	}
	
	public CurrencyDateBase getCurrencyDb(String code, Date date) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		String query = "SELECT c FROM CurrencyDateBase c WHERE LOWER(c.code) = LOWER(:CurrencyCode)";
		TypedQuery<CurrencyDateBase> typedQuery = entityManager.createQuery(query, CurrencyDateBase.class);
		typedQuery.setParameter("CurrencyCode", code);
		CurrencyDateBase currencyDateBase = null;
		Currency currency = findCurrency(code, new Date());
		try {
			currencyDateBase = typedQuery.getSingleResult();
			List<CountryDataBase> countryList = findCountry(currencyDateBase.getId(), code);
			if(currency.getDate().after(date)) {
				changeDate(currency.getDate(), currencyDateBase.getId());
				currencyDateBase = typedQuery.getSingleResult();
			}
			return currencyDateBase;
		} catch (NoResultException e) {
			if(currency != null) {
				addCurrency(currency.getName(), currency.getCode(), currency.getMid().doubleValue(), currency.getDate());
				List<CountryDataBase> countryList = findCountry(currencyDateBase.getId(), code);
				currencyDateBase = typedQuery.getSingleResult();
				return currencyDateBase;
			} else {
				throw new NoResultDatabaseException("Currency not found.");
			}
		} finally {
			entityManager.close();
		}
	}
	
	public List<CurrencyDateBase> getAllCurrency() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		String query = "SELECT c FROM CurrencyDateBase c";
		TypedQuery<CurrencyDateBase> typedQuery = entityManager.createQuery(query, CurrencyDateBase.class);
		List<CurrencyDateBase> currencyList;
		try {
			currencyList = typedQuery.getResultList();
			return currencyList;
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("Table is empty.");
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
			CurrencyDateBase currencyDateBase = new CurrencyDateBase();
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
	
	public void deleteCurrency(int id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		CurrencyDateBase currencyDateBase = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			currencyDateBase = entityManager.find(CurrencyDateBase.class, id);
			entityManager.remove(currencyDateBase);
			entityManager.persist(currencyDateBase);
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			entityManager.close();
		}
	}
	
	public void changeName(String name, int id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		CurrencyDateBase currencyDateBase = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			currencyDateBase = entityManager.find(CurrencyDateBase.class, id);
			currencyDateBase.setName(name);
			entityManager.persist(currencyDateBase);
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			entityManager.close();
		}
	}
	
	public void changeCode(String newCode, int id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		CurrencyDateBase currencyDateBase = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			currencyDateBase = entityManager.find(CurrencyDateBase.class, id);
			currencyDateBase.setCode(newCode);
			entityManager.persist(currencyDateBase);
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException();
		} finally {
			entityManager.close();
		}
	}
	
	public void changeMid(double mid, int id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		CurrencyDateBase currencyDateBase = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			currencyDateBase = entityManager.find(CurrencyDateBase.class, id);
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
	
	public void changeDate(Date date, int id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		CurrencyDateBase currencyDateBase = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			currencyDateBase = entityManager.find(CurrencyDateBase.class, id);
			currencyDateBase.setDate(date);
			entityManager.persist(currencyDateBase);
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			entityManager.close();
		}
	}

	@Override
	public Currency getCurrency(String code, Date date) {
		CurrencyDateBase currencyDateBase = getCurrencyDb(code, date);
		return new Currency(currencyDateBase.getName(), currencyDateBase.getCode(), currencyDateBase.getMid(), currencyDateBase.getDate());
	}

}
