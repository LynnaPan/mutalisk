/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
*/

package io.hybridtheory.mutalisk.rest.test.entity;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;


public final class JobCounters implements Serializable {
    
    private Map<String, Map<String, Long>> counters = new TreeMap<>();

    public Map<String, Map<String, Long>> getCounters() {
        return counters;
    }

    public void setCounters(Map<String, Map<String, Long>> counters) {
        this.counters = counters;
    }
    
    public String toString() {
        return counters.toString();
    }

    public void clear() {
        for (Map.Entry<String, Map<String, Long>> entry : counters.entrySet()) {
            entry.getValue().clear();
        }
        counters.clear();
    }

    public Long getCounterValue(CounterName counterName) {
        if (counters.containsKey(counterName.group.name)
                && counters.get(counterName.group.name).containsKey(counterName.name)) {
            return counters.get(counterName.group.name).get(counterName.name);
        } else {
            return 0L;
        }
    }

    public Long getCounterValue(String groupName, String counterName) {
        if (counters.containsKey(groupName) && counters.get(groupName).containsKey(counterName)) {
            return counters.get(groupName).get(counterName);
        } else {
            return 0L;
        }
    }

    public boolean hasCounterValue(CounterName counterName) {
        if (counters.containsKey(counterName.group.name) && counters.get(counterName.group.name).containsKey(counterName.name)) {
            return true;
        }else{
            return false;
        }
    }

    public void setCounterValue(CounterName counterName, long value) {
        if (!counters.containsKey(counterName.group.name)) {
            counters.put(counterName.group.name, new HashMap<>());
        }
        counters.get(counterName.group.name).put(counterName.name, value);
    }

    public static enum GroupName {
        FileSystemCounters("org.apache.hadoop.mapreduce.FileSystemCounter", "FileSystemCounters"),
        MapReduceTaskCounter("org.apache.hadoop.mapreduce.TaskCounter", "MapReduceTaskCounter"),
        MapReduceJobCounter("org.apache.hadoop.mapreduce.JobCounter", "MapReduceJobCounter"),
        JobMonitoringTaskCounter("ebay.taskCounter", "taskCounter"),
        JobMonitoringConfCounter("ebay.confCounter", "ConfCounter");


        private String name;
        private String displayName;

        GroupName(String name, String displayName) {
            this.name = name;
            this.displayName = displayName;
        }

        public String getName() {
            return name;
        }

        public String getDisplayName() {
            return displayName;
        }
        
    }

    public static enum CounterName {

        FILE_BYTES_READ(GroupName.FileSystemCounters, "FILE_BYTES_READ", "File read bytes"),
        FILE_BYTES_WRITTEN(GroupName.FileSystemCounters, "FILE_BYTES_WRITTEN", "File written bytes"),
        HDFS_BYTES_READ(GroupName.FileSystemCounters, "HDFS_BYTES_READ", "HDFS read bytes"),
        HDFS_BYTES_WRITTEN(GroupName.FileSystemCounters, "HDFS_BYTES_WRITTEN", "HDFS written bytes"),
        HDFS_READ_OPS(GroupName.FileSystemCounters, "HDFS_READ_OPS", "HDFS read ops"),
        HDFS_WRITE_OPS(GroupName.FileSystemCounters, "HDFS_WRITE_OPS", "HDFS write ops"),

        MILLIS_MAPS(GroupName.MapReduceJobCounter, "MILLIS_MAPS", "total maps mills"),
        MILLIS_REDUCES(GroupName.MapReduceJobCounter, "MILLIS_REDUCES", "total reduce mills"),
        TOTAL_LAUNCHED_MAPS(GroupName.MapReduceJobCounter,"TOTAL_LAUNCHED_MAPS","total map attempts"),
        TOTAL_LAUNCHED_REDUCES(GroupName.MapReduceJobCounter,"TOTAL_LAUNCHED_REDUCES","total reduce attempts"),

