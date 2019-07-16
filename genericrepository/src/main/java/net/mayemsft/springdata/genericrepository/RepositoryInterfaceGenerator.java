package net.mayemsft.springdata.genericrepository;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.context.ApplicationContext;
import org.springframework.data.cassandra.config.CassandraSessionFactoryBean;
import org.springframework.data.cassandra.core.CassandraAdminTemplate;
import org.springframework.data.cassandra.repository.support.CassandraRepositoryFactory;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryComposition.RepositoryFragments;

class RepositoryInterfaceGenerator<Entity, ID> {

	
	private Class<? extends Repository<Entity, ID>> interfaceClass;
	private ApplicationContext context;
	
	private Map<String, Repository<Entity, ID>> queryRepos = new HashMap<String, Repository<Entity, ID>>();
	
	private Repository<Entity, ID> crudRepo;

	RepositoryInterfaceGenerator(Class<? extends Repository<Entity, ID>> interfaceClass, ApplicationContext context) {
		this.interfaceClass = interfaceClass;
		this.context = context;

	}
	
	@SuppressWarnings({ "unchecked" })
	Repository<Entity, ID> genQueryRepository(Class<Entity> cls, Class<ID> id, String methodName, boolean multiple, Class<?>... paramTypes) throws Exception {
		if(!queryRepos.containsKey(methodName)) {
			Class<?> repoCls = this.genQueryCode(cls, id, methodName, multiple, paramTypes);
			Repository<Entity, ID> repo = (Repository<Entity, ID>) makeRepositoryInstance(repoCls);
			queryRepos.put(methodName, repo);
		} 
		return queryRepos.get(methodName);
		
	}
	
	@SuppressWarnings({  "unchecked" })
	Repository<Entity, ID> genCrudRepository(Class<Entity> cls, Class<ID> id) throws Exception {
		
		if(this.crudRepo == null) {
			Class<?> repoCls = this.genCrudCode(cls, id);
			this.crudRepo = (Repository<Entity, ID>) makeRepositoryInstance(repoCls);
		}
		return this.crudRepo;
	}

	
	private Class<?> genQueryCode(Class<Entity> cls, Class<ID> id, String methodName, boolean multiple, Class<?>... paramTypes) throws Exception {
		Package pkg = cls.getPackage();
		StringBuilder sourceCode = new StringBuilder();
		
		String className = cls.getSimpleName()+methodName+"generatedRepository";
		String pkgName = pkg.getName()+".repository";
		sourceCode.append("package "+pkgName+";\n");

		sourceCode.append("import org.springframework.data.cassandra.repository.AllowFiltering;\n");
		sourceCode.append("import org.springframework.data.cassandra.repository.Query;\n");
		sourceCode.append("import "+interfaceClass.getName()+";\n");
		sourceCode.append("import "+cls.getName()+";\n");
		
		sourceCode.append("\n");
		
		sourceCode.append("public interface " + className + " extends "+interfaceClass.getSimpleName()+"<"+ cls.getSimpleName()+", "+id.getSimpleName()+"> {\n");
		sourceCode.append("   @AllowFiltering\n");
		if(multiple) {
			sourceCode.append("   Iterable<"+cls.getSimpleName()+"> "+methodName+"(");
		} else {
			sourceCode.append("   "+cls.getSimpleName()+" "+methodName+"(");
		}
		for (int i=0;i<paramTypes.length;i++) {
			if(i>0) {
				sourceCode.append(", ");
			}
			sourceCode.append(paramTypes[i].getSimpleName()+" arg"+i);
		}
		sourceCode.append(");\n");
		sourceCode.append("}\n");
//		System.out.println(sourceCode.toString());
		return CompilerFactory.getInstance().complie(pkgName+"."+className, sourceCode.toString());
		
	}
	
	private Class<?> genCrudCode(Class<Entity> cls, Class<ID> id) throws Exception {
		Package pkg = cls.getPackage();
		StringBuilder sourceCode = new StringBuilder();
		String className = cls.getSimpleName()+"generatedRepository";
		String pkgName = pkg.getName()+".repository";
		sourceCode.append("package "+pkgName+";\n");
		sourceCode.append("import "+interfaceClass.getName()+";\n");
		sourceCode.append("import "+cls.getName()+";\n");
		
		sourceCode.append("\n");
		sourceCode.append("public interface "+className+" extends "+interfaceClass.getSimpleName()+"<"+ cls.getSimpleName()+", "+id.getSimpleName()+"> {\n");
		sourceCode.append("}\n");
//		System.out.println(sourceCode.toString());
		return CompilerFactory.getInstance().complie(pkgName+"."+className, sourceCode.toString());
	}
	
	private Object makeRepositoryInstance(Class<?> repositoryClass) {
		CassandraSessionFactoryBean bean2 = context.getBean(CassandraSessionFactoryBean.class);
		RepositoryFragments repositoryFragmentsToUse = (RepositoryFragments) Optional.empty() //
				.orElseGet(RepositoryFragments::empty); //
		
		CassandraRepositoryFactory factory = new CassandraRepositoryFactory(
				new CassandraAdminTemplate(bean2.getObject(), bean2.getConverter()));
		
		factory.setBeanClassLoader(CompilerFactory.getInstance().getClassLoader());

		return factory.getRepository(repositoryClass, repositoryFragmentsToUse);
	}
	
}
