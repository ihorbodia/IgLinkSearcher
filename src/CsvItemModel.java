import com.opencsv.bean.CsvBindByPosition;

public class CsvItemModel {

    @CsvBindByPosition(position = 0)
    private String _field0;

    @CsvBindByPosition(position = 1)
    private String _field1;

    @CsvBindByPosition(position = 2)
    private String _field2;

    @CsvBindByPosition(position = 3)
    private String _field3;

    @CsvBindByPosition(position = 4)
    public String link;

    @CsvBindByPosition(position = 5)
    public String email;

    @CsvBindByPosition(position = 6)
    private String pseudoInstagram;

    @CsvBindByPosition(position = 7)
    public String foundInstagram;
}
