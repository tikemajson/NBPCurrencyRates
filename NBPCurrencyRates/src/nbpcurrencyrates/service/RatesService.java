package nbpcurrencyrates.service;

import java.util.Date;

public interface RatesService {
	public Currency getRates(String code, Date date);
}
