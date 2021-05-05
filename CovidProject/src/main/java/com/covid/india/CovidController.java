package com.covid.india;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
public class CovidController {

	@GetMapping("/covid19/state")
	public Map getCovid19StateData() {
		Map response = new HashMap();
		String uri = "https://api.covid19india.org/state_district_wise.json";
		RestTemplate restTemplate = new RestTemplate();
		Map<Object, Object> result = (Map<Object, Object>) restTemplate.getForObject(uri, Map.class);

		for (Map.Entry<Object, Object> entry : result.entrySet()) {
			if (!entry.getKey().toString().equalsIgnoreCase("State Unassigned"))
				System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
		}
		return result;
	}

	@GetMapping("/covid19/india")
	public Map getCovid19IndiaData() {
		Map response = new HashMap();
		Map stateDataMap = null;
		String uri = "https://api.covid19india.org/data.json";
		RestTemplate restTemplate = new RestTemplate();
		Map<Object, Object> result = (Map<Object, Object>) restTemplate.getForObject(uri, Map.class);

		for (Map.Entry<Object, Object> entry : result.entrySet()) {
			if (entry.getKey().toString().equalsIgnoreCase("statewise")) {
				List<Map<String, Object>> totalStateDataValues = (List) entry.getValue();
				for (Map<String, Object> values : totalStateDataValues) {
					stateDataMap = new HashMap();
					stateDataMap.put("active", values.get("active"));
					stateDataMap.put("confirmed", values.get("confirmed"));
					stateDataMap.put("deaths", values.get("deaths"));
					stateDataMap.put("recovered", values.get("recovered"));
					stateDataMap.put("state", values.get("state"));
					stateDataMap.put("statecode", values.get("statecode"));
					response.put(values.get("state"), stateDataMap);
				}
			}
		}
		return response;
	}

}
