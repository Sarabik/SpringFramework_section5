package com.springframework.section5.repository;

import com.springframework.section5.entity.BeerOrderShipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BeerOrderShipmentRepository extends JpaRepository<BeerOrderShipment, String> {
}