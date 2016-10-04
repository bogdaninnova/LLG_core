package main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ZipUtils {

    public static void saveCodeInHistory() {
        String dir = System.getProperty("user.dir");
        ZipUtils appZip = new ZipUtils();
        appZip.generateFileList(new File(dir + "\\src"), dir + "\\src");
        appZip.zipIt("history\\" + new Date().getTime() + ".zip", dir + "\\src");
    }

    private List<String> fileList = new ArrayList<>();

    private void zipIt(String zipFile, String SOURCE_FOLDER) {
        byte[] buffer = new byte[1024];
        String source;
        FileOutputStream fos;
        ZipOutputStream zos = null;
        try {
            try {
                source = SOURCE_FOLDER.substring(SOURCE_FOLDER.lastIndexOf("\\") + 1, SOURCE_FOLDER.length());
            } catch (Exception e) {
                source = SOURCE_FOLDER;
            }
            fos = new FileOutputStream(zipFile);
            zos = new ZipOutputStream(fos);

            for (String file : this.fileList) {
                ZipEntry ze = new ZipEntry(source + File.separator + file);
                zos.putNextEntry(ze);
                try (FileInputStream in = new FileInputStream(SOURCE_FOLDER + File.separator + file)) {
                    int len;
                    while ((len = in.read(buffer)) > 0)
                        zos.write(buffer, 0, len);
                }
            }
            zos.closeEntry();
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (zos != null) {
                    zos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void generateFileList(File node, String SOURCE_FOLDER) {
        if (node.isFile())
            fileList.add(generateZipEntry(node.toString(), SOURCE_FOLDER));

        if (node.isDirectory()) {
            String[] subNote = node.list();
            for (String filename : subNote)
                generateFileList(new File(node, filename), SOURCE_FOLDER);
        }
    }

    private String generateZipEntry(String file, String SOURCE_FOLDER) {
        return file.substring(SOURCE_FOLDER.length() + 1, file.length());
    }
}