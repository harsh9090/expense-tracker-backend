package com.harshmithaiwala.expensetracking.expensetracking.controller;

import com.harshmithaiwala.expensetracking.expensetracking.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    // ✅ Get Monthly Summary (Expenses & Income)
    @GetMapping("/monthly-summary")
    public ResponseEntity<List<Map<String, Object>>> getMonthlySummary(Authentication authentication) {
        return ResponseEntity.ok(reportService.getMonthlySummary(authentication.getName()));
    }
    // ✅ Get Category-wise Expense Breakdown
    @GetMapping("/category-wise-summary")
    public ResponseEntity<List<Map<String, Object>>> getCategorySummary(Authentication authentication) {
        return ResponseEntity.ok(reportService.getCategorySummary(authentication.getName()));
    }
    @GetMapping("/category-wise-income")
    public ResponseEntity<List<Map<String, Object>>> getCategoryIncome(Authentication authentication) {
        System.out.println(authentication.getName()+ " this is name");
        return ResponseEntity.ok(reportService.getCategoryIncome(authentication.getName()));
    }

}
