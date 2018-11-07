package hu.mgx.util;

import java.math.*;
import java.security.MessageDigest;
import java.text.*;
import java.util.*;

public abstract class StringUtils {

    private static final SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy/MM/dd");
    private static final SimpleDateFormat sdfTime = new SimpleDateFormat("HH:mm:ss");

    private StringUtils() {
    }

    private static final byte byTab[] = {9};
    private static final byte byCr[] = {13};
    private static final byte byLf[] = {10};
    private static final byte byCrLf[] = {13, 10};
    private static final byte byLfCr[] = {10, 13};
    public static Character chrCr = 13;
    public static Character chrLf = 10;
    public static Character chrNbsp = 160;
    private static final char achrNbsp[] = {chrNbsp};
    public static String sTab = new String(byTab);
    public static String sCr = new String(byCr);
    public static String sLf = new String(byLf);
    public static String sCrLf = new String(byCrLf);
    public static String sLfCr = new String(byLfCr);
    public static String sNbsp = new String(achrNbsp);
    private static String sDigits = new String("0123456789");
    private static String sDigitsPeriodAndSign = new String("0123456789.,-");
    private static String sLowerChars = new String("abcdefghijklmnopqrstuxyvwz");
    private static String sHungarianLowerChars = new String("aábcdeéfghiíjklmnoóöőpqrstuúüűxyvwz");
    private static String sUpperChars = new String("ABCDEFGHIJKLMNOPQRSTUXYVWZ");
    private static String sHungarianUpperChars = new String("AÁBCDEÉFGHIÍJKLMNOÓÖŐPQRSTUÚÜŰmXYVWZ");
    private static String sChars = new String(sLowerChars + sUpperChars);
    private static String sHungarianChars = new String(sHungarianLowerChars + sHungarianUpperChars);

    public static StringBuffer stringReplace(StringBuffer sMiben, String sMit, String sMire) {
        return (new StringBuffer(stringReplace(sMiben.toString(), sMit, sMire, false)));
    }

    public static StringBuffer stringReplace(StringBuffer sMiben, StringBuffer sMit, StringBuffer sMire) {
        return (new StringBuffer(stringReplace(sMiben.toString(), sMit.toString(), sMire.toString(), false)));
    }

    public static String stringReplace(String sMiben, String sMit, String sMire) {
        return (stringReplace(sMiben, sMit, sMire, false));
    }

    public static String stringReplace(String sMiben, char cMit, String sMire) {
        return (stringReplace(sMiben, new StringBuffer().append(cMit).toString(), sMire, false));
    }

    public static String stringReplaceMenu(String sMiben) {
        return (stringReplace(sMiben, "&", "", false));
    }

    public static String stringReplaceXML(String sMiben) {
        return (stringReplace(sMiben, "&", "&amp;", false));
    }

    public static String removeSurroundingQuotationMarks(String sIn) {
        String sTmp = sIn;
        if (sTmp.length() > 0) {
            if (sTmp.startsWith("\"")) {
                sTmp = sTmp.substring(1);
            }
        }
        if (sTmp.length() > 0) {
            if (sTmp.endsWith("\"")) {
                sTmp = sTmp.substring(0, sTmp.length() - 1);
            }
        }
        return (sTmp);
    }

    public static String stringReplace(String sMiben, String sMit, String sMire, boolean bDebug) {
        String sRetVal = "";
        int iNext = -1;

        if (sMiben == null) {
            sMiben = "";
        }
        if (sMit == null) {
            sMit = "";
        }
        if (sMire == null) {
            sMire = "";
        }
        if (sMit.equals("")) {
            return (sMiben); //�res stringet nem cser�l�nk!!!

        }
        if (sMiben.equals("")) {
            return (sMiben); //�res stringben nem cser�l�nk!!!

        }
        iNext = sMiben.indexOf(sMit);
        if (bDebug) {
            System.err.println("stringReplace iNext: " + Integer.toString(iNext));
        }
        while (iNext > -1) {
            sRetVal = sRetVal + sMiben.substring(0, iNext) + sMire;
            if (bDebug) {
                System.err.println("stringReplace sRetVal: " + sRetVal);
            }
            sMiben = sMiben.substring(iNext + sMit.length());
            if (bDebug) {
                System.err.println("stringReplace sMiben: " + sMiben);
            }
            iNext = sMiben.indexOf(sMit);
            if (bDebug) {
                System.err.println("stringReplace iNext: " + Integer.toString(iNext));
            }
        }
        sRetVal = sRetVal + sMiben;
        if (bDebug) {
            System.err.println("stringReplace sRetVal: " + sRetVal);
        }
        return (sRetVal);
    }

    public static String stringReplaceFirst(String sMiben, String sMit, String sMire) {
        return (stringReplaceFirst(sMiben, sMit, sMire, false));
    }

