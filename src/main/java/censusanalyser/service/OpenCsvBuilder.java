package censusanalyser.service;//package CsvBuilder;

import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public class OpenCsvBuilder<E> implements ICsvBuilder{
    @Override
    public Iterator<E> getCSVFileIterator(Reader reader, Class csvClass) throws CsvBuilderException {
        return this.getCsvBean(reader, csvClass).iterator();
    }

    @Override
    public List getCSVFileList(Reader reader, Class csvClass) throws CsvBuilderException {
        return this.getCsvBean(reader, csvClass).parse();
    }

    private CsvToBean<E> getCsvBean(Reader reader, Class csvClass) throws CsvBuilderException {
        try {
            CsvToBeanBuilder<E> csvToBeanBuilder = new CsvToBeanBuilder<E>(reader);
            csvToBeanBuilder.withType(csvClass);
            csvToBeanBuilder.withIgnoreLeadingWhiteSpace(true);
            return csvToBeanBuilder.build();
        }catch (IllegalStateException e){
            throw new CsvBuilderException(e.getMessage(),
                    CsvBuilderException.ExceptionType.UNABLE_TO_PARSE);
        }

    }
}
