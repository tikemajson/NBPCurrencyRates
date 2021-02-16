package nbpcurrencyrates.service;

import java.util.Calendar;
import java.util.Date;

public class RateContext {
	RateService strategy;
	
	public void set(RateService rateService) {
		this.strategy = rateService;
	}
	
	public Currency getCurrency(String code, Date date) {
		return strategy.getCurrency(code, date);
	}
	
	//public void changeCode(String newCode, int id) {};
}
