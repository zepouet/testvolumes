package org.zepouet.strategies;

import java.io.IOException;

public interface CopyOption {
    public void copy(String source, String target) throws IOException;
}
