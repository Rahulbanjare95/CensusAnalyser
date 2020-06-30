package censusanalyser.service;

import CsvBuilder.CsvBuilderException;
import censusanalyser.exceptions.CensusAnalyserException;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;

public class OpenCsvBuilder<E> implements ICsvBuilder {

    public Iterator<E> getCSVFileIterator(Reader reader, Class csvClass) throws CsvBuilderException {
        try {
            CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<E>(reader);
            csvToBeanBuilder.withType(csvClass);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            CsvToBean<E> csvToBean = csvToBeanBuilder.build();
            return  csvToBean.iterator();
        } catch (IllegalStateException e) {
            throw new CsvBuilderException(e.getMessage(),
                    CsvBuilderException.ExceptionType.UNABLE_TO_PARSE);
        }
    }
}

