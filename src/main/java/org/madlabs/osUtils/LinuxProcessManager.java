package org.madlabs.osUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * deepak    4867  3.3  1.7 662128 71224 ?        Rl   14:05   0:00 /usr/bin/qterminal
 *  res = ((proctotal - pr_proctotal) / (cputotal - pr_cputotal) * 100)
 * */
public class LinuxProcessManager {
    private static final Logger LOGGER = LogManager.getLogger(LinuxProcessManager.class);
    private static final Path PROC_DIR_PATH = Paths.get("/proc");
    private static final Path PROC_STAT_FILE_PATH = Paths.get("/proc/stat");
    private static final Pattern  PROCESS_ID_PATH_PATTERN = Pattern.compile("/proc/\\d+");
    private static final String STAT_FILE_NAME = "stat";
    private final Set<String> users;

    public LinuxProcessManager(List<String> users) {
        this.users = Set.copyOf(users);
    }
/**
 * 10591 (qterminal) S 1 10590 10590 0 -1 4194304 6976 0 116 0 90 28 0 0 20 0 7 0 1391337
 * 680374272 18647 18446744073709551615 94670542385152 94670542732317 140736560010368 0 0 0 0
 * 4096 65536 0 0 0 17 1 0 0 9 0 0 94670542822712 94670542839824 94670564933632 140736560015910
 * 140736560015929 140736560015929 140736560017381 0
 * */
    public List<ProcessSnapshot> getProcessList() throws IOException {
        List<ProcessSnapshot> processSnapshots = new ArrayList<>();
        try (DirectoryStream<Path> paths = Files.newDirectoryStream(PROC_DIR_PATH, new DirectoryStream.Filter<Path>() {
            @Override
            public boolean accept(Path path) throws IOException {
                return  (users.contains(Files.getOwner(path).getName()) &&
                        PROCESS_ID_PATH_PATTERN.matcher(path.toString()).matches()) ;
            }
        })) {
            for (Path path: paths) {
                ProcessSnapshot snapshot = new ProcessSnapshot();
                try (InputStream fis = Files.newInputStream(path.resolve(STAT_FILE_NAME))) {
                    // http://man7.org/linux/man-pages/man5/proc.5.html
                    // https://stackoverflow.com/questions/1420426/how-to-calculate-the-cpu-usage-of-a-process-by-pid-in-linux-from-c
                    snapshot.setProcessId(readInt(fis));  //1
                    snapshot.setCmd(readCmd(fis)); //2
                    snapshot.setProcessStatus((char)fis.read());  //3
                    fis.read(); //read a whitespace
                    snapshot.setParentProcessId(readInt(fis)); //4
                    snapshot.setProcessGroupId(readInt(fis)); //5
                    snapshot.setSessionId(readInt(fis)); //6
                    readInt(fis); //7
                    readInt(fis); //8
                    readInt(fis); //9
                    readLong(fis); //10
                    readLong(fis); //11
                    readLong(fis); //12
                    readLong(fis); //13
                    //#14 utime - CPU time spent in user code, measured in clock ticks
                    snapshot.setUserTime(readLong(fis)); //14
                    //#15 stime - CPU time spent in kernel code, measured in clock ticks
                    snapshot.setSystemTime(readLong(fis)); //15
                    // #16 cutime - Waited-for children's CPU time spent in user code (in clock ticks)
                    snapshot.setCumulativeUserTime(readLong(fis)); //16
                    // #17 cstime - Waited-for children's CPU time spent in kernel code (in clock ticks)
                    snapshot.setCumulativeSystemTime(readLong(fis)); //17
                    readLong(fis); //18
                    readLong(fis); //19
                    readLong(fis); //20
                    readLong(fis); //21

                    // #22 starttime - Time when the process started, measured in clock ticks
                    long startTime = readLong(fis); //22

                    LOGGER.info(snapshot);
                    processSnapshots.add(snapshot);
                }
            }
        }
        return processSnapshots;
    }

    public SystemInfoSnapshot getSystemInfoSnapshot() throws IOException {
        long totalTime = 0;
        long idleTime;
        try (InputStream fis = Files.newInputStream(PROC_STAT_FILE_PATH)) {
            fis.read();
            fis.read();
            fis.read();
            fis.read();
            fis.read();
            totalTime += readLong(fis);
            totalTime += readLong(fis);
            totalTime += readLong(fis);
            idleTime = readLong(fis);
            totalTime +=idleTime;

            totalTime += readLong(fis);
            totalTime += readLong(fis);
            totalTime += readLong(fis);
            totalTime += readLong(fis);
            totalTime += readLong(fis);
            totalTime += readLong(fis);
        }
        return new SystemInfoSnapshot(totalTime, idleTime);
    }

    private static int readInt(InputStream in) throws IOException {
        int ret = 0;
        boolean dig = false;
        boolean negate = false;
        for (int c = 0; (c = in.read()) != -1; ) {
            if (c >= '0' && c <= '9') {
                dig = true;
                ret = ret * 10 + c - '0';
            } else if (c == '-' && dig == false ){
                negate = true;
            } else if (dig) break;
        }
        return negate ? -ret: ret;
    }

    private static long readLong(InputStream in) throws IOException {
        long ret = 0;
        boolean dig = false;
        boolean negate = false;
        for (int c = 0; (c = in.read()) != -1; ) {
            if (c >= '0' && c <= '9') {
                dig = true;
                ret = ret * 10 + c - '0';
            } else if (c == '-' && dig == false ){
                negate = true;
            } else if (dig) break;
        }
        return negate ? -ret: ret;
    }

    private static String readCmd(InputStream in) throws IOException {
        StringBuilder ret = new StringBuilder();
        boolean dig = false;
        in.read(); //read '('
        for (int c = 0; (c = in.read()) != -1; ) {
            if (!(c == ')' && Character.isWhitespace(in.read()))) {
                dig = true;
                ret.append((char)c);
            } else if (dig) break;
        }
        return ret.toString();
    }
}
