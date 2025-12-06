package org.oop_project.view.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.time.DayOfWeek;

import org.oop_project.database_handler.models.Employee;
import org.oop_project.database_handler.enums.Role;
import org.oop_project.database_handler.models.Sale;
import org.oop_project.database_handler.operations.EmployeeOperations;
import org.oop_project.database_handler.operations.SaleOperations;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.PieChart;

public class AnalysisController implements Initializable {

	@FXML
	private Button btnDaily;
	@FXML
	private Button btnWeekly;
	@FXML
	private Button btnMonthly;

	@FXML
	private TextArea analyticsOutput;

	@FXML
	private LineChart<String, Number> monthlySalesChart;

	@FXML
	private BarChart<String, Number> transactionsChart;

	@FXML
	private PieChart topProductsPie;

	private final SaleOperations salesManager = new SaleOperations();
	private final EmployeeOperations employeeManager = new EmployeeOperations();

	private final LocalDate today = LocalDate.now();

	private Role viewerRole = Role.ADMIN; // default admin; can be overridden

	public void setViewerRole(Role role) {
		this.viewerRole = role == null ? Role.ADMIN : role;
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		showDaily();
		applyRolePermissions();
	}

	@FXML
	private void onDaily() {
		showDaily();
	}

	@FXML
	private void onWeekly() {
		showWeekly();
	}

	@FXML
	private void onMonthly() {
		showMonthly();
	}

	private void applyRolePermissions() {
		if (viewerRole == Role.CASHIER) {
			if (monthlySalesChart != null) monthlySalesChart.setVisible(false);
			if (transactionsChart != null) transactionsChart.setVisible(false);
			if (topProductsPie != null) topProductsPie.setVisible(false);
		} else {
			if (monthlySalesChart != null) monthlySalesChart.setVisible(true);
			if (transactionsChart != null) transactionsChart.setVisible(true);
			if (topProductsPie != null) topProductsPie.setVisible(true);
		}
	}

	private void showDaily() {
		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.atTime(LocalTime.MAX);
		String range = today.toString();
		loadAndDisplay(start, end, range);
	}

	private void showWeekly() {
		LocalDate weekStart = today.with(DayOfWeek.MONDAY);
		LocalDateTime start = weekStart.atStartOfDay();
		LocalDateTime end = today.atTime(LocalTime.MAX);
		String range = weekStart.toString() + " - " + today.toString();
		loadAndDisplay(start, end, range);
	}

	private void showMonthly() {
		LocalDate monthStart = today.withDayOfMonth(1);
		LocalDateTime start = monthStart.atStartOfDay();
		LocalDateTime end = today.atTime(LocalTime.MAX);
		String range = monthStart.toString() + " - " + today.toString();
		loadAndDisplay(start, end, range);
	}

