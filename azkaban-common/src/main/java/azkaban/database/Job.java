package azkaban.database;

public class Job {
    private String projectName;
    private String jobName;
    private long createTime;
    private long updateTime;
    private String createUser;
    private String updateUser;
    private String type;
    private String command;
    private String dependencies;
    private byte warnLevel;
    private short maxElapse;
    private byte retries;
    private int retriesInterval;
    private String notifyUser;
    private String proxyUser;

    public Job(String projectName, String jobName, long createTime, long updateTime, String createUser, String updateUser,
               String type, String command, String dependencies, byte warnLevel, short maxElapse, byte retries,
               int retriesInterval, String notifyUser, String proxyUser) {
        this.projectName = projectName;
        this.jobName = jobName;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.createUser = createUser;
        this.updateUser = updateUser;
        this.type = type;
        this.command = command;
        this.dependencies = dependencies;
        this.warnLevel = warnLevel;
        this.maxElapse = maxElapse;
        this.retries = retries;
        this.retriesInterval = retriesInterval;
        this.notifyUser = notifyUser;
        this.proxyUser = proxyUser;
    }

    public String getProjectName() {
        return projectName;
    }

    public void setProjectName(String projectName) {
        this.projectName = projectName;
    }

    public String getJobName() {
        return jobName;
    }

    public void setJobName(String jobName) {
        this.jobName = jobName;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(long updateTime) {
        this.updateTime = updateTime;
    }

    public String getCreateUser() {
        return createUser;
    }

    public void setCreateUser(String createUser) {
        this.createUser = createUser;
    }

    public String getUpdateUser() {
        return updateUser;
    }

    public void setUpdateUser(String updateUser) {
        this.updateUser = updateUser;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getDependencies() {
        return dependencies;
    }

    public void setDependencies(String dependencies) {
        this.dependencies = dependencies;
    }

    public byte getWarnLevel() {
        return warnLevel;
    }

    public void setWarnLevel(byte warnLevel) {
        this.warnLevel = warnLevel;
    }

    public short getMaxElapse() {
        return maxElapse;
    }

    public void setMaxElapse(short maxElapse) {
        this.maxElapse = maxElapse;
    }

    public byte getRetries() {
        return retries;
    }

    public void setRetries(byte retries) {
        this.retries = retries;
    }

    public int getRetriesInterval() {
        return retriesInterval;
    }

    public void setRetriesInterval(int retriesInterval) {
        this.retriesInterval = retriesInterval;
    }

    public String getNotifyUser() {
        return notifyUser;
    }

    public void setNotifyUser(String notifyUser) {
        this.notifyUser = notifyUser;
    }

    public String getProxyUser() {
        return proxyUser;
    }

    public void setProxyUser(String proxyUser) {
        this.proxyUser = proxyUser;
    }
}

