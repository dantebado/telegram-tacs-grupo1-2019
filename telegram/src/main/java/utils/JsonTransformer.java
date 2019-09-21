package utils;

import java.time.LocalDateTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class JsonTransformer {
	
	private static Gson gson;
	
	public static Gson getGson() {
		if(gson == null) {
			gson = new  GsonBuilder()
	            .setPrettyPrinting()
	            .create();
		}
		return gson;
	}

}
