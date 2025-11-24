package org.oop_project.view.controllers;

import java.net.URL;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import org.oop_project.database_handler.models.Sale;
import org.oop_project.database_handler.operations.SaleOperations;
import org.oop_project.database_handler.operations.EmployeeOperations;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

public class AnalysisController implements Initializable {

	@FXML
	private Button btnDaily;
	@FXML
	private Button btnWeekly;
	@FXML
	private Button btnMonthly;

	@FXML
	private TextArea analyticsOutput;

	private final SaleOperations saleOp = new SaleOperations();
	private final EmployeeOperations empOp = new EmployeeOperations();

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		// default view: daily
		showDaily();
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

	private void showDaily() {
		LocalDate today = LocalDate.now();
		LocalDateTime start = today.atStartOfDay();
		LocalDateTime end = today.atTime(LocalTime.MAX);
		String range = today.toString();
		loadAndDisplay(start, end, range);
	}

	private void showWeekly() {
		LocalDate today = LocalDate.now();
		LocalDate weekStart = today.with(java.time.DayOfWeek.MONDAY);
		LocalDateTime start = weekStart.atStartOfDay();
		LocalDateTime end = today.atTime(LocalTime.MAX);
		String range = weekStart.toString() + " - " + today.toString();
		loadAndDisplay(start, end, range);
	}

	private void showMonthly() {
		LocalDate today = LocalDate.now();
		LocalDate monthStart = today.withDayOfMonth(1);
		LocalDateTime start = monthStart.atStartOfDay();
		LocalDateTime end = today.atTime(LocalTime.MAX);
		String range = monthStart.toString() + " - " + today.toString();
		loadAndDisplay(start, end, range);
	}

	private void loadAndDisplay(LocalDateTime start, LocalDateTime end, String rangeLabel) {
		try {
			List<Sale> sales = saleOp.getAll();

			double totalIncome = 0.0;
			int totalProducts = 0;
			int transactions = 0;

			Map<String, Double> cashierTotals = new HashMap<>();
			Map<String, Integer> cashierProducts = new HashMap<>();

			if (sales != null) {
				for (Sale s : sales) {
					LocalDateTime dt = s.getDate();
					if (dt == null) continue;
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
					org.oop_project.database_handler.models.Employee emp = empOp.getById(bestId);
					if (emp != null) {
						bestName = (emp.getFirstName() != null ? emp.getFirstName() : "") + " " + (emp.getLastName() != null ? emp.getLastName() : "");
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
			sb.append("Total Income: ").append(String.format("Rs. %.2f", totalIncome)).append(" (").append(transactions).append(" tx)").append(System.lineSeparator());
			sb.append("Total Products Sold: ").append(totalProducts).append(System.lineSeparator()).append(System.lineSeparator());
			sb.append("Best Cashier:\n");
			sb.append("  ID: ").append(bestId).append(System.lineSeparator());
			sb.append("  Name: ").append(bestName).append(System.lineSeparator());
			sb.append("  Sales Value: ").append(String.format("Rs. %.2f", bestVal)).append(System.lineSeparator());
			sb.append("  Products Sold: ").append(bestProdCount).append(System.lineSeparator()).append(System.lineSeparator());

			sb.append("Per-cashier summary:\n");
			for (String cid : cashierTotals.keySet()) {
				double cVal = cashierTotals.getOrDefault(cid, 0.0);
				int cProd = cashierProducts.getOrDefault(cid, 0);
				String cName = cid.equals(bestId) ? bestName : "-";
				// attempt to resolve name if not best
				if (cName.equals("-") ) {
					try {
						var emp = empOp.getById(cid);
						if (emp != null) {
							cName = (emp.getFirstName() != null ? emp.getFirstName() : "") + " " + (emp.getLastName() != null ? emp.getLastName() : "");
						}
					} catch (Exception ign) {
						// ignore
					}
				}
				sb.append(String.format("  %s | %s | Rs. %.2f | %d products", cid, cName, cVal, cProd)).append(System.lineSeparator());
			}

			if (analyticsOutput != null) {
				analyticsOutput.setText(sb.toString());
				analyticsOutput.setStyle("-fx-font-family: monospace; -fx-text-fill: black;");
			}

		} catch (Exception ex) {
			if (analyticsOutput != null) {
				analyticsOutput.setText("Error loading analytics: " + ex.getMessage());
			}
			System.err.println("Error loading analytics: " + ex.getMessage());
			ex.printStackTrace();
		}
	}

}
