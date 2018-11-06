package hu.mgx.util;

import java.io.*;
import java.util.regex.*;

public class FileFilterUtil implements FileFilter
{

    private String sRegExp = "";

    public FileFilterUtil(String sRegExp)
    {
        init(sRegExp);
    }

    public void init(String sRegExp)
    {
        sRegExp = StringUtils.stringReplace(sRegExp, ".", "\\.");
        sRegExp = StringUtils.stringReplace(sRegExp, "*", ".*");
        sRegExp = StringUtils.stringReplace(sRegExp, "?", ".");
        this.sRegExp = "\\A" + sRegExp + "\\z";
    }

    public boolean accept(File pathname)
    {
        Pattern pattern = Pattern.compile(sRegExp);
        Matcher matcher = pattern.matcher(pathname.getName());
        return (matcher.matches());
    }
}
