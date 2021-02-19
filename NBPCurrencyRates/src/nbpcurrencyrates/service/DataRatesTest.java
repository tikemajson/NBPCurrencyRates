package nbpcurrencyrates.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

public class DataRatesTest {
	RateContext rateContext = new RateContext();
	
	@Test
	public void shouldIfCurrencyIsNull() throws ParseException {
		//given
		rateContext.set(new DatabaseReader1());
		String dateString = "2021-02-17";
		String code = "USD";
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		Currency currency;
		
		//when
		currency = rateContext.getCurrency(code, date);
		
		//then
		Assert.assertNotNull(currency);

	}
	
	@Test
	public void shouldAddCurrencyIfNoExists() throws ParseException {
		//given
		rateContext.set(new DatabaseReader1());
		String dateString = "2021-02-14";
		String code = "USD";
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		Currency currency = null;
		DatabaseAction databaseAction = new DatabaseAction();
		
		//when
		currency = rateContext.getCurrency(code, date);
		
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
		
		//then
		Assert.assertNotNull(currency);
	}
	
	@Test
	public void shouldFindContryIfExists (){
		//given
		DatabaseAction databaseAction = new DatabaseAction();
		String code = "USD";
		
		//when
		boolean ifFind = databaseAction.findCountry(code);
		
		//then
		Assert.assertTrue(ifFind);
	}
}
