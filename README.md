# Generic Repository for SpringData
### Background - what is generic SpringData Reposistory
SpringData simplifies the data access by geneating the data access object(DAO) so no more DAO implementation. Here is the Spring doc about it [spring persistence](https://www.baeldung.com/the-persistence-layer-with-spring-data-jpa). 

*Spring Data takes this simplification one step forward and makes it possible to remove the DAO implementations entirely. The interface of the DAO is now **the only artifact that we need to explicitly define**.*

*In order to start leveraging the Spring Data programming model with JPA, a DAO interface needs to extend the JPA specific Repository interface – JpaRepository. This will enable Spring Data to find this interface and automatically create an implementation for it.*

*By extending the interface we get the most relevant CRUD methods for standard data access available in a standard DAO.*

**In SpringData, we need define an interface as a DAO (named repository) for each entity. If there are hundreds of entities, it needs to have the the same number of interfaces to be defined in java files. This project provide a generic repository then it is not required to define the interface any more.**



### Implementation Detail.

Firstly create a repository interface with code.

```java
StringBuilder sourceCode = new StringBuilder();
sourceCode.append("import org.springframework.boot.autoconfigure.security.SecurityProperties.User;\n");
sourceCode.append("import org.springframework.data.cassandra.repository.AllowFiltering;\n");
sourceCode.append("import org.springframework.data.cassandra.repository.Query;\n");
sourceCode.append("import org.springframework.data.repository.CrudRepository;\n");
sourceCode.append("\n");
sourceCode.append("public interface TestRepository extends CrudRepository<Entity, Long> {\n");
sourceCode.append("}");
```

Compile the code and get the class, I use org.mdkt.compiler.InMemoryJavaCompiler

```java
ClassLoader classLoader = org.springframework.util.ClassUtils.getDefaultClassLoader();
compiler = InMemoryJavaCompiler.newInstance();
compiler.useParentClassLoader(classLoader);
Class<?> testRepository = compiler.compile("TestRepository", sourceCode.toString());
```		

And initialize the repository in spring data runtime. this is a little tricky as I debug the SpringData code to find how it initialize a repository interface in spring.

```java
CassandraSessionFactoryBean bean = context.getBean(CassandraSessionFactoryBean.class);
RepositoryFragments repositoryFragmentsToUse = (RepositoryFragments) Optional.empty().orElseGet(RepositoryFragments::empty); 
CassandraRepositoryFactory factory = new CassandraRepositoryFactory(
    new CassandraAdminTemplate(bean.getObject(), bean.getConverter()));
factory.setBeanClassLoader(compiler.getClassloader());
Object repository = factory.getRepository(testRepository, repositoryFragmentsToUse);
```		

Now you can try the save method of the repository.

```java
Method method = repository.getClass().getMethod("save", paramTypes);
T obj = (T) method.invoke(repository, params.toArray());
```		


### Generic Repository

There are two generic repositories,  [CrudRepositoryHelper](https://github.com/maye-msft/generic-repository-springdata/blob/master/genericrepository/src/main/java/com/github/mayemsft/springdata/genericrepository/CrudRepositoryHelper.java) and [CassandraRepositoryHelper](https://github.com/maye-msft/generic-repository-springdata/blob/master/genericrepository/src/main/java/com/github/mayemsft/springdata/genericrepository/CassandraRepositoryHelper.java).

Here is a sample code using CrudRepositoryHelper

```java
CrudRepositoryHelper<Person, String> crudRepositoryHelper 
                  = new CrudRepositoryHelper<Person, String>(Person.class, String.class, context);

//save person1
Person person1 = crudRepositoryHelper.save(Person.newPerson("P1", 41));
LOGGER.info(person1.toString());

//get person1 with findPersonByName
person1 = crudRepositoryHelper.queryOne("findPersonByName", 
                  Arrays.asList(new Object[] {"P1"}), String.class);
LOGGER.info(person1.toString());
```

It shows the CrudRepositoryHelper make save and query function with Person entity, while there is no Person DAO interface defined.

A full sample code is at the package com.github.mayemsft.springdata.genericrepository.sample;
- [CrudApp](https://github.com/maye-msft/generic-repository-springdata/blob/master/genericrepository/src/main/java/com/github/mayemsft/springdata/genericrepository/sample/CrudApp.java)
- [CassandraApp](https://github.com/maye-msft/generic-repository-springdata/blob/master/genericrepository/src/main/java/com/github/mayemsft/springdata/genericrepository/sample/CassandraApp.java)

### TODO
- unit test
- document
- publish to maven repository
