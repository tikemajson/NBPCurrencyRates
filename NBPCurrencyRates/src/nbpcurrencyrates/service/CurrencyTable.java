package nbpcurrencyrates.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CurrencyTable")
public class CurrencyTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	private long id;
	
	@Column(name = "CurrencyName", nullable = true)
	private String name;
	
	@Column(name = "CurrencyCode", nullable = false)
	private String code;
	
	@Column(name = "CurrencyMid", nullable = false)
	private double mid;
	
	@Column(name = "CurrencyDate", nullable = false)
	private Date date;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public BigDecimal getMid() {
		return new BigDecimal(Double.toString(this.mid));
	}

	public void setMid(double mid) {
		this.mid = mid;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	
}
