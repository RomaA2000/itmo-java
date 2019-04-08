package md2html;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class FileMd2Html extends Md2HtmlSource {
    private Reader reader = null;
    private Writer writer = null;

    public FileMd2Html(final String fileNameIn, final String fileNameOut) {
        try {
            reader = new BufferedReader(new InputStreamReader(new FileInputStream(fileNameIn), StandardCharsets.UTF_8));
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileNameOut), StandardCharsets.UTF_8));
        } catch (final IOException e) {
            System.out.println("Error opening file");
        }
    }

    @Override
    protected void write(String in) {
        try {
            writer.write(in);
            writer.close();
        } catch (IOException e) {
            System.out.println("Error writing in file");
        }
    }

    @Override
    protected char readChar() {
        int read = END;
        try {
            read = reader.read();
        } catch (final IOException e) {
            System.out.println("Error opening file");
        }
        return read == -1 ? END : (char) read;
    }
}
