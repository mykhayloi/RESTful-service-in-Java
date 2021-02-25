package edu.rit.croatia.marasovic.iste341.p2;

import com.google.gson.Gson;
import companydata.DataLayer;
import companydata.Department;
import companydata.Employee;
import companydata.Timecard;

import javax.ws.rs.FormParam;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Date;
import java.util.Calendar;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Represents the Business Layer for the Web Service
 *
 * @author Kristina Marasovic <kxmzgr@rit.edu>
 */
class BusinessLayer {

    private DataLayer dl = null;
    Gson gson = null;

    public BusinessLayer() {
        try {
            this.dl = new DataLayer("development");
            this.gson = new Gson();
        } catch (Exception ex) {
            Logger.getLogger(BusinessLayer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /*
    *|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    *Department methods
    *|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    */

    public String getDepartments(String company) {
        List<Department> departments = dl.getAllDepartment(company);
        if (departments.isEmpty()) {
            return "{\"error\":\"No records found. Either the company name is wrong or there are no departments for that company.\"}";
        }
        return gson.toJson(departments);
    }

    public String getDepartment(String company, int departmentId) {
        Department department = dl.getDepartment(company, departmentId);
        if (department == null) {
            return "{\"error\":\"No records found. Either the company name is wrong or there is no department with ID of" + departmentId + " \" }";
        }

        return gson.toJson(department);
    }

    public String updateDepartment(String company) {
        Department request = gson.fromJson(company, Department.class);

        Department existing = dl.getDepartment(request.getCompany(), request.getId());

        if (existing != null) {
            if (existing.getDeptNo().equalsIgnoreCase(request.getDeptNo())) {
                return "{\"error\":\"Unable to update the department. Department number should be unique. Please check your request body. \" }";
            }

            Department updated = dl.updateDepartment(request);

            if (updated != null) {
                return gson.toJson(updated);
            }
        }

        return "{\"error\":\"Unable to update the department. Either the department doesn't exist, or something else went wrong. Please check your request body. \" }";
    }

    public String insertDepartment(String company, String deptName, String deptNo, String location) {
        if (company.isEmpty() || deptName.isEmpty() || deptNo.isEmpty() || location.isEmpty()) {
            return "{\"error\":\"Unable to insert the department. Please check your request body and fill all the parameters. \" }";
        }

        Department request = new Department(company, deptName, deptNo, location);

        Department existing = dl.getDepartmentNo(request.getCompany(), request.getDeptNo());

        if (existing != null && existing.getDeptNo().equalsIgnoreCase(request.getDeptNo())) {
            return "{\"error\":\"Unable to insert the department. Department number should be unique. Please check your request body. \" }";
        }

        Department inserted = dl.insertDepartment(request);

        return gson.toJson(inserted);
    }

    public String deleteDepartment(String company, int departmentId) {
        Department department = dl.getDepartment(company, departmentId);

        if (department == null) {
            return "{\"error\":\"Unable to delete the department. The department with company name of " + company + " and department ID " + departmentId + " does not exist. \" }";
        }

        int deletedId = dl.deleteDepartment(company, departmentId);

        return "{\"success\":\"Department " + departmentId + " from " + company + " deleted.\" }";
    }
    
    
    
    /*
    *|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    *Employee methods
    *|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    */
    
    public String getEmployee(int employeeId) {
        Employee employee = dl.getEmployee(employeeId);

        if (employee == null) {
            return "{\"error\":\"No records found. An employee with the id of " + employeeId + " does no exist \" }";
        }

        return gson.toJson(employee);
    }
    
   public String getAllEmployees(String company) {
        List<Employee> employees = dl.getAllEmployee(company);
        if (employees.isEmpty()) {
            return "{\"error\":\"No records found. Either the company name is wrong or there are no employees for that company.\"}";
        }
        return gson.toJson(employees);
    }
   
   public String insertEmployee(String emp_name, String emp_no, String hire_date, String job, Double salary, int dept_id, int mng_id, String company) throws ParseException {
        // date formating
        java.util.Date hireDate = new SimpleDateFormat("yyyy-MM-dd").parse(hire_date);
        java.sql.Date hireDateSQL = new java.sql.Date(hireDate.getTime()); 
        
        //employee object used for later insertion
        Employee testEmp = null;
        Employee empNew = null;
        
        String validate = this.testEmployee(hireDate, mng_id, dept_id, company);
        if(!validate.equals("")){
            return validate;
        }

        empNew = new Employee(emp_name, emp_no, hireDateSQL, job, salary, dept_id, mng_id);
        try{
            testEmp = dl.insertEmployee(empNew);
        }catch(Exception ex){
            return "{\"error\":\"The Employee number should be original\" }";
        }
         if(testEmp == null){
             return "{\"error\":\"There was an error while creating the new employee\" }";
         }
         return gson.toJson(testEmp);
    }



/////////////////////////////


 public String testEmployee(Date hireDate, int mng_id, int dept_id, String company){
        //making the calendar object for future checking
        Calendar cal = Calendar.getInstance();
        cal.setTime(hireDate);
        
        //aking the time object for future checks
        long mill = System.currentTimeMillis();
        java.sql.Date currDate = new java.sql.Date(mill);
        
        //fetching departrnt with stated id
        Department dep = dl.getDepartment(company, dept_id);
        
        //fetching manager with stated id
        Employee mng = dl.getEmployee(mng_id);
        
        
        //chechking if department exists
        if(dep == null){
            return "{\"error\":\"Unable to find the department. Departent " + dept_id + " does not exist. \" }";
        }else{
            //checking if manager exists
            if(mng == null && mng_id != 0){
                
                    return "{\"error\":\"Unable to find the anager. Manager " + mng_id + " does not exist. \" }";
                
            }else{
                //checking if the hire date is valid
                if(cal.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || cal.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ){
                    return "{\"error\":\"The hire day can not be a Saturday, nor Sunday\" }";
                }else{
                   if(hireDate != currDate && !hireDate.before(currDate)){
                        return "{\"error\":\"The hire day can not be a in the past, nor the current date\" }";
                    }else{
                       return "";
                   }
               } 
            }
        }
    }



///////////////////////////////////

public String updateEmployee(String company, String empId)
    {
        Employee request = gson.fromJson(empId, Employee.class);

        Employee existing = dl.getEmployee(request.getId());
        
        java.sql.Date hireDate = request.getHireDate();
        int mngId = request.getMngId();
        int deptId = request.getDeptId();
        
        String validate = this.testEmployee(hireDate, mngId, deptId, company);
        
        if(!validate.equals("")){
            return validate;
        }
        

        if (existing != null) {
            if (existing.getEmpNo().equalsIgnoreCase(request.getEmpNo())) {
                return "{\"error\":\"Unable to update the employee. Employee number should be unique. Please check your request body. \" }";
            }

            Employee updated = dl.updateEmployee(request);

            if (updated != null) {
                return gson.toJson(updated);
            }
        }

        return "{\"error\":\"Unable to update the employee. Either the employee doesn't exist, or something else went wrong. Please check your request body. \" }";

    }
   
   public String deleteEmployee(int employeeId) {
        Employee employee = dl.getEmployee(employeeId);

        if (employee == null) {
            return "{\"error\":\"Unable to delete the employee. Employee " + employeeId + " does not exist. \" }";
        }

        int deletedId = dl.deleteEmployee(employeeId);

        return "{\"success\":\"Employee " + employeeId + " deleted.\" }";
    }
   
   /*
    *|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    *Time card methods
    *|||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||||
    */
   
   public String getTimeCard(int timecardId) {
        Timecard timecard = dl.getTimecard(timecardId);

        if (timecard == null) {
            return "{\"error\":\"No records found. Timecard id " + timecardId + " does not exist \" }";
        }

        return gson.toJson(timecard);
    }
   
   public String getAllTimeCards(int empId){
        List<Timecard> timecards = dl.getAllTimecard(empId);
        if (timecards.isEmpty()) {
            return "{\"error\":\"No records found. This employee id does not exist.\"}";
        }
        return gson.toJson(timecards);
    }
   

   
public String updateTimeCard(String timecardId)
    {
        Timecard request = gson.fromJson(timecardId, Timecard.class);

        Timecard existing = dl.getTimecard(request.getId());
        
        java.sql.Timestamp startTime = request.getStartTime();
        java.sql.Timestamp endTime = request.getEndTime();
        
        String startStr = startTime.toString();
        String endStr = endTime.toString();
        
        int emp_id = request.getEmpId();
        
        String validated = this.testTimeCard(startStr, endStr, emp_id);
        
        if(validated != ""){
            return validated;
        }
        
        if (existing != null) {
            if (existing.getEmpId() == (request.getEmpId())) {
                return "{\"error\":\"Unable to update the timecard. Timecard id number should be unique. Please check your request body. \" }";
            }

            Timecard updated = dl.updateTimecard(request);

            if (updated != null) {
                return gson.toJson(updated);
            }
        }

        return "{\"error\":\"Unable to update the Timecard. Either the timecard doesn't exist, or something else went wrong. Please check your request body. \" }";

    }


///////////////////////////////////////////////////////////////////

public String testTimeCard(String start_time, String end_time, int emp_id){
       
       Employee emp = dl.getEmployee(emp_id);
       java.sql.Timestamp startTime = java.sql.Timestamp.valueOf(start_time);
       java.sql.Timestamp endTime = java.sql.Timestamp.valueOf(end_time);
       
       //creating calendar objects
       Calendar calStart = Calendar.getInstance();
       Calendar calEnd = Calendar.getInstance();
       Calendar calTest = Calendar.getInstance();
       Calendar calCurr = Calendar.getInstance();
       Calendar calWeekAgo = Calendar.getInstance();
       
       //setting the date 
       calStart.setTime(startTime);
       calEnd.setTime(endTime);
       
       //making the current date object for future checks
        long millisCurr = System.currentTimeMillis();
        java.util.Date currDate = new java.util.Date(millisCurr);
        calCurr.setTime(currDate);
        
        long DAY_IN_MS = 1000 * 60 * 60 * 24;
        java.util.Date weekAgo = new java.util.Date(System.currentTimeMillis() - (7 * DAY_IN_MS));
        calWeekAgo.setTime(weekAgo);
        
        //fetching all timecards for an eployee for future testing
        List<Timecard> timecards = dl.getAllTimecard(emp_id);
        
        
        
        //checking if employee exists
       if(emp == null){
           return "{\"error\":\"The employee with the id of " + emp_id + " does not exist.\" }";
       }
        
        //check if the date is valid
        if(calStart.after(calCurr.get(Calendar.DATE)) || calStart.before(calWeekAgo.get(Calendar.DATE))){
            return "{\"error\":\"Unable to insert timecard. The start date is invalid (either not on the current date or before a week ago) \" }";
        }
        
       //checking the day is valid
       if(calStart.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY || calStart.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY ){
           return "{\"error\":\"Unable to insert timecard on a Saturday or Sunday.\" }";
       }
       //check if both times are on a same day and that the minimal time worked is at least an hour
       if(calStart.get(Calendar.DAY_OF_WEEK) != calEnd.get(Calendar.DAY_OF_WEEK) || calEnd.get(Calendar.HOUR_OF_DAY) < calStart.get(Calendar.HOUR_OF_DAY) + 1){
           return "{\"error\":\"Unable to insert timecard. End time date should be the samme date as start time, and the job shouldn't take less then an hour.\" } ";
       }
      
       //checking if the hours are working hours
       if(calStart.get(Calendar.HOUR_OF_DAY) < 6 || calStart.get(Calendar.HOUR_OF_DAY) > 18 || calEnd.get(Calendar.HOUR_OF_DAY) < 6 || calEnd.get(Calendar.HOUR_OF_DAY) > 18) {
           return "{\"error\":\"Unable to insert timecard. Start and end time should be between 6 and 18.\" }";
       }
       
       //check if there is already timecards on this day
       for(Timecard tc : timecards){
           
           java.sql.Timestamp startTest = tc.getStartTime();
           calTest.setTime(startTest);
           if(calTest.get(Calendar.DATE) == calStart.get(Calendar.DATE)){
               return "{\"error\":\"Unable to insert timecard. Start date invalid. There can be only one timecard in one day\" }";
           }
       }
       
       return "";   
       
   
   }
   


////////////////////////////////////////////////////



   public String insertTimeCard(String start_time, String end_time, int emp_id) throws ParseException {
       
       java.sql.Timestamp startTime = java.sql.Timestamp.valueOf(start_time);
       java.sql.Timestamp endTime = java.sql.Timestamp.valueOf(end_time);
       
       Timecard inserted = null;
       String validation = this.testTimeCard(start_time, end_time, emp_id);
       
       if(validation != ""){
           return validation;
       }
       
       Timecard created = new Timecard(startTime,endTime,emp_id);
       try{
           inserted = dl.insertTimecard(created);
       }catch(Exception ex){
           return "{\"error\":\"The Timecard number should be original\" }";
       }
       
       if(inserted == null){
           return "{\"error\":\"Unable to insert timecard. There was an unexpected error .\" }";
       }
       
       return gson.toJson(inserted);   
       
   }
   
   public String deleteTimecard(int timecardId) {
        Timecard timecard = dl.getTimecard(timecardId);

        if (timecard == null) {
            return "{\"error\":\"Unable to delete the timecard. Timecard " + timecardId + " does not exist. \" }";
        }

        int deletedId = dl.deleteTimecard(timecardId);

        return "{\"success\":\"Timecard " + timecardId + " deleted.\" }";
    }
   
   public String deleteAll(String company)
   {
       List<Department> departments = dl.getAllDepartment(company);
        if (departments.isEmpty()) {
            return "{\"error\":\"This company does not exist\"}";
        }
       
       int deleteAll = dl.deleteCompany(company);
       
       return "{\"success\":\"Everything for this " + company +  " was deleted. \" }";
   }
   
    
    

}