	private void loadAndDisplay(LocalDateTime start, LocalDateTime end, String rangeLabel) {
		try {
			List<Sale> sales = salesManager.getAll();

			double totalIncome = 0.0;
			int totalProducts = 0;
			int transactions = 0;

			Map<String, Double> cashierTotals = new HashMap<>();
			Map<String, Integer> cashierProducts = new HashMap<>();

			if (sales == null) {
				return;
			}

			for (Sale s : sales) {

				LocalDateTime dt = s.getDate();
				
				if (dt == null)
					continue;
				if ((dt.isEqual(start) || dt.isAfter(start)) && (dt.isEqual(end) || dt.isBefore(end))) {
					totalIncome += s.getPrice();
					totalProducts += s.getItemsCount();
					transactions++;
					String c = s.getCashierId();
					if (c != null) {
						cashierTotals.put(c, cashierTotals.getOrDefault(c, 0.0) + s.getPrice());
						cashierProducts.put(c, cashierProducts.getOrDefault(c, 0) + s.getItemsCount());
					}
				}
			}

			String bestId = "-";
			double bestVal = 0.0;
			for (Map.Entry<String, Double> e : cashierTotals.entrySet()) {
				if (e.getValue() > bestVal) {
					bestVal = e.getValue();
					bestId = e.getKey();
				}
			}

			String bestName = "-";
			if (!bestId.equals("-")) {
				try {
					Employee emp = employeeManager.getById(bestId);
					if (emp != null) {
						bestName = (emp.getFirstName() != null ? emp.getFirstName() : "") + " "
								+ (emp.getLastName() != null ? emp.getLastName() : "");
					}
				} catch (Exception ex) {
					// ignore, leave bestName as id fallback
				}
			}

			int bestProdCount;
			if (bestId.equals("-")) {
				bestProdCount = 0;
			} else {
				Integer val = cashierProducts.get(bestId);
				bestProdCount = val == null ? 0 : val.intValue();
			}

			// Build analytics text output
			StringBuilder sb = new StringBuilder();
			sb.append("Date: ").append(rangeLabel).append(System.lineSeparator()).append(System.lineSeparator());
			sb.append("Total Income: ").append(String.format("Rs. %.2f", totalIncome)).append(" (").append(transactions)
					.append(" tx)").append(System.lineSeparator());
			sb.append("Total Products Sold: ").append(totalProducts).append(System.lineSeparator())
					.append(System.lineSeparator());
			sb.append("Best Cashier:\n");
			sb.append("  ID: ").append(bestId).append(System.lineSeparator());
			sb.append("  Name: ").append(bestName).append(System.lineSeparator());
			sb.append("  Sales Value: ").append(String.format("Rs. %.2f", bestVal)).append(System.lineSeparator());
			sb.append("  Products Sold: ").append(bestProdCount).append(System.lineSeparator())
					.append(System.lineSeparator());

			sb.append("Per-cashier summary:\n");
			for (String cid : cashierTotals.keySet()) {
				double cVal = cashierTotals.getOrDefault(cid, 0.0);
				int cProd = cashierProducts.getOrDefault(cid, 0);
				String cName = cid.equals(bestId) ? bestName : "-";
				// attempt to resolve name if not best
				if (cName.equals("-")) {
					try {
						var emp = employeeManager.getById(cid);
						if (emp != null) {
							cName = (emp.getFirstName() != null ? emp.getFirstName() : "") + " "
									+ (emp.getLastName() != null ? emp.getLastName() : "");
						}
					} catch (Exception ign) {
						// ignore
					}
				}
				sb.append(String.format("  %s | %s | Rs. %.2f | %d products", cid, cName, cVal, cProd))
						.append(System.lineSeparator());
			}


			if (analyticsOutput != null) {
				analyticsOutput.setText(sb.toString());
				analyticsOutput.setStyle("-fx-font-family: monospace; -fx-text-fill: black;");
			}

			// Populate charts
			populateMonthlySalesChart();
			populateTransactionsChart();
			populateTopProductsPie(start, end);

		} catch (Exception ex) {
			if (analyticsOutput != null) {
				analyticsOutput.setText("Error loading analytics: " + ex.getMessage());
			}
			System.err.println("Error loading analytics: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	private void populateMonthlySalesChart() {
		try {
			if (monthlySalesChart == null) return;
			monthlySalesChart.getData().clear();
			Map<String, Double> monthlyTotals = new HashMap<>();
			LocalDate now = LocalDate.now();
			LocalDate startMonth = now.minusMonths(11).withDayOfMonth(1);
			LocalDate endMonth = now.withDayOfMonth(now.lengthOfMonth());
			LocalDateTime start = startMonth.atStartOfDay();
			LocalDateTime end = endMonth.atTime(LocalTime.MAX);
			List<Sale> sales = salesManager.getAll();
			if (sales == null) return;
			for (Sale s : sales) {
				LocalDateTime dt = s.getDate();
				if (dt == null) continue;
				if ((dt.isEqual(start) || dt.isAfter(start)) && (dt.isEqual(end) || dt.isBefore(end))) {
					String key = dt.getYear() + "-" + String.format("%02d", dt.getMonthValue());
					monthlyTotals.put(key, monthlyTotals.getOrDefault(key, 0.0) + s.getPrice());
				}
			}
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			for (int i = 11; i >= 0; i--) {
				LocalDate m = now.minusMonths(i);
				String key = m.getYear() + "-" + String.format("%02d", m.getMonthValue());
				double val = monthlyTotals.getOrDefault(key, 0.0);
				series.getData().add(new XYChart.Data<>(key, val));
			}
			series.setName("Monthly Sales");
			monthlySalesChart.getData().add(series);
		} catch (Exception ignored) {}
	}

	private void populateTransactionsChart() {
		try {
			if (transactionsChart == null) return;
			transactionsChart.getData().clear();
			Map<String, Integer> monthlyTx = new HashMap<>();
			LocalDate now = LocalDate.now();
			LocalDate startMonth = now.minusMonths(11).withDayOfMonth(1);
			LocalDate endMonth = now.withDayOfMonth(now.lengthOfMonth());
			LocalDateTime start = startMonth.atStartOfDay();
			LocalDateTime end = endMonth.atTime(LocalTime.MAX);
			List<Sale> sales = salesManager.getAll();
			if (sales == null) return;
			for (Sale s : sales) {
				LocalDateTime dt = s.getDate();
				if (dt == null) continue;
				if ((dt.isEqual(start) || dt.isAfter(start)) && (dt.isEqual(end) || dt.isBefore(end))) {
					String key = dt.getYear() + "-" + String.format("%02d", dt.getMonthValue());
					monthlyTx.put(key, monthlyTx.getOrDefault(key, 0) + 1);
				}
			}
			XYChart.Series<String, Number> series = new XYChart.Series<>();
			for (int i = 11; i >= 0; i--) {
				LocalDate m = now.minusMonths(i);
				String key = m.getYear() + "-" + String.format("%02d", m.getMonthValue());
				int val = monthlyTx.getOrDefault(key, 0);
				series.getData().add(new XYChart.Data<>(key, val));
			}
			series.setName("Transactions");
			transactionsChart.getData().add(series);
		} catch (Exception ignored) {}
	}

	private void populateTopProductsPie(LocalDateTime start, LocalDateTime end) {
		try {
			if (topProductsPie == null) return;
			topProductsPie.getData().clear();
			List<Sale> sales = salesManager.getAll();
			if (sales == null) return;
			Map<String, Double> cashierTotals = new HashMap<>();
			for (Sale s : sales) {
				LocalDateTime dt = s.getDate();
				if (dt == null) continue;
				if ((dt.isEqual(start) || dt.isAfter(start)) && (dt.isEqual(end) || dt.isBefore(end))) {
					String c = s.getCashierId();
					if (c != null) {
						cashierTotals.put(c, cashierTotals.getOrDefault(c, 0.0) + s.getPrice());
					}
				}
			}
			cashierTotals.entrySet().stream()
					.sorted((a, b) -> Double.compare(b.getValue(), a.getValue()))
					.limit(5)
					.forEach(e -> topProductsPie.getData().add(new PieChart.Data(e.getKey(), e.getValue())));
		} catch (Exception ignored) {}
	}

}
