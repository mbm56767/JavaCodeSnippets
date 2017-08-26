/**
 * Created by rajesh on 26/08/17.
 */

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Enumeration;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


public class ZipFileUploadService {
    public void storeZip(MultipartFile file) throws Exception {
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + file.getOriginalFilename());
            }
            final Path pathTarget = this.rootLocation.resolve(file.getOriginalFilename());
            Files.copy(file.getInputStream(), pathTarget);

            if(file.getOriginalFilename().endsWith(".zip")) {
                try (ZipFile zipFile = new ZipFile(String.valueOf(pathTarget))) {
                    Predicate<ZipEntry> isNotDirectory = ze -> !ze.isDirectory();
//                    Predicate<ZipEntry> isPng = ze -> ze.getName().matches(".*png");
//                    Predicate<ZipEntry> isDocx = ze -> ze.getName().matches(".*docx");
//                    Predicate<ZipEntry> isPdf = ze -> ze.getName().matches(".*pdf");
                    Predicate<ZipEntry> isNotOSFile = ze -> !ze.getName().startsWith("__");
                    //Ref: http://blog.codeleak.pl/2014/06/listing-zip-file-content-java-8.html
                    //InputStream stream = zipFile.getInputStream(zipEntry);
                    zipFile.stream()
                            .filter(isNotOSFile.and(isNotDirectory))
                            .forEach(System.out::println);
                }
            }
            else {
                File fileRef = new File(String.valueOf(pathTarget));
                if(fileRef.isFile()) {
                    logger.info("uploaded file format is regular file:{}", fileRef.getName());
                }
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + file.getOriginalFilename(), e);
        }
        catch (Exception e) {
            logger.error("Error found while saving uploaded file or s3 related exception found, this should not come...Fix it on high priority:", e);
        }
    }
}
