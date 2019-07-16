package net.mayemsft.springdata.genericrepository.sample;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.data.cassandra.core.query.CassandraPageRequest;

import net.mayemsft.springdata.genericrepository.CassandraRepositoryHelper;

/**
 * Hello world!
 *
 */
@SpringBootApplication
public class CassandraApp implements CommandLineRunner
{
	private static final Logger LOGGER = LoggerFactory.getLogger(CassandraApp.class);
	
	@Autowired
	private ApplicationContext context;

	public static void main(String[] args) {
		
		SpringApplication.run(CassandraApp.class, args);

	}


	@Override
	public void run(String... args) throws Exception {
		
		CassandraRepositoryHelper<Person, String> cassandraRepositoryHelper = new CassandraRepositoryHelper<Person, String>(Person.class, String.class, context);
		LOGGER.info("save person1");
		Person person1 = cassandraRepositoryHelper.save(Person.newPerson("P1", 41));
		LOGGER.info(person1.toString());
		LOGGER.info("get person1 with findPersonByName");
		person1 = cassandraRepositoryHelper.queryOne("findPersonByName", Arrays.asList(new Object[] {"P1"}), String.class);
		LOGGER.info(person1.toString());
		
		LOGGER.info("get person1 with findById");
		Optional<Person> optionalPerson = cassandraRepositoryHelper.findById(person1.getId());
		LOGGER.info(optionalPerson.get().toString());
		
		LOGGER.info("check person1 with existsById");
		Boolean existPerson = cassandraRepositoryHelper.existsById(person1.getId());
		LOGGER.info(existPerson.toString());
		
		Person person2 = cassandraRepositoryHelper.save(Person.newPerson("P2", 41));
		Person person3 = cassandraRepositoryHelper.save(Person.newPerson("P3", 42));
		Person person4 = cassandraRepositoryHelper.save(Person.newPerson("P4", 42));
		Person person5 = cassandraRepositoryHelper.save(Person.newPerson("P5", 42));
		List<Person> persons = new ArrayList<Person>();
		persons.add(person2);
		persons.add(person3);
		persons.add(person4);
		persons.add(person5);
		
		LOGGER.info("save person2 person3");
		Iterable<Person> personIIterable = cassandraRepositoryHelper.saveAll(persons);
		printPersons(personIIterable);
		
		LOGGER.info("find all persons with findAll");
		personIIterable = cassandraRepositoryHelper.findAll();
		printPersons(personIIterable);
		
		List<String> personIds = new ArrayList<String>();
		personIds.add(person1.getId());
		personIds.add(person2.getId());
		
		LOGGER.info("find all persons with findAllById");
		personIIterable= cassandraRepositoryHelper.findAllById(personIds);
		printPersons(personIIterable);
		
		LOGGER.info("count all persons");
		Long count = cassandraRepositoryHelper.count();
		LOGGER.info(count.toString());
		
		
		LOGGER.info("get persons with findPersonByAge");
		personIIterable = cassandraRepositoryHelper.queryList("findPersonByAge", Arrays.asList(new Object[] {41}), Integer.class);
		printPersons(personIIterable);
		
		LOGGER.info("get person2 with findPersonByNameAndAge");
		personIIterable = cassandraRepositoryHelper.queryList("findPersonByNameAndAge", Arrays.asList(new Object[] {"P2", 41}), String.class, Integer.class);
		printPersons(personIIterable);
		
		LOGGER.info("get first 3 persons");
		personIIterable = cassandraRepositoryHelper.findAll(CassandraPageRequest.first(3));
		printPersons(personIIterable);
		
		LOGGER.info("delete person1 with deleteById");
		cassandraRepositoryHelper.deleteById(person1.getId());
		count = cassandraRepositoryHelper.count();
		LOGGER.info(count.toString());
		
		persons = new ArrayList<Person>();
		persons.add(person2);
		persons.add(person3);
		LOGGER.info("delete person2 person3 with deleteAll");
		cassandraRepositoryHelper.deleteAll(persons);
		count = cassandraRepositoryHelper.count();
		LOGGER.info(count.toString());
		
		LOGGER.info("delete person4 with delete");
		cassandraRepositoryHelper.delete(person4);
		count = cassandraRepositoryHelper.count();
		LOGGER.info(count.toString());
		
		LOGGER.info("delete all with deleteAll");
		cassandraRepositoryHelper.deleteAll();
		count = cassandraRepositoryHelper.count();
		LOGGER.info(count.toString());
		
		
	}


	private void printPersons(Iterable<Person> personIIterable) {
		personIIterable.forEach(new Consumer<Person>() {

			@Override
			public void accept(Person t) {
				LOGGER.info(t.toString());
				
			}
		});
	}
}
