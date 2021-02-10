package nbpcurrencyrates.service;

import java.io.IOException;
import java.math.BigDecimal;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
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

public class JsonURL extends RatesService{
	private String NBP_TABLES = "http://api.nbp.pl/api/exchangerates/tables/A/";
	private String NBP_RATES = "http://api.nbp.pl/api/exchangerates/rates/A/";
	private final CloseableHttpClient closableHttpClient = HttpClients.createDefault();
	
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
	
	@Override
	String getData(Date date, String code) {
		Date newDate = checkData(date);
		System.out.println(NBP_RATES + code + "/" + new SimpleDateFormat("yyyy-MM-dd").format(newDate) + "/?format=json");
		HttpGet httpGet = new HttpGet(NBP_RATES + code + "/" + new SimpleDateFormat("yyyy-MM-dd").format(newDate) + "/?format=json");
		try {
			CloseableHttpResponse closeableHttpResponse = closableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if(closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(httpEntity);
			} else {
				throw new DataNotFoundException("Data not found.");
			}
		} catch (ConnectException ce) {
			throw new TimeOutException("Connection timeout.");
		} catch (IOException ioe){
			throw new RuntimeException(ioe);
		}
	}
	
	BigDecimal getCurrency(String data, String code) {
		if(data != null && code != null) {
			JSONObject obj = new JSONObject(data);
			JSONArray jsonArray = obj.getJSONArray("rates");
			JSONObject jsonObject = new JSONObject(jsonArray.toString().substring(1, jsonArray.toString().length() - 1));
			return jsonObject.getBigDecimal("mid");
		} else {
			throw new DataNotFoundException("Data not found.");
		}
	}
}
