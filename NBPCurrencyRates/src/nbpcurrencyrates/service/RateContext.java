package nbpcurrencyrates.service;

import java.util.Date;

public class RateContext {
	RateService rateService;
	
	public void set(RateService rateService) {
		this.rateService = rateService;
	}
	
	public Currency getCurrency(String code, Date date) {
		return rateService.getCurrency(code, date);
	}
}
