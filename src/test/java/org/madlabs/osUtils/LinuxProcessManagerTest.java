package org.madlabs.osUtils;

import junit.framework.TestCase;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class LinuxProcessManagerTest extends TestCase {
    private static final Logger LOGGER = LogManager.getLogger(LinuxProcessManagerTest.class);
    @Test
    public void testGetProcessList() throws IOException {
        LOGGER.info("Starting Test");
        LinuxProcessManager manager = new LinuxProcessManager(List.of("deepak"));
    }
}