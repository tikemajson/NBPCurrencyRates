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
@Table(name = "RateTable")
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
