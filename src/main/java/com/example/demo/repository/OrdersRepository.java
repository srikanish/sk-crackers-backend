package com.example.demo.repository;

import com.example.demo.model.Orders;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface OrdersRepository extends JpaRepository<Orders, Long> {
    
    // Find orders by mobile number
    List<Orders> findByMobileNoContaining(String mobileNo);
    
    // Find orders by customer name
    List<Orders> findByCustomerNameContainingIgnoreCase(String customerName);
    
    // Find orders by status
    List<Orders> findByStatus(String status);
    
    // Get all orders ordered by date (newest first)
    @Query("SELECT o FROM Orders o ORDER BY o.dateTime DESC")
    List<Orders> findAllOrderedByDateDesc();
}
