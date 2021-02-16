package nbpcurrencyrates.service;

import java.math.BigDecimal;
import java.util.Date;

public class Currency {
	private String name;
	private String code;
	private BigDecimal mid;
	private Date date;
	
	public Currency(String name, String code, BigDecimal mid, Date date) {
		this.name = name;
		this.code = code;
		this.mid = mid;
		this.date = date;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public BigDecimal getMid() {
		return this.mid;
	}
	
	public Date getDate() {
		return this.date;
	}
}
