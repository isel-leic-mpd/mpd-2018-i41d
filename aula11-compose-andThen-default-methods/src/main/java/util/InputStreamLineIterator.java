package util;

import java.io.*;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.function.Supplier;

public class InputStreamLineIterator implements Iterator<String> {
    private Supplier<InputStream> src;
    private BufferedReader srcReader;
    private String line;
    private boolean hasNext;

    public InputStreamLineIterator(Supplier<InputStream> in) {
        this.src = in;
        // reader = new BufferedReader(new InputStreamReader(src.get()));
    }
    private BufferedReader reader() {
        if(srcReader == null)
            srcReader = new BufferedReader(new InputStreamReader(src.get()));
        return srcReader;
    }

    @Override
    public boolean hasNext() {
        if (!hasNext) {
            try {
                line = reader().readLine();
                if (line != null) {
                    hasNext = true;
                } else {
                    reader().close();
                }
            } catch (IOException e) {
                throw new UncheckedIOException(e);
            }
        }
        return hasNext;
    }

    @Override
    public String next() {
        if (!hasNext) throw new NoSuchElementException();
        hasNext = false;
        return line;
    }
}
