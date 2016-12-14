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

/**
 * @author Mickaël Patron
 * @version 2016-05-08
 * @param <T>
 *          classe abstraite ORM générique.
 */
public abstract class AbstractFacade<T> implements Facade<T> {
  /**
   * Instance du logger.
   */
  private Logger logger = Logger.getLogger(getClass().getName());
  /**
   * Type de transaction pour la portabilité J2EE ou J2SE.
   */
  private PersistenceContextType transactionLocal;
  /**
   * Classe de l'entity.
   */
  private Class<T> entityClass;

  /**
   * Constructeur de la classe.
   * @param entityClasse
   *          Classe de l'entity.
   */
  public AbstractFacade(final Class<T> entityClasse) {
    this.entityClass = entityClasse;
    try {
      getEntityManager().getTransaction();
      transactionLocal = PersistenceContextType.EXTENDED;
    } catch (Throwable t) {
      transactionLocal = PersistenceContextType.TRANSACTION;
    }
  }

  /**
   * @return l'EntityManager.
   */
  protected abstract EntityManager getEntityManager();

  /*
   * (non-Javadoc)
   * @see org.jobjects.orm.tools.Facade#create(T)
   */
  @Override
  public final void create(final T entity) {
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
      logger.log(Level.SEVERE,
          "JPA Erreur non prevu. Transaction est rollback.", t);
      if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
        trx.rollback();
      }
    }
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.orm.tools.Facade#save(T)
   */
  @Override
  public final T save(final T entity) {
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
      logger.log(Level.SEVERE,
          "JPA Erreur non prevu. Transaction est rollback.", t);
      if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
        trx.rollback();
      }
    }
    return returnValue;
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.orm.tools.Facade#remove(T)
   */
  @Override
  public final void remove(final T entity) {
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
      logger.log(Level.SEVERE,
          "JPA Erreur non prevu. Transaction est rollback.", t);
      if (PersistenceContextType.EXTENDED.equals(transactionLocal)) {
        trx.rollback();
      }
    }
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.orm.tools.Facade#find(java.lang.Object)
   */
  @Override
  public final T find(final Object id) {
    return getEntityManager().find(entityClass, id);
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.orm.tools.Facade#findAll()
   */
  @Override
  public final List<T> findAll() {
    CriteriaQuery<T> cq = getEntityManager().getCriteriaBuilder()
        .createQuery(entityClass);
    cq.select(cq.from(entityClass));
    return getEntityManager().createQuery(cq).getResultList();
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.orm.tools.Facade#findRange(int[])
   */
  @Override
  public final List<T> findRange(final int rangeMin, final int rangeMax) {
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
   * @see org.jobjects.orm.tools.Facade#count()
   */
  @Override
  public final long count() {
    CriteriaBuilder cb = getEntityManager().getCriteriaBuilder();
    CriteriaQuery<Long> cq = cb.createQuery(Long.class);
    cq.select(cb.count(cq.from(entityClass)));
    return getEntityManager().createQuery(cq).getSingleResult();
  }

  /*
   * (non-Javadoc)
   * @see org.jobjects.myws.orm.Facade#findByNamedQuery(java.lang.String,
   * java.lang.Object[])
   */
  @Override
  public final List<T> findByNamedQuery(final String name,
      final Object... params) {
    TypedQuery<T> query = getEntityManager().createNamedQuery(name,
        entityClass);
    for (int i = 0; i < params.length; i++) {
      query.setParameter(i + 1, params[i]);
    }
    return query.getResultList();
  }
}
