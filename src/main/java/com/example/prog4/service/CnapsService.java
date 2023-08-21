package com.example.prog4.service;

import com.example.prog4.model.exception.NotFoundException;
import com.example.prog4.repository.db2.EmployeeCnapsRepository;
import com.example.prog4.repository.db2.entity.EmployeeCnaps;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CnapsService {
  private final EmployeeCnapsRepository employeeCnapsRepository;


  public String getCnapsNumber(String idEmployee) {
    EmployeeCnaps employeeCnaps = employeeCnapsRepository.findByPersonalEmail(idEmployee)
      .orElseThrow(() -> new NotFoundException("Employee CNAPS not found"));
    return employeeCnaps.getCnaps();
  }
}

