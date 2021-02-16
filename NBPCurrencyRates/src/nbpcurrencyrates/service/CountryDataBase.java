package nbpcurrencyrates.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "CurrencyCountry")
public class CountryDataBase {

	@Id
	@GeneratedValue
	@Column(name = "CountryID", unique = true)
	private int id;
	
	@Column(name = "Country", nullable = true)
	private String country;
	
	@Column(name = "CurrencyCode", nullable = false)
	private String code;
	
	@Column(name = "CurrencyID", nullable = false)
	private int currencyid;
	
	public void setCountry(String country) {
		this.country = country;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public void setCurrencyID(int id) {
		this.currencyid = id;
	}
	
	public String getCountry() {
		return this.country;
	}
	
	public String getCode() {
		return this.code;
	}
	
	public int getCurrencyID() {
		return this.currencyid;
	}
	
}
