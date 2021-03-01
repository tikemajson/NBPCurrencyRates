package nbpcurrencyrates.service;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "RateTable")
@NamedQueries(
		{
			@NamedQuery (
				name = "RateTable.getRate",
				query = "SELECT rate FROM RateTable rate WHERE rate.cid = :currencyId AND rate.date = :rateDate"
			),
			@NamedQuery (
				name = "RateTable.findRate",
				query = "SELECT rate FROM RateTable rate WHERE rate.cid = :currencyId AND rate.date = :rateDate"
			),
			@NamedQuery (
				name = "RateTable.getAllRate",
				query = "SELECT rate FROM RateTable rate WHERE rate.cid = :currencyId"
			),
			@NamedQuery (
				name = "RateTable.getCurrencyWithMaxMidInPeriod",
				query = "SELECT codetable FROM CodeTable codetable JOIN FETCH codetable.rate ratetable WHERE (ratetable.mid = (SELECT MAX(ratetable.mid) FROM RateTable ratetable WHERE (ratetable.cid = :currencyId) AND (ratetable.date >= :currencyDateOne AND ratetable.date <= :currencyDateTwo)))"
				),
			@NamedQuery (
				name = "RateTable.getCurrencyWithMinMidInPeriod",
				query = "SELECT codetable FROM CodeTable codetable JOIN FETCH codetable.rate ratetable WHERE (ratetable.mid = (SELECT MIN(ratetable.mid) FROM RateTable ratetable WHERE (ratetable.cid = :currencyId) AND (ratetable.date >= :currencyDateOne AND ratetable.date <= :currencyDateTwo)))"
			),
			@NamedQuery (
				name = "RateTable.getCurrencyListWithMaxRate",
				query = "SELECT ratetable FROM RateTable ratetable WHERE (ratetable.cid = :currencyId) ORDER BY ratetable.mid DESC"
			),
			@NamedQuery (
				name = "RateTable.getCurrencyListWithMinRate",
				query = "SELECT ratetable FROM RateTable ratetable WHERE (ratetable.cid = :currencyId) ORDER BY ratetable.mid ASC"
			),
			@NamedQuery (
				name = "RateTable.getRateWithMaxDiff",
				query = "SELECT (SELECT rt.date FROM RateTable rt WHERE (rt.mid = (SELECT MAX(rt.mid) FROM RateTable rt WHERE rt.cid = :currencyId AND (rt.date >= :currencyDateOne AND rt.date <= :currencyDateTwo)))), (SELECT rt.date FROM RateTable rt WHERE (rt.mid = (SELECT MIN(rt.mid) FROM RateTable rt WHERE rt.cid = :currencyId AND (rt.date >= :currencyDateOne AND rt.date <= :currencyDateTwo)))), MAX(r.mid), MIN(r.mid) FROM RateTable r WHERE r.cid = :currencyId AND (r.date >= :currencyDateOne AND r.date <= :currencyDateTwo)"
			)
		}
)
public class RateTable {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", unique = true)
	private long id;
	
	@Column(name = "date", nullable = false)
	private Date date;
	
	@Column(name = "mid", nullable = false)
	private double mid;
	
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public BigDecimal getMid() {
		return new BigDecimal(Double.toString(mid));
	}

	public void setMid(double mid) {
		this.mid = mid;
	}

}
