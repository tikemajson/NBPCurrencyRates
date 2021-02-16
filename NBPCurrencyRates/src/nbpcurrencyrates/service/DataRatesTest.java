package nbpcurrencyrates.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Assert;
import org.junit.Test;

class DataRatesTest {
	RatesService ratesService;
	Currency cur;
	String dateString = "2021-02-07";
	Date date;
	
	@Test
	void ifDataRatesIsEquals() {
		ratesService = new JsonURL();
		cur = ratesService.getRates(dateString, "USD");
		date = cur.getDate();
		Assert.assertTrue(new SimpleDateFormat("yyyy-MM-dd").format(date).equals("2021-02-05"));
	}

}
