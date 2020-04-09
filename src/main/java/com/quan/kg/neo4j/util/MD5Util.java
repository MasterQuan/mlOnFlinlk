package com.quan.kg.neo4j.util;

import java.math.BigInteger;
import java.security.MessageDigest;

public class MD5Util {
	public static String getMD5(String str) {
		if(null == str || str.length() < 1)
			return null;
		try {
			//生成一个MD5加密计算摘要
			MessageDigest md = MessageDigest.getInstance("MD5");
			md.update(str.getBytes());
			return new BigInteger(1, md.digest()).toString(16);
		}catch (Exception e) {
			System.out.println("Exception during getMd5, message is : " + e.getMessage());
			return null;
		}
	}
}
