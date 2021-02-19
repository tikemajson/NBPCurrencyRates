package nbpcurrencyrates.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class SalesDocService {
	public static void insert() {
		DatabaseAction databaseAction = new DatabaseAction();
		String dateString = "2021-02-18";
		String code = "USD";
		Date date;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
		RateContext rateContext = new RateContext();
		rateContext.set(new DatabaseReader1());
		Currency currency = rateContext.getCurrency(code, date);
		if(!databaseAction.findCountry(code)) {
			databaseAction.addCountryToCurrency(code);
		}
		
		//PRZERZUCIÆ DO OSOBNEJ METODY JAKO PARAMETR PRZYJ¥Æ NOWY READER
		while(currency == null) {
			rateContext.set(new JSONReader());
			Currency currencyFromJson = rateContext.getCurrency(code, date);
			rateContext.set(new DatabaseReader1());
			currency = rateContext.getCurrency(code, currencyFromJson.getDate());
			if(currency == null) {
				databaseAction.addCurrency(currencyFromJson.getName(), currencyFromJson.getCode(), currencyFromJson.getMid().doubleValue(), currencyFromJson.getDate());
				date = currencyFromJson.getDate();
				currency = rateContext.getCurrency(code, date);
			}
		}
		
		System.out.println(currency.getCode() + ", " + currency.getName() + ", " + currency.getMid() + ", " + new SimpleDateFormat("yyyy-MM-dd").format(currency.getDate()));
	}
	
	public static void main(String[] args) {
		insert();
	}
}
