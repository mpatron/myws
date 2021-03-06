package org.jobjects.myws.orm;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.persistence.PersistenceContextType;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

public abstract class AbstractFacade<T> implements Facade<T> {

	private Logger LOGGER = Logger.getLogger(getClass().getName());
	private PersistenceContextType transactionLocal;

	private Class<T> entityClass;

	public AbstractFacade(Class<T> entityClass) {
		this.entityClass = entityClass;
		try {
			getEntityManager().getTransaction();
			transactionLocal = PersistenceContextType.EXTENDED;
		} catch (Throwable t) {
			transactionLocal = PersistenceContextType.TRANSACTION;
		}
	}

	protected abstract EntityManager getEntityManager();
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jobjects.orm.tools.Facade#create(T)
	 */
	@Override
	public void create(T entity) {
		EntityTransaction trx = null;
		if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
			trx = getEntityManager().getTransaction();
			trx.begin();
		}
		try {
			getEntityManager().persist(entity);
			if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
				trx.commit();
			}
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE,
					"JPA Erreur non prevu. Transaction est rollback.", t);
			if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
				trx.rollback();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jobjects.orm.tools.Facade#save(T)
	 */
	@Override
	public T save(T entity) {
		T returnValue = null;
		EntityTransaction trx = null;
		if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
			trx = getEntityManager().getTransaction();
			trx.begin();
		}
		try {
			returnValue = getEntityManager().merge(entity);
			if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
				trx.commit();
			}
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE,
					"JPA Erreur non prevu. Transaction est rollback.", t);
			if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
				trx.rollback();
			}
		}
		return returnValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jobjects.orm.tools.Facade#remove(T)
	 */
	@Override
	public void remove(T entity) {
		EntityTransaction trx = null;
		if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
			trx = getEntityManager().getTransaction();
			trx.begin();
		}
		try {
			getEntityManager().remove(getEntityManager().merge(entity));
			if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
				trx.commit();
			}
		} catch (Throwable t) {
			LOGGER.log(Level.SEVERE,
					"JPA Erreur non prevu. Transaction est rollback.", t);
			if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
				trx.rollback();
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jobjects.orm.tools.Facade#find(java.lang.Object)
	 */
	@Override
	public T find(Object id) {
		return getEntityManager().find(entityClass, id);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jobjects.orm.tools.Facade#findAll()
	 */
	@Override
	public List<T> findAll() {
		CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder()
				.createQuery(entityClass);
		cq.select(cq.from(entityClass));
		return getEntityManager().createQuery(cq).getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jobjects.orm.tools.Facade#findRange(int[])
	 */
	@Override
	public List<T> findRange(int rangeMin, int rangeMax) {
		CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder()
				.createQuery(entityClass);
		cq.select(cq.from(entityClass));
		TypedQuery<T> q = getEntityManager().createQuery(cq);
		q.setMaxResults(rangeMax - rangeMin);
		q.setFirstResult(rangeMin);
		return q.getResultList();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.jobjects.orm.tools.Facade#count()
	 */
	@Override
	public long count() {
		CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		cq.select(cb.count(cq.from(entityClass)));
		return getEntityManager().createQuery(cq).getSingleResult();
	}

	/**
	 * @see be.bzbit.framework.domain.repository.GenericRepository
	 *      #findByNamedQuery(java.lang.String, java.lang.Object[])
	 */
	@Override
	public List<T> findByNamedQuery(final String name, Object... params) {
		TypedQuery<T> query = getEntityManager().createNamedQuery(name,
				entityClass);
		for (int i = 0; i < params.length; i++) {
			query.setParameter(i + 1, params[i]);
		}
		return query.getResultList();
	}
}
