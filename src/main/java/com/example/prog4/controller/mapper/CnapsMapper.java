package com.example.prog4.controller.mapper;


import com.example.prog4.repository.db1.entity.Employee;
import com.example.prog4.repository.db2.entity.EmployeeCnaps;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
@Transactional
public class CnapsMapper {

  public EmployeeCnaps toEmployeeCnaps(Employee employee){

    return EmployeeCnaps.builder()
      .id(employee.getId())
      .address(employee.getAddress())
      .address(employee.getAddress())
      .cin(employee.getCin())
      .cnaps(employee.getCnaps())
      .firstName(employee.getFirstName())
      .lastName(employee.getLastName())
      .birthdate(employee.getBirthdate())
      .childrenNumber(employee.getChildrenNumber())
      .personalEmail(employee.getPersonalEmail())
      .professionalEmail(employee.getProfessionalEmail())
      .entranceDate(employee.getEntranceDate())
      .departureDate(employee.getDepartureDate())
      .build();
  }

}