    public static String stringReplaceFirst(String sMiben, String sMit, String sMire, boolean bDebug) {
        String sRetVal = "";
        int iNext = -1;

        if (sMiben == null) {
            sMiben = "";
        }
        if (sMit == null) {
            sMit = "";
        }
        if (sMire == null) {
            sMire = "";
        }
        if (sMit.equals("")) {
            return (sMiben); //�res stringet nem cser�l�nk!!!

        }
        if (sMiben.equals("")) {
            return (sMiben); //�res stringben nem cser�l�nk!!!

        }
        iNext = sMiben.indexOf(sMit);
        if (bDebug) {
            System.err.println("stringReplaceFirst iNext: " + Integer.toString(iNext));
        }
        if (iNext > -1) {
            sRetVal = sRetVal + sMiben.substring(0, iNext) + sMire;
            if (bDebug) {
                System.err.println("stringReplaceFirst sRetVal: " + sRetVal);
            }
            sMiben = sMiben.substring(iNext + sMit.length());
            if (bDebug) {
                System.err.println("stringReplaceFirst sMiben: " + sMiben);
            }
//            iNext = sMiben.indexOf(sMit);
//            if (bDebug)
//            {
//                System.err.println("stringReplaceFirst iNext: " + Integer.toString(iNext));
//            }
        }
        sRetVal = sRetVal + sMiben;
        if (bDebug) {
            System.err.println("stringReplaceFirst sRetVal: " + sRetVal);
        }
        return (sRetVal);
    }

    public static String mid(String sString, int iPosition, int iLength) {
        return (rightPad(sString, iPosition + iLength).substring(iPosition, iPosition + iLength));
    }

    public static String mid(String sString, int iPosition) {
        return (mid(sString, iPosition, sString.length() - iPosition));
    }

    public static String left(String sString, int iLength) {
        if (sString == null) {
            return (sString);
        }
        if (sString.length() < iLength) {
            return (sString);
        }
        return (sString.substring(0, iLength));
    }

    public static String right(String sString, int iLength) {
        if (sString == null) {
            return (sString);
        }
        if (sString.length() < iLength) {
            return (sString);
        }
        return (sString.substring(sString.length() - iLength));
    }

    public static String leftTrimChar(String sString, char cChar) {
        if (sString == null) {
            return (sString);
        }
        while (sString.length() > 0 && sString.charAt(0) == cChar) {
            sString = sString.substring(1, sString.length());
        }
        return (sString);
    }

    public static String rightTrimChar(String sString, char cChar) {
        if (sString == null) {
            return (sString);
        }
        while (sString.length() > 0 && sString.charAt(sString.length() - 1) == cChar) {
            sString = sString.substring(0, sString.length() - 1);
        }
        return (sString);
    }

    public static String leftTrimZero(String sString) {
        return (leftTrimChar(sString, '0'));
    }

    public static String pad(String s, int iLength) {
        return (rightPad(s, iLength));
    }

    public static String rightPad(String s, char c, int iLength) {
        if (s.length() > iLength) {
            return (s.substring(0, iLength));
        }
        if (s.length() == iLength) {
            return (s);
        }
        while (s.length() < iLength) {
            s = s + c;
        }
        return (s);
    }

    public static String rightPad(String s, int iLength) {
        return (rightPad(s, ' ', iLength));
    }

    public static String leftPad(String s, char c, int iLength) {
        if (s.length() > iLength) {
            return (s.substring(0, iLength));
        }
        if (s.length() == iLength) {
            return (s);
        }
        while (s.length() < iLength) {
            s = c + s;
        }
        return (s);
    }

    public static String leftPad(String s, int iLength) {
        return (leftPad(s, ' ', iLength));
    }

    public static String spaces(int iLength) {
        return (pad("", iLength));
    }

    public static String repeat(String s, int iCount) {
        String sRetVal = "";
        if (iCount > 0) {
            for (int i = 0; i < iCount; i++) {
                sRetVal += s;
            }
        }
        return (sRetVal);
    }

    public static String getArticle(String s) {
        String sRetVal = "a";
        String sVowels = "a�e�i�o���u���A�E�I�O���U���";
        if (s == null) {
            return ("a");
        }
        if (s.trim().equals("")) {
            return ("a");
        }
        if (sVowels.indexOf(s.charAt(0)) > -1) {
            return ("az");
        }
        return ("a");
    }

    public static String getUpperCaseArticle(String s) {
        String sVowels = "a�e�i�o���u���A�E�I�O���U���";
        if (s == null) {
            return ("A");
        }
        if (s.trim().equals("")) {
            return ("A");
        }
        if (sVowels.indexOf(s.charAt(0)) > -1) {
            return ("Az");
        }
        return ("A");
    }

    public String formatDate(Date d) {
        return (sdfDate.format(d));
    }

    public String formatTime(Date d) {
        return (sdfTime.format(d));
    }

