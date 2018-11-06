package hu.mgx.swing;

import hu.mgx.app.common.*;
import hu.mgx.db.*;

public abstract class CommonSwingUtils
{

    public static CommonTextField createCommonTextField(String sName, String sDisplayName, int iType, int iLength, int iPrecision, int iScale, AppInterface appInterface)
    {
        return (new CommonTextField(new FieldDefinition(sName, sDisplayName, iType, iLength, iPrecision, iScale, false, false, false), appInterface));
    }

    public static CommonLookupField createCommonLookupField(String sName, String sDisplayName, int iType, int iLength, int iPrecision, int iScale, FormatInterface formatInterface)
    {
        return (new CommonLookupField(new FieldDefinition(sName, sDisplayName, iType, iLength, iPrecision, iScale, false, false, false), formatInterface));
    }
}
