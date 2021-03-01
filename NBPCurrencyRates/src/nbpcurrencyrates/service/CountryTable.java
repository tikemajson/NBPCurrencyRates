package nbpcurrencyrates.service;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "CountryTable")
@NamedQueries(
		{
			@NamedQuery (
				name = "CountryTable.getCountryList",
				query = "SELECT country FROM CountryTable country WHERE (country.cid = :currencyId)"
			),
			@NamedQuery (
				name = "CountryTable.findCountry",
				query = "SELECT country FROM CountryTable country WHERE (country.cid = :currencyId)"
			),
			@NamedQuery (
				name = "CountryTable.getCountryWithTwoOrMoreCurrency",
				query = "SELECT country.name, COUNT(country.cid) FROM CountryTable country GROUP BY country.name HAVING COUNT(country.cid) > 1"
			),
			@NamedQuery (
				name = "CountryTable.getCountry",
				query = "SELECT country FROM CountryTable country WHERE country.cid = :currencyId AND country.name = :countryName"
			)
		}
)
public class CountryTable {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private long id;
	
	@Column(name = "name", nullable = false)
	private String name;
	
	@Column(name = "cid", nullable = false)
	private long cid;

	public long getCid() {
		return cid;
	}

	public void setCid(long cid) {
		this.cid = cid;
	}

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
}
