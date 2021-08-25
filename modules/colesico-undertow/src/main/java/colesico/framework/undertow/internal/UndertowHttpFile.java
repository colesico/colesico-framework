package colesico.framework.undertow.internal;

import colesico.framework.http.HttpFile;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;

public class UndertowHttpFile implements HttpFile {

    private final String fileName;
    private final String contentType;
    private final Path filePath;

    public UndertowHttpFile(String fileName, String contentType, Path filePath) {
        this.fileName = fileName;
        this.contentType = contentType;
        this.filePath = filePath;
    }

    @Override
    public void release() {
        try {
            boolean res = filePath.toFile().delete();
            if (!res) {
                throw new RuntimeException("Cant's release uploaded file");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public InputStream getInputStream() {
        try {
            return new FileInputStream(filePath.toFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
