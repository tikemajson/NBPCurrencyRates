package nbpcurrencyrates.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hibernate.boot.model.relational.Database;
import org.junit.Assert;
import org.junit.Test;

public class DataRatesTest {
	RatesContext ratesContext = new RatesContext();
	
	@Test
	public void should_return_currency_if_currency_is_not_null() throws ParseException {
		//given
		String dateString = "2021-02-17";
		String code = "USD";
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		Currency currency;
		
		//when
		ratesContext.set(new DatabaseReader());
		currency = ratesContext.getRates(code, date);
		
		//then
		Assert.assertNotNull(currency);

	}

	
	@Test
	public void should_return_true_if_country_exists (){
		//given
		DatabaseAction databaseAction = new DatabaseAction();
		CodeTable currencyCode = databaseAction.getCode("USD");
		long currencyId = currencyCode.getId();
		
		//when
		boolean trueIfCodeHaveCountry = databaseAction.findCountry(currencyId);
		
		//then
		Assert.assertTrue(trueIfCodeHaveCountry);
	}
	
	@Test
	public void should_return_currency_if_find_min_value_in_period() throws ParseException {
		//given
		DatabaseAction databaseAction = new DatabaseAction();
		String code = "USD";
		String dateStringOne = "2021-02-09";
		String dateStringTwo = "2021-01-29";
		Date dateOne = new SimpleDateFormat("yyyy-MM-dd").parse(dateStringOne);
		Date dateTwo = new SimpleDateFormat("yyyy-MM-dd").parse(dateStringTwo);
		Currency currency;
		
		//when
		currency = databaseAction.getCurrencyWithMinMidInPeriod(code, dateOne, dateTwo);
		
		//then
		
		Assert.assertNotNull(currency);
	}
	
	@Test
	public void should_return_currency_if_find_max_value_in_period() throws ParseException {
		//given
		DatabaseAction databaseAction = new DatabaseAction();
		String code = "USD";
		String dateStringOne = "2021-02-09";
		String dateStringTwo = "2021-01-29";
		Date dateOne = new SimpleDateFormat("yyyy-MM-dd").parse(dateStringOne);
		Date dateTwo = new SimpleDateFormat("yyyy-MM-dd").parse(dateStringTwo);
		Currency currency;
		
		//when
		currency = databaseAction.getCurrencyWithMaxMidInPeriod(code, dateOne, dateTwo);
		
		//then
		Assert.assertNotNull(currency);
	}
	
	@Test
	public void should_return_true_if_find_rate () throws ParseException{
		//given
		DatabaseAction databaseAction = new DatabaseAction();
		CodeTable currencyCode = databaseAction.getCode("EUR");
		long currencyId = currencyCode.getId();
		String dateString = "2021-02-05";
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		
		//when
		boolean trueIfGivenDataHaveRate = databaseAction.findRate(currencyId, date);
		
		//then
		Assert.assertTrue(trueIfGivenDataHaveRate);
	}
	
	@Test
	public void should_return_null_if_rate_is_null () throws ParseException{
		//given
		DatabaseAction databaseAction = new DatabaseAction();
		CodeTable currencyCode = databaseAction.getCode("EUR");
		long currencyId = currencyCode.getId();
		String dateString = "2021-12-02";
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		
		//when
		RateTable rate = databaseAction.getRate(currencyId, date);
		
		//then
		Assert.assertNull(rate);
	}
	
	@Test
	public void should_return_country_if_country_have_to_or_more_currency() {
		//given
		DatabaseAction databaseAction = new DatabaseAction();
		List<CountryTable> countryList;
		
		//when
		countryList = databaseAction.getCountryWithTwoOrMoreCurrency();
		
		//then
		Assert.assertNotNull(countryList);
	}
	
	@Test
	public void should_return_true_if_find_code () {
		//given
		DatabaseAction databaseAction = new DatabaseAction();
		String code = "USD";
		
		//when
		boolean returnTrueIfFindCode = databaseAction.findCode(code);
		
		//then
		Assert.assertTrue(returnTrueIfFindCode);
	}
	
	@Test
	public void should_return_not_null_if_get_greatest_diffrence() throws ParseException {
		//given
		DatabaseAction databaseAction = new DatabaseAction();
		String code = "EUR";
		String dateString = "2021-02-01";
		String dateStringTwo = "2021-02-23";
		Date dateOne = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		Date dateTwo = new SimpleDateFormat("yyyy-MM-dd").parse(dateStringTwo);
		RateTable rate;
		
		//when
		rate = databaseAction.getRateWithMaxDiff(code, dateOne, dateTwo);
		
		//then
		Assert.assertNotNull(rate);
	}
}
