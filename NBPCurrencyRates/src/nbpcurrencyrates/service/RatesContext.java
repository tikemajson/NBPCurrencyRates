package nbpcurrencyrates.service;

public class RatesContext {
	private RatesService strategy;
	
	public void set(RatesService strategy) {
		this.strategy = strategy;
	}
	
	public Currency getRates() {
		return this.strategy.getRates();
	}
}
