package ${mbean_package};

import ${model_class};
import ${id_class};
import ${filter_class};

import it.kion.regola.webapp.action.AutoCompleteBean;
import it.kion.regola.webapp.action.FormPage;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class ${mbean_form_name} extends FormPage<${model_name}, ${id_name}, ${filter_name}>
{
	@SuppressWarnings("unchecked")
	public ${mbean_form_name}()
	{
		id = new ${id_name}();
		idClass = (Class<${id_name}>) id.getClass();
		model = new ${model_name}();
		model.setId(id);
	}

	public void init()
	{
		super.init();

		if (id != null)
		{
			model = getServiceManager().get(id);
			id = model.getId();
			setOriginalId(id.getEncoded());
		}

		//@TODO aggiungi gli autocompletamenti qui
		//getAutoCompleteMateria().init(model.getMateria());
	}

	@Override
	public String save()
	{
		//@TODO qui bisogna popolare il modello
		//dall'hash di autocompletamento
		/*List<Materia> materie = getAutoCompleteMateria().getSelection();
		if (materie.size() != 1)
		{
			//TODO: gestione errore
			log.info(String.format("Impossibile salvare: trovate %d materie", materie.size()));
		} else
		{
			//la metto nel modello
			id.setCodMateria(materie.get(0).getId());
		}*/

		return super.save();
	}

	/*@SuppressWarnings("unchecked")
	public AutoCompleteBean<Materia,String,MateriaFilter> getAutoCompleteMateria()
	{
		return getAutoComplete().get("materia");
	}*/
}
