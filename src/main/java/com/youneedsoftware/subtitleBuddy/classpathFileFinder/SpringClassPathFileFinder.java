package com.youneedsoftware.subtitleBuddy.classpathFileFinder;

import com.google.inject.Singleton;
import org.apache.commons.io.FileUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.util.*;

@Singleton
public class SpringClassPathFileFinder implements ClassPathFileFinder {

    @Override
    public LoadedClassPathFile findFileOnClassPath(String relPath) throws IOException {
        Resource resource = new ClassPathResource(relPath);
        if(!resource.exists()){
            throw new FileNotFoundException("Requested Resource: "+ relPath + " does not exist");
        }

        //see:
        //  https://stackoverflow.com/questions/14876836/file-inside-jar-is-not-visible-for-spring
        //todo not a really good solution
        return convertResourceToFile(resource);
    }

    public List<LoadedClassPathFile> findFilesOnClassPathDir(String relDirPath) throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:"+relDirPath);
        List<LoadedClassPathFile> loadedClassPathFiles = new ArrayList<>(resources.length);
        for (int i = 0;i<resources.length;i++){
            loadedClassPathFiles.add(convertResourceToFile(resources[i]));
        }
        return loadedClassPathFiles;
    }

    private LoadedClassPathFile convertResourceToFile(Resource resource) throws IOException {
        File resourceFile = Files.createTempFile("SubtitleDisplayerTempFile",resource.getFilename()).toFile();
        FileUtils.copyInputStreamToFile(resource.getInputStream(), resourceFile);
        return new LoadedClassPathFile(resourceFile,resource.getFilename());
    }
}
