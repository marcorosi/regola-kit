package org.regola.webapp.security.impl;

import org.regola.model.ContestoPlitvice;
import org.regola.webapp.security.PlitviceContextHolder;
import org.regola.webapp.security.PlitviceUser;

import org.acegisecurity.context.SecurityContextHolder;

public class PlitviceContextHolderAcegi implements PlitviceContextHolder
{
	public ContestoPlitvice getContesto()
	{
		return ((PlitviceUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getContesto();
	}
}
