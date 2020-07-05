package censusanalyser.service;

import CsvBuilder.CsvBuilderException;
import CsvBuilder.CsvBuilderFactory;
import CsvBuilder.ICsvBuilder;
import censusanalyser.exceptions.CensusAnalyserException;
import censusanalyser.model.CensusDAO;
import censusanalyser.model.IndiaCensusCSV;
import censusanalyser.model.IndiaStateCodeCSV;
import censusanalyser.model.USCensusCSV;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.StreamSupport;

public class CensusAnalyser {

//    List<IndiaCensusCSV> censusCSVList = null;
//    List<IndiaStateCodeCSV> stateCSVList = null;
//    List<USCensusCSV> usCensusCSVList = null;
    List<CensusDAO> censusDAOList = null;
    Map<String, CensusDAO> censusMap = null;
  //  Map<String, CensusDAO> stateMap = null;

    public CensusAnalyser() {
        this.censusDAOList = new ArrayList<>();
    }

    public int loadIndiaCensusData(String csvFilePath) throws CensusAnalyserException {
        censusMap = new CensusLoader().loadCensusData(csvFilePath, IndiaCensusCSV.class);
        return  censusMap.size();
    }
    public int loadUSCensusData(String csvFilePath) throws CensusAnalyserException {
        censusMap = new CensusLoader().loadCensusData(csvFilePath, USCensusCSV.class);
        return censusMap.size();
    }

    public int loadIndiaStateData(String csvFilePath) throws CensusAnalyserException {
        try (Reader reader = Files.newBufferedReader(Paths.get(csvFilePath));) {
            ICsvBuilder csvBuilder = CsvBuilderFactory.createCSVBuilder();

            Iterator<IndiaStateCodeCSV> csvStateFileIterator = csvBuilder.getCSVFileIterator(reader, IndiaStateCodeCSV.class);
            Iterable<IndiaStateCodeCSV> csvIterable = () -> csvStateFileIterator;
            StreamSupport.stream(csvIterable.spliterator(),false)
                    .filter( csvState -> censusMap.get(csvState.stateName)!=null)
                    .forEach(csvState -> censusMap.get(csvState.stateName).stateCode=csvState.stateCode );
            return censusMap.size();

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

    public String getStateWiseSortedCensusData() throws CensusAnalyserException, IOException {
        if (censusDAOList == null || censusDAOList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.state);
        this.sort(censusComparator, censusDAOList);
        String sortedStateCensusJson = new Gson().toJson(censusDAOList);
        writerJson((List)censusComparator,"stateWiseSorted.json");
        return sortedStateCensusJson;
    }

    public String getStateWiseSortedStateCodeData() throws CensusAnalyserException {
        if (censusDAOList == null || censusDAOList.size() == 0) {
            throw new CensusAnalyserException("No state data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> stateCodeComparator = Comparator.comparing(state -> state.stateCode);
        this.sort(stateCodeComparator, censusDAOList);
        String sortedStateCensusJson = new Gson().toJson(censusDAOList);
        return sortedStateCensusJson;
    }

    public String getStateWiseSortedCensusDataOnPopulation()  {
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

    public String getStateWiseSortedCensusDataOnArea() throws CensusAnalyserException, IOException {
        if (censusDAOList == null || censusDAOList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusCSVComparator = Comparator.comparing(census -> census.areaInSqKm);
        this.sortDescending(censusCSVComparator, censusDAOList);
        String sortedPopulationDensity = new Gson().toJson(censusDAOList);
//        writerJson(censusDAOList,"IndiaCensusSortedArea.json");
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

    private void writerJson(List listTowriteJson,String filePath) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter fileWriter = new FileWriter(filePath);
        gson.toJson(listTowriteJson, fileWriter);
        fileWriter.close();
    }



    public String getPopulationWiseSortedUSCensusData() throws CensusAnalyserException, IOException {
        if (censusDAOList == null || censusDAOList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
            Comparator<CensusDAO> censusCSVComparator = Comparator.comparing(census -> census.population);
            this.sortDescending(censusCSVComparator, censusDAOList);
            String sortedPopulation = new Gson().toJson(censusDAOList);
            writerJson(censusDAOList,"populationUSSorted.json");
            return sortedPopulation;

    }

    public String getPopulationDensityWiseSortedUSCensusData() throws CensusAnalyserException {
        if (censusDAOList == null || censusDAOList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusCSVComparator = Comparator.comparing(census -> census.populationDensity);
        this.sortDescending(censusCSVComparator, censusDAOList);
        String sortedPopulation = new Gson().toJson(censusDAOList);
        return sortedPopulation;
    }

    public String getHousingUnitWiseSortedUSCensusData() throws CensusAnalyserException, IOException {
        if (censusDAOList == null || censusDAOList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusCSVComparator = Comparator.comparing(census -> census.housingUnits);
        this.sortDescending(censusCSVComparator, censusDAOList);
        String sortedPopulation = new Gson().toJson(censusDAOList);
        writerJson(censusDAOList,"HousingUnitWiseSortedUS.json");
        return sortedPopulation;
    }

    public String getTotalAreaWiseSortedUSCensusData() throws CensusAnalyserException {
        if (censusDAOList == null || censusDAOList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusCSVComparator = Comparator.comparing(census -> census.totalArea);
        this.sortDescending(censusCSVComparator, censusDAOList);
        String sortedPopulation = new Gson().toJson(censusDAOList);
        return sortedPopulation;
    }

    public String getWaterAreaWiseSortedUSCensusData() throws CensusAnalyserException, IOException {
        if (censusDAOList == null || censusDAOList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusCSVComparator = Comparator.comparing(census -> census.waterArea);
        this.sortDescending(censusCSVComparator, censusDAOList);
        String sortedPopulation = new Gson().toJson(censusDAOList);
        return sortedPopulation;
    }

    public String getHousingDensityWiseSortedUSCensusData() throws CensusAnalyserException {
        if (censusDAOList == null || censusDAOList.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusCSVComparator = Comparator.comparing(census -> census.housingDensity);
        this.sortDescending(censusCSVComparator, censusDAOList);
        String sortedPopulation = new Gson().toJson(censusDAOList);
        return sortedPopulation;
    }

}