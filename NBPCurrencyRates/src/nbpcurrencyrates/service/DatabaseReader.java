package nbpcurrencyrates.service;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.NoResultException;
import javax.persistence.Persistence;
import javax.persistence.TypedQuery;

public class DatabaseReader implements RatesService{
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");
	private String currencyCode;
	private Date currencyDate;
	private RatesContext ratesContext = new RatesContext();
	
	public DatabaseReader(String code, Date date) {
		this.currencyCode = code;
		this.currencyDate = date;
		this.ratesContext.set(new JsonUrlReader(code, date, new JsonUrlConverter()));
	}
	
	public DatabaseReader(String code, Date date, RatesService next) {
		this.currencyCode = code;
		this.currencyDate = date;
		this.ratesContext.set(next);
	}
	
	private CurrencyTable getRatesFromDataBase(String code, Date date) {
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
	
	@Override
	public Currency getRates() {
		CurrencyTable currencyTable = getRatesFromDataBase(currencyCode, currencyDate);
		Currency currency = null;
		DatabaseAction databaseAction = new DatabaseAction();
		
		if(!databaseAction.findCountry(currencyCode)) {
			databaseAction.AddCountryListToCurrency(currencyCode);
		}
		
		if(currencyTable != null) {
			currency = new Currency(currencyTable.getName(), currencyTable.getCode(), currencyTable.getMid(), currencyTable.getDate());
			return currency;
		} else {
			currency = ratesContext.getRates();
			currencyTable = getRatesFromDataBase(currencyCode, currency.getDate());
			if(currencyTable != null) {
				return currency;
			} else {
				databaseAction.addCurrency(currency.getName(), currency.getCode(), currency.getMid().doubleValue(), currency.getDate());
				return currency;
			}
		}
	}

}
