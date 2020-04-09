package com.quan.kg.flink.job.ml.alink;

import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.table.api.java.BatchTableEnvironment;

import com.alibaba.alink.common.MLEnvironment;
import com.alibaba.alink.common.MLEnvironmentFactory;
import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.dataproc.SplitBatchOp;
import com.alibaba.alink.operator.batch.recommendation.AlsTopKPredictBatchOp;
import com.alibaba.alink.operator.batch.recommendation.AlsTrainBatchOp;
import com.alibaba.alink.operator.batch.source.CsvSourceBatchOp;
import com.quan.kg.flink.client.FlinkCluster;

public class ALS_Topk_Batch {
	@SuppressWarnings("rawtypes")
	public static void main(String[] args) throws Exception {

		final String URL = "https://alink-release.oss-cn-beijing.aliyuncs.com/data-files/movielens_ratings.csv";
		final String SCHEMA_STR = "userid bigint, movieid bigint, rating double, timestamp string";
		
		ExecutionEnvironment batchEnv = ExecutionEnvironment.createRemoteEnvironment(
				FlinkCluster.URL, 
				FlinkCluster.PORT);
		BatchTableEnvironment batchTableEnv = BatchTableEnvironment.create(batchEnv);
		MLEnvironment mlenv = new MLEnvironment(batchEnv,batchTableEnv);
		MLEnvironmentFactory.setDefault(mlenv);
		
		BatchOperator data = new CsvSourceBatchOp()
				.setFilePath(URL).setSchemaStr(SCHEMA_STR);

		SplitBatchOp spliter = new SplitBatchOp().setFraction(0.8);
		spliter.linkFrom(data);

		BatchOperator trainData = spliter;
		BatchOperator testData = spliter.getSideOutput(0);

		AlsTrainBatchOp als = new AlsTrainBatchOp()
				.setUserCol("userid")
				.setItemCol("movieid")
				.setRateCol("rating")
				.setNumIter(10)
				.setRank(10)
				.setLambda(0.1);

		BatchOperator model = als.linkFrom(trainData);

		AlsTopKPredictBatchOp predictor = new AlsTopKPredictBatchOp()
				.setTopK(3)
				.setUserCol("userid")
				.setPredictionCol("movieid")
				.setPredictionCol("prediction_result");

		BatchOperator preditionResult = predictor
				.linkFrom(model, testData)
				.select("userid,prediction_result")
				.distinct();
		preditionResult.print();
	}
}
