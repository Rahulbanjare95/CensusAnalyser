package censusanalyser.service;

public class CsvBuilderFactory {
    public  static ICsvBuilder createCSVBuilder() {
       return new OpenCsvBuilder<>();
    }
}
