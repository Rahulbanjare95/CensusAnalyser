package censusanalyser;

import censusanalyser.exceptions.CensusAnalyserException;
import censusanalyser.model.CensusDAO;
import censusanalyser.model.IndiaCensusCSV;
import censusanalyser.model.IndiaStateCodeCSV;
import censusanalyser.model.USCensusCSV;
import censusanalyser.service.CensusAnalyser;
import com.google.gson.Gson;
import org.junit.Assert;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;

public class CensusAnalyserTest {
    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String WRONG_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIA_CENSUS_CSV_FILE_PATH_WRONG_TYPE = "./src/test/resources/IndiaStateCensusData.txt";
    private static final String INDIA_CENSUS_FILE_WRONG_DELIMITER = "./src/test/resources/WrongDelimiterStateCensus.csv";
    private static final String INDIA_CENSUS_FILE_WRONG_HEADER = "./src/test/resources/WrongHeader.csv";
    private static final String INDIA_STATE_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String WRONG_STATE_CSV_FILE_PATH = "./src/main/resources/IndiaStateCensusData.csv";
    private static final String INDIA_STATE_CSV_FILE_PATH_WRONG_TYPE = "./src/test/resources/IndiaStateCode.pdf";
    private static final String INDIA_STATE_CODE_FILE_WRONG_DELIMITER = "./src/test/resources/WrongStateCodeDelimiter.csv";
    private static final String INDIA_STATE_CODE_FILE_WRONG_HEADER = "./src/test/resources/WrongStateCodeHeader.csv";
    private static final String US_CENSUS_DATA_CSV_FILE_PATH = "./src/test/resources/USCensusData.csv";

