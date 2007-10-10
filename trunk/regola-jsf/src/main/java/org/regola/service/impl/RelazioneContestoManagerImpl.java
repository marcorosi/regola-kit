package org.regola.service.impl;

import org.regola.dao.GenericDao;
import org.regola.dao.RelazioneContestoFilter;
import org.regola.model.RelazioneContesto;
import org.regola.service.RelazioneContestoManager;
import org.regola.webapp.security.PlitviceContextHolder;

import java.util.List;

public class RelazioneContestoManagerImpl implements RelazioneContestoManager {
	
	private GenericDao<RelazioneContesto,Integer> relazioneContestoDao;
	
	private PlitviceContextHolder plitviceContextHolder;
	
	public GenericDao<RelazioneContesto, Integer> getRelazioneContestoDao() {
		return relazioneContestoDao;
	}

	public void setRelazioneContestoDao(
			GenericDao<RelazioneContesto, Integer> relazioneContestoDao) {
		this.relazioneContestoDao = relazioneContestoDao;
	}
	
	public PlitviceContextHolder getPlitviceContextHolder() {
		return plitviceContextHolder;
	}

	public void setPlitviceContextHolder(PlitviceContextHolder plitviceContextHolder) {
		this.plitviceContextHolder = plitviceContextHolder;
	}

	public List<RelazioneContesto> getCorsiContestoCorrente() {
		RelazioneContestoFilter filter = new RelazioneContestoFilter();
		
		Integer idContesto = plitviceContextHolder.getContesto().getIdContesto(); 
		
		filter.setIdContesto(idContesto);
		filter.setPageSize(10000);
		
		return relazioneContestoDao.find(filter);
	}



}
