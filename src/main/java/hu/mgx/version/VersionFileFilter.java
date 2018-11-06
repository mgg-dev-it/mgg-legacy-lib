package hu.mgx.version;

import java.io.*;
import java.util.regex.*;

import hu.mgx.util.*;

public class VersionFileFilter implements FileFilter
{

    private String sRegExp = "";

    public VersionFileFilter(String sRegExp)
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

    public String getDescription()
    {
        return ("Version loader files");
    }
}
