package censusanalyser.model;
import com.opencsv.bean.CsvBindByName;

public class IndiaStateCodeCSV {
    @CsvBindByName(column = "SrNo", required = true)
    public String SrNo;
    @CsvBindByName(column = "State Name", required = true)
    public String StateName;
    @CsvBindByName(column = "TIN", required = true)
    public int TIN;
    @CsvBindByName(column = "StateCode", required = true)
    public String stateCode;

    @Override
    public String toString() {
        return "IndiaStateCodeCSV{" +
                "SrNo='" + SrNo + '\'' +
                ", StateName=" + StateName +
                ", TIN=" + TIN +
                ", StateCode=" + stateCode +
                '}';
    }
}
