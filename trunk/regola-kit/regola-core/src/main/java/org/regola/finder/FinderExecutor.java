package org.regola.finder;

import java.util.List;

public interface FinderExecutor<T> {

	List<T> executeFinder(String finder, Object... args);

}
