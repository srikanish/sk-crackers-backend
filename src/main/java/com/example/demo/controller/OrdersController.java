package com.example.demo.controller;

import com.example.demo.model.Orders;
import com.example.demo.repository.OrdersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrdersController {

    @Autowired
    private OrdersRepository ordersRepository;

    // Health check endpoint
    @GetMapping("/health")
    public ResponseEntity<Map<String, String>> healthCheck() {
        Map<String, String> response = new HashMap<>();
        response.put("status", "OK");
        response.put("message", "Orders API is running");
        response.put("timestamp", LocalDateTime.now().toString());
        return ResponseEntity.ok(response);
    }

    // Get all orders
    @GetMapping
    public ResponseEntity<?> getAllOrders() {
        try {
            List<Orders> orders = ordersRepository.findAllOrderedByDateDesc();
            System.out.println("Retrieved " + orders.size() + " orders");
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            System.err.println("Error fetching orders: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch orders");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // Create new order - Simplified version
    @PostMapping
    public ResponseEntity<?> createOrder(@RequestBody Map<String, Object> orderData) {
        try {
            System.out.println("=== ORDER CREATION START ===");
            System.out.println("Received order data: " + orderData);
            
            // Extract and validate data
            String name = (String) orderData.get("name");
            String mobile = (String) orderData.get("mobile");
            String address = (String) orderData.get("address");
            Object productListObj = orderData.get("productList");
            Object totalAmountObj = orderData.get("totalAmount");
            
            System.out.println("Extracted - Name: " + name + ", Mobile: " + mobile + ", Address: " + address);
            System.out.println("ProductList: " + productListObj + ", TotalAmount: " + totalAmountObj);
            
            // Basic validation
            if (name == null || name.trim().isEmpty()) {
                System.err.println("Validation failed: Name is empty");
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Name is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            if (mobile == null || mobile.length() != 10) {
                System.err.println("Validation failed: Invalid mobile number");
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Valid 10-digit mobile number is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            if (address == null || address.trim().isEmpty()) {
                System.err.println("Validation failed: Address is empty");
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Address is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            // Handle product list
            List<String> productList = new ArrayList<>();
            if (productListObj instanceof List) {
                productList = (List<String>) productListObj;
            }
            
            // Handle total amount
            Double totalAmount = 0.0;
            if (totalAmountObj != null) {
                if (totalAmountObj instanceof Number) {
                    totalAmount = ((Number) totalAmountObj).doubleValue();
                } else {
                    totalAmount = Double.parseDouble(totalAmountObj.toString());
                }
            }
            
            if (totalAmount <= 0) {
                System.err.println("Validation failed: Invalid total amount");
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Valid total amount is required");
                return ResponseEntity.badRequest().body(errorResponse);
            }
            
            System.out.println("Validation passed. Creating order entity...");
            
            // Create new order entity
            Orders order = new Orders();
            order.setCustomerName(name.trim());
            order.setMobileNo(mobile.trim());
            order.setCustomerAddress(address.trim());
            order.setProductList(productList);
            order.setAmount(totalAmount);
            order.setDateTime(LocalDateTime.now());
            order.setStatus("Pending Payment");
            
            System.out.println("Order entity created: " + order);
            System.out.println("Attempting to save to database...");
            
            // Save to database
            Orders savedOrder = ordersRepository.save(order);
            System.out.println("✅ Order saved successfully with ID: " + savedOrder.getId());
            System.out.println("=== ORDER CREATION SUCCESS ===");
            
            return ResponseEntity.ok(savedOrder);
            
        } catch (Exception e) {
            System.err.println("❌ ERROR CREATING ORDER:");
            System.err.println("Error message: " + e.getMessage());
            System.err.println("Error class: " + e.getClass().getSimpleName());
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to create order");
            errorResponse.put("message", e.getMessage());
            errorResponse.put("type", e.getClass().getSimpleName());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // Search orders by mobile number
    @GetMapping("/search")
    public ResponseEntity<?> searchOrdersByMobile(@RequestParam String mobile) {
        try {
            List<Orders> orders = ordersRepository.findByMobileNoContaining(mobile);
            return ResponseEntity.ok(orders);
        } catch (Exception e) {
            System.err.println("Error searching orders: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to search orders");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // Get order by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getOrderById(@PathVariable Long id) {
        try {
            Optional<Orders> order = ordersRepository.findById(id);
            if (order.isPresent()) {
                return ResponseEntity.ok(order.get());
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Order not found");
                errorResponse.put("message", "Order with ID " + id + " not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            System.err.println("Error fetching order by ID: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to fetch order");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // Update order status
    @PutMapping("/{id}/status")
    public ResponseEntity<?> updateOrderStatus(@PathVariable Long id, @RequestBody Map<String, String> request) {
        try {
            Optional<Orders> orderOpt = ordersRepository.findById(id);
            if (orderOpt.isPresent()) {
                Orders order = orderOpt.get();
                order.setStatus(request.get("status"));
                Orders updatedOrder = ordersRepository.save(order);
                return ResponseEntity.ok(updatedOrder);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Order not found");
                errorResponse.put("message", "Order with ID " + id + " not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            System.err.println("Error updating order status: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to update order status");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }

    // Delete order
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteOrder(@PathVariable Long id) {
        try {
            if (ordersRepository.existsById(id)) {
                ordersRepository.deleteById(id);
                Map<String, String> response = new HashMap<>();
                response.put("message", "Order deleted successfully");
                return ResponseEntity.ok(response);
            } else {
                Map<String, String> errorResponse = new HashMap<>();
                errorResponse.put("error", "Order not found");
                errorResponse.put("message", "Order with ID " + id + " not found");
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
            }
        } catch (Exception e) {
            System.err.println("Error deleting order: " + e.getMessage());
            e.printStackTrace();
            
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("error", "Failed to delete order");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(errorResponse);
        }
    }
}