        MAP_INPUT_RECORDS(GroupName.MapReduceTaskCounter, "MAP_INPUT_RECORDS", "Map input records"),
        MAP_OUTPUT_RECORDS(GroupName.MapReduceTaskCounter, "MAP_OUTPUT_RECORDS", "Map output records"),
        MAP_OUTPUT_BYTES(GroupName.MapReduceTaskCounter, "MAP_OUTPUT_BYTES", "Map output bytes"),
        MAP_OUTPUT_MATERIALIZED_BYTES(GroupName.MapReduceTaskCounter, "MAP_OUTPUT_MATERIALIZED_BYTES", "Map output materialized bytes"),
        SPLIT_RAW_BYTES(GroupName.MapReduceTaskCounter, "SPLIT_RAW_BYTES", "SPLIT_RAW_BYTES"),

        REDUCE_INPUT_GROUPS(GroupName.MapReduceTaskCounter, "REDUCE_INPUT_GROUPS", "Reduce input groups"),
        REDUCE_SHUFFLE_BYTES(GroupName.MapReduceTaskCounter, "REDUCE_SHUFFLE_BYTES", "Reduce shuffle bytes"),
        REDUCE_OUTPUT_RECORDS(GroupName.MapReduceTaskCounter, "REDUCE_OUTPUT_RECORDS", "Reduce output records"),
        REDUCE_INPUT_RECORDS(GroupName.MapReduceTaskCounter, "REDUCE_INPUT_RECORDS", "Reduce input records"),

        COMBINE_INPUT_RECORDS(GroupName.MapReduceTaskCounter, "COMBINE_INPUT_RECORDS", "Combine input records"),
        COMBINE_OUTPUT_RECORDS(GroupName.MapReduceTaskCounter, "COMBINE_OUTPUT_RECORDS", "Combine output records"),
        SPILLED_RECORDS(GroupName.MapReduceTaskCounter, "SPILLED_RECORDS", "Spilled Records"),

        CPU_MILLISECONDS(GroupName.MapReduceTaskCounter, "CPU_MILLISECONDS", "CPU time spent (ms)"),
        GC_MILLISECONDS(GroupName.MapReduceTaskCounter, "GC_TIME_MILLIS", "GC time elapsed (ms)"),
        COMMITTED_HEAP_BYTES(GroupName.MapReduceTaskCounter, "COMMITTED_HEAP_BYTES", "Total committed heap usage (bytes)"),
        PHYSICAL_MEMORY_BYTES(GroupName.MapReduceTaskCounter, "PHYSICAL_MEMORY_BYTES", "Physical memory (bytes) snapshot"),
        VIRTUAL_MEMORY_BYTES(GroupName.MapReduceTaskCounter, "VIRTUAL_MEMORY_BYTES", "Virtual memory (bytes) snapshot"),


        SUCCESSFUL_ATTEMPT_TIME(GroupName.JobMonitoringTaskCounter, "SUCCESSFUL_ATTEMPT_DURATION", "Successful attempt duration(ms)"),
        TOTAL_ATTEMPT_TIME(GroupName.JobMonitoringTaskCounter, "TOTAL_ATTEMPT_DURATION", "Total attempt duration(ms)"),
        SHUFFLE_TIME(GroupName.JobMonitoringTaskCounter, "SUCCESSFUL_ATTEMPT_SHUFFLE_DURATION", "Successful attempt shuffle duration(ms)"),
        SORT_TIME(GroupName.JobMonitoringTaskCounter, "SUCCESSFUL_ATTEMPT_SORT_DURATION", "Successful attempt sort duration(ms)"),
        EXEC_TIME(GroupName.JobMonitoringTaskCounter, "SUCCESSFUL_ATTEMPT_EXEC_DURATION", "Successful attempt logic execution duration(ms)"),
        TASK_DURATION(GroupName.JobMonitoringTaskCounter, "TASK_DURATION", "Total task duration(ms)"),
        TASK_WAIT_TIME(GroupName.JobMonitoringTaskCounter, "TASK_WAIT_TIME", "Total task wait time(ms)"),

