package nbpcurrencyrates.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import nbpcurrencyrates.exception.DataNotFoundException;
import nbpcurrencyrates.exception.TimeOutException;

public class JSONReader implements RateService{
	private String NBP_TABLES = "http://api.nbp.pl/api/exchangerates/tables/A/";
	private String NBP_RATES = "http://api.nbp.pl/api/exchangerates/rates/A/";
	private final CloseableHttpClient closableHttpClient = HttpClients.createDefault();
	
	private Date getDate(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DATE, -1);
		return calendar.getTime();
	}
	
	private Date checkData(Date date) {
		Date tempDate = date;
		HttpGet httpGet = new HttpGet(NBP_TABLES + new SimpleDateFormat("yyyy-MM-dd").format(tempDate));
		try {
			CloseableHttpResponse closableHttpResponse = closableHttpClient.execute(httpGet);
			while(closableHttpResponse.getStatusLine().getStatusCode() != 200) {
				httpGet.releaseConnection();
				closableHttpResponse.close();
				tempDate = getDate(tempDate);
				httpGet = new HttpGet(NBP_TABLES + new SimpleDateFormat("yyyy-MM-dd").format(tempDate));
				closableHttpResponse = closableHttpClient.execute(httpGet);
			}
			closableHttpResponse.close();
			return tempDate;
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
	private String getData(Date date, String code) {
		Date newDate = checkData(date);
		HttpGet httpGet = new HttpGet(NBP_RATES + code + "/" + new SimpleDateFormat("yyyy-MM-dd").format(newDate) + "/?format=json");
		try {
			CloseableHttpResponse closeableHttpResponse = closableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if(closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(httpEntity);
			} else {
				throw new DataNotFoundException("Currency not found.");
			}
		} catch (ConnectException ce) {
			throw new TimeOutException("Connection timeout.");
		} catch (IOException ioe){
			throw new RuntimeException(ioe);
		}
	}
	
	private Currency convertData(String data) {
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
			throw new DataNotFoundException("Data not found.");
		}
	}
	
	public Currency getCurrency(String code, Date date) {
		String data = getData(date, code);
		Currency currency = convertData(data);
		return currency;
	}
}
