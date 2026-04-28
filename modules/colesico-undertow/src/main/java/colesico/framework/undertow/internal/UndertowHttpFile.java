package colesico.framework.undertow.internal;

import colesico.framework.http.HttpFile;
import io.undertow.server.handlers.form.FormData;
import io.undertow.util.HeaderValues;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;

public class UndertowHttpFile implements HttpFile {

    private final FormData.FormValue value;

    public UndertowHttpFile(FormData.FormValue value) {
        this.value = value;
    }

    protected Path filePath() {
        return value.getFileItem().getFile();
    }

    @Override
    public void release() {
        try {
            boolean res = filePath().toFile().delete();
            if (!res) {
                throw new RuntimeException("Cant's release uploaded file");
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String fileName() {
        return value.getFileName();
    }

    @Override
    public String contentType() {
        HeaderValues hv = value.getHeaders().get("Content-Type");
        String contentType = hv != null ? hv.getFirst() : "";
        return contentType;
    }

    @Override
    public InputStream inputStream() {
        try {
            return new FileInputStream(filePath().toFile());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
