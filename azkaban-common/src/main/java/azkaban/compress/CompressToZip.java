package azkaban.compress;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class CompressToZip {
    FileOutputStream target = null;
    BufferedOutputStream bot = null;
    ZipOutputStream zout = null;

    public void createZip(File archiveFile) {
        try {
            target = new FileOutputStream(archiveFile);
            bot = new BufferedOutputStream(target);
            zout = new ZipOutputStream(bot);

            zout = new ZipOutputStream(target);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public void createZipEntry(String entryName, String lines) {
        try {
            zout.putNextEntry(new ZipEntry(entryName));

            zout.write(lines.getBytes(Charset.forName("UTF-8")));

            zout.closeEntry();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void close() {
        try {
            zout.close();
            zout = null;
            bot.close();
            bot = null;
            target.close();
            target = null;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (zout != null) {
                    zout.close();
                    zout = null;
                }
                if (bot != null) {
                    bot.close();
                    bot = null;
                }
                if (target != null) {
                    target.close();
                    target = null;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

