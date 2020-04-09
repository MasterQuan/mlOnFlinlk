package com.quan.kg.flink.job.ml.alink;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;

import com.alibaba.alink.common.MLEnvironment;
import com.alibaba.alink.common.MLEnvironmentFactory;
import com.quan.kg.flink.client.FlinkCluster;

public class Minist {
	public static void main(String[] args) {
		final String URL = "https://alink-release.oss-cn-beijing.aliyuncs.com/data-files/mnist_dense.csv";
		final String SCHEMA_STR= "label bigint,bitmap string";

		ExecutionEnvironment batchEnv = ExecutionEnvironment.createRemoteEnvironment(
				FlinkCluster.URL, 
				FlinkCluster.PORT);
		BatchTableEnvironment batchTableEnv = BatchTableEnvironment.create(batchEnv);
		MLEnvironment mlenv = new MLEnvironment(batchEnv,batchTableEnv);
		MLEnvironmentFactory.setDefault(mlenv);
	}
}
