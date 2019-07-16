package com.github.mayemsft.springdata.genericrepository.sample;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.UUID;

import org.springframework.data.cassandra.core.mapping.PrimaryKey;
import org.springframework.data.cassandra.core.mapping.SASI;
import org.springframework.data.cassandra.core.mapping.Table;

@Table
public class Person {

	@PrimaryKey
	private final String id;

	@SASI
	private final String name;

	private final int age;

	private ByteBuffer pict;

	public Person(String id, String name, int age, ByteBuffer pict) {
		this.id = id;
		this.name = name;
		this.age = age;
		this.pict = pict;
	}

	public String getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public int getAge() {
		return age;
	}

	public ByteBuffer getPict() {
		return pict;
	}

	private static ByteBuffer loadPic() {
		try {
			String file = Person.class.getClassLoader().getResource("new_logo.png").getFile();
//			System.out.println(file);
			RandomAccessFile aFile = new RandomAccessFile(file, "r");
			FileChannel inChannel = aFile.getChannel();
			MappedByteBuffer buffer = inChannel.map(FileChannel.MapMode.READ_ONLY, 0, inChannel.size());
			buffer.load();
			buffer.clear(); // do something with the data and clear/compact it.
			inChannel.close();
			aFile.close();
			return buffer;
		} catch (IOException e) {
			
		}
		return null;
	}

	@Override
	public String toString() {
		return String.format("{ @type = %1$s, id = %2$s, name = %3$s, age = %4$d }", getClass().getName(), getId(),
				getName(), getAge());
	}

	protected static Person newPerson(String name, int age) {
		return newPerson(UUID.randomUUID().toString(), name, age);
	}

	protected static Person newPerson(String id, String name, int age) {
		return new Person(id, name, age, loadPic());
	}


}