package de.emilschlampp.scheCPU.tests.highlang.preprocessing;

import de.emilschlampp.scheCPU.high.preprocessing.HighLangPreprocessor;
import de.emilschlampp.scheCPU.high.preprocessing.PreprocessorEnvironment;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.nio.charset.StandardCharsets;

public class PreProcessTest {
    @Test
    public void testPreprocess() {
        HighLangPreprocessor preprocessor = new HighLangPreprocessor("reserve hi 20\n" +
                "var hi \"Hallo\"\n" +
                "~include print");

        preprocessor.setPreprocessorEnvironment(new PreprocessorEnvironment().setFileInputStreamCreator(in -> {
            if(in.equals("print")) {
                return new ByteArrayInputStream("println hi\nprintln hi2\n~include tt".getBytes(StandardCharsets.UTF_8));
            }
            if(in.equals("tt")) {
                return new ByteArrayInputStream("println tt\n~include testif".getBytes(StandardCharsets.UTF_8));
            }
            if(in.equals("testif")) {
                return new ByteArrayInputStream(("~define def1\n" +
                        "~ifdef def1\n" +
                        "var tt \"ok\"\n" +
                        "~endif\n" +
                        "~ifndef def1\n" +
                        "thisshouldnotbeprinted\n" +
                        "~endif").getBytes(StandardCharsets.UTF_8));
            }

            return null;
        }));

        Assertions.assertEquals(preprocessor.preprocess().getResult(), "reserve hi 20\n" +
                "var hi \"Hallo\"\n" +
                "println hi\n" +
                "println hi2\n" +
                "println tt\n" +
                "var tt \"ok\"");
    }
}
