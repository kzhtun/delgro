package com.info121.mycoach.models;

import java.util.List;

public class JobRes{
	private String jobcount;
	private List<Job> jobs;
	private String responsemessage;
	private String status;
	private String token;

	public void setJobcount(String jobcount){
		this.jobcount = jobcount;
	}

	public String getJobcount(){
		return jobcount;
	}

	public void setJobs(List<Job> jobs){
		this.jobs = jobs;
	}

	public List<Job> getJobs(){
		return jobs;
	}

	public void setResponsemessage(String responsemessage){
		this.responsemessage = responsemessage;
	}

	public String getResponsemessage(){
		return responsemessage;
	}

	public void setStatus(String status){
		this.status = status;
	}

	public String getStatus(){
		return status;
	}

	public void setToken(String token){
		this.token = token;
	}

	public String getToken(){
		return token;
	}

	@Override
 	public String toString(){
		return 
			"JobRes{" + 
			"jobcount = '" + jobcount + '\'' + 
			",jobs = '" + jobs + '\'' + 
			",responsemessage = '" + responsemessage + '\'' + 
			",status = '" + status + '\'' + 
			",token = '" + token + '\'' + 
			"}";
		}
}