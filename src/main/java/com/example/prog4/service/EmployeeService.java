package com.example.prog4.service;

import com.example.prog4.model.EmployeeFilter;
import com.example.prog4.model.exception.NotFoundException;
import com.example.prog4.repository.db1.EmployeeRepository;
import com.example.prog4.repository.db1.dao.EmployeeManagerDao;
import com.example.prog4.repository.db1.entity.Employee;
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
    private CnapsService cnapsService;
    private com.example.prog4.repository.RepositoryImpl repositoryImpl;


    public Employee getOne(String id) {
        Employee employee = repository.findById(id)
          .orElseThrow(() -> new NotFoundException("Not found id=" + id));

        // Obtenir le numéro CNAPS actuel depuis la base de données CNAPS
        String cnapsNumber = cnapsService.getCnapsNumber(employee.getPersonalEmail());

        // Mettre à jour l'employé avec le numéro CNAPS
        employee.setCnaps(cnapsNumber);

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
        repositoryImpl.save(employee);
        repositoryImpl.saveToCnaps(employee);
    }


}
