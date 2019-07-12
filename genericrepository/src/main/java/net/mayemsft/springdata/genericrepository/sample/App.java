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

import net.mayemsft.springdata.genericrepository.CrudRepositoryHelper;


/**
 * Hello world!
 *
 */
@SpringBootApplication
public class App implements CommandLineRunner
{
	private static final Logger LOGGER = LoggerFactory.getLogger(App.class);
	
	@Autowired
	private ApplicationContext context;

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);

	}


	@Override
	public void run(String... args) throws Exception {
		
		CrudRepositoryHelper<Person, String> crudRepositoryHelper = new CrudRepositoryHelper<Person, String>(Person.class, String.class, context);
		//save person1
		Person person1 = crudRepositoryHelper.save(Person.newPerson("P1", 41));
		LOGGER.info(person1.toString());
		//get person1 with findPersonByName
		person1 = crudRepositoryHelper.queryOne("findPersonByName", Arrays.asList(new Object[] {"P1"}), String.class);
		LOGGER.info(person1.toString());
		
		//get person1 with findById
		Optional<Person> optionalPerson = crudRepositoryHelper.findById(person1.getId());
		LOGGER.info(optionalPerson.get().toString());
		
		//check person1 with existsById
		Boolean existPerson = crudRepositoryHelper.existsById(person1.getId());
		LOGGER.info(existPerson.toString());
		
		Person person2 = crudRepositoryHelper.save(Person.newPerson("P2", 41));
		Person person3 = crudRepositoryHelper.save(Person.newPerson("P3", 42));
		Person person4 = crudRepositoryHelper.save(Person.newPerson("P4", 42));
		Person person5 = crudRepositoryHelper.save(Person.newPerson("P5", 42));
		List<Person> persons = new ArrayList<Person>();
		persons.add(person2);
		persons.add(person3);
		persons.add(person4);
		persons.add(person5);
		
		//save person2 person3
		Iterable<Person> personIIterable = crudRepositoryHelper.saveAll(persons);
		printPersons(personIIterable);
		
		//find all persons with findAll
		personIIterable = crudRepositoryHelper.findAll();
		printPersons(personIIterable);
		
		List<String> personIds = new ArrayList<String>();
		personIds.add(person1.getId());
		personIds.add(person2.getId());
		
		//find all persons with findAllById
		personIIterable= crudRepositoryHelper.findAllById(personIds);
		printPersons(personIIterable);
		
		//count all persons
		Long count = crudRepositoryHelper.count();
		LOGGER.info(count.toString());
		
		
		//get persons with findPersonByAge
		personIIterable = crudRepositoryHelper.queryList("findPersonByAge", Arrays.asList(new Object[] {41}), Integer.class);
		printPersons(personIIterable);
		
		//get person2 with findPersonByNameAndAge
		personIIterable = crudRepositoryHelper.queryList("findPersonByNameAndAge", Arrays.asList(new Object[] {"P2", 41}), String.class, Integer.class);
		printPersons(personIIterable);
		
		//delete person1 with deleteById
		crudRepositoryHelper.deleteById(person1.getId());
		count = crudRepositoryHelper.count();
		LOGGER.info(count.toString());
		
		persons = new ArrayList<Person>();
		persons.add(person2);
		persons.add(person3);
		crudRepositoryHelper.deleteAll(persons);
		count = crudRepositoryHelper.count();
		LOGGER.info(count.toString());
		
		crudRepositoryHelper.delete(person4);
		count = crudRepositoryHelper.count();
		LOGGER.info(count.toString());
		
		crudRepositoryHelper.deleteAll();
		count = crudRepositoryHelper.count();
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
