package Models;

public class Topics
{
    private String Text;

    private String Result;

    private String FirstURL;

    private Icon Icon;

    public String getText ()
    {
        return Text;
    }

    public void setText (String Text)
    {
        this.Text = Text;
    }

    public String getResult ()
    {
        return Result;
    }

    public void setResult (String Result)
    {
        this.Result = Result;
    }

    public String getFirstURL ()
    {
        return FirstURL;
    }

    public void setFirstURL (String FirstURL)
    {
        this.FirstURL = FirstURL;
    }

    public Icon getIcon ()
    {
        return Icon;
    }

    public void setIcon (Icon Icon)
    {
        this.Icon = Icon;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [Text = "+Text+", Result = "+Result+", FirstURL = "+FirstURL+", Icon = "+Icon+"]";
    }
}