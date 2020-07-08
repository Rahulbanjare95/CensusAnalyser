package censusanalyser;

import censusanalyser.exceptions.CensusAnalyserException;
import censusanalyser.model.CensusDAO;
import censusanalyser.adapters.CensusAdapterFactory;
import censusanalyser.service.CensusAnalyser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnit;
import org.mockito.junit.MockitoRule;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CensusAnalyserMockitoTest {
    private static final String INDIA_CENSUS_CSV_FILE_PATH = "./src/test/resources/IndiaStateCensusData.csv";
    private static final String INDIA_STATE_CODE_CSV_FILE_PATH = "./src/test/resources/IndiaStateCode.csv";
    private static final String US_CENSUS_DATA_CSV_FILE_PATH = "./src/test/resources/USCensusData.csv";

    @Mock
    private CensusAdapterFactory censusAdapterFactory;
    @InjectMocks
    private CensusAnalyser censusAnalyser;
    Map<String, CensusDAO> censusMapIndia;
    Map<String, CensusDAO> censusUSA;
    @Rule
    public MockitoRule mockitoRule = MockitoJUnit.rule();
    @Before
    public void set() throws CensusAnalyserException{
        this.censusMapIndia = new HashMap<>();
        this.censusMapIndia.put("Bihar", new CensusDAO("Bihar", 103804637, 94163, 1102, "BH"));
        this.censusMapIndia.put("Haryana",new CensusDAO("Haryana",25353081,44212,573,"HR"));
        this.censusMapIndia.put("Goa",new CensusDAO("Goa",1457723,3702,394,"GA"));
        this.censusMapIndia.put("Assam",new CensusDAO("Assam",31169272,78438,397,"AS"));
        this.censusUSA = new HashMap<>();
        this.censusUSA.put("Colorado", new CensusDAO("CO","Colorado",5029196,2212898,269601.56,1170.11,268431.46,18.73,8.26));
        this.censusUSA.put("Delaware", new CensusDAO( "DE","Delaware",897934,405885,6445.76,1399.06,5046.7,177.92,80.43));
    }
    @Test
    public void givenIndiaCensusFile_ShouldReturnCorrectRecordsForDummyInputs(){
        try {
            CensusAnalyser censusAnalyser = mock(CensusAnalyser.class);
            when(censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH))
                    .thenReturn(this.censusMapIndia.size());
            int records = censusAnalyser.loadCensusData(CensusAnalyser.Country.INDIA, INDIA_CENSUS_CSV_FILE_PATH, INDIA_STATE_CODE_CSV_FILE_PATH);
            Assert.assertEquals(4, records);
        }catch (CensusAnalyserException e){
            e.printStackTrace();
        }
    }
    @Test
    public void givenUSCensusFileShouldReturnCorrectRecordsForDummyInputs(){
        try {
            CensusAnalyser censusAnalyser = mock(CensusAnalyser.class);
            when(censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_DATA_CSV_FILE_PATH))
                    .thenReturn(this.censusUSA.size());
            int records = censusAnalyser.loadCensusData(CensusAnalyser.Country.US, US_CENSUS_DATA_CSV_FILE_PATH);
            Assert.assertEquals(2, records);
        }catch (CensusAnalyserException e){
            e.printStackTrace();
        }
    }
}
