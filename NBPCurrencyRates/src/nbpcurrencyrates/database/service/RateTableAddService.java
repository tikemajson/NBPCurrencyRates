package nbpcurrencyrates.database.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import nbpcurrencyrates.service.CodeTable;
import nbpcurrencyrates.service.RateTable;

public class RateTableAddService {
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");
	
	public void addRate(String code, Date date, BigDecimal mid) {
		RateTableGetService rateTableGetService = new RateTableGetService();
		if(!rateTableGetService.findRate(code, date)) {
			EntityManager entityManager = entityManagerFactory.createEntityManager();
			EntityTransaction entityTransaction = null;
			CodeTable currencyCode = new CodeTableGetService().getCode(code);
			try {
				entityTransaction = entityManager.getTransaction();
				entityTransaction.begin();
				RateTable rateTable = new RateTable();
				rateTable.setCid(currencyCode.getId());
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
	}
}
