package io.hybridtheory.mutalisk.rest.test;


import io.hybridtheory.mutalisk.common.api.ElasticExecutor;
import io.hybridtheory.mutalisk.common.api.exception.BulkDeleteException;
import io.hybridtheory.mutalisk.common.conf.ElasticClientConf;
import io.hybridtheory.mutalisk.common.schema.ElasticSearchSchema;
import io.hybridtheory.mutalisk.common.util.StorageUtil;
import io.hybridtheory.mutalisk.rest.executor.ElasticRestExecutor;
import io.hybridtheory.mutalisk.rest.test.entity.MRJobEntity;
import org.apache.http.HttpHost;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import pl.allegro.tech.embeddedelasticsearch.EmbeddedElastic;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

public class SchemaRestTest {

    private static String jsonStr = "{\"site\":\"APOLLO-PHX\",\"submitTime\":1516589698918,\"launchTime\":1516589726173,\"finishTime\":1516596740531,\"jobId\":\"job_1515135454013_254791\",\"jobName\":\"[04912408FF5E4658829B5F23BA405959/6FCAFC7D1DB74BBA8A86C9B3D338E168] com.ebay.scalding.graph.GlobalAspectGraph[site=0, endDate=2018-01-21, topN=50, topQt=10, topAt=.80, thresh=10, nDays=7]/(2/6)\",\"jobNormalizedName\":\"com.ebay.scalding.graph.GlobalAspectGraph[site=0, endDate=2018-01-21, topN=50, topQt=10, topAt=.80, thresh=10, nDays=7]\",\"jobExecutionId\":\"04912408FF5E4658829B5F23BA405959\",\"queueName\":\"hddq-exprce-merch\",\"userName\":\"b_merch\",\"jobType\":\"CASCADING\",\"totalMaps\":1469,\"totalReduces\":400,\"failedMaps\":0,\"failedReduces\":0,\"finishMaps\":1469,\"finishReduces\":400,\"jobStatus\":\"SUCCEEDED\",\"counters\":{\"counters\":{\"ebay.confCounter\":{\"mapreduce.map.memory.mb\":2304,\"mapreduce.reduce.memory.mb\":4096},\"ebay.taskCounter\":{\"REDUCE_COUNT\":400,\"REDUCE_HOST_DISTIBUTION_NO\":392,\"MAP_COUNT\":1469,\"MAP_HOST_DISTIBUTION_NO\":847},\"org.apache.hadoop.mapreduce.FileSystemCounter\":{\"FILE_LARGE_READ_OPS\":0,\"FILE_WRITE_OPS\":0,\"HDFS_READ_OPS\":7076,\"HDFS_BYTES_READ\":2833003316290,\"HDFS_LARGE_READ_OPS\":0,\"FILE_READ_OPS\":0,\"FILE_BYTES_WRITTEN\":16382684337,\"FILE_BYTES_READ\":0,\"HDFS_WRITE_OPS\":800,\"HDFS_BYTES_WRITTEN\":2560513544},\"org.apache.hadoop.mapreduce.JobCounter\":{\"TOTAL_LAUNCHED_MAPS\":1562,\"VCORES_MILLIS_REDUCES\":32624947,\"TOTAL_LAUNCHED_REDUCES\":400,\"NUM_KILLED_MAPS\":95,\"OTHER_LOCAL_MAPS\":172,\"DATA_LOCAL_MAPS\":1049,\"MB_MILLIS_MAPS\":5028121384704,\"SLOTS_MILLIS_REDUCES\":521999152,\"VCORES_MILLIS_MAPS\":2182344351,\"NUM_FAILED_MAPS\":1,\"MB_MILLIS_REDUCES\":133631782912,\"SLOTS_MILLIS_MAPS\":19641099159,\"RACK_LOCAL_MAPS\":341,\"MILLIS_REDUCES\":32624947,\"MILLIS_MAPS\":2182344351},\"org.apache.hadoop.mapreduce.TaskCounter\":{\"MAP_OUTPUT_MATERIALIZED_BYTES\":16039368371,\"REDUCE_INPUT_RECORDS\":1310445585,\"SPILLED_RECORDS\":1310447022,\"MERGED_MAP_OUTPUTS\":587600,\"VIRTUAL_MEMORY_BYTES\":8288175570944,\"MAP_INPUT_RECORDS\":61575067386,\"SPLIT_RAW_BYTES\":487287,\"FAILED_SHUFFLE\":24,\"MAP_OUTPUT_BYTES\":24672413891,\"REDUCE_SHUFFLE_BYTES\":16039368371,\"PHYSICAL_MEMORY_BYTES\":3568713166848,\"GC_TIME_MILLIS\":22146425,\"REDUCE_INPUT_GROUPS\":1237351125,\"COMBINE_OUTPUT_RECORDS\":0,\"SHUFFLED_MAPS\":587600,\"REDUCE_OUTPUT_RECORDS\":55704273,\"MAP_OUTPUT_RECORDS\":1310447022,\"COMBINE_INPUT_RECORDS\":0,\"CPU_MILLISECONDS\":2192675500,\"COMMITTED_HEAP_BYTES\":4052948090880}}},\"confs\":{\"config\":{\"mapreduce.output.fileoutputformat.outputdir\":\"hdfs://apollo-phx-nn-ha/tmp/hadoop/hadoop-b_merch/8162879541__pipe_4_FB17A837A77D41F3909666EF2343B31E\",\"mapreduce.output.fileoutputformat.compress\":\"true\",\"mapreduce.output.fileoutputformat.compress.codec\":\"org.apache.hadoop.io.compress.GzipCodec\",\"cascading.flow.step.num\":\"2\",\"mapreduce.client.submit.file.replication\":\"10\",\"mapreduce.job.reduce.slowstart.completedmaps\":\"1\",\"mapreduce.input.fileinputformat.inputdir\":\"hdfs://apollo-phx-nn-ha/tmp/hadoop/hadoop-b_merch/3296115220__pipe_1_5E56BA493BA6412EB20F3FD024B528DD,hdfs://apollo-phx-nn-ha/sys/edw/dw_lstg_item/snapshot/2018/01/21/00\",\"mapreduce.reduce.java.opts\":\"-server -Xmx3584m -Djava.net.preferIPv4Stack=true -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps\",\"mapreduce.map.output.compress\":\"true\",\"mapreduce.map.memory.mb\":\"2304\",\"mapreduce.map.output.compress.codec\":\"com.hadoop.compression.lzo.LzoCodec\",\"cascading.flow.id\":\"04912408FF5E4658829B5F23BA405959\",\"cascading.app.name\":\"com.ebay.scalding.graph.GlobalAspectGraph[site=0, endDate=2018-01-21, topN=50, topQt=10, topAt=.80, thresh=10, nDays=7]\",\"mapred.output.format.class\":\"org.apache.hadoop.mapred.SequenceFileOutputFormat\",\"mapreduce.reduce.memory.mb\":\"4096\",\"mapreduce.map.java.opts\":\"-server -Xmx2048m -Djava.net.preferIPv4Stack=true\",\"yarn.scheduler.minimum-allocation-mb\":\"256\",\"mapreduce.output.fileoutputformat.compress.type\":\"BLOCK\"}},\"amHost\":\"hdc9-phx04-0190-0114-010.stratus.phx.ebay.com\",\"mapContainer\":2304,\"reduceContainer\":4096,\"mapHCU\":4905213,\"reduceHCU\":130499,\"totalHCU\":5035712,\"mapMemoryWaste\":219893,\"reduceMemoryWaste\":45216,\"totalMemoryWaste\":265109,\"waitTime\":90559,\"inputFolder\":[\"hdfs://apollo-phx-nn-ha/tmp/hadoop/hadoop-b_merch/3296115220__pipe_1_5E56BA493BA6412EB20F3FD024B528DD\",\"hdfs://apollo-phx-nn-ha/sys/edw/dw_lstg_item/snapshot/2018/01/21/00\"],\"outputFolder\":[\"hdfs://apollo-phx-nn-ha/tmp/hadoop/hadoop-b_merch/8162879541__pipe_4_FB17A837A77D41F3909666EF2343B31E\"],\"timestamp\":0}";
    static MRJobEntity job = StorageUtil.gson.fromJson(jsonStr, MRJobEntity.class);

