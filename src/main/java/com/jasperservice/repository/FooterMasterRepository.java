package com.jasperservice.repository;

import com.jasperservice.entity.FooterMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping
public interface FooterMasterRepository extends JpaRepository<FooterMaster, Integer> {
}
