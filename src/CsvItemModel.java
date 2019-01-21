import com.opencsv.bean.CsvBindByPosition;

public class CsvItemModel {

    @CsvBindByPosition(position = 0)
    public String CompanyName;

    @CsvBindByPosition(position = 1)
    private String _field1;

    @CsvBindByPosition(position = 2)
    private String _field2;

    @CsvBindByPosition(position = 3)
    public String URL;

    @CsvBindByPosition(position = 4)
    public String Email;

    @CsvBindByPosition(position = 5)
    private String _field5;

    @CsvBindByPosition(position = 6)
    private String _field6;

    @CsvBindByPosition(position = 7)
    private String _field7;

    @CsvBindByPosition(position = 8)
    private String _field8;

    @CsvBindByPosition(position = 9)
    private String _field9;

    @CsvBindByPosition(position = 10)
    public String FoundedInstagram;

    public String getPureName() {
        return URL.substring(URL.indexOf(".") + 1, URL.lastIndexOf("."));
    }

    public String getNormalizedCompanyName() {
        return CompanyName
                .replaceAll("\\s+", "%20");
    }
}
