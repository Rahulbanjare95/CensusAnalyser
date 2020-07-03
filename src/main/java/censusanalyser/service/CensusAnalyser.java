package censusanalyser.service;

import CsvBuilder.ICsvBuilder;
import CsvBuilder.CsvBuilderException;
import CsvBuilder.CsvBuilderFactory;
import censusanalyser.exceptions.CensusAnalyserException;
import censusanalyser.model.IndiaCensusCSV;
import censusanalyser.model.CensusDAO;
import censusanalyser.model.IndiaStateCodeCSV;
import censusanalyser.model.USCensusCSV;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    List<IndiaCensusCSV> censusCSVList = null;
    List<IndiaStateCodeCSV> stateCSVList = null;
    List<USCensusCSV> usCensusCSVList = null;
    List<CensusDAO> censusDAOList = null;

    public CensusAnalyser(){
        this.censusDAOList = new ArrayList<>();
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCSVBuilder();
            censusDAOList = csvBuilder.getCSVFileList(reader, IndiaCensusCSV.class);
            Iterator<IndiaCensusCSV> csvFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaCensusCSV.class);
            Iterable<IndiaCensusCSV> censusCSVIterable = () -> csvFileIterator;
            StreamSupport.stream(censusCSVIterable.spliterator(), false)
                    .forEach( census -> censusDAOList.add(new CensusDAO(census)));
            int count=0;
            return this.censusDAOList.size();
        } catch (IOException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (RuntimeException e) {
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.WRONG_DELIMITER_HEADER);
        } catch (CsvBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(), e.type.name());
        }
    }

    public int loadIndiaStateData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCSVBuilder();
            stateCSVList = csvBuilder.getCSVFileList(reader, IndiaStateCodeCSV.class);
            return this.stateCSVList.size();
        } catch (IOException e) {
            System.out.println("In I/O Exception");
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM);
        } catch (CsvBuilderException e) {
            throw new CensusAnalyserException(e.getMessage(), e.type.name());
        } catch (RuntimeException e) {
            System.out.println("In runtime");
            throw new CensusAnalyserException(e.getMessage(), CensusAnalyserException.ExceptionType.WRONG_DELIMITER_HEADER);
        }
    }

    private <E> int getCount(Iterator<E> iterator) {
        Iterable<E> csvIterable = () -> iterator;
        int numOfEnteries = (int) StreamSupport.stream(csvIterable.spliterator(), false).count();
        return numOfEnteries;
    }

    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if (censusDAOList == null || censusDAOList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.state);
        this.sort(censusComparator, censusDAOList);
        String sortedStateCensusJson = new Gson().toJson(censusDAOList);
        return sortedStateCensusJson;
    }

    public String getStateWiseSortedStateCodeData() throws CensusAnalyserException {
        if (stateCSVList == null || stateCSVList.size() == 0) {
            throw new CensusAnalyserException("No state data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaStateCodeCSV> stateCodeComparator = Comparator.comparing(state -> state.stateCode);
        this.sort(stateCodeComparator, stateCSVList);
        String sortedStateCensusJson = new Gson().toJson(stateCSVList);
        return sortedStateCensusJson;
    }

    public String getStateWiseSortedCensusDataOnPopulation() throws CensusAnalyserException {
        if (censusDAOList == null || censusDAOList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusCSVComparator = Comparator.comparing(census -> census.population);
        this.sortDescending(censusCSVComparator, censusDAOList);
        String sortedPopulation = new Gson().toJson(censusDAOList);
        return sortedPopulation;
    }

    public String getStateWiseSortedCensusDataOnPopulationDensity() throws CensusAnalyserException {
        if (censusDAOList == null || censusDAOList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusCSVComparator = Comparator.comparing(census -> census.densityPerSqKm);
        this.sortDescending(censusCSVComparator, censusDAOList);
        String sortedPopulationDensity = new Gson().toJson(censusDAOList);
        return sortedPopulationDensity;
    }

    public String getStateWiseSortedCensusDataOnArea() throws CensusAnalyserException {
        if (censusDAOList == null || censusDAOList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        try {
            Comparator<CensusDAO> censusCSVComparator = Comparator.comparing(census -> census.areaInSqKm);
            this.sortDescending(censusCSVComparator, censusDAOList);
            this.writerJson((List) censusCSVComparator);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sortedPopulationDensity = new Gson().toJson(censusDAOList);
        return sortedPopulationDensity;
    }

    private <E> void sort(Comparator<E> censusComparator, List<E> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1; j++) {
                E census1 = (E) list.get(j);
                E census2 = (E) list.get(j + 1);
                if (censusComparator.compare(census1, census2) > 0) {
                    list.set(j, census2);
                    list.set(j + 1, census1);
                }
            }
        }
    }

    private <E> void sortDescending(Comparator<E> censusComparator, List<E> list) {
        for (int i = 0; i < list.size() - 1; i++) {
            for (int j = 0; j < list.size() - 1; j++) {
                E census1 = (E) list.get(j);
                E census2 = (E) list.get(j + 1);
                if (censusComparator.compare(census1, census2) < 0) {
                    list.set(j, census2);
                    list.set(j + 1, census1);
                }
            }
        }
    }

    private void writerJson(List listTowriteJson) throws IOException {
        Gson gson = new Gson();
        FileWriter fileWriter = new FileWriter("listToJson.json");
        gson.toJson(listTowriteJson, fileWriter);
        fileWriter.close();
    }

    public int loadUSCensusData(String csvFilePath) throws CensusAnalyserException {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));
            Comparator<USCensusCSV> censusComparator = Comparator.comparing(census -> census.state);
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCSVBuilder();
            usCensusCSVList = csvBuilder.getCSVFileList(reader, USCensusCSV.class);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (CsvBuilderException e) {
            e.printStackTrace();
        }
        return usCensusCSVList.size();
    }


    public String getPopulationWiseSortedUSCensusData() throws CensusAnalyserException {
        if (usCensusCSVList == null || usCensusCSVList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<USCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.population);
        this.sortDescending(censusCSVComparator, usCensusCSVList);
        String sortedPopulation = new Gson().toJson(usCensusCSVList);
        return sortedPopulation;
    }

    public String getPopulationDensityWiseSortedUSCensusData() throws CensusAnalyserException {
        if (usCensusCSVList == null || usCensusCSVList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<USCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.populationDensity);
        this.sortDescending(censusCSVComparator, usCensusCSVList);
        String sortedPopulation = new Gson().toJson(usCensusCSVList);
        return sortedPopulation;
    }

    public String getHousingUnitWiseSortedUSCensusData() throws CensusAnalyserException {
        if (usCensusCSVList == null || usCensusCSVList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<USCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.housingUnits);
        this.sortDescending(censusCSVComparator, usCensusCSVList);
        String sortedPopulation = new Gson().toJson(usCensusCSVList);
        return sortedPopulation;
    }

    public String getTotalAreaWiseSortedUSCensusData() throws CensusAnalyserException {
        if (usCensusCSVList == null || usCensusCSVList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<USCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.totalArea);
        this.sortDescending(censusCSVComparator, usCensusCSVList);
        String sortedPopulation = new Gson().toJson(usCensusCSVList);
        return sortedPopulation;
    }

    public String getWaterAreaWiseSortedUSCensusData() throws CensusAnalyserException, IOException {
        if (usCensusCSVList == null || usCensusCSVList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<USCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.waterArea);
        this.sortDescending(censusCSVComparator, usCensusCSVList);
        writerJson(usCensusCSVList);
        String sortedPopulation = new Gson().toJson(usCensusCSVList);
        return sortedPopulation;
    }

    public String getHousingDensityWiseSortedUSCensusData() throws CensusAnalyserException {
        if (usCensusCSVList == null || usCensusCSVList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<USCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.housingDensity);
        this.sortDescending(censusCSVComparator, usCensusCSVList);
        String sortedPopulation = new Gson().toJson(usCensusCSVList);
        return sortedPopulation;
    }

}