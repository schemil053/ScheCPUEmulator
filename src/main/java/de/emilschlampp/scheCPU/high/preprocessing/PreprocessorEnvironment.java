package de.emilschlampp.scheCPU.high.preprocessing;

import de.emilschlampp.scheCPU.util.ThrowingFunction;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class PreprocessorEnvironment {
    private boolean allowFileInclusion = true;
    private boolean fileInclusionWhiteList = false;
    private List<String> fileInclusionWhitelist = new ArrayList<>();

    private ThrowingFunction<String, InputStream, IOException> fileInputStreamCreator = FileInputStream::new; // Support for virtual file systems (like databases or fio-discs)


    public boolean isAllowFileInclusion() {
        return allowFileInclusion;
    }

    public PreprocessorEnvironment setAllowFileInclusion(boolean allowFileInclusion) {
        this.allowFileInclusion = allowFileInclusion;
        return this;
    }

    public boolean isFileInclusionWhiteList() {
        return fileInclusionWhiteList;
    }

    public PreprocessorEnvironment setFileInclusionWhiteList(boolean fileInclusionWhiteList) {
        this.fileInclusionWhiteList = fileInclusionWhiteList;
        return this;
    }

    public List<String> getFileInclusionWhitelist() {
        return fileInclusionWhitelist;
    }

    public PreprocessorEnvironment setFileInclusionWhitelist(List<String> fileInclusionWhitelist) {
        this.fileInclusionWhitelist = fileInclusionWhitelist;
        return this;
    }

    public ThrowingFunction<String, InputStream, IOException> getFileInputStreamCreator() {
        return fileInputStreamCreator;
    }

    public PreprocessorEnvironment setFileInputStreamCreator(ThrowingFunction<String, InputStream, IOException> fileInputStreamCreator) {
        this.fileInputStreamCreator = fileInputStreamCreator;
        return this;
    }
}
