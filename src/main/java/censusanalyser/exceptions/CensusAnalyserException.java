package censusanalyser.exceptions;

import censusanalyser.service.CensusAnalyser;

public class CensusAnalyserException extends Exception {
    public enum ExceptionType {
        CENSUS_FILE_PROBLEM, UNABLE_TO_PARSE, WRONG_DELIMITER_HEADER ;
    }
    public ExceptionType type;
    public CensusAnalyserException(String message, String name){
        super(message);
        this.type = ExceptionType.valueOf(name);
    }
    public CensusAnalyserException(String message, ExceptionType type) {
        super(message);
        this.type = type;
    }
}
