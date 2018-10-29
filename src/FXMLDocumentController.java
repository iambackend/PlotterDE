import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.chart.XYChart.Series;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.TextField;

import java.awt.geom.Point2D;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import static java.lang.Integer.max;
import static java.lang.Integer.min;
import static java.lang.Math.abs;

public class FXMLDocumentController implements Initializable {

    Equations equations = new Equations();

    @FXML
    private TextField field;
    @FXML
    private Button button;
    @FXML
    private LineChart<Number, Number> MyChart;
    @FXML
    private CheckBox solutions;
    @FXML
    private CheckBox errors;
    private Series ExactSeries;
    private Series EulerSeries;
    private Series ImprovedEulerSeries;
    private Series RungeKuttaSeries;
    private Series EulerSeriesErrors;
    private Series ImprovedEulerSeriesErrors;
    private Series RungeKuttaSeriesErrors;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        solutions.setSelected(true);
//        updateCharts();
    }

    private void updateCharts() {
        equations.calculateAllSolutions();
        setExactSeries(equations.exact);
        setEulerSeries(equations.euler, equations.exact);
        setImprovedEulerSeries(equations.improved, equations.exact);
        setRungeKuttaSeries(equations.runge, equations.exact);
        MyChart.getData().clear();
        if (solutions.isSelected())
            MyChart.getData().addAll(ExactSeries, EulerSeries, ImprovedEulerSeries, RungeKuttaSeries);
        if (errors.isSelected())
            MyChart.getData().addAll(EulerSeriesErrors, ImprovedEulerSeriesErrors, RungeKuttaSeriesErrors);
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        int n = Integer.parseInt("0" + field.getText());
        n = min(max(n, 1), 10000);
        equations.n = n;
        updateCharts();
    }


    private void setExactSeries(ArrayList<Point2D.Double> series) {
        ExactSeries = new Series();
        ExactSeries.setName("Exact Solution");
        for (Point2D.Double point : series) {
            ExactSeries.getData().add(new Data(point.x, point.y));
        }
    }

    private void setEulerSeries(ArrayList<Point2D.Double> series, ArrayList<Point2D.Double> exact) {
        EulerSeries = new Series();
        EulerSeries.setName("Euler Solution");
        EulerSeriesErrors = new Series();
        EulerSeriesErrors.setName("Euler Errors");
        for (int i = 0; i < series.size(); i++) {
            Point2D.Double point = series.get(i);
            Point2D.Double exact_point = exact.get(i);
            EulerSeries.getData().add(new Data(point.x, point.y));
            EulerSeriesErrors.getData().add(new Data(point.x, abs(point.y - exact_point.y)));
        }
    }

    private void setImprovedEulerSeries(ArrayList<Point2D.Double> series, ArrayList<Point2D.Double> exact) {
        ImprovedEulerSeries = new Series();
        ImprovedEulerSeries.setName("Improved Euler Solution");
        ImprovedEulerSeriesErrors = new Series();
        ImprovedEulerSeriesErrors.setName("Improved Euler Errors");
        for (int i = 0; i < series.size(); i++) {
            Point2D.Double point = series.get(i);
            Point2D.Double exact_point = exact.get(i);
            ImprovedEulerSeries.getData().add(new Data(point.x, point.y));
            ImprovedEulerSeriesErrors.getData().add(new Data(point.x, abs(point.y - exact_point.y)));
        }
    }

    private void setRungeKuttaSeries(ArrayList<Point2D.Double> series, ArrayList<Point2D.Double> exact) {
        RungeKuttaSeries = new Series();
        RungeKuttaSeries.setName("Runge-Kutta Solution");
        RungeKuttaSeriesErrors = new Series();
        RungeKuttaSeriesErrors.setName("Runge-Kutta Errors");
        for (int i = 0; i < series.size(); i++) {
            Point2D.Double point = series.get(i);
            Point2D.Double exact_point = exact.get(i);
            RungeKuttaSeries.getData().add(new Data(point.x, point.y));
            RungeKuttaSeriesErrors.getData().add(new Data(point.x, abs(point.y - exact_point.y)));
        }
    }


}
