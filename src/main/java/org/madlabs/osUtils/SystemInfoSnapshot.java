package org.madlabs.osUtils;

/**
 * print CPU Utilization (1-(idleTime - prev_idleTime)/(totalTime-prev_totalTime))*100"%"
 * */
public class SystemInfoSnapshot {
    private Long totalCpuTime;
    private Long idleCpuTime;

    public SystemInfoSnapshot( long totalCpuTime, long idleCpuTime) {
        this.totalCpuTime = totalCpuTime;
        this.idleCpuTime = idleCpuTime;
    }
}