    @Test
    public void givenIndianCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfRecords);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndiaCensusData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,WRONG_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianCensusWithCorrectFile_ButTypeIncorrect_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH_WRONG_TYPE);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianCensusWith_WrongDelimiter_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_FILE_WRONG_DELIMITER);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.WRONG_DELIMITER_HEADER, e.type);
        }
    }

    @Test
    public void givenIndianCensusWith_WrongHeader_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_FILE_WRONG_HEADER);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.WRONG_DELIMITER_HEADER, e.type);
        }
    }

    @Test
    public void givenIndianStateCSV_ShouldReturnExactCount() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfStateCode = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            Assert.assertEquals(29, numOfStateCode);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenIndiaStateCodeData_WithWrongFile_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,WRONG_STATE_CSV_FILE_PATH);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianStateCodeWithCorrectFile_ButTypeIncorrect_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_STATE_CSV_FILE_PATH_WRONG_TYPE);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.CENSUS_FILE_PROBLEM, e.type);
        }
    }

    @Test
    public void givenIndianStateCodeWith_WrongDelimiter_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_STATE_CODE_FILE_WRONG_DELIMITER);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.WRONG_DELIMITER_HEADER, e.type);
        }
    }

    @Test
    public void givenIndianStateCodeWith_WrongHeader_ShouldThrowException() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            ExpectedException exceptionRule = ExpectedException.none();
            exceptionRule.expect(CensusAnalyserException.class);
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_STATE_CODE_FILE_WRONG_HEADER);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.WRONG_DELIMITER_HEADER, e.type);
        }
    }

    @Test
    public void givenIndianCensusData_WhenSortedOnState_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getStateWiseSortedCensusData();
            IndiaCensusCSV[] indiaCensusDAO = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals("Andhra Pradesh", indiaCensusDAO[0].state);
            Assert.assertEquals("West Bengal", indiaCensusDAO[indiaCensusDAO.length-1].state);
        }
        catch (CensusAnalyserException e)  {

        }
        catch (IOException e){}
    }

    @Test
    public void givenIndianStateData_WhenSorted_OnStateCode_ShouldReturnSortedResult() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA,INDIA_CENSUS_CSV_FILE_PATH,INDIA_STATE_CODE_CSV_FILE_PATH);
            String sortedCodeData = censusAnalyser.getStateWiseSortedStateCodeData();
            IndiaStateCodeCSV[] indiaStateCodeCsv = new Gson().fromJson(sortedCodeData, IndiaStateCodeCSV[].class);
            Assert.assertEquals("AP", indiaStateCodeCsv[0].stateCode);
            Assert.assertEquals("WB", indiaStateCodeCsv[indiaStateCodeCsv.length-1].stateCode);
        } catch (CensusAnalyserException e) {
            Assert.assertEquals(CensusAnalyserException.ExceptionType.NO_CENSUS_DATA, e.type);
        }
    }

    @Test
    public void givenIndianCensusData_whenSorted_OnPopulation_ShouldReturnMostToLeast() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getStateWiseSortedCensusDataOnPopulation();
            IndiaCensusCSV[] indiaCensusCsv = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals(  199812341, indiaCensusCsv[0].population);
            Assert.assertEquals( 607688, indiaCensusCsv[28].population);
        } catch (CensusAnalyserException e) {
            e.getMessage();
        }
    }

    @Test
    public void givenIndianCensusData_whenSorted_OnPopulationDensity_ShouldReturnMostToLeast()  {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getStateWiseSortedCensusDataOnPopulationDensity();
            IndiaCensusCSV[] indiaCensusCsv = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals(1102, indiaCensusCsv[0].densityPerSqKm);
            Assert.assertEquals(50, indiaCensusCsv[indiaCensusCsv.length-1].densityPerSqKm);
        } catch (CensusAnalyserException e) {
            e.getMessage();
        }
    }

    @Test
    public void givenIndianCensusData_whenSorted_OnArea_ShouldReturnMostToLeast() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getStateWiseSortedCensusDataOnArea();
            IndiaCensusCSV[] indiaCensusCsv = new Gson().fromJson(sortedCensusData, IndiaCensusCSV[].class);
            Assert.assertEquals(342239, indiaCensusCsv[0].areaInSqKm);
            Assert.assertEquals(3702, indiaCensusCsv[indiaCensusCsv.length-1].areaInSqKm);
        } catch (CensusAnalyserException | IOException e) {
            e.getMessage();
        }
    }
    /* USCensus test  cases */
    @Test
    public void givenUSCensusCSVFileReturnsCorrectRecords() {
        try {
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            int numOfRecords = censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_DATA_CSV_FILE_PATH);
            Assert.assertEquals(51, numOfRecords);
        } catch (CensusAnalyserException e) {
            System.out.println("Not found desired rows");
        }
    }

    @Test
    public void givenUSCensusData_whenSorted_OnPopulation_ShouldReturnMostToLeast() {
        try{
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_DATA_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getPopulationWiseSortedUSCensusData();
            USCensusCSV[] usCensusCsv = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
            Assert.assertEquals(37253956,usCensusCsv[0].population);
            Assert.assertEquals(563626,usCensusCsv[usCensusCsv.length-1].population);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void givenUSCensusData_whenSorted_OnPopulationDensity_ShouldReturnMostToLeast() {
        try{
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_DATA_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getPopulationDensityWiseSortedUSCensusData();
            USCensusCSV[] usCensusCsv = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
            Assert.assertEquals( (Double) 3805.61,usCensusCsv[0].populationDensity);
            Assert.assertEquals((Double) 0.46,usCensusCsv[usCensusCsv.length-1].populationDensity);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenUSCensusData_whenSorted_OnTotalArea_ShouldReturnMostToLeast() {
        try{
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_DATA_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getTotalAreaWiseSortedUSCensusData();
            USCensusCSV[] usCensusCsv = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
            Assert.assertEquals((Double) 1723338.01,usCensusCsv[0].totalArea);
            Assert.assertEquals((Double) 177.0,usCensusCsv[usCensusCsv.length-1].totalArea);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenUSCensusData_whenSorted_OnHousingUnits_ShouldReturnMostToLeast() {
        try{
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_DATA_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getHousingUnitWiseSortedUSCensusData();
            CensusDAO[] usCensusCsv = new Gson().fromJson(sortedCensusData, CensusDAO[].class);
            Assert.assertEquals( 13680081,usCensusCsv[0].housingUnits);
            Assert.assertEquals( 261868,usCensusCsv[usCensusCsv.length-1].housingUnits);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void givenUSCensusData_whenSorted_OnWaterArea_ShouldReturnMostToLeast() {
        try{
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_DATA_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getWaterAreaWiseSortedUSCensusData();
            USCensusCSV[] usCensusCsv = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
            Assert.assertEquals((Double) 245383.68,usCensusCsv[0].waterArea);
            Assert.assertEquals((Double) 18.88,usCensusCsv[usCensusCsv.length-1].waterArea);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void givenUSCensusData_whenSorted_OnHousingDensity_ShouldReturnMostToLeast() {
        try{
            CensusAnalyser censusAnalyser = new CensusAnalyser();
            censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_DATA_CSV_FILE_PATH);
            String sortedCensusData = censusAnalyser.getHousingDensityWiseSortedUSCensusData();
            USCensusCSV[] usCensusCsv = new Gson().fromJson(sortedCensusData, USCensusCSV[].class);
            Assert.assertEquals((Double) 1876.61,usCensusCsv[0].housingDensity);
            Assert.assertEquals((Double) 0.19,usCensusCsv[usCensusCsv.length-1].housingDensity);
        } catch (CensusAnalyserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getMostPopulousStateInCountries()  {
        CensusAnalyser censusAnalyser = new CensusAnalyser();
        try {
            String mostPopulous = censusAnalyser.getMostPopulousState(INDIA_CENSUS_CSV_FILE_PATH,US_CENSUS_DATA_CSV_FILE_PATH);
            Assert.assertEquals("Uttar Pradesh",mostPopulous);
        } catch (CensusAnalyserException | IOException e) {
            e.printStackTrace();
        }

    }
}
