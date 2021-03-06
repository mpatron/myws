package org.jobjects.myws.orm;

import java.util.List;

public interface Facade<T> {

	public abstract void create(T entity);

	public abstract T save(T entity);

	public abstract void remove(T entity);

	public abstract T find(Object id);

	public abstract List<T> findAll();

	public abstract List<T> findRange(int rangeMin, int rangeMax);

	public abstract long count();
	
	public abstract List<T> findByNamedQuery(final String name, Object... params);

}