    public static short shortValue(String s) {
        if (s == null) {
            return (0);
        }
        short shortRetVal = 0;
        int i = 0;
        int iBegin;
        int iEnd;
        while ((i < s.length()) && (sDigits.indexOf(s.charAt(i)) < 0) && s.charAt(i) != '-') {
            i++;
        }
        iBegin = i;
        while ((i < s.length()) && ((sDigits.indexOf(s.charAt(i)) > -1) || s.charAt(i) == '-')) {
            i++;
        }
        iEnd = i;
        String sNumber = s.substring(iBegin, iEnd);
        short shortSign = 1;
        if (iBegin > 0) {
            if (s.charAt(iBegin - 1) == '-') {
                shortSign = -1;
            }
        }
        try {
            shortRetVal = (short) (shortSign * Short.parseShort(sNumber));
        } catch (NumberFormatException nfe) {
        }
        return (shortRetVal);
    }

    public static int intValue(String s) {
        if (s == null) {
            return (0);
        }
        int iRetVal = 0;
        int i = 0;
        int iBegin;
        int iEnd;
        while ((i < s.length()) && (sDigits.indexOf(s.charAt(i)) < 0) && s.charAt(i) != '-') {
            i++;
        }
        iBegin = i;
        while ((i < s.length()) && ((sDigits.indexOf(s.charAt(i)) > -1) || s.charAt(i) == '-')) {
            i++;
        }
        iEnd = i;
        String sNumber = s.substring(iBegin, iEnd);
        if (sNumber.length() == 0) {
            return (0);
        }
        int iSign = 1;
        if (iBegin > 0) {
            if (s.charAt(iBegin - 1) == '-') {
                iSign = -1;
            }
        }
        try {
            iRetVal = iSign * Integer.parseInt(sNumber);
        } catch (NumberFormatException nfe) {
        }
        return (iRetVal);
    }

    public static long longValue(String s) {
        if (s == null) {
            return (0);
        }
        long longRetVal = 0;
        int i = 0;
        int iBegin;
        int iEnd;
        while ((i < s.length()) && (sDigits.indexOf(s.charAt(i)) < 0) && s.charAt(i) != '-') {
            i++;
        }
        iBegin = i;
        while ((i < s.length()) && ((sDigits.indexOf(s.charAt(i)) > -1) || s.charAt(i) == '-')) {
            i++;
        }
        iEnd = i;
        String sNumber = s.substring(iBegin, iEnd);
        if (sNumber.length() == 0) {
            return (0);
        }
        long longSign = 1;
        if (iBegin > 0) {
            if (s.charAt(iBegin - 1) == '-') {
                longSign = -1;
            }
        }
        try {
            longRetVal = longSign * Long.parseLong(sNumber);
        } catch (NumberFormatException nfe) {
        }
        return (longRetVal);
    }

    public static double doubleValue(Object o) {
        return (doubleValue(isNull(o, "")));
    }

    public static double doubleValue(String s) {
        if (s == null) {
            return (0);
        }
        double doubleRetVal = 0;
        int i = 0;
        int iBegin;
        int iEnd;
        while ((i < s.length()) && (sDigits.indexOf(s.charAt(i)) < 0) && s.charAt(i) != '-') {
            i++;
        }
        iBegin = i;
        while ((i < s.length()) && ((sDigits.indexOf(s.charAt(i)) > -1) || s.charAt(i) == '-')) {
            i++;
        }
        iEnd = i;
        String sNumber = s.substring(iBegin, iEnd);
        if (sNumber.length() == 0) {
            return (0);
        }
        double doubleSign = 1;
        if (iBegin > 0) {
            if (s.charAt(iBegin - 1) == '-') {
                doubleSign = -1;
            }
        }
        try {
            doubleRetVal = doubleSign * Double.parseDouble(sNumber);
        } catch (NumberFormatException nfe) {
        }
        return (doubleRetVal);
    }

    public static float floatValue(Object o) {
        return (floatValue(isNull(o, "")));
    }

    public static float floatValue(String s) {
        if (s == null) {
            return (0);
        }
        float floatRetVal = 0;
        int i = 0;
        int iBegin;
        int iEnd;
        while ((i < s.length()) && (sDigits.indexOf(s.charAt(i)) < 0) && s.charAt(i) != '-') {
            i++;
        }
        iBegin = i;
        while ((i < s.length()) && ((sDigits.indexOf(s.charAt(i)) > -1) || s.charAt(i) == '-')) {
            i++;
        }
        iEnd = i;
        String sNumber = s.substring(iBegin, iEnd);
        if (sNumber.length() == 0) {
            return (0);
        }
        float floatSign = 1;
        if (iBegin > 0) {
            if (s.charAt(iBegin - 1) == '-') {
                floatSign = -1;
            }
        }
        try {
            floatRetVal = floatSign * Float.parseFloat(sNumber);
        } catch (NumberFormatException nfe) {
        }
        return (floatRetVal);
    }

    public static BigDecimal bigDecimalValue(String s) {
        BigDecimal bd = new BigDecimal(0);
        if (s == null) {
            return (bd);
        }
        int i = 0;
        int iBegin;
        int iEnd;
        while ((i < s.length()) && (sDigitsPeriodAndSign.indexOf(s.charAt(i)) < 0)) {
            i++;
        }
        iBegin = i;
        while ((i < s.length()) && (sDigitsPeriodAndSign.indexOf(s.charAt(i)) > -1)) {
            i++;
        }
        iEnd = i;
        String sNumber = s.substring(iBegin, iEnd);
        try {
            bd = new BigDecimal(sNumber);
        } catch (NumberFormatException nfe) {
        }
        return (bd);
    }

