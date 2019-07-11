package net.mayemsft.springdata.genericrepository;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.Repository;

public abstract class AbstractRepositoryHelper<T, ID> implements IRepositoryHelper<T, ID>{

	protected Class<T> cls;
	private RepositoryInterfaceGenerator<T, ID> repogenerator;
	private Repository<T, ID> crudRepsitory;
	private Class<ID> id;


	protected AbstractRepositoryHelper(Class<T> cls, Class<ID> id, Class<? extends Repository<T, ID>> repoParent, ApplicationContext context) throws Exception {
		this.cls = cls;
		this.id = id;
		this.repogenerator = new RepositoryInterfaceGenerator<T, ID>(repoParent, context);
		this.crudRepsitory = this.repogenerator.genCrudRepository(cls, id);
	}
	
	private Object invoke(Repository<T, ID> repository, String methodName, List<Object> params, Class<?>... paramTypes ) throws NoSuchMethodException, SecurityException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
		Method method = repository.getClass().getMethod(methodName, paramTypes);
		return method.invoke(repository, params.toArray());
	}
	
	@SuppressWarnings("unchecked")
	public T queryOne(String methodName, List<Object> params, Class<?>... paramTypes) throws Exception {
		Repository<T, ID> repository = this.repogenerator.genQueryRepository(cls, id, methodName, paramTypes);
		return (T)this.invoke(repository, methodName, params, paramTypes);

	}

	public Iterable<T> queryList(String methodName, List<Object> params, Class<?>... paramTypes) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public <S extends T> S save(S entity)  throws Exception {
		
		return (S)this.invoke(this.crudRepsitory, "save", Arrays.asList(new Object[]{entity}), Object.class);
	}

	public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@SuppressWarnings("unchecked")
	public Optional<T> findById(ID id)  throws Exception {
		return (Optional<T>)this.invoke(this.crudRepsitory, "findById", Arrays.asList(new Object[]{id}), id.getClass());
	}

	public boolean existsById(ID id) {
		// TODO Auto-generated method stub
		return false;
	}

	public Iterable<T> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	public Iterable<T> findAllById(Iterable<ID> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	public void deleteById(ID id) {
		// TODO Auto-generated method stub
		
	}

	public void delete(T entity) {
		// TODO Auto-generated method stub
		
	}

	public void deleteAll(Iterable<? extends T> entities) {
		// TODO Auto-generated method stub
		
	}

	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
	

	
}
