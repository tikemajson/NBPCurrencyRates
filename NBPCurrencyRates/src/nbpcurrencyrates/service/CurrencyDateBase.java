package nbpcurrencyrates.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "currencytable")
public class CurrencyDateBase{;

	@Id
	@GeneratedValue
	@Column(name = "CurrencyID", unique = true)
	private int id;
	
	@Column(name = "CurrencyName", nullable = true)
	private String name;
	
	@Column(name = "CurrencyCode", nullable = false)
	private String code;
	
	@Column(name = "CurrencyMid", nullable = false)
	private double mid;
	
	@Column(name = "CurrencyDate", nullable = false)
	private Date date;
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setMid(double mid) {
		this.mid = mid;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	public int getId() {
		return this.id;
	}
	
	public String getName() {
		return this.name;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public BigDecimal getMid() {
		return new BigDecimal(Double.toString(this.mid));
	}
	
	public Date getDate() {
		return this.date;
	}
}
