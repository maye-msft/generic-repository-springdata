package net.mayemsft.springdata.genericrepository;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.Repository;

abstract class AbstractRepositoryHelper<T, ID> implements IRepositoryHelper<T, ID>{

	protected Class<T> cls;
	protected RepositoryInterfaceGenerator<T, ID> repogenerator;
	protected Repository<T, ID> repository;
	protected Class<ID> id;


	protected AbstractRepositoryHelper(Class<T> cls, Class<ID> id, Class repoParent, ApplicationContext context) throws Exception {
		this.cls = cls;
		this.id = id;
		this.repogenerator = new RepositoryInterfaceGenerator<T, ID>(repoParent, context);
		this.repository = this.repogenerator.genCrudRepository(cls, id);
	}
	
	protected Object invoke(Repository<T, ID> repository, String methodName, List<Object> params, Class<?>... paramTypes )   throws Exception  {
		Method method = repository.getClass().getMethod(methodName, paramTypes);
		return method.invoke(repository, params.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public T queryOne(String methodName, List<Object> params, Class<?>... paramTypes) throws Exception {
		Repository<T, ID> repository = this.repogenerator.genQueryRepository(cls, id, methodName, false, paramTypes);
		return (T)this.invoke(repository, methodName, params, paramTypes);

	}

	@SuppressWarnings("unchecked")
	public Iterable<T> queryList(String methodName, List<Object> params, Class<?>... paramTypes)   throws Exception {
		Repository<T, ID> repository = this.repogenerator.genQueryRepository(cls, id, methodName, true, paramTypes);
		return (Iterable<T>) this.invoke(repository, methodName, params, paramTypes);
	}

	@SuppressWarnings("unchecked")
	public <S extends T> S save(S entity)  throws Exception {
		
		return (S)this.invoke(this.repository, "save", Arrays.asList(new Object[]{entity}), Object.class);
	}

	@SuppressWarnings("unchecked")
	public <S extends T> Iterable<S> saveAll(Iterable<S> entities)   throws Exception {
		return (Iterable<S>) this.invoke(this.repository, "saveAll", Arrays.asList(new Object[]{entities}), Iterable.class);
	}

	@SuppressWarnings("unchecked")
	public Optional<T> findById(ID id)  throws Exception {
		return (Optional<T>)this.invoke(this.repository, "findById", Arrays.asList(new Object[]{id}), Object.class);
	}

	
	public boolean existsById(ID id)   throws Exception {
		return (boolean)this.invoke(this.repository, "existsById", Arrays.asList(new Object[]{id}), Object.class);
	}

	@SuppressWarnings("unchecked")
	public Iterable<T> findAll()   throws Exception {
		return (Iterable<T>)this.invoke(this.repository, "findAll", Arrays.asList(new Object[]{}));
	}

	@SuppressWarnings("unchecked")
	public Iterable<T> findAllById(Iterable<ID> ids)   throws Exception  {
		return (Iterable<T>) this.invoke(this.repository, "findAllById", Arrays.asList(new Object[]{ids}), Iterable.class);
	}

	public long count()   throws Exception  {
		return (Long)this.invoke(this.repository, "count", Arrays.asList(new Object[]{}));
	}

	public void deleteById(ID id)   throws Exception  {
		this.invoke(this.repository, "deleteById", Arrays.asList(new Object[]{id}), Object.class);
		
	}

	public void delete(T entity)   throws Exception  {
		this.invoke(this.repository, "delete", Arrays.asList(new Object[]{entity}), Object.class);
		
	}

	public void deleteAll(Iterable<? extends T> entities)   throws Exception {
		this.invoke(this.repository, "deleteAll", Arrays.asList(new Object[]{entities}), Iterable.class);
		
	}

	public void deleteAll()   throws Exception {
		this.invoke(this.repository, "deleteAll", Arrays.asList(new Object[]{}));
		
	}
	

	
}
