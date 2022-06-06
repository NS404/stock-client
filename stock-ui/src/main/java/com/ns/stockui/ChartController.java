package com.ns.stockui;

import com.ns.stockwebclient.StockPrice;
import com.ns.stockwebclient.WebClientStockClient;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import net.rgielen.fxweaver.core.FxmlView;
import org.springframework.stereotype.Component;

import java.util.function.Consumer;

import static javafx.collections.FXCollections.observableArrayList;

@Component
@FxmlView("/chart.fxml")
public class ChartController implements Consumer<StockPrice> {

    private final ObservableList<Data<String, Double>> seriesData = observableArrayList();
    @FXML
    private LineChart<String, Double> chart;
    private final WebClientStockClient stockClient;

    public ChartController(WebClientStockClient stockClient) {
        this.stockClient = stockClient;
    }

    @FXML
    public void initialize() {
        String symbol = "SYMBOL";
        ObservableList<XYChart.Series<String,Double>> data = FXCollections.observableArrayList();
        data.add(new XYChart.Series<>(symbol,seriesData));
        chart.setData(data);

        stockClient.pricesFor(symbol).subscribe(this);
    }

    @Override
    public void accept(StockPrice stockPrice) {
        Platform.runLater(()->  //update ui
                seriesData.add(new XYChart.Data<>(String.valueOf(stockPrice.getTime().getSecond()),
                                                                stockPrice.getPrice()))

                );
    }

}
