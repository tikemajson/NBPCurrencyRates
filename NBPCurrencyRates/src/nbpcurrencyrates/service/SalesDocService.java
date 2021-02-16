package nbpcurrencyrates.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SalesDocService {
	public static void insert() {
		String dateString = "2021-02-16";
		Date date;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		RateContext rateContext = new RateContext();
		rateContext.set(new DatabaseReader());
		Currency currency = rateContext.getCurrency("USD", date);
		System.out.println(currency.getCode() + ", " + currency.getName() + ", " + currency.getMid() + ", " + new SimpleDateFormat("yyyy-MM-dd").format(currency.getDate()));
	}
	
	public static void main(String[] args) {
		insert();
	}
}
