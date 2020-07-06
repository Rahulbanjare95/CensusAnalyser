package censusanalyser.service;
import CsvBuilder.CsvBuilderException;
import CsvBuilder.CsvBuilderFactory;
import CsvBuilder.ICsvBuilder;
import censusanalyser.exceptions.CensusAnalyserException;
import censusanalyser.model.CensusDAO;
import censusanalyser.model.IndiaCensusCSV;
import censusanalyser.model.IndiaStateCodeCSV;
import censusanalyser.model.USCensusCSV;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.stream.StreamSupport;

public class CensusLoader {

    /**
     * Generalised method to load census data and call the private methods to load
     * @param country to determine for which country the files are loaded
     * @param csvFilePath to give the path of csv data
     * @return loader of given country
     * @throws CensusAnalyserException
     */
    public Map<String , CensusDAO> loadCensusData(CensusAnalyser.Country country, String ... csvFilePath) throws CensusAnalyserException {
         if (country.equals(CensusAnalyser.Country.INDIA))
             return this.loadCensusData(IndiaCensusCSV.class, csvFilePath);
         else if (country.equals(CensusAnalyser.Country.US))
             return  this.loadCensusData(USCensusCSV.class, csvFilePath);
         throw new CensusAnalyserException("Country Not Present", CensusAnalyserException.ExceptionType.NO_SUCH_COUNTRY);
    }

    /**
     * Private method to load the census data according to the country defined in the main method
     * @param csvClass to load classes based on country
     * @param csvFilePath
     * @param <E> to get generalise the classes
     * @return censusMap containing given country details
     * @throws CensusAnalyserException custom exception
     */
    private   <E>  Map<String, CensusDAO> loadCensusData( Class<E> csvClass, String... csvFilePath) throws CensusAnalyserException {
        Map<String, CensusDAO> censusMap = new HashMap<>();
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath[0]));) {
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCSVBuilder();
            Iterator<E> csvFileIterator = csvBuilder.getCSVFileIterator(reader, csvClass);
            Iterable<E> censusCSVIterable = () -> csvFileIterator;
            if (csvClass.getName().equals("censusanalyser.model.IndiaCensusCSV")) {
                StreamSupport.stream(censusCSVIterable.spliterator(), false)
                        .map(IndiaCensusCSV.class::cast)
                        .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            }else if (csvClass.getName().equals("censusanalyser.model.USCensusCSV")){
                StreamSupport.stream(censusCSVIterable.spliterator(), false)
                        .map(USCensusCSV.class::cast)
                        .forEach(censusCSV -> censusMap.put(censusCSV.state, new CensusDAO(censusCSV)));
            }
            if (csvFilePath.length==1) return censusMap;
            this.loadIndiaStateData(censusMap,csvFilePath[1]);
            return censusMap;
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.WRONG_DELIMITER_HEADER);
        } catch (CsvBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(), e.type.name());
        }
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
