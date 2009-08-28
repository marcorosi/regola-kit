package org.regola.webapp.flow;

import java.io.Serializable;
import java.util.HashMap;

import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Highlight;

public class FormActionsIceFaces implements Serializable {

	private static final long serialVersionUID = 1L;

	@SuppressWarnings("unchecked")
	public static HashMap<String, String> validate(String validationContext, Object object)
	{
		return  FormActions.validate(validationContext, object);
	}
	
	public static HashMap<String, Effect> raiseEffects(HashMap<String, String> errors)
	{
		HashMap<String, Effect> effects = new HashMap<String, Effect>();
		
		for (String property: errors.keySet())
		{
			effects.put(property, new Highlight("#FFFF00"));
		}
		
        return effects;
	}

}
