package org.regola.webapp.flow;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;

import org.hibernate.validator.InvalidValue;
import org.regola.formsValidation.Amendment;
import org.regola.formsValidation.AmendmentUtils;
import org.regola.formsValidation.AmendmentsClassValidator;

public class FormActions implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public static HashMap<String, String> validate(String validationContext, Object object)
	{
	
		HashMap<String, String> errors = new HashMap<String, String>();
		
		if (object == null) return errors;
		
		//lettura eventuali emendamenti da eventuale file di configurazione
		List<Amendment> amendments = new AmendmentUtils().readDSLAmendments(validationContext /*amendmentConfiguration*/,  object.getClass());
		
		
		AmendmentsClassValidator validator = new AmendmentsClassValidator( object.getClass(), amendments);
		InvalidValue[] msgs = validator.getInvalidValues(object);

		//populate errors and effects
		for (InvalidValue msg : msgs)
		{
			errors.put(msg.getPropertyName(), msg.getMessage());
		}
		
		//'sovrascrivo' gli errori semantici con gli eventuali precendenti errori sintattici
		//errors.putAll(conversionErrors); //per gli errori si conversione non metto effetti	

		return errors;
	}
}
