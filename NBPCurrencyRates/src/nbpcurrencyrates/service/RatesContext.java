package nbpcurrencyrates.service;

import java.util.Date;

public class RatesContext {
	private RatesService strategy;
	
	public void set(RatesService strategy) {
		this.strategy = strategy;
	}
	
	public Currency getRates(String code, Date date) {
		return this.strategy.getRates(code, date);
	}
}
