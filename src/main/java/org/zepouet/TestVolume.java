package org.zepouet;

import java.io.File;

import org.apache.commons.io.FileUtils;
import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;
import org.zepouet.strategies.CopyOption;
import org.zepouet.strategies.JavaCopyOption;

public class TestVolume implements TestRule {

    private CopyOption copyOption;
    private String pathSnapshot;
    private String pathTarget;

    public TestVolume(String pathSnapshot, String pathTarget) {
        this(pathSnapshot, pathTarget, new JavaCopyOption());
    }

    public TestVolume(String pathSnapshot, String pathTarget, CopyOption copyOption) {
        this.pathSnapshot = pathSnapshot;
        this.pathTarget = pathTarget;
        this.copyOption = copyOption;
    }

    @Override
    public Statement apply(Statement statement, Description description) {
        return new Statement() {
            long start = System.currentTimeMillis();
            @Override
            public void evaluate() throws Throwable {
                try {
                    copyOption.copy(pathSnapshot, pathTarget);
                    statement.evaluate();
                } finally {
                    long end = System.currentTimeMillis();
                    System.out.printf("Timex : %s", (end-start));
                }
            }
        };
    }

}
