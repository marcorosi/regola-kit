package org.regola.validation;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.model.ModelPattern;

public class LazyLoadingArrayList<T, F extends ModelPattern> extends ArrayList<T>
{
	private static final long serialVersionUID = 1L;
	
	protected final Log log = LogFactory.getLog(getClass());

	public interface Fetcher<C, F2 extends ModelPattern>
	{
		public List<C> fetch(F2 filter);
	}

	private Fetcher<T, F> fetcher;
	private F filter;

	public LazyLoadingArrayList(Fetcher<T, F> fetcher, F filter)
	{
		super(filter.getTotalItems());
		
		for (int i=0; i<filter.getTotalItems(); ++i) add(null);
		
		this.filter=filter;
		this.fetcher = fetcher;
		fetch();
	}
	
	private void fetch()
	{
		List<T> newEntries = fetcher.fetch(filter);
		
		int pos=0,offset=filter.getCurrentPage()*filter.getPageSize();
		
		for (T newEntry: newEntries)
		{
			set(offset+pos++,newEntry);
		}

	}

	@Override
	public T get(int index)
	{
		if (super.get(index) == null)
		{
			// le richieste avvengono sempre a gruppi di pagine
			int nominalPage  = filter.getCurrentPage();
			int requestPage = index / filter.getPageSize();
			
			if (requestPage!=filter.getCurrentPage())
			{
				log.error(String.format("Pagination error: index %d, current page %d, size %d", index, filter.getCurrentPage(), filter.getPageSize()));
				//---fix al problema di jsf che comanda anche il caricamento dell'oggetto successivo alla pagina corrente
				return null;
			}
			
			filter.setCurrentPage(requestPage);
			fetch();
			filter.setCurrentPage(nominalPage);

		}

		return super.get(index);
	}

}
