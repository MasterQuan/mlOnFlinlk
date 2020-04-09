package com.quan.kg.flink.job.graph;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.graph.Edge;
import org.apache.flink.graph.Graph;
import org.apache.flink.graph.Vertex;
import org.apache.flink.graph.library.CommunityDetection;
import org.apache.flink.types.NullValue;

import com.quan.kg.flink.client.FlinkCluster;

public class CommunityDetectionJob {

	public static void main(String[] args) throws Exception {
		final String jarFiles = "D:\\workspace\\project\\JavaProject\\flink\\jobs\\CommunityDetectionJob.jar";
		final int limit = 10000;
		final int parallelism = 1;
		String sink = "/home/quan/sinks/com-lj.community"
				.concat(String.valueOf(System.currentTimeMillis()))
				.concat("txt");

		ExecutionEnvironment env = ExecutionEnvironment.createRemoteEnvironment(
				FlinkCluster.URL, 
				FlinkCluster.PORT,
				parallelism,
				jarFiles);
		//import data from text files, filter and split them
		DataSet<Tuple2<Long, Long>> tuple2s = env
				.readTextFile("/home/quan/data/com-lj.ungraph.txt")
				.first(limit)
				.filter(s->!s.startsWith("#"))
				.map(s->new Tuple2<Long, Long>(
						Long.valueOf(s.split("	")[0]), 
						Long.valueOf(s.split("	")[1])))
				.returns(Types.TUPLE(Types.LONG,Types.LONG));

		Graph<Long, Long, Double> graph = Graph
				.fromTuple2DataSet(tuple2s, env)
				.mapVertices(new MapFunction<Vertex<Long,NullValue>, Long>() {
					private static final long serialVersionUID = 1L;
					@Override
					public Long map(Vertex<Long,NullValue> value) throws Exception {
						return value.f0;
					}
				}).mapEdges(new MapFunction<Edge<Long,NullValue>, Double>() {
					/**
					 * 
					 */
					private static final long serialVersionUID = 1L;

					@Override
					public Double map(Edge<Long, NullValue> value) throws Exception {
						return 0.5;
					}
				});

		Graph<Long, Long, Double> graphWithCommunity = graph.run(new CommunityDetection<>(30, 0.5));
		graphWithCommunity.getVertices().writeAsText(sink);
		env.execute("Graph Community Detection Job!");
	}
}
