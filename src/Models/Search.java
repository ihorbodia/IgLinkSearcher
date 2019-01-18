package Models;

public class Search
{
    private String AbstractText;

    private String Answer;

    private String ImageIsLogo;

    private String Type;

    private String Heading;

    private String Infobox;

    private String DefinitionSource;

    private String AbstractSource;

    private String DefinitionURL;

    private String Redirect;

    private String Image;

    private String AbstractURL;

    private RelatedTopics[] RelatedTopics;

    private String ImageWidth;

    private String[] Results;

    private String AnswerType;

    private String ImageHeight;

    private String Abstract;

    private String Definition;

    private String Entity;

    public String getAbstractText ()
    {
        return AbstractText;
    }

    public void setAbstractText (String AbstractText)
    {
        this.AbstractText = AbstractText;
    }

    public String getAnswer ()
    {
        return Answer;
    }

    public void setAnswer (String Answer)
    {
        this.Answer = Answer;
    }

    public String getImageIsLogo ()
    {
        return ImageIsLogo;
    }

    public void setImageIsLogo (String ImageIsLogo)
    {
        this.ImageIsLogo = ImageIsLogo;
    }

    public String getType ()
    {
        return Type;
    }

    public void setType (String Type)
    {
        this.Type = Type;
    }

    public String getHeading ()
    {
        return Heading;
    }

    public void setHeading (String Heading)
    {
        this.Heading = Heading;
    }

    public String getInfobox ()
    {
        return Infobox;
    }

    public void setInfobox (String Infobox)
    {
        this.Infobox = Infobox;
    }

    public String getDefinitionSource ()
    {
        return DefinitionSource;
    }

    public void setDefinitionSource (String DefinitionSource)
    {
        this.DefinitionSource = DefinitionSource;
    }

    public String getAbstractSource ()
    {
        return AbstractSource;
    }

    public void setAbstractSource (String AbstractSource)
    {
        this.AbstractSource = AbstractSource;
    }

    public String getDefinitionURL ()
    {
        return DefinitionURL;
    }

    public void setDefinitionURL (String DefinitionURL)
    {
        this.DefinitionURL = DefinitionURL;
    }

    public String getRedirect ()
    {
        return Redirect;
    }

    public void setRedirect (String Redirect)
    {
        this.Redirect = Redirect;
    }

    public String getImage ()
    {
        return Image;
    }

    public void setImage (String Image)
    {
        this.Image = Image;
    }

    public String getAbstractURL ()
    {
        return AbstractURL;
    }

    public void setAbstractURL (String AbstractURL)
    {
        this.AbstractURL = AbstractURL;
    }

    public RelatedTopics[] getRelatedTopics ()
    {
        return RelatedTopics;
    }

    public void setRelatedTopics (RelatedTopics[] RelatedTopics)
    {
        this.RelatedTopics = RelatedTopics;
    }

    public String getImageWidth ()
    {
        return ImageWidth;
    }

    public void setImageWidth (String ImageWidth)
    {
        this.ImageWidth = ImageWidth;
    }

    public String[] getResults ()
    {
        return Results;
    }

    public void setResults (String[] Results)
    {
        this.Results = Results;
    }

    public String getAnswerType ()
    {
        return AnswerType;
    }

    public void setAnswerType (String AnswerType)
    {
        this.AnswerType = AnswerType;
    }

    public String getImageHeight ()
    {
        return ImageHeight;
    }

    public void setImageHeight (String ImageHeight)
    {
        this.ImageHeight = ImageHeight;
    }

    public String getAbstract ()
    {
        return Abstract;
    }

    public void setAbstract (String Abstract)
    {
        this.Abstract = Abstract;
    }

    public String getDefinition ()
    {
        return Definition;
    }

    public void setDefinition (String Definition)
    {
        this.Definition = Definition;
    }

    public String getEntity ()
    {
        return Entity;
    }

    public void setEntity (String Entity)
    {
        this.Entity = Entity;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [AbstractText = "+AbstractText+", Answer = "+Answer+", ImageIsLogo = "+ImageIsLogo+", Type = "+Type+", Heading = "+Heading+", Infobox = "+Infobox+", DefinitionSource = "+DefinitionSource+", AbstractSource = "+AbstractSource+", DefinitionURL = "+DefinitionURL+", Redirect = "+Redirect+", Image = "+Image+", AbstractURL = "+AbstractURL+", RelatedTopics = "+RelatedTopics+", ImageWidth = "+ImageWidth+", Results = "+Results+", AnswerType = "+AnswerType+", ImageHeight = "+ImageHeight+", Abstract = "+Abstract+", Definition = "+Definition+", Entity = "+Entity+"]";
    }
}