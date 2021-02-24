package nbpcurrencyrates.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;

import nbpcurrencyrates.exception.NoResultDatabaseException;

public class DatabaseAction {
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");

	public RateTable getRateWithMaxDiff(String code, Date dateOne, Date dateTwo) {
		EntityManager entiManager = entityManagerFactory.createEntityManager();
		Query query = entiManager.createQuery("SELECT (SELECT rt.date FROM RateTable rt WHERE (rt.mid = (SELECT MAX(rt.mid) FROM RateTable rt WHERE rt.cid = :currencyId AND (rt.date >= :currencyDateOne AND rt.date <= :currencyDateTwo)))), (SELECT rt.date FROM RateTable rt WHERE (rt.mid = (SELECT MIN(rt.mid) FROM RateTable rt WHERE rt.cid = :currencyId AND (rt.date >= :currencyDateOne AND rt.date <= :currencyDateTwo)))), MAX(r.mid), MIN(r.mid) FROM RateTable r WHERE r.cid = :currencyId AND (r.date >= :currencyDateOne AND r.date <= :currencyDateTwo)");
		CodeTable curCode = getCode(code);
		List<Object[]> objects;
		RateTable rateTable = null;
		query.setParameter("currencyId", curCode.getId());
		if(dateOne.before(dateTwo)) {
			query.setParameter("currencyDateOne", dateOne);
			query.setParameter("currencyDateTwo", dateTwo);
		} else {
			query.setParameter("currencyDateOne", dateTwo);
			query.setParameter("currencyDateTwo", dateOne);
		}
		
		try {
			objects = query.getResultList();
			for (Object[] obj : objects) {
				rateTable = new RateTable();
				Date dateMin;
				try {
					dateMin = new SimpleDateFormat("yyyy-MM-dd").parse(obj[1].toString());
				} catch (ParseException e) {
				 throw new RuntimeException(e);
				}
				//BigDecimal maxValue = new BigDecimal(obj[2].toString());
				BigDecimal minValue = new BigDecimal(obj[3].toString());
				//BigDecimal diff = maxValue.subtract(minValue);
				//System.out.println("DateMax: " + dateMin + ", DateMin: " + dateMin + ", MaxValue : " + maxValue + ", MinValue: " + minValue + ", DIFF: " + diff);
				rateTable.setDate(dateMin);
				rateTable.setMid(minValue.doubleValue());
			}
			return rateTable;
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} finally {
			entiManager.close();
		}
	}
	
	public List<RateTable> getCurrencyListWithMinRate(String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createQuery("SELECT ratetable FROM RateTable ratetable WHERE ratetable.cid = :currencyId ORDER BY ratetable.mid ASC").setFirstResult(0).setMaxResults(5);
		CodeTable curCode = getCode(code);
		query.setParameter("currencyId", curCode.getId());
		List<RateTable> rateList;
		try {
			rateList = query.getResultList();
			if(rateList.size() > 0) {
				return rateList;
			} else {
				throw new NoResultDatabaseException("No result.");
			}
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} finally {
			entityManager.close();
		}
	}
	
	public List<RateTable> getCurrencyListWithMaxRate(String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createQuery("SELECT ratetable FROM RateTable ratetable WHERE ratetable.cid = :currencyId ORDER BY ratetable.mid DESC").setFirstResult(0).setMaxResults(5);
		CodeTable curCode = getCode(code);
		query.setParameter("currencyId", curCode.getId());
		List<RateTable> rateList;
		try {
			rateList = query.getResultList();
			if(rateList.size() > 0) {
				return rateList;
			} else {
				throw new NoResultDatabaseException("No result.");
			}
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} finally {
			entityManager.close();
		}
	}
	
	public List<CountryTable> getCountryWithTwoOrMoreCurrency() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createQuery("SELECT country.name, COUNT(country.cid) FROM CountryTable country GROUP BY country.name HAVING COUNT(country.cid) > 1");
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
	
	public Currency getCurrencyWithMaxMidInPeriod(String code, Date dateOne, Date dateTwo) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createQuery("SELECT ratetable FROM RateTable ratetable WHERE (ratetable.cid = :currencyId) AND (ratetable.date >= :currencyDateOne AND ratetable.date <= :currencyDateTwo) ORDER BY ratetable.mid DESC").setFirstResult(0).setMaxResults(1);
		CodeTable curCode = getCode(code);
		RateTable rateTable = null;
		Currency currency = null;
		query.setParameter("currencyId", curCode.getId());
		if(dateOne.before(dateTwo)) {
			query.setParameter("currencyDateOne", dateOne);
			query.setParameter("currencyDateTwo", dateTwo);
		} else {
			query.setParameter("currencyDateOne", dateTwo);
			query.setParameter("currencyDateTwo", dateOne);
		}
		
