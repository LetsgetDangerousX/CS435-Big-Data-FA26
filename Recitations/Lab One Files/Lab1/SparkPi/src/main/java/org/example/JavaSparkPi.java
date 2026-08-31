/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.example;

import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.sql.SparkSession;

/**
 * Computes an approximation to pi
 * Usage: JavaSparkPi [partitions]
 */
public final class JavaSparkPi {
    private static final long NUM_DARTS = 100_000L;

    public static void main(String[] args) {
        SparkSession spark = SparkSession
                .builder()
                .appName("JavaSparkPi")
                .master("local") //comment out to run in distributed mode
                .getOrCreate();

        int slices = (args.length == 1) ? Integer.parseInt(args[0]) : 2;
        long n = NUM_DARTS * slices;
        JavaRDD<Long> dataSet = spark.range(0L, n, 1L, slices).javaRDD();
        long count = dataSet.map(JavaSparkPi::throwDart).reduce(Long::sum);
        System.out.println("Pi is roughly " + 4.0 * count / n);
        spark.stop();
    }

    private static long throwDart(long num) {
        double x = Math.random() * 2 - 1;
        double y = Math.random() * 2 - 1;
        if (x * x + y * y <= 1) {
            return 1L;
        } else {
            return 0L;
        }
    }
}