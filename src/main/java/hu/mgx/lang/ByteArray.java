package hu.mgx.lang;

public class ByteArray
{

    private byte[] b;

    public ByteArray()
    {
        b = new byte[0];
    }

    public ByteArray(byte[] b)
    {
        this.b = b;
    }

    public void setBytes(byte[] b)
    {
        this.b = b;
    }

    public byte[] getBytes()
    {
        return (b);
    }

    public String toString()
    {
        if (b == null)
        {
            return ("");
        //if (b.length > 0) return("[]");
        }
        if (b.length > 0)
        {
            return (new String(b));
        }
        return ("");
    }
}
