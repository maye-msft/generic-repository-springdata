package com.github.mayemsft.springdata.genericrepository;

import java.util.Arrays;
import java.util.List;

import org.springframework.context.ApplicationContext;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;
import org.springframework.data.cassandra.repository.CassandraRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.repository.Repository;

public class CassandraRepositoryHelper<T, ID> extends AbstractRepositoryHelper<T, ID> {

	@SuppressWarnings("unchecked")
	public CassandraRepositoryHelper(Class<T> cls, Class<ID> id,
			ApplicationContext context) throws Exception {
		super(cls, id, CassandraRepository.class, context);
	}

	/**
	 * Returns a {@link Slice} of entities meeting the paging restriction provided in the {@code Pageable} object.
	 *
	 * @param pageable must not be {@literal null}.
	 * @return a {@link Slice} of entities.
	 * @throws Exception 
	 * @see CassandraPageRequest
	 * @since 2.0
	 */
	@SuppressWarnings("unchecked")
	public Slice<T> findAll(Pageable pageable) throws Exception {
		return (Slice<T>) this.invoke(this.repository, "findAll", Arrays.asList(new Object[]{pageable}), Pageable.class);
		
	}

	/**
	 * Inserts the given entity. Assumes the instance to be new to be able to apply insertion optimizations. Use the
	 * returned instance for further operations as the save operation might have changed the entity instance completely.
	 * Prefer using {@link #save(Object)} instead to avoid the usage of store-specific API.
	 *
	 * @param entity must not be {@literal null}.
	 * @return the saved entity
	 * @throws Exception 
	 * @since 2.0
	 */
	@SuppressWarnings("unchecked")
	public <S extends T> S insert(S entity) throws Exception {
		return (S) this.invoke(this.repository, "insert", Arrays.asList(new Object[]{entity}), Object.class);
	}

	/**
	 * Inserts the given entities. Assumes the given entities to have not been persisted yet and thus will optimize the
	 * insert over a call to {@link #saveAll(Iterable)}. Prefer using {@link #saveAll(Iterable)} to avoid the usage of
	 * store specific API.
	 *
	 * @param entities must not be {@literal null}.
	 * @return the saved entities
	 * @throws Exception 
	 * @since 2.0
	 */
	@SuppressWarnings("unchecked")
	public <S extends T> List<S> insert(Iterable<S> entities) throws Exception {
		return (List<S>) this.invoke(this.repository, "insert", Arrays.asList(new Object[]{entities}), Iterable.class);
	}
}
