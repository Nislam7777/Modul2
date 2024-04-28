package android.me.b1_si_prak1b_13120220021;

import java.io.File;

public class DataFileGambar {
    private String pathfile;
    private File file;

    public DataFileGambar(String pathfile, File file) {
        this.pathfile = pathfile;
        this.file = file;
    }

    public String getPathfile() {
        return pathfile;
    }

    public void setPathfile(String pathfile) {
        this.pathfile = pathfile;
    }

    public File getFile() {
        return file;
    }

    public void setFile(File file) {
        this.file = file;
    }
}
