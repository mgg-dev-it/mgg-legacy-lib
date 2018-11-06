package hu.mgx.util;

import java.security.*;

public class PwUtils
{

    public PwUtils()
    {
    }

    public static String codePw(String sPw)
    {
        MessageDigest md;
        try
        {
            md = MessageDigest.getInstance("SHA-512");
            md.update(sPw.getBytes());
            byte b[] = md.digest();
            return (ByteUtils.byteArraytoHexString(b));
        }
        catch (NoSuchAlgorithmException nsae)
        {
            System.err.println(nsae.getLocalizedMessage());
            nsae.printStackTrace(System.err);
        }
        return ("");
    }

    public static void main(String[] args)
    {
        System.err.println(codePw("12345678"));
    }
}
