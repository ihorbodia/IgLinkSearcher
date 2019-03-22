package Utils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import java.io.File;

public class DirUtils {

    public static boolean isFileOk(File file, String extension) {
        if (file != null && !StringUtils.isEmpty(file.getAbsolutePath()) && file.exists() && file.isFile() && FilenameUtils.isExtension(file.getAbsolutePath(), extension)) {
            return true;
        }
        return false;
    }

    public static boolean isDirOk(File dir) {
        if (dir == null || StringUtils.isEmpty(dir.getAbsolutePath()) || !dir.exists() || !dir.isDirectory()) {
            return false;
        }
        return true;
    }
}
