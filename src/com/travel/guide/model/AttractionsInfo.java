package com.travel.guide.model;

import java.util.ArrayList;

public class AttractionsInfo {

	public String telephone;
	public String date;
	public String name;

	public double lng;
	public double lat;
	public String star;
	public String url;
	public String abstractSummary;
	public String description;

	public String price;
	public String openTime;
	public ArrayList<Attention> attention = new ArrayList<Attention>();

	public static class Attention {
		public String name;
		public String description;
	}
}
