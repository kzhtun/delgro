package com.info121.delgro.models;

public class JobCount {
	private String tomorrowjobcount;
	private String todayjobcount;
	private String futurejobcount;

	public void setTomorrowjobcount(String tomorrowjobcount){
		this.tomorrowjobcount = tomorrowjobcount;
	}

	public String getTomorrowjobcount(){
		return tomorrowjobcount;
	}

	public void setTodayjobcount(String todayjobcount){
		this.todayjobcount = todayjobcount;
	}

	public String getTodayjobcount(){
		return todayjobcount;
	}

	public void setFuturejobcount(String futurejobcount){
		this.futurejobcount = futurejobcount;
	}

	public String getFuturejobcount(){
		return futurejobcount;
	}

	@Override
 	public String toString(){
		return 
			"JobCount{" +
			"tomorrowjobcount = '" + tomorrowjobcount + '\'' + 
			",todayjobcount = '" + todayjobcount + '\'' + 
			",futurejobcount = '" + futurejobcount + '\'' + 
			"}";
		}
}
