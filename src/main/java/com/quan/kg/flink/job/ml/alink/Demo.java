package com.quan.kg.flink.job.ml.alink;

import java.util.Random;

public class Demo {
	public static void main(String[] args) {
		for(int i = 1; i<=200; i++) {
			for (int j = 0; j< 30; j++) {
				System.out.print(new Random().nextInt(50) + " ");
				}
			System.out.println();
			}
	}
}
