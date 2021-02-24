package nbpcurrencyrates.service;

import java.io.IOException;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import nbpcurrencyrates.exception.CurrencyNotFoundException;
import nbpcurrencyrates.exception.DataConvertException;
import nbpcurrencyrates.exception.InvalidHostNameException;
import nbpcurrencyrates.exception.TimeoutException;

public class JsonUrlReader implements RatesService{
	private RatesContext nextReader = new RatesContext();
	private DataConverterService converter;
	private String NBP_URL = "http://api.nbp.pl/api/exchangerates/rates/A/";
	private final CloseableHttpClient closableHttpClient = HttpClients.createDefault();
	
	public JsonUrlReader (DataConverterService converter) {
		this.converter = converter;
	}
	
	public JsonUrlReader(String nbpUrl, DataConverterService converter) {
		this.NBP_URL = nbpUrl;
		this.converter = converter;
	}
	
	public JsonUrlReader(String nbpUrl, DataConverterService converter, RatesService next) {
		this.NBP_URL = nbpUrl;
		this.converter = converter;
		this.nextReader.set(next);;
	}
	
	private Date getDateWithRate(String code, Date date) {
		Date tempDate = date;
		HttpGet httpGet = new HttpGet(NBP_URL + code + "/" + new SimpleDateFormat("yyyy-MM-dd").format(tempDate));
		try {
			CloseableHttpResponse closableHttpResponse = closableHttpClient.execute(httpGet);
			while(closableHttpResponse.getStatusLine().getStatusCode() != 200) {
				httpGet.releaseConnection();
				closableHttpResponse.close();
				tempDate = new GetDate().getPreviousDate(tempDate);
				httpGet = new HttpGet(NBP_URL + code + "/" + new SimpleDateFormat("yyyy-MM-dd").format(tempDate));
				closableHttpResponse = closableHttpClient.execute(httpGet);
			}
			closableHttpResponse.close();
			return tempDate;
		} catch (CurrencyNotFoundException cnfe) {
			throw new RuntimeException(cnfe);
		} catch (ClientProtocolException cpe) {
			throw new InvalidHostNameException("Hostname is invalid.");
		} catch (ConnectException ce) {
			throw new TimeoutException("Connection timeout.");
		} catch (IOException ioe) {
			throw new RuntimeException(ioe);
		}
	}
	
	private String getRatesStringFromNBP(String code, Date date) {
		Date newDate = getDateWithRate(code, date);
		HttpGet httpGet = new HttpGet(NBP_URL + code + "/" + new SimpleDateFormat("yyyy-MM-dd").format(newDate) + "/?format=json");
		try {
			CloseableHttpResponse closeableHttpResponse = closableHttpClient.execute(httpGet);
			HttpEntity httpEntity = closeableHttpResponse.getEntity();
			if(closeableHttpResponse.getStatusLine().getStatusCode() == 200) {
				return EntityUtils.toString(httpEntity);
			} else {
				throw new CurrencyNotFoundException("Currency not found.");
			}
		} catch (ClientProtocolException cpe) {
			throw new InvalidHostNameException("Hostname is invalid.");
		} catch (ConnectException ce) {
			throw new TimeoutException("Connection timeout.");
		} catch (IOException ioe){
			throw new RuntimeException(ioe);
		}
	}
	
	@Override
	public Currency getRates(String code, Date date) {
		String jsonRateString = getRatesStringFromNBP(code, date);
		Currency currency = converter.convertData(jsonRateString);
		if(currency != null) {
			return currency;
		} else {
			if(nextReader != null) {
				return nextReader.getRates(code, date);
			} else {
				throw new DataConvertException("Can't convert data to currency object.");
			}
		}
	}
}
