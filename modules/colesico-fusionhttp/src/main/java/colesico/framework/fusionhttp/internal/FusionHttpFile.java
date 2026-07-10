package colesico.framework.fusionhttp.internal;

import colesico.framework.http.HttpFile;
import io.fusionauth.http.FileInfo;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

public class FusionHttpFile implements HttpFile {
    private final FileInfo fileInfo;

    public FusionHttpFile(FileInfo fileInfo) {
        this.fileInfo = fileInfo;
    }

    @Override
    public void release() {

    }

    @Override
    public String fileName() {
        return fileInfo.getFileName();
    }

    @Override
    public String contentType() {
        return fileInfo.getContentType();
    }

    @Override
    public InputStream inputStream() {
        try {
            return Files.newInputStream(fileInfo.getFile());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
