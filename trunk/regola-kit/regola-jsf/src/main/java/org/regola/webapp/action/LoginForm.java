package org.regola.webapp.action;

import javax.faces.event.ActionEvent;

import org.acegisecurity.AuthenticationException;
import org.acegisecurity.AuthenticationManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.validator.NotEmpty;
import org.regola.webapp.action.plug.FormPagePlugProxy;
import org.regola.webapp.security.AuthenticationUtils;

/**
 * JSF backing bean for the login page 
 * 
 * @author marco
 *
 */
public class LoginForm {

    protected final Log log = LogFactory.getLog(getClass());
    String username = "";
    String password = "";
    private AuthenticationManager authenticationManager;

    @NotEmpty
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @NotEmpty
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void init() {

        formPage.setPlug(new FormPagePlugProxy(this));
        formPage.init();

    }
    FormPage formPage;

    public void setFormPage(FormPage formPage) {
        this.formPage = formPage;

    }

    public FormPage getFormPage() {
        return formPage;
    }

    public void submit(ActionEvent event) {
        formPage.submit(event);

        if (formPage.validate(this).length > 0) {
            return;
        }

        try {
            String targetUrl = AuthenticationUtils.acegiProgrammaticLogin(
                    getUsername(), getPassword(), formPage.getRequest(),
                    formPage.getSession(), authenticationManager);
            formPage.setErrore("Login effettuato");

            if (targetUrl != null) {
                String ctxPath = formPage.getRequest().getContextPath();
                int idx = targetUrl.indexOf(ctxPath);
                String target = targetUrl.substring(idx + ctxPath.length());

                log.debug(String.format("authentication successful, forwarding to %s obtained from %s", target, targetUrl));

                formPage.forward(target);
            }

        } catch (AuthenticationException e) {
            log.debug(e.getMessage());
            formPage.setErrore("Username/Password errate (" + e.getMessage() + ")");
            formPage.getComponent().shakeWindow();
            setPassword("");
        }
    }

    public AuthenticationManager getAuthenticationManager() {
        return authenticationManager;
    }

    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
}
