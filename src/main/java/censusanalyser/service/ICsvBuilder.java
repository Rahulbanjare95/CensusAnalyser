package censusanalyser.service;//package CsvBuilder;

import java.io.Reader;
import java.util.Iterator;
import java.util.List;

public interface ICsvBuilder<E> {
    public Iterator<E> getCSVFileIterator(Reader reader, Class csvClass) throws CsvBuilderException;
    public List<E> getCSVFileList(Reader reader, Class csvClass) throws CsvBuilderException;
}
