package nbpcurrencyrates.database.service;

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

import nbpcurrencyrates.exception.DataConvertException;
import nbpcurrencyrates.exception.NoResultDatabaseException;
import nbpcurrencyrates.service.CodeTable;
import nbpcurrencyrates.service.Currency;
import nbpcurrencyrates.service.RateTable;

public class RateTableGetService {
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");
	
	public Currency getRateWithMaxDiff(String code, Date dateOne, Date dateTwo) {
		EntityManager entiManager = entityManagerFactory.createEntityManager();
		CodeTable curCode = new CodeTableGetService().getCode(code);
		Query query = entiManager.createNamedQuery("RateTable.getRateWithMaxDiff").setParameter("currencyId", curCode.getId());
		List<Object[]> objects;
		Currency currency = null;
		if(dateOne.before(dateTwo)) {
			query.setParameter("currencyDateOne", dateOne).setParameter("currencyDateTwo", dateTwo);
		} else {
			query.setParameter("currencyDateOne", dateTwo).setParameter("currencyDateTwo", dateOne);
		}
		
		try {
			objects = query.getResultList();
			for (Object[] obj : objects) {
				Date dateMin;
				try {
					dateMin = new SimpleDateFormat("yyyy-MM-dd").parse(obj[1].toString());
				} catch (ParseException e) {
				 throw new RuntimeException(e);
				}
				BigDecimal minValue = new BigDecimal(obj[3].toString());
				currency = new Currency(curCode.getCode(), curCode.getName(), minValue, dateMin);
			}
			return currency;
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} finally {
			entiManager.close();
		}
	}

	
	public List<Currency> getCurrencyListWithMinRate(String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CodeTable currCode = new CodeTableGetService().getCode(code);
		Query query = entityManager.createNamedQuery("RateTable.getCurrencyListWithMinRate").setParameter("currencyId", currCode.getId()).setMaxResults(5);
		List<Currency> currencyList = new ArrayList<>();
		List<RateTable> rateList;
		try {
			rateList = query.getResultList();
			if(rateList.size() > 0) {
				for (RateTable rateTable : rateList) {
					currencyList.add(new Currency(currCode.getCode(), currCode.getName(), rateTable.getMid(), rateTable.getDate()));
				}
				return currencyList;
			} else {
				throw new NoResultDatabaseException("No result.");
			}
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} finally {
			entityManager.close();
		}
	}
	
	public List<Currency> getCurrencyListWithMaxRate(String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CodeTable currCode = new CodeTableGetService().getCode(code);
		Query query = entityManager.createNamedQuery("RateTable.getCurrencyListWithMaxRate").setParameter("currencyId", currCode.getId()).setMaxResults(5);
		List<Currency> currencyList = new ArrayList<>();
		List<RateTable> rateList;
		try {
			rateList = query.getResultList();
			if(rateList.size() > 0) {
				for (RateTable rateTable : rateList) {
					currencyList.add(new Currency(currCode.getCode(), currCode.getName(), rateTable.getMid(), rateTable.getDate()));
				}
				return currencyList;
			} else {
				throw new NoResultDatabaseException("No result.");
			}
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} finally {
			entityManager.close();
		}
	}
	
	public Currency getCurrencyWithMaxMidInPeriod(String code, Date dateOne, Date dateTwo) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CodeTable currencyCode = new CodeTableGetService().getCode(code);
		Query query = entityManager.createNamedQuery("RateTable.getCurrencyWithMaxMidInPeriod").setParameter("currencyId", currencyCode.getId());
		if(dateOne.before(dateTwo)) {
			query.setParameter("currencyDateOne", dateOne).setParameter("currencyDateTwo", dateTwo);
		} else {
			query.setParameter("currencyDateOne", dateTwo).setParameter("currencyDateTwo", dateOne);
		}
		CodeTable codeTable;
		Currency currency = null;
		try {
			codeTable = (CodeTable) query.getSingleResult();
			if(codeTable != null) {
				for (RateTable rateTable : codeTable.getRate()) {
					currency = new Currency(codeTable.getCode(), codeTable.getName(), rateTable.getMid(), rateTable.getDate());
				}
				return currency;
			} else {
				throw new NoResultDatabaseException("No result.");
			}
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} catch (NullPointerException ne) {
			throw new DataConvertException("Can't convert database object to currency, database object is null.");
		} finally {
			entityManager.close();
		}
	}
	
	public Currency getCurrencyWithMinMidInPeriod(String code, Date dateOne, Date dateTwo) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CodeTable currencyCode = new CodeTableGetService().getCode(code);
		Query query = entityManager.createNamedQuery("RateTable.getCurrencyWithMinMidInPeriod").setParameter("currencyId", currencyCode.getId());
		if(dateOne.before(dateTwo)) {
			query.setParameter("currencyDateOne", dateOne).setParameter("currencyDateTwo", dateTwo);
		} else {
			query.setParameter("currencyDateOne", dateTwo).setParameter("currencyDateTwo", dateOne);
		}
		CodeTable codeTable;
		Currency currency = null;
		try {
			codeTable = (CodeTable) query.getSingleResult();
			if(codeTable != null) {
				for (RateTable rateTable : codeTable.getRate()) {
					currency = new Currency(codeTable.getCode(), codeTable.getName(), rateTable.getMid(), rateTable.getDate());
				}
				return currency;
			} else {
				throw new NoResultDatabaseException("No result.");
			}
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} catch (NullPointerException ne) {
			throw new DataConvertException("Can't convert database object to currency, database object is null.");
		} finally {
			entityManager.close();
		}
	}
	
	public RateTable getRate(String code, Date date) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CodeTable currencyCode = new CodeTableGetService().getCode(code);
		Query query = entityManager.createNamedQuery("RateTable.getRate").setParameter("currencyId", currencyCode.getId()).setParameter("rateDate", date);	
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
	
	public boolean findRate(String code, Date date) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CodeTable currencyCode = new CodeTableGetService().getCode(code);
		Query query = entityManager.createNamedQuery("RateTable.findRate").setParameter("currencyId", currencyCode.getId()).setParameter("rateDate", date);	
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
}
