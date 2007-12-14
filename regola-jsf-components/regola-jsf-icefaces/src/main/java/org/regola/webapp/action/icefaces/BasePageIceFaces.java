/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.regola.webapp.action.icefaces;

import com.icesoft.faces.async.render.IntervalRenderer;
import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.context.effects.Shake;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;
import org.regola.webapp.action.component.BasePageComponent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author nicola
 */
public abstract class BasePageIceFaces implements BasePageComponent {

    protected final Log log = LogFactory.getLog(getClass());
    private RenderManager renderManager;
    protected PersistentFacesState state;
    @SuppressWarnings("unused")
    protected IntervalRenderer clock;
    Effect effectPanel = null;

    public PersistentFacesState getState() {
        return state;
    }

    public void setState(PersistentFacesState state) {
        this.state = state;
    }

    public Effect getEffectPanel() {
        return effectPanel;
    }

    public void setEffectPanel(Effect effect) {
        effectPanel = effect;
    }

    public RenderManager getRenderManager() {
        return renderManager;
    }

    public void setRenderManager(RenderManager renderManger) {
        this.renderManager = renderManger;
    }

    abstract public void init();

    public void shakeWindow() {
        setEffectPanel( new Shake() );
    }
}
