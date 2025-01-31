package de.emilschlampp.scheCPU.high.preprocessing;

import de.emilschlampp.scheCPU.util.ThrowingFunction;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PreprocessorLimitations {
    private boolean allowFileInclusion = true;
    private boolean fileInclusionWhiteList = false;
    private List<String> fileInclusionWhitelist = new ArrayList<>();

    private ThrowingFunction<String, InputStream, IOException> fileInputStreamCreator = FileInputStream::new; // Support for virtual file systems (like databases or fio-discs)
}