        MAP_HOST_DISTIBUTION_NO(GroupName.JobMonitoringTaskCounter, "MAP_HOST_DISTIBUTION_NO","Hosts involved in job mapper task running"),
        REDUCE_HOST_DISTRIBUTION_NO(GroupName.JobMonitoringTaskCounter, "REDUCE_HOST_DISTIBUTION_NO","Hosts involved in job reduce task runnning"),
        MAP_COUNT(GroupName.JobMonitoringTaskCounter, "MAP_COUNT","Hosts involved in job mapper task running"),
        REDUCE_COUNT(GroupName.JobMonitoringTaskCounter, "REDUCE_COUNT","Hosts involved in job reduce task runnning"),

        REDUCE_START_PERCENTAGE_CONF(GroupName.JobMonitoringConfCounter, "mapreduce.job.reduce.slowstart.completedmaps", "Reduce start time"),
        MAP_MEMORY_CONTAINER_CONF(GroupName.JobMonitoringConfCounter, "mapreduce.map.memory.mb", "Mapper memory container size"),
        REDUCE_MEMORY_CONTAINER_CONF(GroupName.JobMonitoringConfCounter, "mapreduce.reduce.memory.mb", "Reduce memory container size"),
        YARN_MIN_MEMORY_CONF(GroupName.JobMonitoringConfCounter, "yarn.scheduler.minimum-allocation-mb", "Yarn min allocation unit"),
        INPUT_FOLDER(GroupName.JobMonitoringConfCounter, "mapreduce.input.fileinputformat.inputdir", "Input folder"),
        OUTPUT_FOLDER(GroupName.JobMonitoringConfCounter, "mapreduce.output.fileoutputformat.outputdir", "Output folder"),
        MAP_OUTPUT_COMPRESSION_CODEC(GroupName.JobMonitoringConfCounter, "mapreduce.map.output.compress.codec", "Map output compress codec"),
        ENABLE_MAP_COMPRESS(GroupName.JobMonitoringConfCounter, "mapreduce.map.output.compress", "Map output compress or not"),
        OUTPUT_COMPRESSION_CODEC(GroupName.JobMonitoringConfCounter, "mapreduce.output.fileoutputformat.compress.codec", "Output compress codec"),
        ENABLE_JOB_COMPRESS(GroupName.JobMonitoringConfCounter, "mapreduce.output.fileoutputformat.compress", "Output compress or not"),
        SOURCE_REPLICATION(GroupName.JobMonitoringConfCounter, "mapreduce.client.submit.file.replication", "Client source code replication factor"),

        CASCADING_APP_NAME(GroupName.JobMonitoringConfCounter, "cascading.app.name", "Cascading app name"),
        CASCADING_FLOW_ID(GroupName.JobMonitoringConfCounter, "cascading.flow.id", "Cascading flow id"),
        CASCADING_STEP_NUM(GroupName.JobMonitoringConfCounter, "cascading.flow.step.num", "Cascading flow step num"),

        HIVE_FLOW_ID(GroupName.JobMonitoringConfCounter, "hive.query.id", "Hive query id"),
        HIVE_QUERY(GroupName.JobMonitoringConfCounter, "hive.query.string","Hive SQL Query"),

        PIG_SCRIPT(GroupName.JobMonitoringConfCounter, "pig.script", "Pig script"),
        PIG_SCRIPT_ID(GroupName.JobMonitoringConfCounter, "pig.script.id", "Pig script id"),
        PIG_PARENT_ID(GroupName.JobMonitoringConfCounter, "pig.parent.jobid","Pig parent job id"),
        ;


        private GroupName group;
        private String name;
        private String displayName;

        CounterName(GroupName group, String name, String displayName) {
            this.group = group;
            this.name = name;
            this.displayName = displayName;
        }

        public String getName() {
            return name;
        }

        public String getDisplayName() {
            return displayName;
        }

        public String getGroupName() {
            return group.name();
        }

    }
}
