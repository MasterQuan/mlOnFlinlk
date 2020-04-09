package com.quan.kg.flink.job.ml.alink;

import org.apache.flink.api.java.ExecutionEnvironment;

import com.alibaba.alink.common.MLEnvironment;
import com.alibaba.alink.common.MLEnvironmentFactory;
import com.alibaba.alink.operator.batch.BatchOperator;
import com.alibaba.alink.operator.batch.nlp.Word2VecPredictBatchOp;
import com.alibaba.alink.operator.batch.nlp.Word2VecTrainBatchOp;
import com.alibaba.alink.operator.batch.source.TextSourceBatchOp;
import com.alibaba.alink.pipeline.Pipeline;
import com.alibaba.alink.pipeline.nlp.Segment;
import com.alibaba.alink.pipeline.nlp.StopWordsRemover;
import com.alibaba.alink.pipeline.nlp.Word2Vec;
import com.alibaba.alink.pipeline.nlp.Word2VecModel;
import com.quan.kg.flink.client.FlinkCluster;

public class Word2vec_CH {
	@SuppressWarnings("rawtypes")
	public static void main(String[] args)  throws Exception {

		final String URL = "/home/quan/data/doc_ch.txt";
		final String SCHEMA_STR = "tokens";

		ExecutionEnvironment batchEnv = ExecutionEnvironment.createRemoteEnvironment(
				FlinkCluster.URL, 
				FlinkCluster.PORT);
		MLEnvironment mlenv = new MLEnvironment(batchEnv,null);
		MLEnvironmentFactory.setDefault(mlenv);

		BatchOperator data = new TextSourceBatchOp()
				.setFilePath(URL)
				.setTextCol(SCHEMA_STR);

		Pipeline pipeline = new Pipeline()
				.add(new Segment().setSelectedCol(SCHEMA_STR))
				.add(new StopWordsRemover().setSelectedCol(SCHEMA_STR));
		
		Word2Vec word2vec = new Word2Vec()
				.setSelectedCol(SCHEMA_STR)
				.setMinCount(2)
				.setVectorSize(10);
		Word2VecModel model =  word2vec.fit(pipeline.fit(data).transform(data));
		model.getVectors().print();
	}
}
