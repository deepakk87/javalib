package org.madlabs.osUtils;

public class ProcessSnapshot {
    private int processId;
    private String cmd;
    /**
     * R  Running
     * S  Sleeping in an interruptible wait
     * D  Waiting in uninterruptible disk sleep
     * Z  Zombie
     * T  Stopped (on a signal) or (before Linux 2.6.33)
     *  trace stopped
     * t  Tracing stop (Linux 2.6.33 onward)
     * X  Dead (from Linux 2.6.0 onward)
     * */
    private char processStatus;
    private int parentProcessId;
    private int processGroupId;
    private int sessionId;
    private long userTime;
    private long systemTime;
    private long cumulativeUserTime;
    private long cumulativeSystemTime;

    public void setProcessId(int processId) {
        this.processId = processId;

    }

    public int getProcessId() {
        return processId;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getCmd() {
        return cmd;
    }

    public void setProcessStatus(char processStatus) {
        this.processStatus = processStatus;
    }

    public char getProcessStatus() {
        return processStatus;
    }

    public void setParentProcessId(int parentProcessId) {
        this.parentProcessId = parentProcessId;
    }

    public int getParentProcessId() {
        return parentProcessId;
    }

    public void setProcessGroupId(int processGroupId) {
        this.processGroupId = processGroupId;
    }

    public int getProcessGroupId() {
        return processGroupId;
    }

    public void setSessionId(int sessionId) {
        this.sessionId = sessionId;
    }

    public int getSessionId() {
        return sessionId;
    }

    @Override
    public String toString() {
        return "ProcessSnapshot{" +
                "processId=" + processId +
                ", cmd='" + cmd + '\'' +
                ", processStatus=" + processStatus +
                ", parentProcessId=" + parentProcessId +
                ", processGroupId=" + processGroupId +
                ", sessionId=" + sessionId +
                '}';
    }

    public void setUserTime(long userTime) {
        this.userTime = userTime;
    }

    public long getUserTime() {
        return userTime;
    }

    public void setSystemTime(long systemTime) {
        this.systemTime = systemTime;
    }

    public long getSystemTime() {
        return systemTime;
    }

    public void setCumulativeUserTime(long cumulativeUserTime) {
        this.cumulativeUserTime = cumulativeUserTime;
    }

    public long getCumulativeUserTime() {
        return cumulativeUserTime;
    }

    public void setCumulativeSystemTime(long cumulativeSystemTime) {
        this.cumulativeSystemTime = cumulativeSystemTime;
    }

    public long getCumulativeSystemTime() {
        return cumulativeSystemTime;
    }
}
