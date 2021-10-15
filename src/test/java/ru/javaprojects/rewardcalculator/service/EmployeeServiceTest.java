package ru.javaprojects.rewardcalculator.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.javaprojects.rewardcalculator.model.Employee;
import ru.javaprojects.rewardcalculator.repository.EmployeeRepository;
import ru.javaprojects.rewardcalculator.to.EmployeeTo;
import ru.javaprojects.rewardcalculator.util.exception.NotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static ru.javaprojects.rewardcalculator.DepartmentTestData.DEPARTMENT_1_ID;
import static ru.javaprojects.rewardcalculator.EmployeeTestData.NOT_FOUND;
import static ru.javaprojects.rewardcalculator.EmployeeTestData.getNew;
import static ru.javaprojects.rewardcalculator.EmployeeTestData.getNewTo;
import static ru.javaprojects.rewardcalculator.EmployeeTestData.getUpdated;
import static ru.javaprojects.rewardcalculator.EmployeeTestData.getUpdatedTo;
import static ru.javaprojects.rewardcalculator.EmployeeTestData.*;
import static ru.javaprojects.rewardcalculator.PositionTestData.*;

class EmployeeServiceTest extends AbstractServiceTest {

    @Autowired
    private EmployeeService service;

    @Autowired
    private EmployeeRepository repository;

    @Test
    void create() {
        Employee created = service.create(getNewTo());
        int newId = created.id();
        Employee newEmployee = getNew();
        newEmployee.setId(newId);
        EMPLOYEE_MATCHER.assertMatch(created, newEmployee);
        EMPLOYEE_MATCHER.assertMatch(service.get(newId), newEmployee);
    }

    @Test
    void createWithNotExistedPosition() {
        EmployeeTo newEmployeeTo = getNewTo();
        newEmployeeTo.setPositionId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.create(newEmployeeTo));
    }

    @Test
    void get() {
        Employee employee = service.get(EMPLOYEE_1_ID);
        EMPLOYEE_MATCHER.assertMatch(employee, employee1);
    }

    @Test
    void getNotFound() {
        assertThrows(NotFoundException.class, () -> service.get(NOT_FOUND));
    }

    @Test
    void getAllByDepartmentId() {
        List<Employee> employees = service.getAllByDepartmentId(DEPARTMENT_1_ID);
        EMPLOYEE_MATCHER.assertMatch(employees, employee1, employee2, employee3);
    }

    @Test
    void getAllByDepartmentIdWithNotExistedDepartment() {
        assertThrows(NotFoundException.class, () -> service.getAllByDepartmentId(NOT_FOUND));
    }

    @Test
    void delete() {
        service.delete(EMPLOYEE_1_ID);
        assertThrows(NotFoundException.class, () -> service.get(EMPLOYEE_1_ID));
    }

    @Test
    void deleteNotFound() {
        assertThrows(NotFoundException.class, () -> service.delete(NOT_FOUND));
    }

    @Test
    void update() {
        service.update(getUpdatedTo());
        EMPLOYEE_MATCHER.assertMatch(service.get(EMPLOYEE_1_ID), getUpdated());
    }

    @Test
    void updateWithPositionChanging() {
        EmployeeTo updatedTo = getUpdatedTo();
        updatedTo.setPositionId(POSITION_3_ID);
        service.update(updatedTo);
        Employee employee = repository.findByIdWithPosition(EMPLOYEE_1_ID);
        EMPLOYEE_MATCHER.assertMatch(employee, getUpdated());
        POSITION_MATCHER.assertMatch(employee.getPosition(), position3);
    }

    @Test
    void updateNotFound() {
        EmployeeTo updatedTo = getUpdatedTo();
        updatedTo.setId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.update(updatedTo));
    }

    @Test
    void updateWithNotExistedPosition() {
        EmployeeTo updatedTo = getUpdatedTo();
        updatedTo.setPositionId(NOT_FOUND);
        assertThrows(NotFoundException.class, () -> service.update(updatedTo));
    }

    @Test
    void createWithException() {
        validateRootCause(ConstraintViolationException.class, () -> service.create(new EmployeeTo(null, " ", POSITION_1_ID)));
    }
}