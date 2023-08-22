package com.example.prog4.repository;

import com.example.prog4.repository.db1.EmployeeRepository;
import com.example.prog4.repository.db2.EmployeeCnapsRepository;
import com.example.prog4.repository.db1.entity.Employee;
import com.example.prog4.repository.db2.entity.EmployeeCnaps;
import org.springframework.stereotype.Repository;

@Repository
public class RepositoryImpl {

  private final EmployeeRepository employeeRepository;
  private final EmployeeCnapsRepository employeeCnapsRepository;

  public RepositoryImpl(EmployeeRepository employeeRepository, EmployeeCnapsRepository employeeCnapsRepository) {
    this.employeeRepository = employeeRepository;
    this.employeeCnapsRepository = employeeCnapsRepository;
  }

  public Employee findByIdEmployee(String id) {
    return employeeRepository.findById(id).orElse(null);
  }

  public Employee saveEmployee(Employee employee) {
   return employeeRepository.save(employee);
  }

  public EmployeeCnaps findByIdEmployeeCnaps(String id) {
    return employeeCnapsRepository.findById(id).orElse(null);
  }

  public EmployeeCnaps saveEmployeeCnaps(EmployeeCnaps employeeCnaps) {
   return employeeCnapsRepository.save(employeeCnaps);
  }
}
