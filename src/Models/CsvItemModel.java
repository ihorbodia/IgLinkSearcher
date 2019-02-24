package Models;

import com.opencsv.bean.CsvBindByPosition;
import org.apache.commons.lang3.StringUtils;

public class CsvItemModel {

    public CsvItemModel() {
        foundInstagram = "";
    }

    @CsvBindByPosition(position = 0)
    public String companyName;

    @CsvBindByPosition(position = 1)
    private String _field1;

    @CsvBindByPosition(position = 2)
    private String _field2;

    @CsvBindByPosition(position = 3)
    public String URL;

    @CsvBindByPosition(position = 4)
    public String email;

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
    public String foundInstagram;

    @CsvBindByPosition(position = 11)
    public String foundTwitter;

    @CsvBindByPosition(position = 12)
    public String notFound;

    public String getPureName() {
        String result = "";
        if (URL.indexOf(".") > 0 && URL.lastIndexOf(".") > 0 && URL.lastIndexOf(".") != URL.indexOf(".")) {
            result = URL.substring(URL.indexOf(".") + 1, URL.lastIndexOf("."));
        }
        else if (email.indexOf(".") > 0 && email.lastIndexOf(".") > 0 && email.indexOf(".") != email.lastIndexOf(".")) {
            result = email.substring(email.indexOf(".") + 1, email.lastIndexOf("."));
        }
        else if (!StringUtils.isEmpty(URL)){
            result = URL;
        }
        else if (!StringUtils.isEmpty(email)){
            result = email;
        }
        return result;
    }

    public String getPureNameToLower() {
        return getPureName().toLowerCase();
    }


    public String getCompanyName() {
        return companyName;
    }
    public String getCompanyNameToLower() {
        return companyName.toLowerCase();
    }

    public String get_field1() {
        return _field1;
    }

    public String get_field2() {
        return _field2;
    }

    public String getURL() {
        return URL;
    }
    public String getURLToLower() {
        return URL.toLowerCase();
    }

    public String getemail() {
        return email;
    }

    public String get_field5() {
        return _field5;
    }

    public String get_field6() {
        return _field6;
    }

    public String get_field7() {
        return _field7;
    }

    public String get_field8() {
        return _field8;
    }

    public String get_field9() {
        return _field9;
    }

    public String getfoundInstagram(){
        return foundInstagram;
    }

    public String getfoundTwitter(){
        return foundTwitter;
    }

    public String getnotFound(){
        return notFound;
    }
}
