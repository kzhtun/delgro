package com.info121.coach.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Job {
	@SerializedName("BookNo")
	@Expose
	public String bookNo;
	@SerializedName("Customer")
	@Expose
	public String customer;
	@SerializedName("Customer_Tel")
	@Expose
	public String customerTel;
	@SerializedName("Destination")
	@Expose
	public String destination;
	@SerializedName("JobCat")
	@Expose
	public String jobCat;
	@SerializedName("JobNo")
	@Expose
	public String jobNo;
	@SerializedName("JobStatus")
	@Expose
	public String jobStatus;
	@SerializedName("JobType")
	@Expose
	public String jobType;
	@SerializedName("PickUp")
	@Expose
	public String pickUp;
	@SerializedName("PickUpTime")
	@Expose
	public String pickUpTime;
	@SerializedName("StatusOrder")
	@Expose
	public String statusOrder;
	@SerializedName("UsageDate")
	@Expose
	public String usageDate;
	@SerializedName("VehicleType")
	@Expose
	public String vehicleType;

	public void setCustomerTel(String customerTel){
		this.customerTel = customerTel;
	}

	public String getCustomerTel(){
		return customerTel;
	}

	public void setUsageDate(String usageDate){
		this.usageDate = usageDate;
	}

	public String getUsageDate(){
		return usageDate;
	}

	public void setDestination(String destination){
		this.destination = destination;
	}

	public String getDestination(){
		return destination;
	}

	public void setCustomer(String customer){
		this.customer = customer;
	}

	public String getCustomer(){
		return customer;
	}

	public void setPickUp(String pickUp){
		this.pickUp = pickUp;
	}

	public String getPickUp(){
		return pickUp;
	}

	public void setVehicleType(String vehicleType){
		this.vehicleType = vehicleType;
	}

	public String getVehicleType(){
		return vehicleType;
	}

	public void setJobCat(String jobCat){
		this.jobCat = jobCat;
	}

	public String getJobCat(){
		return jobCat;
	}

	public void setJobStatus(String jobStatus){
		this.jobStatus = jobStatus;
	}

	public String getJobStatus(){
		return jobStatus;
	}

	public void setJobType(String jobType){
		this.jobType = jobType;
	}

	public String getJobType(){
		return jobType;
	}

	public void setStatusOrder(String statusOrder){
		this.statusOrder = statusOrder;
	}

	public String getStatusOrder(){
		return statusOrder;
	}

	public void setJobNo(String jobNo){
		this.jobNo = jobNo;
	}

	public String getJobNo(){
		return jobNo;
	}

	public void setPickUpTime(String pickUpTime){
		this.pickUpTime = pickUpTime;
	}

	public String getPickUpTime(){
		return pickUpTime;
	}

	public void setBookNo(String bookNo){
		this.bookNo = bookNo;
	}

	public String getBookNo(){
		return bookNo;
	}

	@Override
 	public String toString(){
		return 
			"Job{" +
			"customer_Tel = '" + customerTel + '\'' + 
			",usageDate = '" + usageDate + '\'' + 
			",destination = '" + destination + '\'' + 
			",customer = '" + customer + '\'' + 
			",pickUp = '" + pickUp + '\'' + 
			",vehicleType = '" + vehicleType + '\'' + 
			",jobCat = '" + jobCat + '\'' + 
			",jobStatus = '" + jobStatus + '\'' + 
			",jobType = '" + jobType + '\'' + 
			",statusOrder = '" + statusOrder + '\'' + 
			",jobNo = '" + jobNo + '\'' + 
			",pickUpTime = '" + pickUpTime + '\'' + 
			",bookNo = '" + bookNo + '\'' + 
			"}";
		}
}