    public static String convertCrLfToSpace(String s) {
        String sRetVal = "";
        sRetVal = stringReplace(s, sCrLf, " ");
        sRetVal = stringReplace(s, sCr, " ");
        sRetVal = stringReplace(s, sLf, " ");
        return (sRetVal);
    }

    public static String digitFilter(String s) {
        String sRetVal = "";
        for (int i = 0; i < s.length(); i++) {
            if (sDigits.indexOf(s.charAt(i)) > -1) {
                sRetVal += s.substring(i, i + 1);
            }
        }
        return (sRetVal);
    }

    public static String digitFilterOut(String s) {
        String sRetVal = "";
        for (int i = 0; i < s.length(); i++) {
            if (sDigits.indexOf(s.charAt(i)) == -1) {
                sRetVal += s.substring(i, i + 1);
            }
        }
        return (sRetVal);
    }

    public static String digitPeriodOrSignFilter(String s) {
        String sRetVal = "";
        for (int i = 0; i < s.length(); i++) {
            if (sDigitsPeriodAndSign.indexOf(s.charAt(i)) > -1) {
                sRetVal += s.substring(i, i + 1);
            }
        }
        return (sRetVal);
    }

    public static boolean isDigitsPeriodOrSign(String s) {
        s = isNull(s, "");
        if (s.length() < 1) {
            return (false);
        }
        for (int i = 0; i < s.length(); i++) {
            if (sDigitsPeriodAndSign.indexOf(s.charAt(i)) == -1) {
                return (false);
            }
        }
        return (true);
    }

    public static boolean isChars(String s) {
        return (isChars(s, ""));
    }

    public static boolean isChars(String s, String sAdditionalCharacters) {
        String sCharacters = sChars + sAdditionalCharacters;
        s = isNull(s, "");
        if (s.length() < 1) {
            return (false);
        }
        for (int i = 0; i < s.length(); i++) {
            //if (sChars.indexOf(s.charAt(i)) == -1) {
            if (sCharacters.indexOf(s.charAt(i)) == -1) {
                return (false);
            }
        }
        return (true);
    }

    public static boolean isHungarianChars(String s) {
        s = isNull(s, "");
        if (s.length() < 1) {
            return (false);
        }
        for (int i = 0; i < s.length(); i++) {
            if (sHungarianChars.indexOf(s.charAt(i)) == -1) {
                return (false);
            }
        }
        return (true);
    }

    public static boolean containsAllowedCharactersOrEmpty(String s, String sAllowedCharacters) {
        s = isNull(s, "");
        if (s.length() < 1) {
            return (true);
        }
        for (int i = 0; i < s.length(); i++) {
            if (sAllowedCharacters.indexOf(s.charAt(i)) == -1) {
                return (false);
            }
        }
        return (true);
    }

    public static boolean isDigits(String s) {
        s = isNull(s, "");
        if (s.length() < 1) {
            return (false);
        }
        for (int i = 0; i < s.length(); i++) {
            if (sDigits.indexOf(s.charAt(i)) == -1) {
                return (false);
            }
        }
        return (true);
    }

    public static boolean isNDigits(String s, int n) {
        s = isNull(s, "");
        if (s.length() != n) {
            return (false);
        }
        for (int i = 0; i < s.length(); i++) {
            if (sDigits.indexOf(s.charAt(i)) == -1) {
                return (false);
            }
        }
        return (true);
    }

    public static String findNDigits(String s, int n) {
        s = isNull(s, "");
        for (int i = 0; i < s.length() - n + 1; i++) {
            if (sDigits.indexOf(s.charAt(i)) > -1) {
                if (isNDigits(s.substring(i, i + n), n)) {
                    if (i == s.length() - n || sDigits.indexOf(s.charAt(i + n)) < 0) { //MaG 2017.10.19.
                        if (i == 0 || sDigits.indexOf(s.charAt(i - 1)) < 0) { //MaG 2017.10.19.
                            return (s.substring(i, i + n));
                        }
                    }
                }
            }
        }
        return ("");
    }

    public static String isNull(String sValue, String sIfNull) {
        if (sValue == null) {
            return (sIfNull);
        }
        return (sValue);
    }

    public static String isNull(Object oValue, String sIfNull) {
        if (oValue == null) {
            return (sIfNull);
        }
        return (oValue.toString());
    }

    public static String isNull(byte[] b, String sIfNull) {
        if (b == null) {
            return (sIfNull);
        }
        return (new String(b));
    }

    public static boolean isEmpty(String sValue) {
        return (StringUtils.isNull(sValue, "").trim().equals(""));
    }

    public static String getBytesStringNull(java.sql.ResultSet rs, String sFieldName, String sNullValue) throws java.sql.SQLException {
        byte[] b;
        b = rs.getBytes(sFieldName);
        return (b != null ? new String(b).trim() : sNullValue);
    }

