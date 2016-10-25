package com.origintech.hadoop.patent.citation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;
import org.apache.hadoop.mapred.FileOutputFormat;
import org.apache.hadoop.mapred.TextOutputFormat;
import org.apache.hadoop.mapreduce.lib.output.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.Iterator;

/**
 * Created by bcshuai on 9/18/16.
 */
public class CitationCount {
    public static class MapperClass extends MapReduceBase implements
            Mapper<Text, Text, IntWritable, IntWritable> {
        private final IntWritable ONE = new IntWritable(1);
        private final IntWritable buf = new IntWritable();
        @Override
        public void configure(JobConf job) {

        }

        public void map(Text text, Text text2, OutputCollector<IntWritable, IntWritable> outputCollector, Reporter reporter)
                throws IOException {
            if(!text2.toString().matches("[0123456789]+"))
                return;
            buf.set(Integer.parseInt(text2.toString()));
            outputCollector.collect(buf, ONE);
        }
    }
    public static class ReducerClass extends MapReduceBase implements
            Reducer<Text, IntWritable, Text, IntWritable> {

        public void reduce(Text text,
                           Iterator<IntWritable> iterator, OutputCollector<Text, IntWritable> outputCollector,
                           Reporter reporter) throws IOException {
            int count = 0;
            while(iterator.hasNext()){
                count += iterator.next().get();
            }
            outputCollector.collect(text, new IntWritable(count));
        }
    }
    public static void deleteOldDirecory(String path){
        Configuration c = new Configuration();
        try {
            FileSystem fs = FileSystem.get(URI.create("hdfs://vm-cluster-node1:8022"), c);
            if (fs.exists(new Path(path))) {
                System.out.println("the target dir exists");
                fs.delete(new Path(path), true);
            }
        }catch (IOException e){
            e.printStackTrace();
        }
    }
    public static void main(String[] args) throws IOException{
        final String inputPath = "hdfs://vm-cluster-node1:8022/user/bcshuai/playground/patent/cite75_99.txt";
        final String outputPath = "hdfs://vm-cluster-node1:8022/user/bcshuai/playground/patent/citation_count";

        JobConf job = new JobConf(CitationCount.class);

        job.addResource("classpath:/hadoop/core-site.xml");
        job.addResource("classpath:/hadoop/hdfs-site.xml");
        job.addResource("classpath:/hadoop/mapred-site.xml");

        deleteOldDirecory("/user/bcshuai/playground/patent/citation_count");

        FileInputFormat.setInputPaths(job, new Path(inputPath));
        FileOutputFormat.setOutputPath(job, new Path(outputPath));

        job.setJobName("Citation Count");
        job.set("mapreduce.input.keyvaluelinerecordreader.key.value.separator",",");
        job.setMapperClass(MapperClass.class);
        job.setCombinerClass(ReducerClass.class);
        job.setReducerClass(ReducerClass.class);

        job.setInputFormat(KeyValueTextInputFormat.class);
        job.setOutputFormat(TextOutputFormat.class);
        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(IntWritable.class);

        JobClient.runJob(job);
        System.exit(0);
    }
}
