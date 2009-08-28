package org.regola.webapp.action.webgalileo;

import java.io.Serializable;
import java.util.HashMap;

import org.hibernate.validator.InvalidValue;
import org.regola.model.ModelPattern;
import org.regola.service.GenericManager;
import org.regola.webapp.action.AutoCompleteBean;
import org.regola.webapp.action.BasePage;
import org.regola.webapp.action.FormPage;
import org.regola.webapp.action.component.FormPageComponent;


public class FormPageWebGalileo<T, ID extends Serializable, F extends ModelPattern>
        extends BasePageWebGalileo
        implements FormPageComponent<T, ID, F> {


    FormPage<T, ID, F> formPage;

    public FormPage<T, ID, F> getFormPage() {
        return formPage;
    }

    public void setFormPage(FormPage<T, ID, F> formPage) {
        this.formPage = formPage;
    }

    public <MODEL, MODELID extends Serializable, FILTER extends ModelPattern> void addAutoCompleteLookUp(String property, MODEL model, FILTER filter, GenericManager<MODEL, MODELID> manager) {

        AutoCompleteBean<MODEL, MODELID, FILTER> ac = new AutoCompleteBeanWebGalileo<MODEL, MODELID, FILTER>();
        ac.init(model, filter, manager);

        formPage.getLookups().put(property, ac);
    }

    public void init() {


    }

    @SuppressWarnings("unchecked")
    public <T> InvalidValue[] validate(InvalidValue[] msgs) {
       
        return msgs;
    }

    
    public void setPage(FormPage<T, ID, F> page) {
        formPage = page;

    }
}
