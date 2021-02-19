package nbpcurrencyrates.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

public class SalesDocumentService {
	public static void insert() {
		String url = "http://api.nbp.pl/api/exchangerates/rates/A/";
		String dateString = "2021-02-18";
		String code = "EUR";
		Date date;
		try {
			date = new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
		
	}
	
	public static void main(String[] args) {
		insert();
	}
}
