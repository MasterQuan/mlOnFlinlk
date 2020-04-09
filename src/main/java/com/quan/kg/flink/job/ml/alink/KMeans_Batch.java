package com.quan.kg.flink.job.ml.alink;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;

import com.alibaba.alink.common.MLEnvironment;
import com.alibaba.alink.common.MLEnvironmentFactory;
import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp;
import com.alibaba.alink.pipeline.Pipeline;
import com.alibaba.alink.pipeline.clustering.KMeans;
import com.alibaba.alink.pipeline.dataproc.vector.VectorAssembler;
import com.quan.kg.flink.client.FlinkCluster;

public class KMeans_Batch{
	public static void main(String[] args) throws Exception {

		final String URL = "https://alink-release.oss-cn-beijing.aliyuncs.com/data-files/iris.csv";
		final String SCHEMA_STR = "sepal_length double, sepal_width double, petal_length double, petal_width double, category string";

		ExecutionEnvironment batchEnv = ExecutionEnvironment.createRemoteEnvironment(
				FlinkCluster.URL, 
				FlinkCluster.PORT);
		BatchTableEnvironment batchTableEnv = BatchTableEnvironment.create(batchEnv);
		MLEnvironment mlenv = new MLEnvironment(batchEnv,batchTableEnv);
		MLEnvironmentFactory.setDefault(mlenv);

		BatchOperator data = new CsvSourceBatchOp()
				.setFilePath(URL)
				.setSchemaStr(SCHEMA_STR);

		VectorAssembler va = new VectorAssembler()
				.setSelectedCols(new String[]{"sepal_length", "sepal_width", "petal_length", "petal_width"})
				.setOutputCol("features");

		KMeans kMeans = new KMeans()
				.setVectorCol("features")
				.setK(3)
				.setPredictionCol("cluster_id")
				.setPredictionDetailCol("prediction_detail")
				.setReservedCols("category")
				.setMaxIter(100);

		Pipeline pipeline = new Pipeline()
				.add(va)
				.add(kMeans);
		pipeline.fit(data).transform(data).print();
	}
}
