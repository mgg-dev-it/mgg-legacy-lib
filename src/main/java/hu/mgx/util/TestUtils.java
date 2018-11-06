package hu.mgx.util;

import hu.mgx.app.common.AppInterface;
import hu.mgx.app.common.ErrorHandlerInterface;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Vector;

/**
 * Test utility routines
 *
 * @author MaG
 */
public abstract class TestUtils {

    public TestUtils() {
    }

    public static Vector<String> createWordList(ErrorHandlerInterface ehi, int iShrinkFactor) {
        InputStreamReader isr = null;
        BufferedReader br = null;
        String sLine = null;
        Vector<String> vLines = new Vector<>();

        java.io.InputStream inputStream = TestUtils.class.getResourceAsStream("/hu/mgx/util/resources/wordlist_small.txt");
        isr = new InputStreamReader(inputStream);
        br = new BufferedReader(isr);
        int iSkip = Math.max(iShrinkFactor, 0);
        try {
            while ((sLine = br.readLine()) != null) {
                if (iSkip < 1) {
                    vLines.add(sLine);
                    iSkip = iShrinkFactor;
                }
                --iSkip;
            }

        } catch (IOException ioe) {
            ehi.handleError(ioe);
        }
        return (vLines);
    }

}
