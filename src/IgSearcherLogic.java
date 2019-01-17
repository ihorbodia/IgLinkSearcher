import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IgSearcherLogic {

    Path inputFilePath;
    File inputFile;

    public IgSearcherLogic() {

    }

    public void Run(){
    }

    public void Stop () {

    }

    public void restoreProperties() {

    }

    public void setInputFilePath(String path) {
        if (!StringUtils.isEmpty(path)) {
            inputFilePath = Paths.get(path);
        }
    }
}
