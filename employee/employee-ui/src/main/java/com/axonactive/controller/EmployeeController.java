package com.axonactive.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.persistence.NoResultException;

import org.primefaces.PrimeFaces;

import com.axonactive.converter.DepartmentConverter;
import com.axonactive.dto.DepartmentDTO;
import com.axonactive.dto.EmployeeDTO;
import com.axonactive.entity.DepartmentEntity;
import com.axonactive.service.DepartmentService;
import com.axonactive.service.EmployeeService;

import lombok.Getter;
import lombok.Setter;

@SuppressWarnings("deprecation")
@ManagedBean(name = "employeeController")
@ViewScoped
public class EmployeeController implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 7889451587603628722L;

	private transient @Getter @Setter DepartmentDTO department = new DepartmentDTO();
	private transient @Getter @Setter EmployeeDTO employee = new EmployeeDTO();
	private transient @Getter @Setter DepartmentEntity departmentEntity = new DepartmentEntity();
	private @Getter @Setter int id;

	@Inject
	private EmployeeService empService;

	@Inject
	private DepartmentService depService;

	
	private transient DepartmentConverter departmentConverter = new DepartmentConverter();

	private transient @Getter @Setter List<EmployeeDTO> employeeList = new ArrayList<>();

	private transient @Getter @Setter List<DepartmentDTO> departmentList = new ArrayList<>();

	@PostConstruct
	public void init() {
		employeeList = empService.getAllEmployeeList();
		departmentList = depService.getAllDepartmentList();
		if (departmentList.isEmpty())
			throw new NoResultException("No source found");
		else
			department = departmentList.get(0);
	}

	public void addNewEmployee() {
		employee.setDepartment(department);
		empService.addEmployee(employee);
		employeeList = empService.getAllEmployeeList();	
		createNewEmployee();
		PrimeFaces.current().executeScript("PF('addEmployee').hide()");
	}

	public void updateEmployeeFromPage() {
		employee.setDepartment(department);
		empService.updateEmployee(employee);
		employeeList =empService.getAllEmployeeList();
		createNewEmployee();
		PrimeFaces.current().executeScript("PF('UpdateEmployee').hide()");
	}

	public void deleteEmployeeFromPage(Integer id) {
		empService.deleteEmployeeById(id);
		employeeList = empService.getAllEmployeeList();
	}

	public void viewEmployee(EmployeeDTO emp) {
		setEmployee(emp);
		setId(emp.getDepartment().getId());
		PrimeFaces.current().executeScript("PF('UpdateEmployee').show()");
	}

	public void changeDepartment(ValueChangeEvent dept) {
		department = departmentConverter
				.toDTO(depService.findDepartmentById(Integer.parseInt(dept.getNewValue().toString())));
	}

	private void createNewEmployee() {
		employee = new EmployeeDTO();
	}
}
