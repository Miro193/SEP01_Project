package controller;

import org.junit.jupiter.api.BeforeAll;

public abstract class BaseTest {

    @BeforeAll
    static void initHeadlessMode() {
        // Only trigger if running in Jenkins or a CI environment
        if ("true".equals(System.getenv("CI")) || System.getenv("JENKINS_URL") != null) {
            System.setProperty("java.awt.headless", "true");
            System.setProperty("testfx.robot", "glass");
            System.setProperty("testfx.headless", "true");
            System.setProperty("prism.order", "sw");
            System.setProperty("prism.text", "t2k");
        }
    }
}
