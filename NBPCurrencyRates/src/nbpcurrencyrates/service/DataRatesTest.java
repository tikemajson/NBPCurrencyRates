package nbpcurrencyrates.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.hamcrest.Matcher;

import org.hibernate.boot.model.relational.Database;
import org.junit.Assert;
import org.junit.Test;

import nbpcurrencyrates.database.service.CodeTableAddService;
import nbpcurrencyrates.database.service.CodeTableGetService;
import nbpcurrencyrates.database.service.CountryTableAddService;
import nbpcurrencyrates.database.service.CountryTableGetService;
import nbpcurrencyrates.database.service.RateTableAddService;
import nbpcurrencyrates.readers.DatabaseReader;

public class DataRatesTest {
	RatesContext ratesContext = new RatesContext();
	
	@Test
	public void should_return_expected_date_if_currency_with_given_date_exists() throws ParseException {
		//given
		String dateString = "2021-02-17";
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		String code = "CHF";
		String name = "frank szfajcarski";
		BigDecimal currencyValue = new BigDecimal("2.0005");
		CodeTableAddService codeTableAddService = new CodeTableAddService();
		codeTableAddService.addCurrency(code, name, currencyValue, date);
		Currency currency;
		
		//when
		ratesContext.set(new DatabaseReader());
		currency = ratesContext.getRates(code, date);
		
		//then
		Assert.assertEquals(date, currency.getDate());

	}

	
	@Test
	public void should_return_expected_country() {
		//given
		String code = "USD";
		String name = "dolar amerykañski";
		String country = "Meksyk";
		CodeTableGetService codeTableGetService = new CodeTableGetService();
		CodeTableAddService codeTableAddService = new CodeTableAddService();
		CountryTableGetService countryTableGetService = new CountryTableGetService();
		CountryTableAddService countryTableAddService = new CountryTableAddService();
		codeTableAddService.addCode(code, name);
		CodeTable codeTable = codeTableGetService.getCode(code);
		countryTableAddService.addCountryToCurrency(code, country);
		
		//when
		CountryTable expectedCountry = countryTableGetService.getCountry(code, country);
		
		//then
		Assert.assertEquals(country, expectedCountry.getName());
	}
	
	@Test
	public void should_return_expected_rate_for_given_date_and_code() throws ParseException {
		//given
		String code = "EUR";
		String dateString = "2021-02-25";
		BigDecimal expectedValue = new BigDecimal("4.5143");
		Date date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		RatesContext ratesContext = new RatesContext();
		ratesContext.set(new DatabaseReader());
		
		//when
		Currency currency = ratesContext.getRates(code, date);
		
		//then
		Assert.assertEquals(expectedValue, currency.getMid());
	}
	
	
	@Test
	public void should_return_expected_code() {
		//given
		String expectedCode = "ABC";
		String expectedName = "Algiera";
		CodeTableAddService codeTableAddService = new CodeTableAddService();
		CodeTableGetService codeTableGetService = new CodeTableGetService();
		codeTableAddService.addCode(expectedCode, expectedName);
		
		//when
		CodeTable code = codeTableGetService.getCode(expectedCode);
		
		//then
		Assert.assertEquals(expectedCode, code.getCode());
		Assert.assertEquals(expectedName, code.getName());
	}
	/*
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
		String dateString = "2021-02-06";
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
	*/
}
