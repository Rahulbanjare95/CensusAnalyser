package CsvBuilder;

import censusanalyser.exceptions.CensusAnalyserException;
import javafx.beans.property.adapter.ReadOnlyJavaBeanFloatPropertyBuilder;

public class CsvBuilderException extends Exception {


    public enum ExceptionType {
        CENSUS_FILE_PROBLEM, UNABLE_TO_PARSE, WRONG_DELIMITER_HEADER ;
    }

    public ExceptionType type;

    public CsvBuilderException(String message, ExceptionType type) {

    }

}
