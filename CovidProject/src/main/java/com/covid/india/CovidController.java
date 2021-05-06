package com.covid.india;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

	@GetMapping("/covid19/district/{statename}")
	@CrossOrigin
	public List getCovid19DistrictDataByStateName(@PathVariable("statename") String stateName) {
		Map response = new HashMap();
		List finalData = new ArrayList();
		Map districtDataMap = null;
		String uri = "https://api.covid19india.org/state_district_wise.json";
		RestTemplate restTemplate = new RestTemplate();
		Map<Object, Object> result = (Map<Object, Object>) restTemplate.getForObject(uri, Map.class);
		Map<String, Object> districtMap = null;
		for (Map.Entry<Object, Object> entry : result.entrySet()) {
			if (!entry.getKey().toString().equalsIgnoreCase("State Unassigned"))
				if (entry.getKey().toString().equalsIgnoreCase(stateName)) {
					districtMap = (Map<String, Object>) entry.getValue();
					Map<String, Object> data = (Map<String, Object>) districtMap.get("districtData");
					for (Map.Entry<String, Object> entryDistrict : data.entrySet()) {
						Map finalMap = (Map) data.get(entryDistrict.getKey());
						districtDataMap = new HashMap();
						districtDataMap.put("active", finalMap.get("active"));
						districtDataMap.put("confirmed", finalMap.get("confirmed"));
						districtDataMap.put("deceased", finalMap.get("deceased"));
						districtDataMap.put("recovered", finalMap.get("recovered"));
						districtDataMap.put("district", entryDistrict.getKey());
						finalData.add(districtDataMap);
					}
					break;
				}
		}
		response.put("data", finalData);
		return finalData;
	}

	@GetMapping("/covid19/india")
	@CrossOrigin
	public List getCovid19IndiaData() {
		List finalData = new ArrayList();
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
					finalData.add(stateDataMap);
				}
			}
		}
		return finalData;
	}

	@GetMapping("/covid19/india/{state}")
	@CrossOrigin
	public List getCovid19IndiaDataByState(@PathVariable("state") String state) {
		List finalData = new ArrayList();
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
					if (state.length() > 0 && StringUtils.startsWithIgnoreCase(values.get("state").toString(), state)) {
						stateDataMap.put("active", values.get("active"));
						stateDataMap.put("confirmed", values.get("confirmed"));
						stateDataMap.put("deaths", values.get("deaths"));
						stateDataMap.put("recovered", values.get("recovered"));
						stateDataMap.put("state", values.get("state"));
						stateDataMap.put("statecode", values.get("statecode"));
						finalData.add(stateDataMap);
					} else if (state.length() <= 0) {
						stateDataMap.put("active", values.get("active"));
						stateDataMap.put("confirmed", values.get("confirmed"));
						stateDataMap.put("deaths", values.get("deaths"));
						stateDataMap.put("recovered", values.get("recovered"));
						stateDataMap.put("state", values.get("state"));
						stateDataMap.put("statecode", values.get("statecode"));
						finalData.add(stateDataMap);
					}

				}
			}
		}
		return finalData;
	}
}
