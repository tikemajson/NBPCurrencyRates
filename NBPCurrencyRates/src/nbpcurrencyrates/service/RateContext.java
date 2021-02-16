package nbpcurrencyrates.service;

import java.util.Date;

public class RateContext {
	RateService strategy;
	
	public void set(RateService rateService) {
		this.strategy = rateService;
	}
	
	public Currency getCurrency(String code, Date date) {
		return strategy.getCurrency(code, date);
	}
}
