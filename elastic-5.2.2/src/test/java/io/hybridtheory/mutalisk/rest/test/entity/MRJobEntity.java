package io.hybridtheory.mutalisk.rest.test.entity;




import io.hybridtheory.mutalisk.common.schema.annotation.ElasticSearchIndex;
import io.hybridtheory.mutalisk.common.schema.annotation.ElasticSearchMeta;

import java.util.List;

@ElasticSearchMeta(index = "mr_job_entity", type = "data", id = {"site", "jobId"})
public class MRJobEntity extends SiteBasedEntity {

    @ElasticSearchIndex
    private long submitTime = 0l;
    @ElasticSearchIndex
    private long launchTime = 0l;
    @ElasticSearchIndex
    private long finishTime = 0l;

    @ElasticSearchIndex
    private String jobId;
    @ElasticSearchIndex
    private String jobName;
    private String jobNormalizedName;

    @ElasticSearchIndex
    private String jobExecutionId;
    @ElasticSearchIndex
    private String queueName;
    @ElasticSearchIndex
    private String userName;
    @ElasticSearchIndex
    private String jobType;
    private int totalMaps = 0;
    private int totalReduces = 0;
    private int failedMaps = 0;
    private int failedReduces = 0;
    private int finishMaps = 0;
    private int finishReduces = 0;

    @ElasticSearchIndex
    private String jobStatus;

    private JobCounters counters;
    private MRJobConfig confs = new MRJobConfig();
    private String amHost;
    private String diagnosis;
    private long mapContainer = 0l;
    private long reduceContainer = 0l;
    private long mapHCU = 0l;
    private long reduceHCU = 0l;
    private long totalHCU = 0l;
    private long mapMemoryWaste = 0l;
    private long reduceMemoryWaste = 0l;
    private long totalMemoryWaste = 0l;
    private long waitTime = 0l;
    private List<String> inputFolder;
    private List<String> outputFolder;


    public MRJobEntity() {
    }

    public MRJobEntity(String site) {
        this.site = site;
    }

    public long getMapContainer() {
        return mapContainer;
    }

    public MRJobEntity setMapContainer(long mapContainer) {
        this.mapContainer = mapContainer;
        return this;
    }

    public long getReduceContainer() {
        return reduceContainer;
    }

    public MRJobEntity setReduceContainer(long reduceContainer) {
        this.reduceContainer = reduceContainer;
        return this;
    }

    public MRJobEntity setJobNormalizedName(String jobNormalizedName) {
        this.jobNormalizedName = jobNormalizedName;
        return this;
    }

    public MRJobEntity setJobExecutionId(String jobExecutionId) {
        this.jobExecutionId = jobExecutionId;
        return this;
    }

    public MRJobEntity setMapMemoryWaste(Long mapMemoryWaste) {
        this.mapMemoryWaste = mapMemoryWaste;
        return this;
    }

    public MRJobEntity setReduceMemoryWaste(Long reduceMemoryWaste) {
        this.reduceMemoryWaste = reduceMemoryWaste;
        return this;
    }

    public MRJobEntity setTotalMemoryWaste(Long totalMemoryWaste) {
        this.totalMemoryWaste = totalMemoryWaste;
        return this;
    }

    public MRJobEntity setWaitTime(Long waitTime) {
        this.waitTime = waitTime;
        return this;
    }

    public MRJobEntity setTotalHCU(Long totalHCU) {
        this.totalHCU = totalHCU;
        return this;
    }

    public MRJobEntity setReduceHCU(Long reduceHCU) {
        this.reduceHCU = reduceHCU;
        return this;
    }

    public MRJobEntity setMapHCU(Long mapHCU) {
        this.mapHCU = mapHCU;
        return this;
    }

    public MRJobEntity setDiagnosis(String diagnosis) {
        this.diagnosis = diagnosis;
        return this;
    }

    public MRJobEntity setAmHost(String amHost) {
        this.amHost = amHost;
        return this;
    }

    public MRJobEntity setUserName(String userName) {
        this.userName = userName;
        return this;
    }

    public MRJobEntity setSubmitTime(long submitTime) {
        this.submitTime = submitTime;
        return this;
    }

    public MRJobEntity setLaunchTime(long launchTime) {
        this.launchTime = launchTime;
        return this;
    }

    public MRJobEntity setFinishTime(long finishTime) {
        this.finishTime = finishTime;
        return this;
    }


    public MRJobEntity setJobId(String jobId) {
        this.jobId = jobId;
        return this;
    }

