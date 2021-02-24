package nbpcurrencyrates.service;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "CodeTable")
public class CodeTable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "cid", unique = true)
	private long id;
	
	@Column(name = "CodeName", nullable = false)
	private String code;
	
	@Column(name = "CodeFullName", nullable = false)
	private String name;

	@OneToMany(fetch = FetchType.EAGER, targetEntity = CountryTable.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "cid", referencedColumnName = "cid")
	private Set<CountryTable> country;
	
	@OneToMany(fetch = FetchType.EAGER, targetEntity = RateTable.class, cascade = CascadeType.ALL)
	@JoinColumn(name = "cid", referencedColumnName = "cid")
	private Set<RateTable> rate;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<RateTable> getRate() {
		return rate;
	}

	public void setRate(Set<RateTable> rate) {
		this.rate = rate;
	}

	public Set<CountryTable> getCountry() {
		return country;
	}

	public void setCountry(Set<CountryTable> country) {
		this.country = country;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
}
