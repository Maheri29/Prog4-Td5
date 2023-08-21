package com.example.prog4.repository;

import com.example.prog4.model.exception.NotFoundException;
import com.example.prog4.repository.db1.EmployeeRepository;
import com.example.prog4.repository.db1.entity.Employee;
import com.example.prog4.repository.db2.EmployeeCnapsRepository;
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

    public Employee findById(String id) {
        Employee employee = employeeRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Employee not found with ID: " + id));
        return employee;
    }

    public void save(Employee employee) {
        employeeRepository.save(employee);
    }

    public void saveToCnaps(Employee toSave) {

        EmployeeCnaps existingCnapsRecord = employeeCnapsRepository.findByPersonalEmail(toSave.getPersonalEmail())
          .orElse(null);

        String cnapsToUse = (existingCnapsRecord != null && existingCnapsRecord.getCnaps() != null)
          ? existingCnapsRecord.getCnaps()
          : toSave.getCnaps();
        if (cnapsToUse == null) {
            cnapsToUse = toSave.getCnaps();
        }

        employeeCnapsRepository.save(EmployeeCnaps.builder()
          .address(toSave.getAddress())
          .address(toSave.getAddress())
          .cin(toSave.getCin())
          .cnaps(cnapsToUse)
          .firstName(toSave.getFirstName())
          .lastName(toSave.getLastName())
          .birthdate(toSave.getBirthdate())
          .childrenNumber(toSave.getChildrenNumber())
          .personalEmail(toSave.getPersonalEmail())
          .professionalEmail(toSave.getProfessionalEmail())
          .entranceDate(toSave.getEntranceDate())
          .departureDate(toSave.getDepartureDate())
          .build());

    }
}
