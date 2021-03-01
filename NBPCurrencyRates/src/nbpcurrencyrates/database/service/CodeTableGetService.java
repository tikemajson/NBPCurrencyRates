package nbpcurrencyrates.database.service;

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

public class CodeTableGetService {
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");
	/*
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
	*/
	
	public CodeTable getCode(String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createNamedQuery("CodeTable.getCode").setParameter("currencyCode", code);
		CodeTable codeTable = null;
		try {
			codeTable = (CodeTable) query.getSingleResult();
			return codeTable;
		} catch (NoResultException e) {
			throw new NoResultDatabaseException("No result.");
		} finally {
			entityManager.close();
		}
	}
	
	public boolean findCode(String code) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createNamedQuery("CodeTable.findCode").setParameter("currencyCode", code);
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
