package censusanalyser.adapters;
import CsvBuilder.CsvBuilderException;
import CsvBuilder.CsvBuilderFactory;
import CsvBuilder.ICsvBuilder;
import censusanalyser.exceptions.CensusAnalyserException;
import censusanalyser.model.CensusDAO;
import censusanalyser.model.IndiaCensusCSV;
import censusanalyser.model.IndiaStateCodeCSV;

import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class IndiaCensusAdapter extends CensusAdapter {
    @Override
    public Map<String, CensusDAO> loadCensusData(String... csvFilePath) throws CensusAnalyserException {
        Map<String, CensusDAO> censusMap = super.loadCensusData(IndiaCensusCSV.class,csvFilePath[0]);
        this.loadIndiaStateData(censusMap, csvFilePath[1]);
        return censusMap;
    }
    /**
     * Loads IndiaStateData to accomodate it with the IndiaCensusData
     * @param censusMap for State Codes
     * @param csvFilePath to locate the State Code data
     * @return censusMap with State Codes
     * @throws CensusAnalyserException
     */
    private int loadIndiaStateData(Map<String, CensusDAO> censusMap, String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCSVBuilder();
            Iterator<IndiaStateCodeCSV> csvStateFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable = () -> csvStateFileIterator;
            StreamSupport.stream(csvIterable.spliterator(),false)
                    .filter( csvState -> censusMap.get(csvState.stateName)!=null)
                    .forEach(csvState -> censusMap.get(csvState.stateName).stateCode=csvState.stateCode );
            return censusMap.size();
        } catch (RuntimeException | CsvBuilderException | IOException e) {
            System.out.println("In runtime");
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.WRONG_DELIMITER_HEADER);
        }
    }


}
