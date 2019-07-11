package net.mayemsft.springdata.genericrepository;

import org.springframework.context.ApplicationContext;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;

public class CrudRepositoryHelper<T, ID> extends AbstractRepositoryHelper<T, ID> {

	@SuppressWarnings("unchecked")
	public CrudRepositoryHelper(Class<T> cls, Class<ID> id,
			ApplicationContext context) throws Exception {
		super(cls, id, (Class<? extends Repository<T, ID>>) CrudRepository.class, context);
		// TODO Auto-generated constructor stub
	}

}
