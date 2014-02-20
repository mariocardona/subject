
import java.io.File;
import java.io.IOException;

import org.testng.Assert;
import org.testng.annotations.Test;
import subject.Subject;

/**
 * Created by mcar on 2/20/14.
 */
public class SubjectTest {
    @Test
    public void validate () throws IOException {
        // path to the csv output by WriteArticleSubjectsToCsv.write()
        String outputCsvFilePath = System.getProperty("user.dir") + "\\subject.csv";

        Subject.main();

        File file = new File(outputCsvFilePath);
        if(!file.exists()){
            Assert.assertTrue(false, "Class did not create output file " + outputCsvFilePath);
        }
    }
}
