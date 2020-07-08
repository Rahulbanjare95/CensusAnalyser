package censusanalyser.adapters;

import censusanalyser.exceptions.CensusAnalyserException;
import censusanalyser.model.CensusDAO;
import censusanalyser.model.USCensusCSV;

import java.util.Map;

public class USCensusAdapter extends CensusAdapter {

    @Override
    public Map<String, CensusDAO> loadCensusData(String ... csvFilePath) throws CensusAnalyserException {
        Map<String, CensusDAO> stringCensusDAOMap = super.loadCensusData(USCensusCSV.class, csvFilePath);
        return  stringCensusDAOMap;
    }
}
