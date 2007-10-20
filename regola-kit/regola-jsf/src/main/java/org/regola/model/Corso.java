package org.regola.model;

import java.io.Serializable;
import java.sql.Date;
import java.util.Comparator;

public class Corso implements Serializable
{
	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unused")
	private static Comparator comparatore;

	private Date dataOra;

	@SuppressWarnings("unused")
	private Integer idcorso;

	private String codCorNaz;

	private String codCorso;

	private String codCorsoAlma;

	private String descAbbCorso;

	private String descCorso;

	private String frmName;

	private String terminale;

	private String titolo = "";

	private String uteMatr;

	private String tipoCorso;

	private String corsoPerVerbali;
	private Integer idContesto;

	public Corso()
	{
		super();
	}

	public void setCodCorNaz(String codCorNaz)
	{
		this.codCorNaz = codCorNaz;
	}

	public String getCodCorNaz()
	{
		return this.codCorNaz;
	}

	public void setCodCorso(String codCorso)
	{
		this.codCorso = codCorso;
	}

	public String getCodCorso()
	{
		return this.codCorso;
	}

	public void setCodCorsoAlma(String codCorsoAlma)
	{
		this.codCorsoAlma = codCorsoAlma;
	}

	public String getCodCorsoAlma()
	{
		return this.codCorsoAlma;
	}

	public void setComparatore(Comparator comparatore)
	{
		Corso.comparatore = comparatore;
	}

	public static Comparator getComparatore()
	{
		return new ordinaXDescCorso(true);
	}

	public void setDataOra(Date dataOra)
	{
		this.dataOra = dataOra;
	}

	public Date getDataOra()
	{
		return this.dataOra;
	}

	public void setDescAbbCorso(String descAbbCorso)
	{
		this.descAbbCorso = descAbbCorso;
	}

	public String getDescAbbCorso()
	{
		return this.descAbbCorso;
	}

	public String getDescAbbCorsoLower()
	{
		return this.descAbbCorso.toLowerCase();
	}

	public void setDescCorso(String descCorso)
	{
		this.descCorso = descCorso;
	}

	public String getDescCorso()
	{
		return this.descCorso;
	}

	public void setFrmName(String frmName)
	{
		this.frmName = frmName;
	}

	public String getFrmName()
	{
		return this.frmName;
	}

	public void setTerminale(String terminale)
	{
		this.terminale = terminale;
	}

	public String getTerminale()
	{
		return this.terminale;
	}

	public void setTitolo(String titolo)
	{
		this.titolo = titolo;
	}

	public String getTitolo()
	{
		return titolo;
	}

	public void setUteMatr(String uteMatr)
	{
		this.uteMatr = uteMatr;
	}

	public String getUteMatr()
	{
		return this.uteMatr;
	}

	public String getTipoCorso()
	{
		return tipoCorso;
	}

	public void setTipoCorso(String tipoCorso)
	{
		this.tipoCorso = tipoCorso;
	}

	public boolean isTriennale()
	{
		return "L".equals(tipoCorso);
	}

	public boolean isSpecialistica()
	{
		return "LS".equals(tipoCorso) || "TU".equals(tipoCorso);
	}

	@Override
	public boolean equals(Object o)
	{
		if (o == this)
		{
			return true;
		}

		if (!(o instanceof Corso))
		{
			return false;
		}

		Corso pk = (Corso) o;
		return (getCodCorso().equals(pk.getCodCorso()));
	}

	@Override
	public int hashCode()
	{
		int result = 17;
		result = (37 * result) + codCorso.hashCode();
		return result;
	}

	public void setCorsoPerVerbali(String s)
	{
		this.corsoPerVerbali = s;
	}
	
	public boolean isCorsoPerVerbali()
	{
		return "S".equals(this.corsoPerVerbali);
	}
	
	/**
	 * @return true se il corso è un 'normale' corso di plitvice. Al momento serve per escludere i corsi
	 * da visualizzare solo nel modulo dei verbali
	 */
	public boolean isCorsoStandard()
	{
		return !isCorsoPerVerbali();
	}

	public Integer getIdContesto()
	{
		return idContesto;
	}

	public void setIdContesto(Integer idContesto)
	{
		this.idContesto = idContesto;
	}
}

class ordinaXDescCorso implements Comparator
{
	private boolean ascending = true;

	public ordinaXDescCorso(boolean ascending)
	{
		this.ascending = ascending;
	}

	public int compare(Object object1, Object object2)
	{
		int val = 0;
		String valore1 = null;
		String valore2 = null;

		Corso c1 = (Corso) object1;
		valore1 = c1.getDescAbbCorso();

		Corso c2 = (Corso) object2;
		valore2 = c2.getDescAbbCorso();

		if (valore1 == null)
		{
			if (valore2 == null)
			{
				val = 0;
			} else
			{
				val = -1;
			}
		} else
		{
			if (valore2 == null)
			{
				val = 1;
			} else
			{
				val = valore1.compareTo(valore2);
			}
		}

		return ascending ? val : (val * -1);
	}
}
