/*
 * Copyright 2013-2018 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.github.mayemsft.springdata.genericrepository.sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.cassandra.config.AbstractCassandraConfiguration;
import org.springframework.data.cassandra.config.CassandraCqlClusterFactoryBean;
import org.springframework.data.cassandra.config.SchemaAction;

@Configuration
public class BasicConfiguration extends AbstractCassandraConfiguration {
	private static final String KEYSPACE = "mykeyspace";
	private static final String NODES = "127.0.0.1"; // comma seperated nodes

	@Bean
	@Override
	public CassandraCqlClusterFactoryBean cluster() {
		CassandraCqlClusterFactoryBean bean = new CassandraCqlClusterFactoryBean();
		bean.setContactPoints(NODES);
		bean.setJmxReportingEnabled(false);
		return bean;
	}

	@Override
	public SchemaAction getSchemaAction() {
		return SchemaAction.RECREATE;
	}

	@Override
	protected String getKeyspaceName() {
		return KEYSPACE;
	}

	@Override
	public String[] getEntityBasePackages() {
		return new String[] { "net.mayemsft.springdata.genericrepository.sample" };
	}

}