    private static String jsonStr2 = "{\"site\":\"ARES-LVS\",\"submitTime\":1516589698920,\"launchTime\":1516589726143,\"finishTime\":1516596740533,\"jobId\":\"job_1515135454013_254791\",\"jobName\":\"[04912408FF5E4658829B5F23BA405959/6FCAFC7D1DB74BBA8A86C9B3D338E168] com.ebay.scalding.graph.GlobalAspectGraph[site=0, endDate=2018-01-21, topN=50, topQt=10, topAt=.80, thresh=10, nDays=7]/(2/6)\",\"jobNormalizedName\":\"com.ebay.scalding.graph.GlobalAspectGraph[site=0, endDate=2018-01-21, topN=50, topQt=10, topAt=.80, thresh=10, nDays=7]\",\"jobExecutionId\":\"04912408FF5E4658829B5F23BA405959\",\"queueName\":\"hddq-exprce-merch\",\"userName\":\"b_merch\",\"jobType\":\"CASCADING\",\"totalMaps\":1469,\"totalReduces\":400,\"failedMaps\":0,\"failedReduces\":0,\"finishMaps\":1469,\"finishReduces\":400,\"jobStatus\":\"SUCCEEDED\",\"counters\":{\"counters\":{\"ebay.confCounter\":{\"mapreduce.map.memory.mb\":2304,\"mapreduce.reduce.memory.mb\":4096},\"ebay.taskCounter\":{\"REDUCE_COUNT\":400,\"REDUCE_HOST_DISTIBUTION_NO\":392,\"MAP_COUNT\":1469,\"MAP_HOST_DISTIBUTION_NO\":847},\"org.apache.hadoop.mapreduce.FileSystemCounter\":{\"FILE_LARGE_READ_OPS\":0,\"FILE_WRITE_OPS\":0,\"HDFS_READ_OPS\":7076,\"HDFS_BYTES_READ\":2833003316290,\"HDFS_LARGE_READ_OPS\":0,\"FILE_READ_OPS\":0,\"FILE_BYTES_WRITTEN\":16382684337,\"FILE_BYTES_READ\":0,\"HDFS_WRITE_OPS\":800,\"HDFS_BYTES_WRITTEN\":2560513544},\"org.apache.hadoop.mapreduce.JobCounter\":{\"TOTAL_LAUNCHED_MAPS\":1562,\"VCORES_MILLIS_REDUCES\":32624947,\"TOTAL_LAUNCHED_REDUCES\":400,\"NUM_KILLED_MAPS\":95,\"OTHER_LOCAL_MAPS\":172,\"DATA_LOCAL_MAPS\":1049,\"MB_MILLIS_MAPS\":5028121384704,\"SLOTS_MILLIS_REDUCES\":521999152,\"VCORES_MILLIS_MAPS\":2182344351,\"NUM_FAILED_MAPS\":1,\"MB_MILLIS_REDUCES\":133631782912,\"SLOTS_MILLIS_MAPS\":19641099159,\"RACK_LOCAL_MAPS\":341,\"MILLIS_REDUCES\":32624947,\"MILLIS_MAPS\":2182344351},\"org.apache.hadoop.mapreduce.TaskCounter\":{\"MAP_OUTPUT_MATERIALIZED_BYTES\":16039368371,\"REDUCE_INPUT_RECORDS\":1310445585,\"SPILLED_RECORDS\":1310447022,\"MERGED_MAP_OUTPUTS\":587600,\"VIRTUAL_MEMORY_BYTES\":8288175570944,\"MAP_INPUT_RECORDS\":61575067386,\"SPLIT_RAW_BYTES\":487287,\"FAILED_SHUFFLE\":24,\"MAP_OUTPUT_BYTES\":24672413891,\"REDUCE_SHUFFLE_BYTES\":16039368371,\"PHYSICAL_MEMORY_BYTES\":3568713166848,\"GC_TIME_MILLIS\":22146425,\"REDUCE_INPUT_GROUPS\":1237351125,\"COMBINE_OUTPUT_RECORDS\":0,\"SHUFFLED_MAPS\":587600,\"REDUCE_OUTPUT_RECORDS\":55704273,\"MAP_OUTPUT_RECORDS\":1310447022,\"COMBINE_INPUT_RECORDS\":0,\"CPU_MILLISECONDS\":2192675500,\"COMMITTED_HEAP_BYTES\":4052948090880}}},\"confs\":{\"config\":{\"mapreduce.output.fileoutputformat.outputdir\":\"hdfs://apollo-phx-nn-ha/tmp/hadoop/hadoop-b_merch/8162879541__pipe_4_FB17A837A77D41F3909666EF2343B31E\",\"mapreduce.output.fileoutputformat.compress\":\"true\",\"mapreduce.output.fileoutputformat.compress.codec\":\"org.apache.hadoop.io.compress.GzipCodec\",\"cascading.flow.step.num\":\"2\",\"mapreduce.client.submit.file.replication\":\"10\",\"mapreduce.job.reduce.slowstart.completedmaps\":\"1\",\"mapreduce.input.fileinputformat.inputdir\":\"hdfs://apollo-phx-nn-ha/tmp/hadoop/hadoop-b_merch/3296115220__pipe_1_5E56BA493BA6412EB20F3FD024B528DD,hdfs://apollo-phx-nn-ha/sys/edw/dw_lstg_item/snapshot/2018/01/21/00\",\"mapreduce.reduce.java.opts\":\"-server -Xmx3584m -Djava.net.preferIPv4Stack=true -verbose:gc -XX:+PrintGCDetails -XX:+PrintGCTimeStamps\",\"mapreduce.map.output.compress\":\"true\",\"mapreduce.map.memory.mb\":\"2304\",\"mapreduce.map.output.compress.codec\":\"com.hadoop.compression.lzo.LzoCodec\",\"cascading.flow.id\":\"04912408FF5E4658829B5F23BA405959\",\"cascading.app.name\":\"com.ebay.scalding.graph.GlobalAspectGraph[site=0, endDate=2018-01-21, topN=50, topQt=10, topAt=.80, thresh=10, nDays=7]\",\"mapred.output.format.class\":\"org.apache.hadoop.mapred.SequenceFileOutputFormat\",\"mapreduce.reduce.memory.mb\":\"4096\",\"mapreduce.map.java.opts\":\"-server -Xmx2048m -Djava.net.preferIPv4Stack=true\",\"yarn.scheduler.minimum-allocation-mb\":\"256\",\"mapreduce.output.fileoutputformat.compress.type\":\"BLOCK\"}},\"amHost\":\"hdc9-phx04-0190-0114-010.stratus.phx.ebay.com\",\"mapContainer\":2304,\"reduceContainer\":4096,\"mapHCU\":4905213,\"reduceHCU\":130499,\"totalHCU\":5035712,\"mapMemoryWaste\":219893,\"reduceMemoryWaste\":45216,\"totalMemoryWaste\":265109,\"waitTime\":90559,\"inputFolder\":[\"hdfs://apollo-phx-nn-ha/tmp/hadoop/hadoop-b_merch/3296115220__pipe_1_5E56BA493BA6412EB20F3FD024B528DD\",\"hdfs://apollo-phx-nn-ha/sys/edw/dw_lstg_item/snapshot/2018/01/21/00\"],\"outputFolder\":[\"hdfs://apollo-phx-nn-ha/tmp/hadoop/hadoop-b_merch/8162879541__pipe_4_FB17A837A77D41F3909666EF2343B31E\"],\"timestamp\":0}";
    static MRJobEntity job2 = StorageUtil.gson.fromJson(jsonStr2, MRJobEntity.class);

