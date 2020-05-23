package org.madlabs.osUtils;

import java.util.List;
/**
 * print CPU Utilization (1-(idleTime - prev_idleTime)/(totalTime-prev_totalTime))*100"%"
 * */
public class SystemInfoSnapshot {
    private List<ProcessSnapshot> processSnapshots;
    private Long totalTime;
    private Long idleTime;

    public SystemInfoSnapshot(List<ProcessSnapshot> processSnapshots, long totalTime, long idleTime) {
        this.processSnapshots = processSnapshots;
        this.totalTime = totalTime;
        this.idleTime = idleTime;
    }
}
