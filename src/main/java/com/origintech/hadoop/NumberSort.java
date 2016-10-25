package com.origintech.hadoop;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;
import java.util.Iterator;

/**
 * Created by bcshuai on 9/14/16.
 */
public class NumberSort {
    public static class NumberSortMapper extends MapReduceBase
            implements Mapper<IntWritable, Text, IntWritable, IntWritable>{

        public void map(IntWritable intWritable, Text text, OutputCollector<IntWritable, IntWritable> outputCollector, Reporter reporter) throws IOException {

        }
    }
    public static class NumberSortReducer extends MapReduceBase
            implements Reducer<IntWritable, IntWritable, Text, Text>{

        public void reduce(IntWritable intWritable, Iterator<IntWritable> iterator, OutputCollector<Text, Text> outputCollector, Reporter reporter) throws IOException {

        }
    }
}
