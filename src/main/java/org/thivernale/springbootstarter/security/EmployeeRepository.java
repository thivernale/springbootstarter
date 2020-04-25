package org.thivernale.springbootstarter.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thivernale.springbootstarter.security.models.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Integer> {
}
