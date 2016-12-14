package org.jobjects.myws.orm;

import java.util.List;

public interface Facade<T> {

	void create(T entity);

	T save(T entity);

	void remove(T entity);

	T find(Object id);

	List<T> findAll();

	List<T> findRange(int rangeMin, int rangeMax);

	long count();
	
	List<T> findByNamedQuery(String name, Object... params);
}