package com.example.prog4.service;

import com.example.prog4.controller.mapper.CnapsMapper;
import com.example.prog4.model.EmployeeFilter;
import com.example.prog4.model.exception.NotFoundException;
import com.example.prog4.repository.db1.EmployeeRepository;
import com.example.prog4.repository.db1.dao.EmployeeManagerDao;
import com.example.prog4.repository.db1.entity.Employee;
import com.example.prog4.repository.db2.entity.EmployeeCnaps;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class EmployeeService {
    private EmployeeRepository repository;
    private EmployeeManagerDao employeeManagerDao;
    private CnapsMapper cnapsMapper;
    private com.example.prog4.repository.RepositoryImpl repositoryImpl;



    public Employee getOne(String id) {
        Employee employee = repository.findById(id)
          .orElseThrow(() -> new NotFoundException("Not found id=" + id));

        // Récupérez le numéro CNAPS depuis la table "employee_cnaps"
        EmployeeCnaps cnapsData = repositoryImpl.findByIdEmployeeCnaps(id);

        // Mettez à jour le numéro CNAPS de l'employé s'il est différent
        if (cnapsData != null && !employee.getCnaps().equals(cnapsData.getCnaps())) {
            employee.setCnaps(cnapsData.getCnaps());
        }

        return employee;
    }

    @Transactional
    public List<Employee> getAll(EmployeeFilter filter) {
        Sort sort = Sort.by(filter.getOrderDirection(), filter.getOrderBy().toString());
        Pageable pageable = PageRequest.of(filter.getIntPage() - 1, filter.getIntPerPage(), sort);
        return employeeManagerDao.findByCriteria(
                filter.getLastName(),
                filter.getFirstName(),
                filter.getCountryCode(),
                filter.getSex(),
                filter.getPosition(),
                filter.getEntrance(),
                filter.getDeparture(),
                pageable
        );
    }

    public void saveOne(Employee employee) {
        // Enregistrez l'employé dans la table employee
        Employee savedEmployee = repositoryImpl.saveEmployee(employee);

        // Récupérez l'ID généré pour l'employé
        String employeeId = savedEmployee.getId();

        // Créez un objet EmployeeCnaps avec le même ID que l'employé
        EmployeeCnaps employeeCnaps = cnapsMapper.toEmployeeCnaps(savedEmployee);

        // Assurez-vous que l'ID est le même pour l'employé dans employee_cnaps
        employeeCnaps.setId(employeeId);

        // Récupérez l'objet EmployeeCnaps correspondant depuis la base de données
        EmployeeCnaps existingEmployeeCnaps = repositoryImpl.findByIdEmployeeCnaps(employeeId);


        // Empêchez la modification du numéro CNAPS s'il existe déjà dans la table employee_cnaps
        if (existingEmployeeCnaps != null) {
            employeeCnaps.setCnaps(existingEmployeeCnaps.getCnaps());
        }

        // Enregistrez l'objet EmployeeCnaps dans la table employee_cnaps
        repositoryImpl.saveEmployeeCnaps(employeeCnaps);
    }


}
