package ${package}.dao;

import java.util.List;


import org.regola.dao.GenericDao;

import ${package}.model.Language;



public interface LanguageDao extends GenericDao<Language, Integer> {

	public List<Language> readAll();
	
}
