package net.mayemsft.springdata.genericrepository;

import java.util.List;
import java.util.Optional;

interface IRepositoryHelper<T, ID> {
	/**
	 * Returns instance of the type.
	 *
	 * @param methodName 
	 * @param params 
	 * @param paramTypes 
	 * @return entity
	 * @throws Exception 
	 */
	public T queryOne(String methodName, List<Object> params, Class<?>... paramTypes) throws Exception;
	
	
	/**
	 * Returns instances of the type.
	 *
	 * @param methodName 
	 * @param params 
	 * @param paramTypes 
	 * @return entities
	 */
	public Iterable<T> queryList(String methodName, List<Object> params, Class<?>... paramTypes)  throws Exception;
	
	/**
	 * Saves a given entity. Use the returned instance for further operations as the save operation might have changed the
	 * entity instance completely.
	 *
	 * @param entity must not be {@literal null}.
	 * @return the saved entity will never be {@literal null}.
	 */
	<S extends T> S save(S entity)  throws Exception;

	/**
	 * Saves all given entities.
	 *
	 * @param entities must not be {@literal null}.
	 * @return the saved entities will never be {@literal null}.
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	<S extends T> Iterable<S> saveAll(Iterable<S> entities) throws Exception;

	/**
	 * Retrieves an entity by its id.
	 *
	 * @param id must not be {@literal null}.
	 * @return the entity with the given id or {@literal Optional#empty()} if none found
	 * @throws Exception 
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	Optional<T> findById(ID id) throws Exception;

	/**
	 * Returns whether an entity with the given id exists.
	 *
	 * @param id must not be {@literal null}.
	 * @return {@literal true} if an entity with the given id exists, {@literal false} otherwise.
	 * @throws IllegalArgumentException if {@code id} is {@literal null}.
	 */
	boolean existsById(ID id) throws Exception;

	/**
	 * Returns all instances of the type.
	 *
	 * @return all entities
	 */
	Iterable<T> findAll() throws Exception;

	/**
	 * Returns all instances of the type with the given IDs.
	 *
	 * @param ids
	 * @return
	 */
	Iterable<T> findAllById(Iterable<ID> ids) throws Exception;

	/**
	 * Returns the number of entities available.
	 *
	 * @return the number of entities
	 */
	long count() throws Exception;

	/**
	 * Deletes the entity with the given id.
	 *
	 * @param id must not be {@literal null}.
	 * @throws IllegalArgumentException in case the given {@code id} is {@literal null}
	 */
	void deleteById(ID id) throws Exception;

	/**
	 * Deletes a given entity.
	 *
	 * @param entity
	 * @throws IllegalArgumentException in case the given entity is {@literal null}.
	 */
	void delete(T entity) throws Exception;

	/**
	 * Deletes the given entities.
	 *
	 * @param entities
	 * @throws IllegalArgumentException in case the given {@link Iterable} is {@literal null}.
	 */
	void deleteAll(Iterable<? extends T> entities) throws Exception;

	/**
	 * Deletes all entities managed by the repository.
	 */
	void deleteAll() throws Exception;
	
}