    static ElasticExecutor executor;
    static EmbeddedElastic esServer;

    @BeforeClass
    public static void init() throws IOException, InterruptedException {

        // build up one embedded elastic server
        esServer = ElasticEmbeddedServer.buildLocalserver().start();

        ElasticClientConf conf = new ElasticClientConf("test", new HttpHost[]{
            new HttpHost("localhost", 9200)
        });

        executor = new ElasticRestExecutor(conf);
    }

    @AfterClass
    public static void deinit() throws IOException {
        executor.close();
        esServer.stop();
    }

    protected void ensureNewJobEntityIndex() {
        // ensure deleted
        if (executor.existedIndex(MRJobEntity.class)) {
            executor.deleteIndex(MRJobEntity.class);
        }

        Assert.assertFalse(executor.existedIndex(MRJobEntity.class));
    }

    protected void ensureJobEntityIndex() {
        // ensure created
        if (!executor.existedIndex(MRJobEntity.class)) {
            executor.createIndex(MRJobEntity.class);
        }

        Assert.assertTrue(executor.existedIndex(MRJobEntity.class));
    }

    @Test
    public void indexCreation() {
        ensureNewJobEntityIndex();
        ElasticSearchSchema schema = ElasticSearchSchema.getOrBuild(MRJobEntity.class);
        executor.createIndex(schema.index, schema.type, schema.toIndexSetting(), schema.toTypeMapping());


        Assert.assertTrue(executor.existedIndex(MRJobEntity.class));
    }

