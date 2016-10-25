package com.origintech.utils;

import java.io.*;
import java.util.Random;

/**
 * Created by bcshuai on 9/14/16.
 */
public class RandomNumberGenerator {
    /**
     * 生成指定个数的随机数
     * @param count
     */
    private int count = 0;
    private String out = "";
    public RandomNumberGenerator(int count, String outPath){
        this.count = count;
        this.out = outPath;
    }
    public void gen(){
        File f = new File(out);
        FileWriter fw = null;
        try {
            fw = new FileWriter(out);
            Random r = new Random();
            for(int i = 0; i < count; i++){
                int v = r.nextInt(count);
                fw.write("" + v + "\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if(fw != null){
                try {
                    fw.close();
                }catch (IOException e){

                }
            }
        }
    }
    public static void main(String[] args){
        RandomNumberGenerator g = new RandomNumberGenerator(1000000, "numbers.dat");
        g.gen();
    }
}
