package censusanalyser.model;

import com.opencsv.bean.CsvBindByName;

public class USCensusCSV {

        @CsvBindByName(column = "State Id")
        public String stateId;

        @CsvBindByName(column = "State")
        public String state;

        @CsvBindByName(column = "Population")
        public Integer population;

        @CsvBindByName(column = "Housing units")
        public Integer housingUnits;

        @CsvBindByName(column = "Total area")
        public Double totalArea;

        @CsvBindByName(column = "Water area")
        public Double waterArea;

        @CsvBindByName(column = "Land area")
        public Double landArea;

        @CsvBindByName(column = "Population Density")
        public Double populationDensity;

        @CsvBindByName(column = "Housing Density")
        public Double housingDensity;

        @Override
        public String toString() {
            return "usCensusCSV{" +
                    "State Id='" + stateId + '\'' +
                    ", State ='" + state + '\'' +
                    ", Population='" + population + '\'' +
                    ", Housing units='" + housingUnits + '\'' +
                    ", Total area='" + totalArea + '\'' +
                    ", Water area='" + waterArea + '\'' +
                    ", Land Area='" + landArea + '\'' +
                    ", Population Density='" + populationDensity + '\'' +
                    ", Housing Density='" + housingDensity + '\'' +
                    '}';
        }
}
