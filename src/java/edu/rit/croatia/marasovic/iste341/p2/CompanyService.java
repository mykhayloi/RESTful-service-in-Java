package edu.rit.croatia.marasovic.iste341.p2;

import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.text.ParseException;

/**
 * RESTful Web Service - Timecards Tracker
 *
 * @author Kristina Marasovic <kxmzgr@rit.edu>
 */
@Path("CompanyService")
public class CompanyService {

    BusinessLayer bl = null;

    @Context
    private UriInfo context;

    /**
     * Creates a new instance of CompanyService
     */
    public CompanyService() {
        this.bl = new BusinessLayer();
    }

    /**
     * Gets all departments of a company. 
     * 
     * @param company company name like kxmzgr (RIT username)
     * @return an instance of java.lang.String
     */
    @GET
    @Path("departments")
    @Produces(MediaType.APPLICATION_JSON)
    public String getDepartments(@QueryParam("company") String company) {
        return bl.getDepartments(company);
    }

    /**
     * Gets a specific department by the company name and department ID.
     *
     * @param company
     * @param deptId
     * @return
     */
    @Path("department")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getDepartment(@QueryParam("company") String company, @QueryParam("dept_id") int deptId) {
        return bl.getDepartment(company, deptId);
    }

    /**
     * Updates a department.
     *
     * @param department
     * @return
     */
    @Path("department")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateDepartment(String department) {
        return bl.updateDepartment(department);
    }

    /**
     * Inserts a new department.
     *
     * @param company
     * @param deptName
     * @param deptNo
     * @param location
     * @return
     */
    @Path("department")
    @POST //Used to delete an existing resource
    @Produces(MediaType.APPLICATION_JSON)
    public String insertDepartment(@FormParam("company") String company, @FormParam("dept_name") String deptName, @FormParam("dept_no") String deptNo, @FormParam("location") String location) {
        return bl.insertDepartment(company, deptName, deptNo, location);
    }

    /**
     * Deletes a department by company name and department ID.
     * 
     * @param company
     * @param deptId
     * @return 
     */
    @Path("department")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteDepartment(@QueryParam("company") String company, @QueryParam("dept_id") int deptId) {
        return bl.deleteDepartment(company, deptId);
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * Gets individual employee
     * @param company
     * @return 
     */
    
    @Path("employee")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getEmployee(@QueryParam("employee_id") int empId) {
        return bl.getEmployee(empId);
    }
    
    /**
     * Gets all the employees for a specific company
     * @param company
     * @return 
     */
    
    @Path("employees")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllEmployee(@QueryParam("company") String company) {
        return bl.getAllEmployees(company);
    }
    
    /**
     * Updates the employee 
     * @param employee
     * @return 
     */
    @Path("employee")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateEmployee(@QueryParam("company") String company,String employee) {
        return bl.updateEmployee(company,employee);
    }
    
    @Path("employee")
    @POST //Used to delete an existing resource
    @Produces(MediaType.APPLICATION_JSON)
    public String insertEmployee(@FormParam("emp_name") String emp_name, @FormParam("emp_no") String emp_no, @FormParam("hire_date") String hire_date, @FormParam("job") String job,@FormParam("salary") double salary,@FormParam("dept_id") int dept_id,@FormParam("mng_id") int mng_id,@FormParam("company") String company) throws ParseException {
        return bl.insertEmployee(emp_name, emp_no, hire_date, job, salary, dept_id,mng_id,company);
    }
    
    
    
    /**
     * Deletes an employee
     * @param empId
     * @return 
     */
    @Path("employee")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteEmployee(@QueryParam("empId") int empId) {
        return bl.deleteEmployee(empId);
    }
    
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    /**
     * return the time card by specific timecard ids
     * @param company
     * @param empId
     * @return 
     */
    @Path("timecard")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getTimecard(@QueryParam("timecard") int timecard_id) {
        return bl.getTimeCard(timecard_id);
    }
    
    /**
     * returns all time cards for specific employee
     * @param emp_id
     * @return 
     */
    @Path("timecards")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getAllTimecard(@QueryParam("emp_id") int emp_id) {
        return bl.getAllTimeCards(emp_id);
    }
    
    /**
     * Updates a time card
     * @param timecard
     * @return 
     */
    @Path("timecard")
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public String updateTimeCard(String timecard) {
        return bl.updateTimeCard(timecard);
    }
    
      @Path("timecard")
    @POST //Used to delete an existing resource
    @Produces(MediaType.APPLICATION_JSON)
    public String insertTimecard(@FormParam("start_time") String start_time, @FormParam("end_time") String end_time, @FormParam("emp_id") int emp_id) throws ParseException {
        return bl.insertTimeCard(start_time, end_time, emp_id);
    }
    
    
    /**
     * Deletes an employee
     * @param empId
     * @return 
     */
    
    @Path("timecard")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteTimecard(@QueryParam("timecard_id") int timecard_id) {
        return bl.deleteTimecard(timecard_id);
    }
    
    @Path("deleteAll")
    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public String deleteAll(@QueryParam("company") String company) {
        return bl.deleteAll(company);
    }
    
    
    
    
}
