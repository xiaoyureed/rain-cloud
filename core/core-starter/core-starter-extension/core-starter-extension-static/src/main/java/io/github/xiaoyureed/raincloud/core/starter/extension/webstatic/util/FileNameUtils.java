package io.github.xiaoyureed.raincloud.core.starter.extension.webstatic.util;

import org.apache.commons.io.FilenameUtils;

public final class FileNameUtils {
    private FileNameUtils() {}

    public static String getExtension(String filePath) {
        return FilenameUtils.getExtension(filePath);
    }
}
