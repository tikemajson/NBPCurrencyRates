package nbpcurrencyrates.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public abstract class RatesService {
	Date date;
	String code;
	
	public RatesService() {}
	
	public Date getDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}
	
	public BigDecimal getRates(String date, String code) {
		Date newDate;
		try {
			newDate = new SimpleDateFormat("yyyy-MM-dd").parse(date);
		} catch(ParseException pe) {
			throw new RuntimeException(pe);
		}
		String data = getData(newDate, code);
		return getCurrency(data, code);
	}
	
	abstract String getData(Date date, String code);
	abstract BigDecimal getCurrency(String data, String code);
}