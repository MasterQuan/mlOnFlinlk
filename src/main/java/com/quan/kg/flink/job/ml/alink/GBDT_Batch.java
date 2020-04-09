package com.quan.kg.flink.job.ml.alink;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;

import com.alibaba.alink.common.MLEnvironment;
import com.alibaba.alink.common.MLEnvironmentFactory;
import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp;
import com.alibaba.alink.pipeline.classification.GbdtClassificationModel;
import com.alibaba.alink.pipeline.classification.GbdtClassifier;
import com.quan.kg.flink.client.FlinkCluster;

public class GBDT_Batch {
	public static void main(String[] args) throws Exception {
		final String train_url = "https://alink-release.oss-cn-beijing.aliyuncs.com/data-files/adult_train.csv";
		final String test_url = "https://alink-release.oss-cn-beijing.aliyuncs.com/data-files/adult_test.csv";
		String SCHEMA_STR = "age bigint, workclass string, fnlwgt bigint, education string, " +
				"education_num bigint, marital_status string, occupation string, " +
				"relationship string, race string, sex string, capital_gain bigint, " +
				"capital_loss bigint, hours_per_week bigint, native_country string, label string";

		ExecutionEnvironment batchEnv = ExecutionEnvironment.createRemoteEnvironment(
				FlinkCluster.URL, 
				FlinkCluster.PORT);
		BatchTableEnvironment batchTableEnv = BatchTableEnvironment.create(batchEnv);
		MLEnvironment mlenv = new MLEnvironment(batchEnv,batchTableEnv);
		MLEnvironmentFactory.setDefault(mlenv);

		BatchOperator trainData = new CsvSourceBatchOp()
				.setFilePath(train_url)
				.setSchemaStr(SCHEMA_STR);

		BatchOperator testData = new CsvSourceBatchOp()
				.setFilePath(test_url)
				.setSchemaStr(SCHEMA_STR);

		GbdtClassifier gbdt = new GbdtClassifier()
				.setFeatureCols(new String[]{"age", "capital_gain", "capital_loss", "hours_per_week",
						"workclass", "education", "marital_status", "occupation"})
				.setCategoricalCols(new String[]{"workclass", "education", "marital_status", "occupation"})
				.setLabelCol("label")
				.setNumTrees(20)
				.setPredictionCol("prediction_result");

		GbdtClassificationModel model = gbdt.fit(trainData);
		model.transform(testData).print();
	}
}
