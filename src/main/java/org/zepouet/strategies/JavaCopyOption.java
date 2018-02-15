package org.zepouet.strategies;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;

public class JavaCopyOption implements CopyOption {
    @Override
    public void copy(String source, String target) throws IOException {
        FileUtils.copyDirectory(new File(source), new File(target));
    }
}
