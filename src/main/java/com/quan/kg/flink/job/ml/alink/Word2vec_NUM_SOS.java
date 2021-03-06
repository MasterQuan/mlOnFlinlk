package com.quan.kg.flink.job.ml.alink;

import org.apache.flink.api.java.ExecutionEnvironment;

import com.alibaba.alink.common.MLEnvironment;
import com.alibaba.alink.common.MLEnvironmentFactory;
import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.nlp.Word2VecPredictBatchOp;
import com.alibaba.alink.operator.batch.nlp.Word2VecTrainBatchOp;
import com.alibaba.alink.operator.batch.outlier.SosBatchOp;
import com.alibaba.alink.operator.batch.source.TextSourceBatchOp;
import com.alibaba.alink.pipeline.clustering.KMeans;
import com.alibaba.alink.pipeline.nlp.Word2Vec;
import com.quan.kg.flink.client.FlinkCluster;

public class Word2vec_NUM_SOS {
	public static void main(String[] args)  throws Exception {
		
		final String URL = "/home/quan/data/doc_num.txt";
		final String SCHEMA_STR = "tokens";

		ExecutionEnvironment batchEnv = ExecutionEnvironment.createRemoteEnvironment(
				FlinkCluster.URL, 
				FlinkCluster.PORT);
		MLEnvironment mlenv = new MLEnvironment(batchEnv,null);
		MLEnvironmentFactory.setDefault(mlenv);

		//import original dataset
		BatchOperator data = new TextSourceBatchOp()
				.setFilePath(URL)
				.setTextCol(SCHEMA_STR);
		
		
		BatchOperator train = new Word2VecTrainBatchOp()
				.setSelectedCol(SCHEMA_STR)
		 		.setMinCount(1)
				.setVectorSize(10)
				.linkFrom(data);
		
		SosBatchOp sos = new SosBatchOp()
				.setVectorCol(train.getColNames()[1])
				.setPredictionCol("outlier_score")
				.setPerplexity(3.0)
				.linkFrom(train);
		
		sos.linkFrom(train).print();
	}
}
