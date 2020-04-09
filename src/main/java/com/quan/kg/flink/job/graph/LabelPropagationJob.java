package com.quan.kg.flink.job.graph;

import org.apache.flink.api.common.functions.MapFunction;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.api.java.DataSet;
import org.apache.flink.api.java.ExecutionEnvironment;
import org.apache.flink.api.java.tuple.Tuple2;
import org.apache.flink.graph.Graph;
import org.apache.flink.graph.Vertex;
import org.apache.flink.graph.library.LabelPropagation;
import org.apache.flink.types.NullValue;

import com.quan.kg.flink.client.FlinkCluster;

public class LabelPropagationJob {

	public static void main(String[] args) throws Exception {
		final String jarFiles = "D:\\workspace\\project\\JavaProject\\flink\\jobs\\LabelPropagationJob.jar";
		ExecutionEnvironment env = ExecutionEnvironment.createRemoteEnvironment(
				FlinkCluster.URL, 
				FlinkCluster.PORT,
				jarFiles);
		String sink = "/home/quan/sinks/com-lj.community"
				.concat(String.valueOf(System.currentTimeMillis()))
				.concat("txt");
		//import data from text files, filter and split them
		DataSet<Tuple2<String, String>> tuple2s = env
				.readTextFile(sink)
				.first(10000)
				.filter(s->!s.startsWith("#"))                         //filter
				.map(s->new Tuple2<String, String>(                    //map to Tuple2
						s.split("	")[0], 
						s.split("	")[1]))
				.returns(Types.TUPLE(Types.STRING,Types.STRING));

		Graph<String, String, NullValue> graph = Graph
				.fromTuple2DataSet(tuple2s, env)
				.mapVertices(new MapFunction<Vertex<String,NullValue>, String>() {
					private static final long serialVersionUID = 1L;
					@Override
					public String map(Vertex<String, NullValue> value) throws Exception {
						return value.f0;
					}
				});
		DataSet<Vertex<String, String>> verticesWithCommunity = graph.run(new LabelPropagation<>(30));
		verticesWithCommunity.writeAsText("/home/quan/sinks/com-lj.lpalabel.txt");
		env.execute("Master's Graph Job!");
	}
}
