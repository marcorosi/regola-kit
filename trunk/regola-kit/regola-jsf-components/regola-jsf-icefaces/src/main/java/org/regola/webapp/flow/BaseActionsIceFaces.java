package org.regola.webapp.flow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.webflow.action.MultiAction;
import org.springframework.webflow.execution.Event;
import org.springframework.webflow.execution.RequestContext;

import com.icesoft.faces.async.render.RenderManager;
import com.icesoft.faces.context.effects.Effect;
import com.icesoft.faces.webapp.xmlhttp.PersistentFacesState;

@Deprecated
public class BaseActionsIceFaces  {

	protected PersistentFacesState state;
	protected final Log log = LogFactory.getLog(getClass());
    protected RenderManager renderManager;
	protected transient Effect effectPanel = null;
	
	public RenderManager getRenderManager() {
		return renderManager;
	}

	public void setRenderManager(RenderManager renderManager) {
		this.renderManager = renderManager;
	}

	public PersistentFacesState getState() {
		return state;
	}

	public void setState(PersistentFacesState state) {
		this.state = state;
	}

	public Log getLog() {
		return log;
	}

	public Effect getEffectPanel() {
		return effectPanel;
	}

	public void setEffectPanel(Effect effectPanel) {
		this.effectPanel = effectPanel;
	}

	
}
