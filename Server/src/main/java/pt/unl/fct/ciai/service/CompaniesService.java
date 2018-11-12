package pt.unl.fct.ciai.service;

import org.springframework.stereotype.Service;
import pt.unl.fct.ciai.utils.Utils;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.Company;
import pt.unl.fct.ciai.model.Employee;
import pt.unl.fct.ciai.repository.CompaniesRepository;
import pt.unl.fct.ciai.repository.EmployeesRepository;

import java.util.Optional;

@Service
public class CompaniesService {

    private final CompaniesRepository companiesRepository;
    private final EmployeesRepository employeesRepository;

    public CompaniesService(CompaniesRepository companiesRepository, EmployeesRepository employeesRepository) {
        this.companiesRepository = companiesRepository;
        this.employeesRepository = employeesRepository;
    }

    public Iterable<Company> getCompanies(String search) {
        return search == null ?
                companiesRepository.findAll() :
                companiesRepository.searchCompanies(search);
    }

    public Company addCompany(Company company) {
        return companiesRepository.save(company);
    }

    public Optional<Company> getCompany(long id) {
        return companiesRepository.findById(id);
    }

    public void updateCompany(Company newCompany) {
        Company company = getCompanyIfPresent(newCompany.getId());
        Utils.copyNonNullProperties(newCompany, company);
        companiesRepository.save(company);
    }

    public void deleteCompany(long id) {
        Company company = getCompanyIfPresent(id);
        companiesRepository.delete(company);
    }

    public Iterable<Employee> getEmployees(long id, String search) {
        return search ==  null ?
                companiesRepository.getEmployees(id) :
                companiesRepository.searchEmployees(id, search);
    }

    public Employee addEmployee(long cid, Employee employee) {
        Company company = getCompanyIfPresent(cid);
        employee.setCompany(company);
        return employeesRepository.save(employee);
    }

    public Optional<Employee> getEmployee(long cid, long eid) {
        return Optional.ofNullable(companiesRepository.getEmployee(cid, eid));
    }

    public Employee updateEmployee(long cid, Employee newEmployee) {
        getCompanyIfPresent(cid);
        Employee employee = getEmployeeIfPresent(cid, newEmployee.getId());
        Utils.copyNonNullProperties(newEmployee, employee);
        return employeesRepository.save(employee);
    }

    public void deleteEmployee(long cid, long eid) {
        getCompanyIfPresent(cid); // Opcional, apenas retorna uma mensagem diferente
        Employee employee = getEmployeeIfPresent(cid, eid);
        employeesRepository.delete(employee);
    }

    private Company getCompanyIfPresent(long id) {
        return this.getCompany(id)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Company with id %d not found.", id)));
    }

    private Employee getEmployeeIfPresent(long cid, long eid) {
        return this.getEmployee(cid, eid)
                .orElseThrow(() ->
                        new NotFoundException(String.format("Employee with id %d is not part of company with id %d.", eid, cid)));
    }

}
