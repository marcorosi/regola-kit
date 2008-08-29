package org.regola.webapp.util;

import java.util.ResourceBundle;
import static org.regola.webapp.util.FacesUtils.getRequest;
import static org.regola.webapp.util.FacesUtils.getServletContext;

public class ResourcesUtil {
	
	public static final String jstlBundleParam = "javax.servlet.jsp.jstl.fmt.localizationContext";
	
	public static ResourceBundle getBundle() {
		return ResourceBundle.getBundle(getBundleName(), getRequest()
				.getLocale());
	}
	
	public static String getBundleName() {
		// get name of resource bundle from JSTL settings, JSF makes this too
		// hard
		return getServletContext().getInitParameter(jstlBundleParam);
	}

}
