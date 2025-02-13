package com.jasperservice.repository;

import com.jasperservice.entity.HeaderMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HeaderMasterRepository extends JpaRepository<HeaderMaster, Integer> {

}
