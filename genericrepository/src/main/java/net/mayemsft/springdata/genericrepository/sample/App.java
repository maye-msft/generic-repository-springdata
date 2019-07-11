package net.mayemsft.springdata.genericrepository.sample;

import java.util.Arrays;

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
		
		CrudRepositoryHelper<Person, Long> crudRepositoryHelper = new CrudRepositoryHelper<Person, Long>(Person.class, Long.class, context);
		Person person = crudRepositoryHelper.save(Person.newPerson("P1", 41));
		LOGGER.info(person.getName());
		Person person2 = crudRepositoryHelper.queryOne("findPersonByName", Arrays.asList(new String[] {"P1"}), String.class);
		LOGGER.info(""+person2.getAge());

		
	}
}