    @Test
    public void indexCount() throws ExecutionException, InterruptedException {
        ensureNewJobEntityIndex();
        executor.createIndex(MRJobEntity.class);

        Assert.assertTrue(executor.existedIndex(MRJobEntity.class));

        // assert total count is zero
        Assert.assertTrue(executor.countIndex(MRJobEntity.class) == 0);

        //@NOTE, since the insert action return does only means acknowledgement, need sleep for
        // a while to ensure data is visible

        // insert one
        Assert.assertTrue(executor.insert(job));
        Thread.sleep(3000);
        Assert.assertTrue(executor.countIndex(MRJobEntity.class) == 1);

        // insert another one with different id
        Assert.assertTrue(executor.insertById(job, "xxx_xxxx_xxxxx_xxx"));
        Thread.sleep(3000);
        Assert.assertTrue(executor.countIndex(MRJobEntity.class) == 2);
    }

    @Test
    public void indexClear() throws ExecutionException, InterruptedException, BulkDeleteException {
        ensureJobEntityIndex();

        // insert two to ensure at existed two existed
        Assert.assertTrue(executor.insert(job));
        Assert.assertTrue(executor.insertById(job, "xxx_xxxx_xxxxx_xxx"));
        Thread.sleep(3000);
        Assert.assertTrue(executor.countIndex(MRJobEntity.class) >= 2);

        // do clean up
        executor.clearIndexType(MRJobEntity.class);
        Thread.sleep(3000);
        Assert.assertTrue(executor.countIndex(MRJobEntity.class) == 0);
    }
}
