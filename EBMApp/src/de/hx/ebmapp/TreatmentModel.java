package de.hx.ebmapp;

import java.io.Serializable;

//import android.os.Parcel;
//import android.os.Parcelable;

public class TreatmentModel implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String description;
	String name;
	String source;
	String sourceClean;
	public TreatmentModel(String description, String name, String source,
			String sourceClean) {
		super();
		this.description = description;
		this.name = name;
		this.source = source;
		this.sourceClean = sourceClean;
	}
	
//	private TreatmentModel(Parcel in) {
//		description = in.readString();
//		name = in.readString();
//		source = in.readString();
//		sourceClean = in.readString();
//	}
//
//	public static final Parcelable.Creator<TreatmentModel> CREATOR = new Parcelable.Creator<TreatmentModel>() {
//		public TreatmentModel createFromParcel(Parcel in) {
//			return new TreatmentModel(in);
//		}
//
//		public TreatmentModel[] newArray(int size) {
//			return new TreatmentModel[size];
//		}
//	};
//
//	@Override
//	public int describeContents() {
//		return 0;
//	}
//
//	@Override
//	public void writeToParcel(Parcel dest, int flags) {
//		dest.writeString(description);
//		dest.writeString(name);
//		dest.writeString(source);
//		dest.writeString(sourceClean);
//	}
//	
//	public String toString(){
//		return name;
//	}
}
