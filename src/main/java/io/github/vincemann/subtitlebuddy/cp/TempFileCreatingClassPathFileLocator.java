package io.github.vincemann.subtitlebuddy.cp;

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

/**
 * Finds files on the classpath.
 * Files inside the running jar are only accessible as a Stream, so they are copied to a temp file.
 *
 */
@Singleton
public class TempFileCreatingClassPathFileLocator implements ClassPathFileLocator {

    @Override
    public ClassPathFile findOnClassPath(String relPath) throws IOException {
        Resource resource = new ClassPathResource(relPath);
        if(!resource.exists()){
            throw new FileNotFoundException("Requested Resource: "+ relPath + " does not exist");
        }

        //  https://stackoverflow.com/questions/14876836/file-inside-jar-is-not-visible-for-spring
        return copyToTempFile(resource);
    }

    public List<ClassPathFile> findAllInDir(String relDirPath) throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver().getResources("classpath*:"+relDirPath);
        List<ClassPathFile> loadedClassPathFiles = new ArrayList<>(resources.length);
        for (int i = 0;i<resources.length;i++){
            loadedClassPathFiles.add(copyToTempFile(resources[i]));
        }
        return loadedClassPathFiles;
    }

    private ClassPathFile copyToTempFile(Resource resource) throws IOException {
        File resourceFile = Files.createTempFile("SubtitleBuddyTempFile",resource.getFilename()).toFile();
        FileUtils.copyInputStreamToFile(resource.getInputStream(), resourceFile);
        return new ClassPathFile(resourceFile,resource.getFilename());
    }
}
