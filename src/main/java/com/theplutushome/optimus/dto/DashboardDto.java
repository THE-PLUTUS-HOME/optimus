package com.theplutushome.optimus.dto;

import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DashboardDto {
    private String totalOrders;
    private String totalWeekOrdersPercentageIncrease;

    private String totalCustomers;
    private String totalWeekCustomersPercentageIncrease;

    private String totalRevenue;
    private String totalWeekRevenuePercentageIncrease;

    private String totalProfit;
    private String totalDayProfitPercentageIncrease;

    private String[] weekOrders = new String[7];
    private String[] monthOrders = new String[12];

    private String[] weekRevenue = new String[7];
    private String[] weekCostOfSales = new String[7];
    private String[] weekProfit = new String[7];

    private String[] monthRevenue = new String[12];
    private String[] monthCostOfSales = new String[12];
    private String[] monthProfit = new String[12];

    private String[] yearRevenue = new String[12];
    private String[] yearCostOfSales = new String[12];
    private String[] yearProfit = new String[12];

    private List<OrdersDto> recentOrders = new ArrayList<>();

}