    public static String getBytesString(java.sql.ResultSet rs, String sFieldName) throws java.sql.SQLException {
        return (getBytesStringNull(rs, sFieldName, ""));
    }

    public static String getFilenameWithoutExtension(String sFilename) {
        String sRetVal = sFilename;
        int iLastIndex = sFilename.lastIndexOf(".");
        if (iLastIndex > 0) {
            sRetVal = sFilename.substring(0, iLastIndex);
        }
        return (sRetVal);
    }

    public static String getExtensionFromFilename(String sFilename) {
        String sRetVal = "";
        int iLastIndex = sFilename.lastIndexOf(".");
        if (iLastIndex > 0) {
            sRetVal = sFilename.substring(iLastIndex + 1);
        }
        return (sRetVal);
    }

    public static String overWrite(String sInput, int iStart, int iLength, String sOver) {
        if (iStart < 0) {
            return (sInput);
        }
        if (iLength < 1) {
            return (sInput);
        }
        int iEnd = iStart + iLength;
        String sRetval = sInput;
        if (iEnd > sRetval.length()) {
            sRetval = rightPad(sRetval, iEnd);
        }
        sRetval = sRetval.substring(0, iStart) + rightPad(sOver, iLength) + sRetval.substring(iStart + iLength, sRetval.length());
        return (sRetval);
    }

    public static String spaceProtection(String sCommand) {
        boolean bInQuote = false;
        boolean bInApostrophe = false;
        String sRetVal = "";

        for (int i = 0; i < sCommand.length(); i++) {
            if (sCommand.charAt(i) == '\"') {
                bInQuote = !bInQuote;
            }
            if (sCommand.charAt(i) == '\'') {
                bInApostrophe = !bInApostrophe;
            }
            if (sCommand.charAt(i) == ' ') {
                if (bInQuote || bInApostrophe) {
                    sRetVal += "<space>";
                } else {
                    sRetVal += sCommand.charAt(i);
                }
            } else {
                sRetVal += sCommand.charAt(i);
            }
        }
        return (sRetVal);
    }

