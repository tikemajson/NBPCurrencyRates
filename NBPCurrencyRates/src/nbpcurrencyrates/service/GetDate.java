package nbpcurrencyrates.service;

import java.util.Calendar;
import java.util.Date;

public class GetDate {
	public Date getPreviousDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}
}
