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
    private com.example.prog4.repository.RepositoryImpl repositoryImpl;




    public Employee getOne(String id) {
        Employee employee = repository.findById(id)
          .orElseThrow(() -> new NotFoundException("Not found id=" + id));


        // Récupérez le numéro CNAPS depuis la table "employee_cnaps"
        //This method search the end_to_end_id
        EmployeeCnaps cnapsData = repositoryImpl.findByIdEmployeeCnaps(employee.getId()).orElse(null);

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
        String cnapsNumber = "(that employee doesn't have CNAPS number yet)";

        if (employee.getId() != null && !employee.getId().isEmpty()) {
            // The end_to_end_id is the employee name and his cnaps number
            String endToEndId = employee.getId();
            // This method searches the end_to_end_id
            EmployeeCnaps cnapsData = repositoryImpl.findByIdEmployeeCnaps(endToEndId).orElse(null);

            if (cnapsData != null) {
                cnapsNumber = cnapsData.getCnaps();
            }
        }

        employee.setCnaps(cnapsNumber);
        repositoryImpl.saveEmployee(employee);
    }



}
