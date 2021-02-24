package nbpcurrencyrates.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SalesDocumentService {
	public static void insert() {
		String url = "http://api.nbp.pl/api/exchangerates/rates/A/";
		String dateString = "2021-01-11";
		String dateStringTwo = "2021-02-20";
		String code = "EUR";
		Date date;
		Date dateTwo;
		DatabaseAction databaseAction = new DatabaseAction();
		
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
			dateTwo = new SimpleDateFormat("yyyy-MM-dd").parse(dateStringTwo);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}

		RatesContext ratesContext = new RatesContext();
		ratesContext.set(new DatabaseReader());
		Currency currency = ratesContext.getRates(code, date);
		System.out.println("KURS: " + currency.getCode() + ", " + currency.getName() + ", " + currency.getMid() + ", " + new SimpleDateFormat("yyyy-MM-dd").format(currency.getDate()));
		
		currency = databaseAction.getCurrencyWithMinMidInPeriod(code, date, dateTwo);
		System.out.println("MIN: " + currency.getCode() + ", " + currency.getName() + ", " + currency.getMid() + ", " + new SimpleDateFormat("yyyy-MM-dd").format(currency.getDate()));
		
		currency = databaseAction.getCurrencyWithMaxMidInPeriod(code, date, dateTwo);
		System.out.println("MAX: " + currency.getCode() + ", " + currency.getName() + ", " + currency.getMid() + ", " + new SimpleDateFormat("yyyy-MM-dd").format(currency.getDate()));
	
		List<CountryTable> countryList = databaseAction.getCountryWithTwoOrMoreCurrency();
		for (CountryTable countryTable : countryList) {
			System.out.println("COUNTRY: " + countryTable.getName());
		}
		
		System.out.println("==========================");
		
		List<RateTable> rateList = databaseAction.getCurrencyListWithMinRate("EUR");
		for (RateTable rate : rateList) {
			System.out.println("MinValuesRate: " + rate.getMid() + ", " + new SimpleDateFormat("yyyy-MM-dd").format(rate.getDate()));
		}
		
		System.out.println("==========================");
		
		rateList = databaseAction.getCurrencyListWithMaxRate("EUR");
		for (RateTable rate : rateList) {
			System.out.println("MaxValuesRate: " + rate.getMid() + ", " + new SimpleDateFormat("yyyy-MM-dd").format(rate.getDate()));
		}
		
		System.out.println("==========================");
		
		RateTable rateTable = databaseAction.getRateWithMaxDiff(code, date, dateTwo);
		System.out.println("Currency with greatest diff beetwen " + dateString + " - " + dateStringTwo + ", date: " + new SimpleDateFormat("yyyy-MM-dd").format(rateTable.getDate()) + ", mid: " + rateTable.getMid());
	}
	
	public static void main(String[] args) {
		insert();
	}
}
