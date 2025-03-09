package com.sample.api.core.utils;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

@Slf4j
public class FileUtils {

    private static final String DEFAULT_CLASS_PATH_PREFIX = "your directory on class path";
    private static final Resource[] EMPTY_RESOURCES = new Resource[0];
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private FileUtils() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    public static String[] fetchFileNames(String filePath, String suffix) {
        try {
            Resource[] resources = fetchResources(new String[]{filePath});
            return getFilteredFileNames(resources, suffix);
        } catch (IOException e) {
            log.warn("Error fetching file names: {}", ExceptionUtils.getStackTrace(e));
        }
        return EMPTY_STRING_ARRAY;
    }

    private static String[] getFilteredFileNames(Resource[] resources, String suffix) throws IOException {
        List<String> fileNames = new ArrayList<>();
        for (Resource resource : resources) {
            fileNames.addAll(getFileNamesWithSuffix(resource, suffix));
        }
        return fileNames.toArray(EMPTY_STRING_ARRAY);
    }

    private static List<String> getFileNamesWithSuffix(Resource resource, String suffix) {
        try (Stream<Path> files = Files.list(resource.getFile().toPath())) {
            return files
                    .map(path -> path.getFileName().toString())
                    .filter(fileName -> fileName.endsWith(suffix))
                    .map(fileName -> DEFAULT_CLASS_PATH_PREFIX + fileName)
                    .toList();
        } catch (IOException e) {
            log.warn("Error extracting file names from resource: {}", ExceptionUtils.getStackTrace(e));
        }
        return Collections.emptyList();
    }

    public static Resource[] fetchResources(String[] filePaths) {
        List<Resource> resourceList = new ArrayList<>();
        for (String path : filePaths) {
            try {
                Resource[] resources = fetchResourcesFromPath(path);
                Collections.addAll(resourceList, resources);
            } catch (IOException e) {
                log.debug("Failed to fetch resources for path: {}", path, e);
            }
        }
        return resourceList.toArray(EMPTY_RESOURCES);
    }

    private static Resource[] fetchResourcesFromPath(String filePath) throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver(new DefaultResourceLoader())
                .getResources(filePath);
        return ArrayUtils.isNotEmpty(resources) ? resources : EMPTY_RESOURCES;
    }

    public static Resource fetchResource(String location) {
        return new PathMatchingResourcePatternResolver().getResource(location);
    }
}
