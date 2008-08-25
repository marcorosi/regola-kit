package org.regola.security.cas.providers.ticketvalidator;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.providers.cas.TicketResponse;
import org.acegisecurity.providers.cas.ticketvalidator.CasProxyTicketValidator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.regola.security.cas.util.ClusteredUtils;
import org.regola.security.cas.util.SessionIdExtractor;

public class ClusteredCasProxyTicketValidator extends CasProxyTicketValidator 
{
    private static final Log logger = LogFactory.getLog(ClusteredCasProxyTicketValidator.class);
    
    private SessionIdExtractor sessionIdExtractor;
    
	@Override
	public TicketResponse confirmTicketValid(String serviceTicket)
			throws AuthenticationException 
	{
        // Attempt to validate presented ticket using CAS' ProxyTicketValidator class
        ClusteredProxyTicketValidator pv = new ClusteredProxyTicketValidator();

        pv.setCasValidateUrl(super.getCasValidate());
        pv.setServiceTicket(ClusteredUtils.decodePassword(serviceTicket));
        pv.setSessionid(ClusteredUtils.decodeSessionId(serviceTicket));
        pv.setService(super.getServiceProperties().getService());
        pv.setSessiondIdParameterNameForCas(sessionIdExtractor.getSessionIdParamaterNameForCas());
        
        if (super.getServiceProperties().isSendRenew()) {
            logger.warn(
                  "The current CAS ProxyTicketValidator does not support the 'renew' property. "
                + "The ticket cannot be validated as having been issued by a 'renew' authentication. "
                + "It is expected this will be corrected in a future version of CAS' ProxyTicketValidator.");
        }

        if ((getProxyCallbackUrl() != null) && (!"".equals(getProxyCallbackUrl()))) {
            pv.setProxyCallbackUrl(getProxyCallbackUrl());
        }

        return validateNow(pv);
	}

	public void setSessionIdExtractor(SessionIdExtractor sessionIdExtractor) {
		this.sessionIdExtractor = sessionIdExtractor;
	}

}
