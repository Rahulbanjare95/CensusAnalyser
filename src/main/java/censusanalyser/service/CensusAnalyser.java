package censusanalyser.service;
import censusanalyser.exceptions.CensusAnalyserException;
import censusanalyser.model.CensusDAO;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class CensusAnalyser {

    public enum Country{ INDIA, US };
    Map<String, CensusDAO> censusMap = new HashMap<>();

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
        List<CensusDAO> censusDAOList =censusMap.values().stream()
                .collect(Collectors.toList());
        censusDAOList.sort(Comparator.comparing((CensusDAO c) -> c.state));
        String sortedStateCensusJson = new Gson().toJson(censusDAOList);
        try {
            writerJson(censusDAOList,"stateWiseIndiaSorted.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sortedStateCensusJson;
    }

    public String getStateWiseSortedStateCodeData() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<CensusDAO> censusDAOList =censusMap.values().stream()
                .collect(Collectors.toList());
        censusDAOList.sort(Comparator.comparing((CensusDAO c) -> c.stateCode));
        String sortedStateCensusJson = new Gson().toJson(censusDAOList);
        try {
            writerJson(censusDAOList, "statecodeIndia.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sortedStateCensusJson;
    }

    public String getStateWiseSortedCensusDataOnPopulation() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<CensusDAO> censusDAOList =censusMap.values().stream()
                .collect(Collectors.toList());
        censusDAOList.sort((CensusDAO c1 , CensusDAO c2)-> c2.population-c1.population);
        String sortedPopulation = new Gson().toJson(censusDAOList);
        return sortedPopulation;
    }

    public String getStateWiseSortedCensusDataOnPopulationDensity() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<CensusDAO> censusDAOList =censusMap.values().stream()
                .collect(Collectors.toList());
        censusDAOList.sort((CensusDAO c1 ,CensusDAO c2)->c2.densityPerSqKm-c1.densityPerSqKm);
        String sortedPopulationDensity = new Gson().toJson(censusDAOList);
        try {
            writerJson(censusDAOList,"densityIndiaPopulation.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sortedPopulationDensity;
    }

    public String getStateWiseSortedCensusDataOnArea() throws CensusAnalyserException, IOException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<CensusDAO> censusDAOList =censusMap.values().stream()
                .collect(Collectors.toList());
        censusDAOList.sort((CensusDAO c1 ,CensusDAO c2)->c2.areaInSqKm-c1.areaInSqKm);
        String sortedPopulationDensity = new Gson().toJson(censusDAOList);
        writerJson(censusDAOList,"IndiaCensusSortedAreaList.json");
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
        List<CensusDAO> censusDAOList =censusMap.values().stream()
                .collect(Collectors.toList());
           censusDAOList.sort((CensusDAO c1, CensusDAO c2)-> c2.population-c1.population);
            String sortedPopulation = new Gson().toJson(censusDAOList);
            writerJson(censusDAOList,"USSorted.json");
            return sortedPopulation;
    }

    public String getPopulationDensityWiseSortedUSCensusData() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<CensusDAO> censusDAOList =censusMap.values().stream()
                .collect(Collectors.toList());
        censusDAOList.sort((CensusDAO c1 ,CensusDAO c2)-> (int) (c2.populationDensity-c1.populationDensity));
       String sortedPopulation = new Gson().toJson(censusDAOList);
        try {
            writerJson(censusDAOList,"USSortedPopulationDensity.json");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sortedPopulation;
    }

    public String getHousingUnitWiseSortedUSCensusData() throws CensusAnalyserException, IOException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> housingComparator = Comparator.comparing(census -> census.housingUnits);
        List<CensusDAO> censusDAOList =censusMap.values().stream()
                .collect(Collectors.toList());
        censusDAOList.sort((CensusDAO c1 , CensusDAO c2)-> (int) (c2.housingUnits-c1.housingUnits));
        String sortedPopulation = new Gson().toJson(censusDAOList);
        writerJson(censusDAOList,"HousingUnitWiseSortedUS.json");
        return sortedPopulation;
    }

    public String getTotalAreaWiseSortedUSCensusData() throws CensusAnalyserException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        Comparator<CensusDAO> totalAreaUSComparator = Comparator.comparing(census -> census.totalArea);
        List<CensusDAO> censusDAOList =censusMap.values().stream()
                .collect(Collectors.toList());
        censusDAOList.sort((CensusDAO c1 , CensusDAO c2)-> (int) (c2.totalArea-c1.totalArea));
        String sortedPopulation = new Gson().toJson(censusDAOList);
        return sortedPopulation;
    }

    public String getWaterAreaWiseSortedUSCensusData() throws CensusAnalyserException, IOException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<CensusDAO> censusDAOList =censusMap.values().stream()
                .collect(Collectors.toList());
        censusDAOList.sort((CensusDAO c1 , CensusDAO c2)-> (int) (c2.waterArea-c1.waterArea));
        String sortedPopulation = new Gson().toJson(censusDAOList);
        return sortedPopulation;
    }

    public String getHousingDensityWiseSortedUSCensusData() throws CensusAnalyserException, IOException {
        if (censusMap == null || censusMap.size() == 0) {
            throw new CensusAnalyserException("No census data found", CensusAnalyserException.ExceptionType.NO_CENSUS_DATA);
        }
        List<CensusDAO> censusDAOList =censusMap.values().stream()
                .collect(Collectors.toList());
        censusDAOList.sort((CensusDAO c1 , CensusDAO c2)-> (int) (c2.housingDensity-c1.housingDensity));
        String sortedPopulation = new Gson().toJson(censusDAOList);
        writerJson(censusDAOList,"housingDensityUS.json");
        return sortedPopulation;
    }

    /**
     * @param csvFilePath Used options to read multiple files in an array where 0 corresponds to IndiaCensusCSV file
     *                    and 1 corresponds to USCensusCSV file
     * @return most populous state when compared on population density
     * @throws CensusAnalyserException thrown at runtime
     * @throws IOException thrown while loading csv file
     */
    public String getMostPopulousState(String...csvFilePath) throws CensusAnalyserException, IOException {
        this.loadCensusData(Country.INDIA,csvFilePath[0]);
        CensusDAO[] censusIndia = new Gson().fromJson(this.getStateWiseSortedCensusDataOnPopulationDensity(),CensusDAO[].class);
        this.loadCensusData(Country.US,csvFilePath[1]);
        CensusDAO[] censusUS = new Gson().fromJson(this.getHousingDensityWiseSortedUSCensusData(), CensusDAO[].class);
        if (Double.compare(censusIndia[0].densityPerSqKm,censusUS[0].populationDensity)>0)
            return censusIndia[0].state;
        return censusUS[0].state;
    }
}