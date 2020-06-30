package censusanalyser.service;

import censusanalyser.exceptions.CensusAnalyserException;

import java.io.Reader;
import java.util.Iterator;

public interface ICsvBuilder<E> {
    public Iterator<E> getCSVFileIterator(Reader reader, Class csvClass) throws CensusAnalyserException;
}
