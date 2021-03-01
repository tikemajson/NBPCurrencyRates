package nbpcurrencyrates.database.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;

import nbpcurrencyrates.service.CodeTable;
import nbpcurrencyrates.service.Currency;

public class CodeTableAddService {
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");
	
	public void addCode(String code, String name) {
		CodeTableGetService codeTableGetService = new CodeTableGetService();
		if(!codeTableGetService.findCode(code)) {
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
	}
	
	public void addCurrency(String code, String name, BigDecimal mid, Date date) {
		CodeTableGetService codeTableGetService = new CodeTableGetService();
		RateTableGetService rateTableGetService = new RateTableGetService();
		RateTableAddService rateTableAddService = new RateTableAddService();
		CountryTableGetService countryTableGetService = new CountryTableGetService();
		CountryTableAddService countryTableAddService = new CountryTableAddService();
		
		addCode(code, name);
		rateTableAddService.addRate(code, date, mid);
		CurrencyCode currencyCode = CurrencyCode.getByCode(code);
		CountryCode countryCode = currencyCode.getCountryList().get(0);
		countryTableAddService.addCountryToCurrency(code, countryCode.getName());
	}
}
