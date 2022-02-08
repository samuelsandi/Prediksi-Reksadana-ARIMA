package com.ta.prediksireksadanaarima.utilities;

import com.ta.prediksireksadanaarima.models.MutualFundPriceModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.time.LocalDate;
import com.workday.insights.timeseries.arima.Arima;
import com.workday.insights.timeseries.arima.struct.ArimaParams;
import com.workday.insights.timeseries.arima.struct.ForecastResult;

public class Predictor {

    private final ArrayList<MutualFundPriceModel> priceList;

    public Predictor(ArrayList<MutualFundPriceModel> priceList){
        this.priceList = priceList;
    }

    public ArrayList<MutualFundPriceModel> predict() {

        ArrayList<Double> pricesWithoutDates = new ArrayList<>();
        for (int i=0; i<priceList.size(); i++){
            pricesWithoutDates.add((double)priceList.get(i).getPrice());
        }
        double[] dataArray = pricesWithoutDates.stream().mapToDouble(Double::doubleValue).toArray();

        int forecastSize = 5;
        ArimaParams params = new ArimaParams(2, 1, 2, 0, 0, 0, 0);
        ForecastResult forecastResult = Arima.forecast_arima(dataArray, forecastSize, params);
        double[] forecastData = forecastResult.getForecast();

        LocalDate lastDate = LocalDate.parse(priceList.get(priceList.size()-1).getDate());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        ArrayList<MutualFundPriceModel> pricesAndDates = new ArrayList<>();

        for (int i=0; i<forecastSize; i++){
            pricesAndDates.add(new MutualFundPriceModel(lastDate.format(formatter),
                                                        (float)forecastData[i]));
            lastDate = lastDate.plusDays(1);
        }

        return pricesAndDates;
    }
}