    public static String formatPercentage(double dPercentage) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00%");
        return (decimalFormat.format(dPercentage));
    }

    public static String formatPercentage(double dCurrent, double dTotal) {
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00%");
        return (decimalFormat.format(dCurrent / dTotal));
    }

    public static String formatPercentage(long lCurrent, long lTotal) {
        double dTotal = new Double(lTotal).doubleValue();
        double dCurrent = new Double(lCurrent).doubleValue();
        DecimalFormat decimalFormat = new DecimalFormat("#,##0.00%");
        return (decimalFormat.format(dCurrent / dTotal));
    }

    public static String filterHungarianChars(String s) {
        return (filter(s, sHungarianChars));
//        String sRetVal = "";
//        s = isNull(s, "");
//        if (s.length() < 1) {
//            return ("");
//        }
//        for (int i = 0; i < s.length(); i++) {
//            if (sHungarianChars.indexOf(s.charAt(i)) > -1) {
//                sRetVal += s.charAt(i);
//            }
//        }
//        return (sRetVal);
    }

    public static String filter(String s, String sFilterChars) {
        String sRetVal = "";
        s = isNull(s, "");
        if (s.length() < 1) {
            return ("");
        }
        for (int i = 0; i < s.length(); i++) {
            if (sFilterChars.indexOf(s.charAt(i)) > -1) {
                sRetVal += s.charAt(i);
            }
        }
        return (sRetVal);
    }

    public static String filterOut(String s, String sFilterChars) {
        String sRetVal = "";
        s = isNull(s, "");
        if (s.length() < 1) {
            return ("");
        }
        for (int i = 0; i < s.length(); i++) {
            if (sFilterChars.indexOf(s.charAt(i)) == -1) {
                sRetVal += s.charAt(i);
            }
        }
        return (sRetVal);
    }

    public static String mirror(String s) {
        String sRetVal = "";
        s = isNull(s, "");
        if (s.length() < 1) {
            return ("");
        }
        for (int i = 0; i < s.length(); i++) {
            sRetVal = s.charAt(i) + sRetVal;
        }
        return (sRetVal);
    }

    public static String trimCrLf(String s) {
        String sRetVal = s.trim();
        int iPreviousLength = sRetVal.length();
        int iLength = iPreviousLength + 1;
        while (iLength != iPreviousLength) {
            while (sRetVal.length() > 0 && sRetVal.charAt(0) == chrCr) {
                sRetVal = sRetVal.substring(1, sRetVal.length());
            }
            while (sRetVal.length() > 0 && sRetVal.charAt(0) == chrLf) {
                sRetVal = sRetVal.substring(1, sRetVal.length());
            }
            while (sRetVal.length() > 0 && sRetVal.charAt(sRetVal.length() - 1) == chrCr) {
                sRetVal = sRetVal.substring(0, sRetVal.length() - 1);
            }
            while (sRetVal.length() > 0 && sRetVal.charAt(sRetVal.length() - 1) == chrLf) {
                sRetVal = sRetVal.substring(0, sRetVal.length() - 1);
            }
            sRetVal = sRetVal.trim();
            iLength = sRetVal.length();
        }
        sRetVal = sRetVal.trim();
        return (sRetVal);
    }

    public static String md5(String s) throws java.security.NoSuchAlgorithmException {
        StringBuilder sbMD5 = new StringBuilder();
        MessageDigest messageDigest = MessageDigest.getInstance("MD5");
        byte b[] = messageDigest.digest(s.getBytes());
        for (int i = 0; i < b.length; i++) {
            sbMD5.append(right("00" + Integer.toHexString(b[i] & 0xff), 2));
        }
        return (sbMD5.toString());
    }

    public static String vowel2Index(String s) {
        String sRetVal = "";
        String sVowel = "aeiou";
        for (int i = 0; i < s.length(); i++) {
            if (sVowel.indexOf(s.substring(i, i + 1).toLowerCase()) > -1) {
                sRetVal += Integer.toString(i + 1);
            } else {
                sRetVal += s.substring(i, i + 1);
            }
        }
        return (sRetVal);
    }

    public static String eliminateDoubleSpaces(String s) {
        String sRetVal = s;
        while (sRetVal.contains("  ")) {
            sRetVal = stringReplace(sRetVal, "  ", " ");
        }
        return (sRetVal);
    }

    public static boolean startsWith(String sName, String... sStarts) {
        if (sName == null) {
            return (false);
        }
        for (int i = 0; i < sStarts.length; i++) {
            if (sName.startsWith(sStarts[i])) {
                return (true);
            }
        }
        return (false);
    }

    public static boolean endsWith(String sName, String... sEnds) {
        for (int i = 0; i < sEnds.length; i++) {
            if (sName.endsWith(sEnds[i])) {
                return (true);
            }
        }
        return (false);
    }

    public static boolean notEquals(String sName, String... sValues) {
        for (int i = 0; i < sValues.length; i++) {
            if (sName.equals(sValues[i])) {
                return (false);
            }
        }
        return (true);
    }

    public static String convertFilterStringToRegexString(String sFilter) {
        return (convertFilterStringToRegexString(sFilter, false, true));
    }

    /**
     * Converts user given string into a regexp expression for filtering in
     * tables.
     *
     * @param sFilter the user input. Simple space means 'OR'. ' vagy ', ' or ',
     * ' oder ' means 'OR' too. ' �s ', ' and ', ' und ' means 'AND'.
     * @param bFilterFromTheStart bFilterFromTheStart
     * @param bMultiWord bMultiWord
     * @return the regular expression
     */
    //@todo task : t�bb sz� megad�sa eset�n kital�lni, hogyan lehetne �S kapcsolatba hozni a megadott szavakra keres�st, valamint eld�nteni azt, hogy a keres�st v�gz� �S, vagy VAGY kapcsolatban szeretett volna a szavakra keresni
    public static String convertFilterStringToRegexString(String sFilter, boolean bFilterFromTheStart, boolean bMultiWord) {
        if (!sFilter.contains("[") && !sFilter.contains("]") && !sFilter.contains("|") && !sFilter.contains("&") && !sFilter.contains("\\")) {
//            sFilter = stringReplace(sFilter, " or ", "|");
//            sFilter = stringReplace(sFilter, " OR ", "|");
//            sFilter = stringReplace(sFilter, " vagy ", "|");
//            sFilter = stringReplace(sFilter, " VAGY ", "|");
//            sFilter = stringReplace(sFilter, " oder ", "|");
//            sFilter = stringReplace(sFilter, " ODER ", "|");
//            sFilter = stringReplace(sFilter, " and ", "&");
//            sFilter = stringReplace(sFilter, " AND ", "&");
//            sFilter = stringReplace(sFilter, " �s ", "&");
//            sFilter = stringReplace(sFilter, " �S ", "&");
//            sFilter = stringReplace(sFilter, " und ", "&");
//            sFilter = stringReplace(sFilter, " UND ", "&");
            String sTmp = "";
            for (int i = 0; i < sFilter.length(); i++) {
                if (sFilter.substring(i, i + 1).equalsIgnoreCase(" ")) {
                    //sTmp += "|";
                    if (bMultiWord) {
                        sTmp += "|";
                    } else {
                        sTmp += " ";
                    }
                } else if (sFilter.substring(i, i + 1).equalsIgnoreCase("|")) {
                    sTmp += "|";
//                } else if (sFilter.substring(i, i + 1).equalsIgnoreCase("&")) {
//                    sTmp += "&";
                } else {
                    sTmp += "[" + sFilter.substring(i, i + 1).toLowerCase() + "|" + sFilter.substring(i, i + 1).toUpperCase() + "]";
                }
            }
            sFilter = sTmp;
        }
        if (bFilterFromTheStart) {
            sFilter = "^" + sFilter;
        }
        return (sFilter);
    }

    public static String setCharAt(String sInput, int iPosition, char c) {
        String sTmp = isNull(sInput, "");
        if (sTmp.length() <= iPosition) {
            return (rightPad(sTmp, iPosition) + c);
        }
        return (sTmp.substring(0, iPosition) + c + sTmp.substring(iPosition + 1));
    }

    public static String indent(String sInput, String sIndent, int iLevel) {
        iLevel = Math.max(iLevel, 0);
        String sMultiIndent = repeat(sIndent, iLevel);
        String sRetVal = sMultiIndent + sInput;
        sRetVal = stringReplace(sRetVal, sCrLf, "<CRLF>");
        sRetVal = stringReplace(sRetVal, sLfCr, "<LFCR>");
        sRetVal = stringReplace(sRetVal, sCr, "<CR>");
        sRetVal = stringReplace(sRetVal, sLf, "<LF>");
        sRetVal = stringReplace(sRetVal, "<CRLF>", sCrLf + sMultiIndent);
        sRetVal = stringReplace(sRetVal, "<LFCR>", sLfCr + sMultiIndent);
        sRetVal = stringReplace(sRetVal, "<CR>", sCr + sMultiIndent);
        sRetVal = stringReplace(sRetVal, "<LF>", sLf + sMultiIndent);
        return (sRetVal);
    }

    public static String formatJava(String sInput, String sIndent) {
        String[] a = sInput.split("\r\n");
        StringBuffer sb = new StringBuffer("");
        int iLevel = 0;
        String sTmp;
        for (int i = 0; i < a.length; i++) {
            sTmp = a[i].trim();
            if (sTmp.contains("}")) {
                --iLevel;
            }
            if (sTmp.length() > 0 || iLevel < 2) {
                sb.append(repeat(sIndent, iLevel) + sTmp);
                sb.append(sCrLf);
            }
            if (sTmp.contains("{")) {
                ++iLevel;
            }
        }
        return (sb.toString());
    }

    public static String toUpperCaseFirstChar(String sInput) {
        if (sInput == null) {
            return (sInput);
        }
        if (sInput.length() == 1) {
            return (sInput.toUpperCase());
        }
        return (sInput.substring(0, 1).toUpperCase() + sInput.substring(1));
    }

    public static String getDigits() {
        return (sDigits);
    }

    public static String getChars() {
        return (sChars);
    }

    public static String getDigitsAndChars() {
        return (sDigits + sChars);
    }

    public static String findTheLineWhichContains(String s1, String s2) {
        String[] sArray = s1.split("\r\n");
        for (int i = 0; i < sArray.length; i++) {
//            System.out.println(sArray[i]);
            if (sArray[i].indexOf(s2) > -1) {
                return (sArray[i]);
            }
        }
        return ("");
    }

    public static String[] convertVectorToArray(Vector<String> v) {
        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i < v.size(); i++) {
            a.add(v.elementAt(i));
        }
        return (a.toArray(new String[0]));
    }

    public static boolean existsInArray(String[] sArray, String sElement, boolean bCaseSensitive) {
        for (int i = 0; i < sArray.length; i++) {
            if (bCaseSensitive) {
                if (sArray[i].equals(sElement)) {
                    return (true);
                }
            } else {
                if (sArray[i].equalsIgnoreCase(sElement)) {
                    return (true);
                }
            }
        }
        return (false);
    }

    public static String[] addElementsToArray(String[] sArray, String... sElements) {
        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i < sArray.length; i++) {
            a.add(sArray[i]);
        }
        for (int i = 0; i < sElements.length; i++) {
            a.add(sElements[i]);
        }
        return (a.toArray(new String[0]));
    }

    public static String[] createEmptyArray() {
        return (createArrayFromElements());
    }

    public static String[] createArrayFromElements(String... sElements) {
        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i < sElements.length; i++) {
            a.add(sElements[i]);
        }
        return (a.toArray(new String[0]));
    }

    public static String[] subtractElementsFromArray(String[] sArray, String... sElements) {
        ArrayList<String> a = new ArrayList<>();
        for (int i = 0; i < sArray.length; i++) {
            boolean bFound = false;
            for (int j = 0; j < sElements.length; j++) {
                if (sArray[i].equals(sElements[j])) {
                    bFound = true;
                }
            }
            if (!bFound) {
                a.add(sArray[i]);
            }
        }
        return (a.toArray(new String[0]));
    }

    public static String filterHTML(String s) {
        boolean bInHTML = false;
        StringBuffer sbOut = new StringBuffer("");
        for (int i = 0; i < s.length(); i++) {
//            switch (s.substring(i, i + 1)) {
//                case "<":
//                    bInHTML = true;
//                    break;
//                case ">":
//                    bInHTML = false;
//                    break;
//                default:
//            }
            if (s.charAt(i) == '<') {
                bInHTML = true;
            }
            if (!bInHTML && s.charAt(i) != '<' && s.charAt(i) != '>') {
                sbOut.append(s.charAt(i));
            }
            if (s.charAt(i) == '>') {
                bInHTML = false;
                sbOut.append(" ");
            }
        }
        //return (eliminateDoubleSpaces(sbOut.toString()).trim());
        return (eliminateDoubleSpaces(sbOut.toString().replace("&nbsp;", " ")).trim());
    }

    public static String getDistinctChars(String s) {
        String sRetVal = "";
        if (s == null) {
            s = "";
        }
        for (int i = 0; i < s.length(); i++) {
            if (sRetVal.indexOf(s.charAt(i)) == -1) {
                sRetVal += s.charAt(i);
            }
        }
        return (sRetVal);
    }

    public static String getNumberTextually(int i) {
        return (getNumberTextually(Integer.toString(i)));
    }

    public static String getNumberTextually(BigDecimal bd) {
        return (getNumberTextually(bd.toPlainString()));
    }

    private static String getNumberTextually(String s) {
        String sRetVal = "";
        s = isNull(s, "");
        if (s.length() == 0) {
            return ("");
        }
        if (s.length() > 12) {
            return ("");
        }
        s = leftPad(s, '0', 12);
        String sRetVal9 = getNumberTextually3(s.substring(0, 3), 9); //milli�rdok
        String sRetVal6 = getNumberTextually3(s.substring(3, 6), 6); //milli�k
        String sRetVal3 = getNumberTextually3(s.substring(6, 9), 3); //ezresek
        String sRetVal0 = getNumberTextually3(s.substring(9, 12), 0); //ezer alatt

        sRetVal = sRetVal9;
        if (sRetVal9.length() > 0 && (sRetVal6 + sRetVal3 + sRetVal0).length() > 0) {
            sRetVal += "-";
        }
        sRetVal += sRetVal6;
        if (sRetVal6.length() > 0 && (sRetVal3 + sRetVal0).length() > 0) {
            sRetVal += "-";
        }
        sRetVal += sRetVal3;
        if (sRetVal.equalsIgnoreCase("egyezer")) {
            sRetVal = "ezer";
        } else if (sRetVal3.length() > 0 && sRetVal0.length() > 0) {
            sRetVal += "-";
        }
        sRetVal += sRetVal0;

        return (sRetVal);
    }

    private static String getNumberTextually3(String s, int iExponent) {
        String sRetVal = "";
        s = leftPad(s, '0', 3);

        //sz�zasok
        switch (s.charAt(0)) {
            case '0':
                sRetVal += "";
                break;
            case '1':
                sRetVal += "sz�z"; //egysz�z?
                break;
            case '2':
                sRetVal += "k�tsz�z";
                break;
            case '3':
                sRetVal += "h�romsz�z";
                break;
            case '4':
                sRetVal += "n�gysz�z";
                break;
            case '5':
                sRetVal += "�tsz�z";
                break;
            case '6':
                sRetVal += "hatsz�z";
                break;
            case '7':
                sRetVal += "h�tsz�z";
                break;
            case '8':
                sRetVal += "nyolcsz�z";
                break;
            case '9':
                sRetVal += "kilencsz�z";
                break;
            default:
        }

        //tizesek
        switch (s.charAt(1)) {
            case '0':
                sRetVal += "";
                break;
            case '1':
                if (s.charAt(2) == '0') {
                    sRetVal += "t�z";
                } else {
                    sRetVal += "tizen";
                }
                break;
            case '2':
                if (s.charAt(2) == '0') {
                    sRetVal += "h�sz";
                } else {
                    sRetVal += "huszon";
                }
                break;
            case '3':
                sRetVal += "harminc";
                break;
            case '4':
                sRetVal += "negyven";
                break;
            case '5':
                sRetVal += "�tven";
                break;
            case '6':
                sRetVal += "hatvan";
                break;
            case '7':
                sRetVal += "hetven";
                break;
            case '8':
                sRetVal += "nyolcvan";
                break;
            case '9':
                sRetVal += "kilencven";
                break;
            default:
        }

        //egyesek
        switch (s.charAt(2)) {
            case '0':
                sRetVal += "";
                break;
            case '1':
                sRetVal += "egy";
                break;
            case '2':
                if (iExponent == 0) {
                    sRetVal += "kett�";
                } else {
                    sRetVal += "k�t";
                }
                break;
            case '3':
                sRetVal += "h�rom";
                break;
            case '4':
                sRetVal += "n�gy";
                break;
            case '5':
                sRetVal += "�t";
                break;
            case '6':
                sRetVal += "hat";
                break;
            case '7':
                sRetVal += "h�t";
                break;
            case '8':
                sRetVal += "nyolc";
                break;
            case '9':
                sRetVal += "kilenc";
                break;
            default:
        }

        if (iExponent == 3 && sRetVal.length() > 0) {
            sRetVal += "ezer";
        }
        if (iExponent == 6 && sRetVal.length() > 0) {
            sRetVal += "milli�";
        }
        if (iExponent == 9 && sRetVal.length() > 0) {
            sRetVal += "milli�rd";
        }
        return (sRetVal);
    }

    public static int countOccurence(String s1, String s2) {
        int iCount = 0;
        int iPos = s1.indexOf(s2);
        while (iPos > -1) {
            ++iCount;
            iPos = s1.indexOf(s2, iPos + 1);
        }
        return (iCount);
    }
}
