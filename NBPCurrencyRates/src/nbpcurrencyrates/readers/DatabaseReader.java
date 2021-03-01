package nbpcurrencyrates.readers;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import com.neovisionaries.i18n.CountryCode;
import com.neovisionaries.i18n.CurrencyCode;

import nbpcurrencyrates.converters.JsonUrlConverter;
import nbpcurrencyrates.database.service.CodeTableAddService;
import nbpcurrencyrates.database.service.CodeTableGetService;
import nbpcurrencyrates.exception.NoResultDatabaseException;
import nbpcurrencyrates.service.CodeTable;
import nbpcurrencyrates.service.Currency;
import nbpcurrencyrates.service.RateTable;
import nbpcurrencyrates.service.RatesContext;
import nbpcurrencyrates.service.RatesService;

public class DatabaseReader implements RatesService{
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");
	private RatesContext ratesContext = new RatesContext();
	
	public DatabaseReader() {
		this.ratesContext.set(new JsonUrlReader(new JsonUrlConverter()));
	}
	
	public DatabaseReader(RatesService next) {
		this.ratesContext.set(next);
	}
	
	private Currency convertDbObjectToCurrency(CodeTable codeTable) {
		Currency currency = null;
		for (RateTable rateTable : codeTable.getRate()) {
				currency = new Currency(codeTable.getCode(), codeTable.getName(), rateTable.getMid(), rateTable.getDate());
		}
		return currency;
	}

	private CodeTable getRatesFromDatabase(String code, Date date) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		CodeTable currencyCode = new CodeTableGetService().getCode(code);
		Query query = entityManager.createNamedQuery("CodeTable.getRatesFromDatabase").setParameter("currencyId", currencyCode.getId()).setParameter("rateDate", date);
		CodeTable codeTable = null;
		try {
			codeTable = (CodeTable) query.getSingleResult();
			if(codeTable != null) {
				return codeTable;
			} else {
				return codeTable;
			}
		} catch (NoResultException e) {
			return codeTable;
		} finally {
			entityManager.close();
		}
	}
	
	@Override
	public Currency getRates(String code, Date date) {
		CodeTable codeTable = getRatesFromDatabase(code, date);
		Currency currency;

		if(codeTable != null) {
			currency = convertDbObjectToCurrency(codeTable);
			return currency;
		} else {
			CodeTableAddService codeTableAddService = new CodeTableAddService();
			currency = ratesContext.getRates(code, date);
			codeTableAddService.addCurrency(currency.getCode(), currency.getName(), currency.getMid(), currency.getDate());
			codeTable = getRatesFromDatabase(code, currency.getDate());
			currency = convertDbObjectToCurrency(codeTable);
			return currency;
		}
	}
}
