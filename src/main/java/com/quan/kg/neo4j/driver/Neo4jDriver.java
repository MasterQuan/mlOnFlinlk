package com.quan.kg.neo4j.driver;

import java.util.Map;
import java.util.concurrent.TimeUnit;

import javax.annotation.PostConstruct;

import org.neo4j.driver.v1.AccessMode;
import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Config;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import org.neo4j.driver.v1.StatementResult;
import org.neo4j.driver.v1.Transaction;
import org.neo4j.driver.v1.TransactionWork;
import org.neo4j.driver.v1.Values;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.quan.kg.neo4j.constant.Neo4jConstants;

@Service
public class Neo4jDriver {
	
	@Value("${neo4j.url}")
	private String url;
	
	@Value("${neo4j.username}")
	private String username;
	
	@Value("${neo4j.password}")
	private String password;
	
	private static volatile Driver driver;
	
	private Neo4jDriver() {}
	
	@PostConstruct
	private void initDriver() {
		try {
			driver = GraphDatabase.driver(url, 
					AuthTokens.basic(username, password), 
					Config.build()
					.withConnectionAcquisitionTimeout(60, TimeUnit.SECONDS)
					.withMaxTransactionRetryTime(60, TimeUnit.SECONDS).toConfig());
		} catch (Exception e) {
			System.out.println("Init Neo4j Driver Failed! Exception is:" + e.getMessage());
		}
	}

	public Driver getDriver() {
		if(driver == null) {
			synchronized (driver) {
				initDriver();
			}
		}
		return driver;
	}
	
	public void close() {
		driver.close();
	}
	
	private static TransactionWork<StatementResult> getTransaction(final String CQL, final Object...values){
		
		TransactionWork<StatementResult> transaction = new TransactionWork<StatementResult>() {
			@Override
			public StatementResult execute(Transaction tx) {
				StatementResult results = tx.run(CQL, Values.parameters(values));
				return results;
			}
		};
		
		return transaction;
	}
	
	public static StatementResult executeReadCQL(final String CQL, final Object...values) {
		
		StatementResult result = null;
		try(Session session = driver.session(AccessMode.READ)){
			TransactionWork<StatementResult> work = getTransaction(CQL, values);
			result = session.readTransaction(work);
		}catch (Exception e) {
			System.out.println("Exception occuring during executeReadCQL, and message is:" + e.getMessage());
		}
		return result;
	}

	public static StatementResult executeWriteCQL(final String CQL, final Object...values) {

		StatementResult result = null;
		try(Session session = driver.session(AccessMode.WRITE)){
			TransactionWork<StatementResult> work = getTransaction(CQL, values);
			result = session.writeTransaction(work);
		}catch (Exception e) {
			System.out.println("Exception occuring during executeWriteCQL, and message is:" + e.getMessage());
		}
		return result;
	}
	
	public static void executeWriteCQL(Map<String, Integer> summary, final String CQL, final Object...values) {
		
		StatementResult sr = executeWriteCQL(CQL, values);
		if(sr != null) {
			summary.put(Neo4jConstants.NODE_CREATE, 
					summary.get(Neo4jConstants.NODE_CREATE) 
					+ sr.summary().counters().nodesCreated());
			summary.put(Neo4jConstants.RELATIONSHIP_CREATE, 
					summary.get(Neo4jConstants.RELATIONSHIP_CREATE)
					+ sr.summary().counters().relationshipsCreated());
		}
	}
}
