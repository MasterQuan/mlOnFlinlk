package com.quan.kg.neo4j.samples;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import com.quan.kg.neo4j.constant.Neo4jCQLs;
import com.quan.kg.neo4j.driver.Neo4jDriver;

public class BatchInsertSamples {

	public static void main(String[] args) {
		List<Map<String, String>> orders = initOrdersData();
		List<Map<String, String>> contacts = initContactsData(null);
		List<Map<String, String>> records = initRecordsData(null);
		
		Map<String, Integer> summary = new HashMap<String, Integer>();
		Neo4jDriver.executeWriteCQL(summary, Neo4jCQLs.BATCH_NODES_INSERT_ORDER, "maps", orders);
		Neo4jDriver.executeWriteCQL(summary, Neo4jCQLs.BATCH_NODES_INSERT_PERSON, "maps", orders);
		Neo4jDriver.executeWriteCQL(summary, Neo4jCQLs.BATCH_NODES_INSERT_MOBILE, "maps", orders);
		Neo4jDriver.executeWriteCQL(summary, Neo4jCQLs.BATCH_NODES_INSERT_DEVICE, "maps", orders);
		Neo4jDriver.executeWriteCQL(summary, Neo4jCQLs.BATCH_NODES_INSERT_IP, "maps", orders);
		Neo4jDriver.executeWriteCQL(summary, Neo4jCQLs.BATCH_NODES_INSERT_HOME, "maps", orders);
		Neo4jDriver.executeWriteCQL(summary, Neo4jCQLs.BATCH_NODES_INSERT_CPNY, "maps", orders);
		Neo4jDriver.executeWriteCQL(summary, Neo4jCQLs.BATCH_NODES_INSERT_CPNY_PHONE, "maps", orders);
		Neo4jDriver.executeWriteCQL(summary, Neo4jCQLs.BATCH_NODES_INSERT_CPNY_ADDR, "maps", orders);
		Neo4jDriver.executeWriteCQL(summary, Neo4jCQLs.BATCH_NODES_INSERT_GPS, "maps", orders);
		
		Neo4jDriver.executeWriteCQL(summary, Neo4jCQLs.BATCH_NODES_INSERT_CONTACT, "maps", contacts);
		Neo4jDriver.executeWriteCQL(summary, Neo4jCQLs.BATCH_NODES_INSERT_CONTACT, "maps", records);

	}


	private static List<Map<String, String>> initOrdersData() {
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		
		return datas;
	}

	private static List<Map<String, String>> initContactsData(String orderNo){
		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		
		return datas;
	}

	private static List<Map<String, String>> initRecordsData(String orderNo){ 

		List<Map<String, String>> datas = new ArrayList<Map<String, String>>();
		return datas;
	}
}