    public MRJobEntity setJobName(String jobName) {
        this.jobName = jobName;
        return this;
    }

    public MRJobEntity setQueueName(String queueName) {
        this.queueName = queueName;
        return this;
    }

    public MRJobEntity setTotalMaps(int totalMaps) {
        this.totalMaps = totalMaps;
        return this;
    }

    public MRJobEntity setTotalReduces(int totalReduces) {
        this.totalReduces = totalReduces;
        return this;
    }

    public MRJobEntity setFailedMaps(int failedMaps) {
        this.failedMaps = failedMaps;
        return this;
    }

    public MRJobEntity setFailedReduces(int failedReduces) {
        this.failedReduces = failedReduces;
        return this;
    }

    public MRJobEntity setFinishMaps(int finishMaps) {
        this.finishMaps = finishMaps;
        return this;
    }

    public MRJobEntity setFinishReduces(int finishReduces) {
        this.finishReduces = finishReduces;
        return this;
    }

    public MRJobEntity setJobStatus(String jobStatus) {
        this.jobStatus = jobStatus;
        return this;
    }

    public MRJobEntity setCounters(JobCounters counters) {
        this.counters = counters;
        return this;
    }

    public MRJobEntity setConfs(MRJobConfig confs) {
        this.confs = confs;
        return this;
    }

    public long getSubmitTime() {
        return submitTime;
    }

    public long getLaunchTime() {
        return launchTime;
    }

    public long getFinishTime() {
        return finishTime;
    }

    public String getJobId() {
        return jobId;
    }

    public String getJobName() {
        return jobName;
    }

    public String getQueueName() {
        return queueName;
    }

    public String getUserName() {
        return userName;
    }

    public int getTotalMaps() {
        return totalMaps;
    }

    public int getTotalReduces() {
        return totalReduces;
    }

    public int getFailedMaps() {
        return failedMaps;
    }

    public int getFailedReduces() {
        return failedReduces;
    }

    public int getFinishMaps() {
        return finishMaps;
    }

    public int getFinishReduces() {
        return finishReduces;
    }

    public String getJobStatus() {
        return jobStatus;
    }

    public JobCounters getCounters() {
        return counters;
    }

    public String getAmHost() {
        return amHost;
    }

    public MRJobConfig getConfs() {
        return confs;
    }

    public long getMapMemoryWaste() {
        return mapMemoryWaste;
    }

    public long getReduceMemoryWaste() {
        return reduceMemoryWaste;
    }

    public String getJobNormalizedName() {
        return jobNormalizedName;
    }

    public List<String> getInputFolder() {
        return inputFolder;
    }

    public long getMapHCU() {
        return mapHCU;
    }

    public long getReduceHCU() {
        return reduceHCU;
    }

    public MRJobEntity setInputFolder(List<String> inputFolder) {
        this.inputFolder = inputFolder;
        return this;
    }

    public List<String> getOutputFolder() {
        return outputFolder;
    }

    public MRJobEntity setOutputFolder(List<String> outputFolder) {
        this.outputFolder = outputFolder;
        return this;
    }

    public String getJobType() {
        return jobType;
    }

    public MRJobEntity setJobType(String jobType) {
        this.jobType = jobType;
        return this;
    }


    public String getJobExecutionId() {
        return jobExecutionId;
    }

    public String getDiagnosis() {
        return diagnosis;
    }

    public MRJobEntity setMapHCU(long mapHCU) {
        this.mapHCU = mapHCU;
        return this;
    }

    public MRJobEntity setReduceHCU(long reduceHCU) {
        this.reduceHCU = reduceHCU;
        return this;
    }

    public long getTotalHCU() {
        return totalHCU;
    }

    public MRJobEntity setTotalHCU(long totalHCU) {
        this.totalHCU = totalHCU;
        return this;
    }

    public MRJobEntity setMapMemoryWaste(long mapMemoryWaste) {
        this.mapMemoryWaste = mapMemoryWaste;
        return this;
    }

    public MRJobEntity setReduceMemoryWaste(long reduceMemoryWaste) {
        this.reduceMemoryWaste = reduceMemoryWaste;
        return this;
    }

    public long getTotalMemoryWaste() {
        return totalMemoryWaste;
    }

    public MRJobEntity setTotalMemoryWaste(long totalMemoryWaste) {
        this.totalMemoryWaste = totalMemoryWaste;
        return this;
    }

    public long getWaitTime() {
        return waitTime;
    }

    public MRJobEntity setWaitTime(long waitTime) {
        this.waitTime = waitTime;
        return this;
    }
}
