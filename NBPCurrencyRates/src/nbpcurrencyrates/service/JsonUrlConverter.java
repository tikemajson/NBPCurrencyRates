package nbpcurrencyrates.service;

import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.json.JSONArray;
import org.json.JSONObject;

import nbpcurrencyrates.exception.DataConvertException;
import nbpcurrencyrates.exception.CurrencyNotFoundException;

public class JsonUrlConverter implements DataConverterService{
	private Currency convertJsonToRate(String data) {
		if(data != null) {
			JSONObject obj = new JSONObject(data);
			JSONArray jsonArrayRates = obj.getJSONArray("rates");
			JSONObject jsonObjectRates = new JSONObject(jsonArrayRates.toString().substring(1, jsonArrayRates.toString().length() - 1));
			String name = obj.getString("currency");
			String cur_code = obj.getString("code");
			BigDecimal mid = jsonObjectRates.getBigDecimal("mid");
			String date = jsonObjectRates.getString("effectiveDate");
			try {
				return new Currency(name, cur_code, mid, new SimpleDateFormat("yyyy-MM-dd").parse(date));
			} catch(ParseException pe) {
				throw new RuntimeException(pe);
			}
		} else {
			throw new CurrencyNotFoundException("Data not found.");
		}
	}

	@Override
	public Currency convertData(String data) {
		Currency currency = convertJsonToRate(data);
		if(currency != null) {
			return currency;
		} else {
			throw new DataConvertException("Can't conver json string to currency object.");
		}
	}
}
