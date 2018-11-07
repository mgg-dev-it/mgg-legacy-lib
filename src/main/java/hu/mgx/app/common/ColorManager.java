package hu.mgx.app.common;

import java.awt.Color;

public class ColorManager
{

    public ColorManager()
    {
    }

    public static Color getBackgroundColor()
    {
        return (new Color(224, 241, 255));
    }

//    public static Color getfocusGainedColor(){
//        return(Color.YELLOW);
//    }
//    
//    public static Color getfocusLostColor(){
//        return(Color.WHITE);
//    }
//
//    public static Color getDisabledBackgroundColor(){
//        return(new Color(208, 208, 208));
//    }
    public static Color inputBackgroundFocusGained()
    {
        //return(Color.YELLOW);
        //return (new Color(255, 255, 128));
        return (new Color(255, 255, 192));
    }

    public static Color inputBackgroundFocusLost()
    {
        return (Color.WHITE);
    }

    public static Color inputBackgroundDisabled()
    {
        //return (new Color(208, 208, 208));
        return (new Color(238, 238, 238));
    }

    public static Color inputBackgroundWarning()
    {
        //return(new Color(255, 192, 128));
        //return(new Color(255, 192, 0));
        //return(new Color(255, 128, 0));
        //return(new Color(255, 160, 0));
        //return(new Color(255, 255, 144));
        return (new Color(255, 208, 0)); //~világos narancs
    //return(new Color(255, 208, 64));
    //return(new Color(255, 160, 64));
    }

    public static Color inputBackgroundService()
    {
        //return(new Color(255, 255, 144));
        //return(new Color(255, 192, 128));
        //return(new Color(255, 192, 0));
        return (new Color(255, 255, 0));
    //return(Color.RED);
    //return(new Color(255, 160, 0));
    }

    public static Color inputForegroundNormal()
    {
        return (Color.BLACK);
    }

    public static Color inputForegroundChanged()
    {
        return (Color.BLUE);
        //return (Color.RED);
    }
}
