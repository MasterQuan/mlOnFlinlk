package com.quan.kg.neo4j.constant;

public class Neo4jCQLs {

	//批量插入订单 node
	public static final String BATCH_NODES_INSERT_ORDER = "UNWIND $maps as map\n" 
			+ "MERGE Order(o:{orderNo:map.orderNo}) "
			+ "ON CREATE SET o.amount = map.amount, o.appDate = map.appDate, o.stage = map.stage";

	//批量插入个人 node
	public static final String BATCH_NODES_INSERT_PERSON = "UNWIND $maps as map\n" 
			+ "MERGE Person(o:{idNo:map.idNo}) ON CREATE SET o.name = map.name";

	//批量插入手机号码 node
	public static final String BATCH_NODES_INSERT_MOBILE = "UNWIND $maps as map\n" 
			+ "MERGE Mobile(o:{mobile:map.mobile})\n"
			+ "MERGE Owner(o:{name:map.mobileName})";

	//批量插入设备 node
	public static final String BATCH_NODES_INSERT_DEVICE = "UNWIND $maps as map\n" 
			+ "MERGE Device(o:{fingerPrint:map.fingerPrint})\n";

	//批量插入IP node
	public static final String BATCH_NODES_INSERT_IP = "UNWIND $maps as map\n" 
			+ "MERGE IP(o:{ip:map.ip})\n";

	//批量插入GPS node
	public static final String BATCH_NODES_INSERT_GPS = "UNWIND $maps as map\n" 
			+ "MERGE GPS(o:{longitude:map.longitude, latitude:map.latitude})\n";

	//批量插入MD5 node
	public static final String BATCH_NODES_INSERT_CONTACT = "UNWIND $maps as map\n" 
			+ "MERGE Contact(o:{md5:map.md5})\n";

	//批量插入通讯录 node
	public static final String BATCH_NODES_INSERT_CONTACTS = "UNWIND $maps as map\n" 
			+ "MERGE Contacts(o:{orderNo:map.orderNo})\n";

	//批量插入通话记录 node
	public static final String BATCH_NODES_INSERT_RECORDS = "UNWIND $maps as map\n" 
			+ "MERGE Records(o:{orderNo:map.orderNo})\n";

	//批量插入银行卡 node
	public static final String BATCH_NODES_INSERT_BANKCARD = "UNWIND $maps as map\n" 
			+ "MERGE BankCard(o:{cardNo:map.cardNo})\n";

	//批量插入家庭地址 node
	public static final String BATCH_NODES_INSERT_HOME = "UNWIND $maps as map\n" 
			+ "MERGE Home(o:{address:map.homeAddr})\n";

	//批量插入公司名称 node
	public static final String BATCH_NODES_INSERT_CPNY = "UNWIND $maps as map\n" 
			+ "MERGE Company(o:{name:map.cpnyName})\n";

	//批量插入公司电话 node
	public static final String BATCH_NODES_INSERT_CPNY_PHONE = "UNWIND $maps as map\n" 
			+ "MERGE CpnyPhone(o:{phone:map.cpnyPhone})\n";

	//批量插入公司地址 node
	public static final String BATCH_NODES_INSERT_CPNY_ADDR = "UNWIND $maps as map\n" 
			+ "MERGE CpnyAddr(o:{address:map.cpnyAddr})\n";

	//批量插入申请人 relationship
	public static final String BATCH_RELS_INSERT_APPLYBY = "UNWIND $maps as map\n" 
			+ "MATCH Order(o:{orderNo:map.orderNo})\n"
			+ "MATCH Person(p:{idNo:map.idNo})\n"
			+ "MERGE (o)-[r:ApplyBy{orderNo:map.orderNo}]->(p)";

	//批量插入申请设备 relationship
	public static final String BATCH_RELS_INSERT_APPLYON = "UNWIND $maps as map\n" 
			+ "MATCH Order(o:{orderNo:map.orderNo})\n"
			+ "MATCH Device(d:{fingerPrint:map.fingerPrint})\n"
			+ "MERGE (o)-[r:ApplyOn{orderNo:map.orderNo}]->(d)";

	//批量插入通讯录 relationship
	public static final String BATCH_RELS_INSERT_CONTACTS = "UNWIND $maps as map\n" 
			+ "MATCH Device(d:{fingerPrint:map.fingerPrint})\n"
			+ "MATCH Contacts(d:{orderNo:map.orderNo})\n"
			+ "MERGE (o)-[r:hasContacts{orderNo:map.orderNo}]->(d)";

	//批量插入通讯录MD5 relationship
	public static final String BATCH_RELS_INSERT_CONTACT = "UNWIND $maps as map\n" 
			+ "MATCH Contacts(cs:{orderNo:map.orderNo})\n"
			+ "MATCH Contact(c:{md5:map.md5})\n"
			+ "MERGE (cs)-[r:ContactsHas{orderNo:map.orderNo}]->(c)";

	//批量插入通话记录 relationship
	public static final String BATCH_RELS_INSERT_RECORDS = "UNWIND $maps as map\n" 
			+ "MATCH Device(d:{fingerPrint:map.fingerPrint})\n"
			+ "MATCH Records(o:{orderNo:map.orderNo})\n"
			+ "MERGE (d)-[r:hasRecords{orderNo:map.orderNo}]->(o)";

	//批量插入通讯录MD5 relationship
	public static final String BATCH_RELS_INSERT_RECORD = "UNWIND $maps as map\n" 
			+ "MATCH Records(rs:{orderNo:map.orderNo})\n"
			+ "MATCH Contact(c:{md5:map.md5})\n"
			+ "MERGE (rs)-[r:RecordsHas{orderNo:map.orderNo}]->(c)";	

	//批量插入家庭地址 relationship
	public static final String BATCH_RELS_INSERT_HOME = "UNWIND $maps as map\n" 
			+ "MERGE Person(p:{idNo:map.idNo})\n"
			+ "MERGE Home(h:{address:map.homeAddr})\n"
			+ "MERGE (p)-[r:LiveIn{orderNo:map.orderNo}]->(h)";
	
	//批量插入公司 relationship
	public static final String BATCH_RELS_INSERT_CPNY = "UNWIND $maps as map\n" 
			+ "MERGE Person(p:{idNo:map.idNo})\n"
			+ "MERGE Company(c:{name:map.cpnyName})\n"
			+ "MERGE (p)-[r:WorkIn{orderNo:map.orderNo}]->(c)";
	 
	//批量插入公司 relationship
	public static final String BATCH_RELS_INSERT_CPNYADDR = "UNWIND $maps as map\n" 
			+ "MERGE Company(c:{name:map.cpnyName})\n"
			+ "MERGE CpnyAddr(a:{name:map.homeAddr})\n"
			+ "MERGE (c)-[r:LocateIn{orderNo:map.orderNo}]->(a)";
	
	//批量插入公司 relationship
	public static final String BATCH_RELS_INSERT_CPNYPHONE = "UNWIND $maps as map\n" 
			+ "MERGE Company(c:{name:map.cpnyName})\n"
			+ "MERGE CpnyPhone(p:{name:map.cpnyPhone})\n"
			+ "MERGE (c)-[r:HasPhone{orderNo:map.orderNo}]->(p)";
	
	
	

}
