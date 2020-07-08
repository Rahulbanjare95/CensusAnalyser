package censusanalyser.service;
import censusanalyser.adapters.CensusAdapterFactory;
import censusanalyser.exceptions.CensusAnalyserException;
import censusanalyser.model.CensusDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;

import static java.util.stream.Collectors.toCollection;

public class CensusAnalyser {

    public enum Country{ INDIA, US };
    private Country country;

    Map<String, CensusDAO> censusMap = new HashMap<>();
    public CensusAnalyser(Country country){
        this.country = country;
    }

    /**
     * loadCensusData loads the required csv files and returns a map
     * @param country Takes the enum constant
     * @param csvFilePath Takes the path of csv file
     * @return censusMap size
     * @throws CensusAnalyserException while loading csv file
     */
    public int loadCensusData(Country country, String ... csvFilePath) throws CensusAnalyserException {
        censusMap = CensusAdapterFactory.getCensusData(country,csvFilePath);
        return  censusMap.size();
    }

    /**
     * compares the information about state in IndiaCensusCSV file
     *@return sorted state  data in json format
     * @throws CensusAnalyserException custom exception when no data is given
     * @throws IOException when error is encountered while creating json
     */
    public String getStateWiseSortedCensusData() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> censusComparator = Comparator.comparing(census -> census.state);
        ArrayList censusDTOS = censusMap.values().stream()
                                            .sorted(censusComparator)
                                            .map(censusDAO -> censusDAO.getCensusDTO(country))
                                            .collect(toCollection(ArrayList::new));
        String sortedStateCensusJson = new Gson().toJson(censusDTOS);
        try {
            writerJson(censusDTOS,"stateWiseIndiaSorted.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sortedStateCensusJson;
    }

    public String getStateWiseSortedCensusDataOnPopulation() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        ArrayList censusDTOS = censusMap.values().stream().sorted((census1, census2) ->census2.population - census1.population)
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(toCollection(ArrayList::new));
        String sortedPopulation = new Gson().toJson(censusDTOS);
        return sortedPopulation;
    }

    public String getStateWiseSortedCensusDataOnPopulationDensity() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        ArrayList censusDTOS = censusMap.values().stream().sorted((census1, census2) ->census2.densityPerSqKm - census1.densityPerSqKm)
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(toCollection(ArrayList::new));
        String sortedPopulationDensity = new Gson().toJson(censusDTOS);
        try {
            writerJson(censusDTOS,"densityIndiaPopulation.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sortedPopulationDensity;
    }

    public String getStateWiseSortedCensusDataOnArea() throws CensusAnalyserException, IOException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        ArrayList censusDTOS = censusMap.values().stream().sorted((census1, census2) ->census2.areaInSqKm - census1.areaInSqKm)
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(toCollection(ArrayList::new));
        String sortedPopulationDensity = new Gson().toJson(censusDTOS);
        writerJson(censusDTOS,"IndiaCensusSortedAreaList.json");
        return sortedPopulationDensity;
    }

    /**
     *  Method writee a json file when a list is given
     * @param listTowriteJson contains list for which file has to be written
     * @param filePath path where file is to be written
     * @throws IOException to handle error while creating json file
     */
    private void writerJson(List listTowriteJson, String filePath) throws IOException {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        FileWriter fileWriter = new FileWriter(filePath);
        gson.toJson(listTowriteJson, fileWriter);
        fileWriter.close();
    }

    /**
     * Sorts US census data on basis of fields specified
     * @return sorted json file
     * @throws CensusAnalyserException
     * @throws IOException
     */
    public String getPopulationWiseSortedUSCensusData() throws CensusAnalyserException, IOException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        ArrayList censusDTOS = censusMap.values().stream().sorted((census1, census2) ->census2.population - census1.population)
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(toCollection(ArrayList::new));
        String sortedPopulation = new Gson().toJson(censusDTOS);
        writerJson(censusDTOS,"USSortedPopultion.json");
        return sortedPopulation;
    }

    public String getPopulationDensityWiseSortedUSCensusData() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        ArrayList censusDTOS = censusMap.values().stream().sorted((census1, census2) -> (int) (census2.populationDensity - census1.populationDensity))
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(toCollection(ArrayList::new));
        String sortedPopulation = new Gson().toJson(censusDTOS);
        try {
            writerJson(censusDTOS,"USSortedPopulationDensity.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sortedPopulation;
    }

    public String getHousingUnitWiseSortedUSCensusData() throws CensusAnalyserException, IOException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        ArrayList censusDTOS = censusMap.values().stream().sorted((census1, census2) -> (int) (census2.housingUnits - census1.housingUnits))
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(toCollection(ArrayList::new));
        String sortedPopulation = new Gson().toJson(censusDTOS);
        writerJson(censusDTOS,"HousingUnitWiseSortedUS.json");
        return sortedPopulation;
    }

    public String getTotalAreaWiseSortedUSCensusData() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        ArrayList censusDTOS = censusMap.values().stream().sorted((census1, census2) -> (int) (census2.totalArea - census1.totalArea))
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(toCollection(ArrayList::new));
        String sortedPopulation = new Gson().toJson(censusDTOS);
        return sortedPopulation;
    }

    public String getWaterAreaWiseSortedUSCensusData() throws CensusAnalyserException, IOException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        ArrayList censusDTOS = censusMap.values().stream().sorted((census1, census2) -> (int) (census2.waterArea - census1.waterArea))
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(toCollection(ArrayList::new));
        String sortedPopulation = new Gson().toJson(censusDTOS);
        return sortedPopulation;
    }

    public String getHousingDensityWiseSortedUSCensusData() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        ArrayList censusDTOS = censusMap.values().stream().sorted((census1, census2) -> (int) (census2.housingDensity - census1.housingDensity))
                .map(censusDAO -> censusDAO.getCensusDTO(country))
                .collect(toCollection(ArrayList::new));
        String sortedPopulation = new Gson().toJson(censusDTOS);
        return sortedPopulation;
    }
}