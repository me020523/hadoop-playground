package com.origintech.hadoop.patent.preprocess;

import com.origintech.hadoop.patent.citation.CitationCount;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by bcshuai on 9/18/16.
 */
public class ChangeSeparator {
    public static class MapperClass extends MapReduceBase implements Mapper<LongWritable, Text, Text, Text> {
        @Override
        public void configure(JobConf job) {

        }

        public void map(LongWritable text, Text text2, OutputCollector<Text, Text> outputCollector, Reporter reporter) throws IOException {
            int index = text2.find(",");
            Text key = new Text(text2.toString().substring(0, index));
            Text value = new Text(text2.toString().substring(index + 1));
            outputCollector.collect(key, value);
        }
    }
    public static class ReducerClass extends MapReduceBase implements Reducer<Text, Text, Text, Text>{

        public void reduce(Text text, Iterator<Text> iterator, OutputCollector<Text, Text> outputCollector, Reporter reporter) throws IOException {
            if(iterator.hasNext()){
                outputCollector.collect(text, iterator.next());
            }
        }
    }
    public static void main(String[] args) throws  IOException{
        final String inputPath = "hdfs://vm-cluster-node1:8022/user/bcshuai/playground/patent/cite75_99.txt";
        final String outputPath = "hdfs://vm-cluster-node1:8022/user/bcshuai/playground/patent/process";

        JobConf job = new JobConf(ChangeSeparator.class);
        FileInputFormat.setInputPaths(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job,new Path(outputPath));

        job.setJobName("Chang Separator");
        job.addResource("classpath:/hadoop/core-site.xml");
        job.addResource("classpath:/hadoop/hdfs-site.xml");
        job.addResource("classpath:/hadoop/mapred-site.xml");

        job.setMapperClass(MapperClass.class);
        job.setReducerClass(ReducerClass.class);

        job.setInputFormat(TextInputFormat.class);
        job.setOutputFormat(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        JobClient.runJob(job);

        System.exit(0);
    }
}
