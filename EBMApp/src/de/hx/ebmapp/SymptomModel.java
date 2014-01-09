package de.hx.ebmapp;

import android.os.Parcel;
import android.os.Parcelable;

public class SymptomModel implements Parcelable {
	String name;
	String percentage;
	String percentageLong;
	String source;
	String url;

	public SymptomModel(String name, String percentage, String percentageLong,
			String source, String url) {
		super();
		this.name = name;
		this.percentage = percentage;
		this.percentageLong = percentageLong;
		this.source = source;
		this.url = url;
	}

	private SymptomModel(Parcel in) {
		name = in.readString();
		percentage = in.readString();
		percentageLong = in.readString();
		source = in.readString();
		url = in.readString();
	}

	public static final Parcelable.Creator<SymptomModel> CREATOR = new Parcelable.Creator<SymptomModel>() {
		public SymptomModel createFromParcel(Parcel in) {
			return new SymptomModel(in);
		}

		public SymptomModel[] newArray(int size) {
			return new SymptomModel[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeString(percentage);
		dest.writeString(percentageLong);
		dest.writeString(source);
		dest.writeString(url);
	}
	
	public String toString(){
		return name;
	}
}
