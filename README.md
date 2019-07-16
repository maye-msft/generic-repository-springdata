# Generic Repository for SpringData
### Background - what is generic SpringData Reposistory
SpringData simplifies the data access by geneate the data access object(DAO) so no more DAO implementation. Here is the Spring doc about it [spring persistence](https://www.baeldung.com/the-persistence-layer-with-spring-data-jpa). 

*Spring Data takes this simplification one step forward and makes it possible to remove the DAO implementations entirely. The interface of the DAO is now **the only artifact that we need to explicitly define**.*

*In order to start leveraging the Spring Data programming model with JPA, a DAO interface needs to extend the JPA specific Repository interface – JpaRepository. This will enable Spring Data to find this interface and automatically create an implementation for it.*

*By extending the interface we get the most relevant CRUD methods for standard data access available in a standard DAO.*

**In SpringData, we need define an interface as a DAO for each entity. If there are hundreds of entities, it needs to have the the same number of interfaces to be defined in java files. This project provide a generic repository then it is not required to define the interface any more.**

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

** TODO
- unit test
- document
- publish to maven repository
