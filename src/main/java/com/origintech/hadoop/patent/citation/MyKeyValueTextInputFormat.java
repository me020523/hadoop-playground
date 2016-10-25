package com.origintech.hadoop.patent.citation;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapred.*;

import java.io.IOException;

/**
 * Created by bcshuai on 9/18/16.
 */
public class MyKeyValueTextInputFormat extends KeyValueTextInputFormat {
    @Override
    public RecordReader<Text, Text> getRecordReader(InputSplit genericSplit, JobConf job, Reporter reporter) throws IOException {
        return new MyKeyValueLineRecordReader(job, (FileSplit)genericSplit);
    }

    protected static class MyKeyValueLineRecordReader extends KeyValueLineRecordReader {
        private final LineRecordReader lineRecordReader;
        private byte separator = 9;
        private LongWritable dummyKey;
        private Text innerValue;

        public MyKeyValueLineRecordReader(Configuration job, FileSplit split) throws IOException{
            super(job, split);
            this.lineRecordReader = new LineRecordReader(job, split);
            this.dummyKey = this.lineRecordReader.createKey();
            this.innerValue = this.lineRecordReader.createValue();
            this.separator = (byte)',';
        }
    }
}
