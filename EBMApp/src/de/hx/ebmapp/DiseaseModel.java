package de.hx.ebmapp;

import java.util.ArrayList;

public class DiseaseModel {
	String deathRate;
	String code;
	int id;
	String name;
	String probability;
	String reliability;
	ArrayList<SymptomModel> symptomsArray;
	TreatmentModel treatment;
	
	public DiseaseModel(String deathRate, String code, int id,
			String name, String probability, String reliability,
			ArrayList<SymptomModel> symptomsArray, TreatmentModel treatment) {
		this.deathRate = deathRate;
		this.code = code;
		this.id = id;
		this.name = name;
		this.probability = probability;
		this.reliability = reliability;
		this.symptomsArray = symptomsArray;
		this.treatment = treatment;
	}
	public String toString(){
		return this.name;
	}
}