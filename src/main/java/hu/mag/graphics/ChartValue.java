package hu.mag.graphics;

/**
 *
 * @author MaG
 */
public class ChartValue {

    private String sCategory;

    private int iXValue;
    private int iYValue;
    private String sXLabel;
    private String sYLabel;

    public ChartValue(int iXValue, int iYValue) {
        this.sCategory = null;
        this.iXValue = iXValue;
        this.iYValue = iYValue;
        this.sXLabel = null;
        this.sYLabel = null;
    }

    public ChartValue(String sCategory, int iXValue, int iYValue) {
        this.sCategory = sCategory;
        this.iXValue = iXValue;
        this.iYValue = iYValue;
        this.sXLabel = null;
        this.sYLabel = null;
    }

    public ChartValue(String sCategory, int iXValue, int iYValue, String sXLabel) {
        this.sCategory = sCategory;
        this.iXValue = iXValue;
        this.iYValue = iYValue;
        this.sXLabel = sXLabel;
        this.sYLabel = null;
    }

    public ChartValue(int iXValue, int iYValue, String sXLabel, String sYLabel) {
        this.sCategory = null;
        this.iXValue = iXValue;
        this.iYValue = iYValue;
        this.sXLabel = sXLabel;
        this.sYLabel = sYLabel;
    }

    public void setCategory(String sCategory) {
        this.sCategory = sCategory;
    }

    public String getCategory() {
        return sCategory;
    }

    public void setXValue(int iXValue) {
        this.iXValue = iXValue;
    }

    public int getXValue() {
        return (iXValue);
    }

    public void setYValue(int iYValue) {
        this.iYValue = iYValue;
    }

    public int getYValue() {
        return (iYValue);
    }

    public void setXLabel(String sXLabel) {
        this.sXLabel = sXLabel;
    }

    public String getXLabel() {
        return (sXLabel);
    }

    public void setYLabel(String sYLabel) {
        this.sYLabel = sYLabel;
    }

    public String getYLabel() {
        return (sYLabel);
    }
}
