package nbpcurrencyrates.service;

import java.util.Date;

public interface RateService {
	Currency getCurrency(String code, Date date);
}
