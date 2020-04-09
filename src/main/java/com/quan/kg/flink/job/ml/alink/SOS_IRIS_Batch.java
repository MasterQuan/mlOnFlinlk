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
import com.quan.kg.flink.client.FlinkCluster;

public class SOS_IRIS_Batch {
	public static void main(String[] args) throws Exception {

		final String URL = "https://alink-release.oss-cn-beijing.aliyuncs.com/data-files/iris.csv";
		final String SCHEMA_STR = "sepal_length double, sepal_width double, petal_length double, petal_width double, category string";

		ExecutionEnvironment batchEnv = ExecutionEnvironment.createRemoteEnvironment(
				FlinkCluster.URL, 
				FlinkCluster.PORT);
		MLEnvironment mlenv = new MLEnvironment(batchEnv,null);
		MLEnvironmentFactory.setDefault(mlenv);

		BatchOperator data = new CsvSourceBatchOp()
				.setFilePath(URL)
				.setSchemaStr(SCHEMA_STR)
				.select(new String[]{
						"sepal_length",
						"sepal_width",
						"petal_length",
						"petal_width"});
		
		SosBatchOp sos = new SosBatchOp()
				.setVectorCol("sepal_length")
				.setVectorCol("sepal_width")
				.setVectorCol("petal_length")
				.setVectorCol("petal_width")
				.setPredictionCol("outlier_score")
				.setPerplexity(3.0);
		
		sos.linkFrom(data).print();
	}
}
