package com.quan.kg.neo4j.samples;

public class BatchDataExamples {
	public static void main(String[] args) {
		//生成一个人30天内的数据
		int n = 0;
		for(int i = 0; i<10; i++) {
			//每天12个小时的GPS
			for(int m = 0; m<10; m++) {
				System.out.println("merge(g"+n+":GPS{latitude:" 
						+(198.000 + Math.random() + Math.random() * 0.5) + ",longitude:"
						+(89.994 + Math.random() + Math.random() * 0.5) +"})");
				n++;
			}
		}
		
//		for(int i = 1; i< 84; i++) {
//			System.out.println("match(g" + (i-1) +")-[NextTo]->(g"+i+")");
//		}
		
	}
}
