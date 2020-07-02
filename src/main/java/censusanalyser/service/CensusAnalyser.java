package censusanalyser.service;

import CsvBuilder.CsvBuilderException;
import CsvBuilder.CsvBuilderFactory;
import CsvBuilder.ICsvBuilder;
import censusanalyser.exceptions.CensusAnalyserException;
import censusanalyser.model.IndiaCensusCSV;
import censusanalyser.model.IndiaStateCodeCSV;
import censusanalyser.model.USCensusCSV;
import com.google.gson.Gson;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

    List<IndiaCensusCSV> censusCSVList = null;
    List<IndiaStateCodeCSV> stateCSVList = null;
    List<USCensusCSV> usCensusCSVList = null;

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCSVBuilder();
            censusCSVList = csvBuilder.getCSVFileList(reader, IndiaCensusCSV.class);
            return censusCSVList.size();
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
        if (censusCSVList == null || censusCSVList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusCSV> censusComparator = Comparator.comparing(census -> census.state);
        this.sort(censusComparator, censusCSVList);
        String sortedStateCensusJson = new Gson().toJson(censusCSVList);
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
        if (censusCSVList == null || censusCSVList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.population);
        this.sortDescending(censusCSVComparator, censusCSVList);
        String sortedPopulation = new Gson().toJson(censusCSVList);
        return sortedPopulation;
    }

    public String getStateWiseSortedCensusDataOnPopulationDensity() throws CensusAnalyserException {
        if (censusCSVList == null || censusCSVList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<IndiaCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.densityPerSqKm);
        this.sortDescending(censusCSVComparator, censusCSVList);
        String sortedPopulationDensity = new Gson().toJson(censusCSVList);
        return sortedPopulationDensity;
    }

    public String getStateWiseSortedCensusDataOnArea() throws CensusAnalyserException {
        if (censusCSVList == null || censusCSVList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        try {
            Comparator<IndiaCensusCSV> censusCSVComparator = Comparator.comparing(census -> census.areaInSqKm);
            this.sortDescending(censusCSVComparator, censusCSVList);
            this.writerJson(censusCSVList);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sortedPopulationDensity = new Gson().toJson(censusCSVList);
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
}


