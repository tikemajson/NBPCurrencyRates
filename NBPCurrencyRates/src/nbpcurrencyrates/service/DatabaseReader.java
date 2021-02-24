package nbpcurrencyrates.service;

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

import nbpcurrencyrates.exception.NoResultDatabaseException;

public class DatabaseReader implements RatesService{
	private static EntityManagerFactory entityManagerFactory = Persistence.createEntityManagerFactory("NBPCurrencyRates");
	private RatesContext ratesContext = new RatesContext();
	
	public DatabaseReader() {
		this.ratesContext.set(new JsonUrlReader(new JsonUrlConverter()));
	}
	
	public DatabaseReader(RatesService next) {
		this.ratesContext.set(next);
	}
	
	private List<CodeTable> getRatesFromDatabase(String code, Date date) {
		EntityManager entityManager = entityManagerFactory.createEntityManager();
		Query query = entityManager.createQuery("SELECT codetable FROM CodeTable codetable JOIN codetable.rate ratetable WHERE LOWER(codetable.code) = LOWER(:currencyCode) AND ratetable.date = :rateDate");
		query.setParameter("currencyCode", code);
		query.setParameter("rateDate", date);
		List<CodeTable> codeTable = null;
		try {
			codeTable = query.getResultList();
			if(codeTable.size() > 0) {
				return codeTable;
			} else {
				return null;
			}
		} catch (NoResultException e) {
			return codeTable;
		} finally {
			entityManager.close();
		}
	}
	
	@Override
	public Currency getRates(String code, Date date) {
		List<CodeTable> codeTable = getRatesFromDatabase(code, date);
		Currency currency = null;
		DatabaseAction databaseAction = new DatabaseAction();
		if(codeTable != null) {
			for (RateTable rateTable : codeTable.get(0).getRate()) {
				if(date.equals(rateTable.getDate())) {
					currency = new Currency(codeTable.get(0).getCode(), codeTable.get(0).getName(), rateTable.getMid(), rateTable.getDate());
					break;
				}
			}
			return currency;
		} else {
			currency = ratesContext.getRates(code, date);
			CodeTable codeDb = null;
			
			if(!databaseAction.findCode(code)) {
				databaseAction.addCode(currency.getCode(), currency.getName());
				codeDb = databaseAction.getCode(code);
			} else {
				codeDb = databaseAction.getCode(code);
			}
			
			if(!databaseAction.findRate(codeDb.getId(), date)) {
				databaseAction.addRate(codeDb.getId(), currency.getDate(), currency.getMid());
			}
			
			if(!databaseAction.findCountry(codeDb.getId())) {
				CurrencyCode currencyCode = CurrencyCode.getByCode(code);
				CountryCode countryCode = currencyCode.getCountryList().get(0);
				databaseAction.addCountryToCurrency(codeDb.getId(), countryCode.getName());
			}
			
			codeTable = getRatesFromDatabase(code, currency.getDate());
			if(codeTable != null) {
				for (RateTable rateTable : codeTable.get(0).getRate()) {
					if(date.equals(rateTable.getDate())) {
						currency = new Currency(codeTable.get(0).getCode(), codeTable.get(0).getName(), rateTable.getMid(), rateTable.getDate());
						break;
					}
				}
				return currency;
			} else {
				throw new NoResultDatabaseException("No result");
			}
		}
	}
}
