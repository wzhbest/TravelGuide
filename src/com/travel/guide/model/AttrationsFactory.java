package com.travel.guide.model;

import java.io.IOException;

import android.util.JsonReader;

import com.travel.guide.data.HttpJsonFactoryBase;

public class AttrationsFactory extends HttpJsonFactoryBase<AttractionsInfo>{

	@Override
	protected AttractionsInfo AnalysisData(JsonReader reader) throws IOException {
		return null;
	}

	@Override
	protected String CreateUri(Object... args) {
		return null;
	}

}
