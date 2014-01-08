package de.hx.ebmapp;

public class DiseaseModel {
	String deathRate;
	String code;
	int id;
	String name;
	String probability;
	String reliability;
	
	public DiseaseModel(String deathRate, String code, int id,
			String name, String probability, String reliability) {
		this.deathRate = deathRate;
		this.code = code;
		this.id = id;
		this.name = name;
		this.probability = probability;
		this.reliability = reliability;
	}
	public String toString(){
		return this.name;
	}
}