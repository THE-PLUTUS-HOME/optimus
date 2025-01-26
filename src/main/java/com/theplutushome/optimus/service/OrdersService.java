package com.theplutushome.optimus.service;

import com.theplutushome.optimus.advice.OrderNotFoundException;
import com.theplutushome.optimus.dto.DashboardDto;
import com.theplutushome.optimus.dto.OrdersDto;
import com.theplutushome.optimus.dto.PaymentOrderDto;
import com.theplutushome.optimus.entity.PaymentOrder;
import com.theplutushome.optimus.entity.enums.PaymentOrderStatus;
import com.theplutushome.optimus.repository.OrderRepository;
import com.theplutushome.optimus.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.IsoFields;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class OrdersService {

    private OrderRepository orderRepository;
    private JwtUtil jwtUtil;

    @Autowired
    public OrdersService(OrderRepository orderRepository, JwtUtil jwtUtil) {
        this.orderRepository = orderRepository;
        this.jwtUtil = jwtUtil;
    }

    public List<PaymentOrderDto> getAllOrders(String email, String authHeader) {
        jwtUtil.verifyToken(authHeader);
        List<PaymentOrderDto> allOrders = orderRepository.findAllByEmail(email);
        if (!allOrders.isEmpty()) {
            return allOrders;
        }
        return null;
    }

    public PaymentOrder findOrder(int orderId, String authHeader) {
        return null;
    }

    public void createOrder(PaymentOrder order) {
        order.setStatus(PaymentOrderStatus.PENDING);
        order.setAmountPaid(0.0);
        orderRepository.save(order);
    }

    public void updateOrder(PaymentOrder order) {
        orderRepository.save(order);
    }

    public PaymentOrder findOrderByClientReference(String reference) {
        PaymentOrder order = orderRepository.findPaymentOrderByClientReference(reference);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        return order;
    }

    public PaymentOrder findOrderByPhoneNumber(String phoneNumber) {
        PaymentOrder order = orderRepository.findPaymentOrderByPhoneNumberAndStatus(phoneNumber,
                PaymentOrderStatus.PENDING);
        if (order == null) {
            throw new OrderNotFoundException();
        }
        return order;
    }

    public List<PaymentOrder> findPendingOrdersByEmail(String email) {
        return orderRepository.findPaymentOrdersByEmailAndStatus(email, PaymentOrderStatus.PENDING);
    }

    public DashboardDto getDashboardData() {
        // Fetch all orders
        Iterable<PaymentOrder> orders = orderRepository.findAll();
        List<PaymentOrder> orderList = new ArrayList<>();
        orders.forEach(orderList::add);

        DashboardDto dashboardDto = new DashboardDto();

        // 1. Total Orders
        int totalOrders = orderList.size();
        dashboardDto.setTotalOrders(String.valueOf(totalOrders));

        // 2. Total Customers (unique emails)
        Set<String> uniqueCustomers = new HashSet<>();
        for (PaymentOrder order : orderList) {
            uniqueCustomers.add(order.getEmail());
        }
        dashboardDto.setTotalCustomers(String.valueOf(uniqueCustomers.size()));

        // 3. Total Revenue (sum of amountGHS)
        double totalRevenue = 0.0;
        for (PaymentOrder order : orderList) {
            totalRevenue += order.getAmountGHS();
        }
        dashboardDto.setTotalRevenue(String.valueOf(totalRevenue));

        // 4. Total Profit (fee * rate)
        double totalProfit = 0.0;
        for (PaymentOrder order : orderList) {
            totalProfit += (order.getFee() * order.getRate());
        }
        dashboardDto.setTotalProfit(String.valueOf(totalProfit));

        // 6. Total Week Orders Percentage Increase
        int currentWeekOrders = getOrdersForCurrentWeek(orderList);
        int previousWeekOrders = getOrdersForPreviousWeek(orderList);
        double totalWeekOrdersPercentageIncrease = 0.0;
        if (previousWeekOrders > 0) {
            totalWeekOrdersPercentageIncrease = ((double) (currentWeekOrders - previousWeekOrders) / previousWeekOrders)
                    * 100;
        }
        dashboardDto.setTotalWeekOrdersPercentageIncrease(String.format("%.2f", totalWeekOrdersPercentageIncrease));

        // 7. Total Week Revenue Percentage Increase
        double currentWeekRevenue = getRevenueForCurrentWeek(orderList);
        double previousWeekRevenue = getRevenueForPreviousWeek(orderList);
        double totalWeekRevenuePercentageIncrease = 0.0;
        if (previousWeekRevenue > 0) {
            totalWeekRevenuePercentageIncrease = ((currentWeekRevenue - previousWeekRevenue) / previousWeekRevenue)
                    * 100;
        }
        dashboardDto.setTotalWeekRevenuePercentageIncrease(String.format("%.2f", totalWeekRevenuePercentageIncrease));

        // 8. Total Week Customers Percentage Increase
        int currentWeekCustomers = getUniqueCustomersForCurrentWeek(orderList);
        int previousWeekCustomers = getUniqueCustomersForPreviousWeek(orderList);
        double totalWeekCustomersPercentageIncrease = 0.0;
        if (previousWeekCustomers > 0) {
            totalWeekCustomersPercentageIncrease = ((currentWeekCustomers - previousWeekCustomers)
                    / (double) previousWeekCustomers) * 100;
        }
        dashboardDto
                .setTotalWeekCustomersPercentageIncrease(String.format("%.2f", totalWeekCustomersPercentageIncrease));

        // 9. Total Day Profit Percentage Increase
        double currentDayProfit = getProfitForCurrentDay(orderList);
        double previousDayProfit = getProfitForPreviousDay(orderList);
        double totalDayProfitPercentageIncrease = 0.0;
        if (previousDayProfit > 0) {
            totalDayProfitPercentageIncrease = ((currentDayProfit - previousDayProfit) / previousDayProfit) * 100;
        }
        dashboardDto.setTotalDayProfitPercentageIncrease(String.format("%.2f", totalDayProfitPercentageIncrease));

        // 10. Set weekly and monthly data (orders, revenue, profit, cost of sales)
        dashboardDto.setWeekOrders(aggregateDataForPeriod(orderList, "week", "orders"));
        dashboardDto.setMonthOrders(aggregateDataForPeriod(orderList, "month", "orders"));

        dashboardDto.setWeekRevenue(aggregateDataForPeriod(orderList, "week", "revenue"));
        dashboardDto.setMonthRevenue(aggregateDataForPeriod(orderList, "month", "revenue"));

        dashboardDto.setWeekCostOfSales(aggregateDataForPeriod(orderList, "week", "costOfSales"));
        dashboardDto.setMonthCostOfSales(aggregateDataForPeriod(orderList, "month", "costOfSales"));

        dashboardDto.setWeekProfit(aggregateDataForPeriod(orderList, "week", "profit"));
        dashboardDto.setMonthProfit(aggregateDataForPeriod(orderList, "month", "profit"));

        dashboardDto.setYearRevenue(aggregateDataForPeriod(orderList, "year", "revenue"));
        dashboardDto.setYearCostOfSales(aggregateDataForPeriod(orderList, "year", "costOfSales"));
        dashboardDto.setYearProfit(aggregateDataForPeriod(orderList, "year", "profit"));

        // 11. Set recent orders (latest 3 orders)
        List<PaymentOrder> recentOrders = orderList.stream()
                .sorted(Comparator.comparing(PaymentOrder::getCreatedAt).reversed())
                .limit(3)
                .collect(Collectors.toList());
        dashboardDto.setRecentOrders(recentOrders.stream().map(this::convertToDto).collect(Collectors.toList()));

        return dashboardDto;
    }

    // Helper method to aggregate data by period (week, month, year)
    private String[] aggregateDataForPeriod(List<PaymentOrder> orderList, String period, String type) {
        String[] data = new String[period.equals("week") ? 7 : (period.equals("month") ? 12 : 12)];

        // Example aggregation logic
        for (int i = 0; i < data.length; i++) {
            // Logic to aggregate data (order counts, revenue, profit, cost of sales) based
            // on period
            data[i] = "100"; // Placeholder for actual data aggregation
        }

        return data;
    }

    // Helper methods to get data for specific periods
    private int getOrdersForCurrentWeek(List<PaymentOrder> orders) {
        return (int) orders.stream()
                .filter(order -> isCurrentWeek(order.getCreatedAt()))
                .count();
    }

    private int getOrdersForPreviousWeek(List<PaymentOrder> orders) {
        return (int) orders.stream()
                .filter(order -> isPreviousWeek(order.getCreatedAt()))
                .count();
    }

    private double getRevenueForCurrentWeek(List<PaymentOrder> orders) {
        return orders.stream()
                .filter(order -> isCurrentWeek(order.getCreatedAt()))
                .mapToDouble(PaymentOrder::getAmountGHS)
                .sum();
    }

    private double getRevenueForPreviousWeek(List<PaymentOrder> orders) {
        return orders.stream()
                .filter(order -> isPreviousWeek(order.getCreatedAt()))
                .mapToDouble(PaymentOrder::getAmountGHS)
                .sum();
    }

    private int getUniqueCustomersForCurrentWeek(List<PaymentOrder> orders) {
        return (int) orders.stream()
                .filter(order -> isCurrentWeek(order.getCreatedAt()))
                .map(PaymentOrder::getEmail)
                .distinct()
                .count();
    }

    private int getUniqueCustomersForPreviousWeek(List<PaymentOrder> orders) {
        return (int) orders.stream()
                .filter(order -> isPreviousWeek(order.getCreatedAt()))
                .map(PaymentOrder::getEmail)
                .distinct()
                .count();
    }

    private double getProfitForCurrentDay(List<PaymentOrder> orders) {
        return orders.stream()
                .filter(order -> isCurrentDay(order.getCreatedAt()))
                .mapToDouble(order -> order.getFee() * order.getRate())
                .sum();
    }

    private double getProfitForPreviousDay(List<PaymentOrder> orders) {
        return orders.stream()
                .filter(order -> isPreviousDay(order.getCreatedAt()))
                .mapToDouble(order -> order.getFee() * order.getRate())
                .sum();
    }

    // Time-based filtering methods
    private boolean isCurrentWeek(LocalDateTime orderDate) {
        LocalDateTime now = LocalDateTime.now();
        return orderDate.getYear() == now.getYear()
                && orderDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    private boolean isPreviousWeek(LocalDateTime orderDate) {
        LocalDateTime now = LocalDateTime.now().minusWeeks(1);
        return orderDate.getYear() == now.getYear()
                && orderDate.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR) == now.get(IsoFields.WEEK_OF_WEEK_BASED_YEAR);
    }

    private boolean isCurrentDay(LocalDateTime orderDate) {
        LocalDateTime now = LocalDateTime.now();
        return orderDate.toLocalDate().equals(now.toLocalDate());
    }

    private boolean isPreviousDay(LocalDateTime orderDate) {
        LocalDateTime now = LocalDateTime.now().minusDays(1);
        return orderDate.toLocalDate().equals(now.toLocalDate());
    }

    public OrdersDto convertToDto(PaymentOrder order){
        OrdersDto ordersDto = new OrdersDto();
        ordersDto.setClientReference(order.getClientReference());
        ordersDto.setAmountGHS(order.getAmountGHS());
        ordersDto.setFee(order.getFee());
        ordersDto.setStatus(order.getStatus());
        ordersDto.setCrypto(order.getCrypto());
        ordersDto.setCryptoAmount(order.getCryptoAmount());
        ordersDto.setRate(order.getRate());
        ordersDto.setAddress(order.getAddress());
        ordersDto.setPhoneNumber(order.getPhoneNumber());
        ordersDto.setTransactionId(order.getTransactionId());
        ordersDto.setCreatedAt(order.getCreatedAt().toString());
        ordersDto.setUpdatedAt(order.getUpdatedAt().toString());
        return ordersDto;
    }
    

    public List<OrdersDto> getAllOrders(){
        Iterable<PaymentOrder> orders = orderRepository.findAll();
        List<PaymentOrder> orderList = new ArrayList<>();
        orders.forEach(orderList::add);
        return orderList.stream().map(this::convertToDto).collect(Collectors.toList());
    }
}
