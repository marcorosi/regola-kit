package org.regola.model;

import java.io.Serializable;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.StringTokenizer;

import javax.persistence.Transient;

public class ItemId implements Serializable {

	private static final long serialVersionUID = -7440178962501455406L;

	private Integer invoiceId;
	private Integer itemNumber;

	public ItemId() {
	}

	public Integer getInvoiceId() {
		return invoiceId;
	}

	public void setInvoiceId(Integer invoiceId) {
		this.invoiceId = invoiceId;
	}

	public Integer getItemNumber() {
		return itemNumber;
	}

	public void setItemNumber(Integer itemNumber) {
		this.itemNumber = itemNumber;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((getInvoiceId() == null) ? 0 : getInvoiceId().hashCode());
		result = prime
				* result
				+ ((getItemNumber() == null) ? 0 : getItemNumber()
						.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ItemId other = (ItemId) obj;
		if (getInvoiceId() == null) {
			if (other.getInvoiceId() != null)
				return false;
		} else if (!getInvoiceId().equals(other.getInvoiceId()))
			return false;
		if (getItemNumber() == null) {
			if (other.getItemNumber() != null)
				return false;
		} else if (!getItemNumber().equals(other.getItemNumber()))
			return false;
		return true;
	}
	
	private static final String DELIMITATORE = "#";

	@Transient
	public String getEncoded() {
		StringBuilder sb = new StringBuilder();
		sb.append(itemNumber).append(DELIMITATORE).append(invoiceId);

		return URLEncoder.encode(sb.toString());
	}

	public void setEncoded(String d) {
		if (d != null && d.trim().length() > 0) {
			d = URLDecoder.decode(d);
			StringTokenizer st = new StringTokenizer(d, DELIMITATORE);
			setItemNumber(Integer.valueOf(st.nextToken()));
			setInvoiceId(Integer.valueOf(st.nextToken()));
		} else {
			throw new IllegalArgumentException(
					"Stringa di inizializzazione nulla o vuota");
		}
	}	

}
