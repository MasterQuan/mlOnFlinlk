package com.quan.kg.flink.job.ml.alink;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;

import com.alibaba.alink.common.MLEnvironment;
import com.alibaba.alink.common.MLEnvironmentFactory;
import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.outlier.SosBatchOp;
import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp;
import com.alibaba.alink.pipeline.Pipeline;
import com.alibaba.alink.pipeline.clustering.KMeans;
import com.alibaba.alink.pipeline.dataproc.vector.VectorAssembler;
import com.alibaba.alink.pipeline.dataproc.vector.VectorMinMaxScaler;
import com.quan.kg.flink.client.FlinkCluster;

public class MinMaxScaler_IRIS_Batch {
	public static void main(String[] args) throws Exception {

		final String URL = "https://alink-release.oss-cn-beijing.aliyuncs.com/data-files/iris.csv";
		final String SCHEMA_STR = "sepal_length String, sepal_width String, petal_length String, petal_width String, category string";

		ExecutionEnvironment batchEnv = ExecutionEnvironment.createRemoteEnvironment(
				FlinkCluster.URL, 
				FlinkCluster.PORT);
		MLEnvironment mlenv = new MLEnvironment(batchEnv,null);
		MLEnvironmentFactory.setDefault(mlenv);

		BatchOperator data = new CsvSourceBatchOp()
				.setFilePath(URL)
				.setSchemaStr(SCHEMA_STR);
		VectorMinMaxScaler scaler = new VectorMinMaxScaler()
				.setSelectedCol("sepal_length");
		scaler.fit(data).transform(data).print();
	}
}