		try {
			rateTable = (RateTable) query.getSingleResult();
			currency = new Currency(curCode.getCode(), curCode.getName(), rateTable.getMid(), rateTable.getDate());
			return currency;
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} finally {
			entityManager.close();
		}
	}
	
	public Currency getCurrencyWithMinMidInPeriod(String code, Date dateOne, Date dateTwo) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createQuery("SELECT ratetable FROM RateTable ratetable WHERE (ratetable.cid = :currencyId) AND (ratetable.date >= :currencyDateOne AND ratetable.date <= :currencyDateTwo) ORDER BY ratetable.mid ASC").setFirstResult(0).setMaxResults(1);
		CodeTable curCode = getCode(code);
		RateTable rateTable = null;
		Currency currency = null;
		query.setParameter("currencyId", curCode.getId());
		if(dateOne.before(dateTwo)) {
			query.setParameter("currencyDateOne", dateOne);
			query.setParameter("currencyDateTwo", dateTwo);
		} else {
			query.setParameter("currencyDateOne", dateTwo);
			query.setParameter("currencyDateTwo", dateOne);
		}
		
		try {
			rateTable = (RateTable) query.getSingleResult();
			currency = new Currency(curCode.getCode(), curCode.getName(), rateTable.getMid(), rateTable.getDate());
			return currency;
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} finally {
			entityManager.close();
		}
	}
	
	public boolean findCountry(long id) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createQuery("SELECT country FROM CountryTable country WHERE country.cid = :currencyId").setFirstResult(0).setMaxResults(1);	
		query.setParameter("currencyId", id);
		CountryTable countryTable = null;
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
	
	public void addCountryToCurrency(long currencyid, String name) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			CountryTable countryTable = new CountryTable();
			countryTable.setCid(currencyid);
			countryTable.setName(name);
			entityManager.persist(countryTable);
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			entityManager.close();
		}
	}
	
	public RateTable getRate(long id, Date date) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createQuery("SELECT rate FROM RateTable rate WHERE rate.cid = :currencyId AND rate.date = :rateDate");	
		query.setParameter("currencyId", id);
		query.setParameter("rateDate", date);
		RateTable rateTable = null;
		try {
			rateTable = (RateTable) query.getSingleResult();
			if(rateTable != null) {
				return rateTable;
			} else {
				return null;
			}
		} catch (NoResultException e) {
			return null;
		} finally {
			entityManager.close();
		}
	}
	
	public boolean findRate(long id, Date date) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createQuery("SELECT rate FROM RateTable rate WHERE rate.cid = :currencyId AND rate.date = :rateDate");	
		query.setParameter("currencyId", id);
		query.setParameter("rateDate", date);
		RateTable rateTable = null;
		try {
			rateTable = (RateTable) query.getSingleResult();
			if(rateTable != null) {
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
	
	public void addRate(long currencyId, Date date, BigDecimal mid) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			RateTable rateTable = new RateTable();
			rateTable.setCid(currencyId);
			rateTable.setDate(date);
			rateTable.setMid(mid.doubleValue());
			entityManager.persist(rateTable);
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			entityManager.close();
		}
	}
	
	public void addCode(String code, String name) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		EntityTransaction entityTransaction = null;
		try {
			entityTransaction = entityManager.getTransaction();
			entityTransaction.begin();
			CodeTable codeTable = new CodeTable();
			codeTable.setCode(code);
			codeTable.setName(name);
			entityManager.persist(codeTable);
			entityTransaction.commit();
		} catch (Exception e) {
			throw new RuntimeException(e);
		} finally {
			entityManager.close();
		}
	}
	
	public CodeTable getCode(String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createQuery("SELECT codetable FROM CodeTable codetable WHERE LOWER(codetable.code) = LOWER(:currencyCode)");
		query.setParameter("currencyCode", code);
		CodeTable codeTable = null;
		try {
			codeTable = (CodeTable) query.getSingleResult();
			if(codeTable != null) {
				return codeTable;
			} else {
				return null;
			}
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} finally {
			entityManager.close();
		}
	}
	
	public boolean findCode(String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createQuery("SELECT codetable FROM CodeTable codetable WHERE LOWER(codetable.code) = LOWER(:currencyCode)");
		query.setParameter("currencyCode", code);
		CodeTable codeTable = null;
		try {
			codeTable = (CodeTable) query.getSingleResult();
			if(codeTable != null) {
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
