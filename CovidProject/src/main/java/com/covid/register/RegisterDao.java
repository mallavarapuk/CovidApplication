package com.covid.register;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Service;

@Service
public class RegisterDao {

	@Autowired
	private JdbcTemplate jdbcTemplate;
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public Map createUserRegistration(Register register) {
		Map response = new HashMap();
		response.put("success", false);
		response.put("message", "Invalid inputs!");

		try {
			String fullName = register.getFullName();
			String bloodGroup = register.getBloodGroup();
			String mobileNumber = register.getMobileNumber();
			String country = register.getCountry();
			String state = register.getState();
			String district = register.getDistrict();
			String city = register.getCity();
			String emailId = register.getEmailId();
			String userName = register.getUserName();
			String password = register.getPassword();
			String reTypePassword = register.getReTypePassword();
			String availablity = register.getAvailablity();
			String isWillingPlasma = register.getIsWillingPlasma();

			if (!password.equals(reTypePassword)) {
				response.put("message", "Password must be matched!");
				return response;
			}

			String INSERT_QUERY = "INSERT INTO tblUsers (FullName,UserName,BloodGroup,MobileNumber,"
					+ "Country,State,District,City,EmailId,Password,Availability,IsWillingPlasma) "
					+ "VALUES (:FullName,:UserName,:BloodGroup,:MobileNumber,:Country,:State,:District,"
					+ ":City,:EmailId,:Password,:Availability,:IsWillingPlasma)";
			SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("FullName", fullName)
					.addValue("BloodGroup", bloodGroup).addValue("UserName", userName)
					.addValue("MobileNumber", mobileNumber).addValue("Country", country).addValue("State", state)
					.addValue("District", district).addValue("City", city).addValue("EmailId", emailId)
					.addValue("Password", password).addValue("Availability", availablity)
					.addValue("IsWillingPlasma", isWillingPlasma);
			int i = namedParameterJdbcTemplate.update(INSERT_QUERY, parameterSource);

			if (i > 0) {
				response.put("success", true);
				response.put("message", "Successfully Registered!");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	public Map isUserNameExisted(String username) {
		Map response = new HashMap();
		response.put("success", false);
		response.put("message", "Invalid inputs!");

		try {
			String SQL = "SELECT COUNT(Id) From tblUsers WHERE UserName = '" + username.trim() + "'";
			int i = jdbcTemplate.queryForObject(SQL, Integer.class);
			if (i < 0) {
				response.put("message", "Sorry!! Username already existed. Please try another one!");
			} else {
				response.put("success", true);
				response.put("message", "Success");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public Map updateUserRegistration(Register register) {

		Map response = new HashMap();
		response.put("success", false);
		response.put("message", "Invalid inputs!");

		try {

			String fullName = register.getFullName();
			String bloodGroup = register.getBloodGroup();
			String mobileNumber = register.getMobileNumber();
			String country = register.getCountry();
			String state = register.getState();
			String district = register.getDistrict();
			String city = register.getCity();
			String emailId = register.getEmailId();
			String userName = register.getUserName();
//			String password = register.getPassword();
//			String reTypePassword = register.getReTypePassword();
			String availablity = register.getAvailablity();
			String isWillingPlasma = register.getIsWillingPlasma();

			String UPDATE_QUERY = "UPDATE tblUsers SET FullName=:FullName,UserName=:UserName,"
					+ "BloodGroup=:BloodGroup,MobileNumber=:MobileNumber,"
					+ "Country=:Country,State=:State,District=:District,"
					+ "City=:City,EmailId=:EmailId,Availability=:Availability,IsWillingPlasma=:IsWillingPlasma";

			SqlParameterSource parameterSource = new MapSqlParameterSource().addValue("FullName", fullName)
					.addValue("BloodGroup", bloodGroup).addValue("UserName", userName)
					.addValue("MobileNumber", mobileNumber).addValue("Country", country).addValue("State", state)
					.addValue("District", district).addValue("City", city).addValue("EmailId", emailId)
					.addValue("Availability", availablity).addValue("IsWillingPlasma", isWillingPlasma);
			int i = namedParameterJdbcTemplate.update(UPDATE_QUERY, parameterSource);

			if (i > 0) {
				response.put("success", true);
				response.put("message", "Successfully Updated!");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return response;
	}

	public Map userLogin(Register register) {

		List finalList = new ArrayList();
		Map map = null;

		Map response = new HashMap();
		response.put("success", false);
		response.put("message", "Invalid inputs!");
		response.put("data", finalList);

		try {
			String userName = register.getUserName();
			String password = register.getPassword();
			if (password != null && password.length() > 0) {
				if (userName != null && userName.length() > 0) {

					String GET_QUERY = "SELECT FullName,UserName,BloodGroup,MobileNumber,"
							+ "Country,State,District,City,EmailId,Password,Availability,IsWillingPlasma,Password"
							+ " FROM tblUsers WHERE UserName=?";
					List<Map<String, Object>> listOfUser = jdbcTemplate.queryForList(GET_QUERY, userName);

					if (listOfUser != null && listOfUser.size() > 0) {

						if (password.equals(listOfUser.get(0).get("Password"))) {

							for (Map<String, Object> rows : listOfUser) {
								map = new HashMap();
								if (rows.get("FullName") != null
										&& !rows.get("FullName").toString().equalsIgnoreCase(""))
									map.put("FullName", rows.get("FullName").toString());
								else
									map.put("FullName", "");

								if (rows.get("UserName") != null
										&& !rows.get("UserName").toString().equalsIgnoreCase(""))
									map.put("UserName", rows.get("UserName").toString());
								else
									map.put("UserName", "");

								if (rows.get("BloodGroup") != null
										&& !rows.get("BloodGroup").toString().equalsIgnoreCase(""))
									map.put("BloodGroup", rows.get("BloodGroup").toString());
								else
									map.put("BloodGroup", "");

								if (rows.get("MobileNumber") != null
										&& !rows.get("MobileNumber").toString().equalsIgnoreCase(""))
									map.put("MobileNumber", rows.get("MobileNumber").toString());
								else
									map.put("MobileNumber", "");

								if (rows.get("Country") != null && !rows.get("Country").toString().equalsIgnoreCase(""))
									map.put("Country", rows.get("Country").toString());
								else
									map.put("Country", "");

								if (rows.get("State") != null && !rows.get("State").toString().equalsIgnoreCase(""))
									map.put("State", rows.get("State").toString());
								else
									map.put("State", "");

								if (rows.get("District") != null
										&& !rows.get("District").toString().equalsIgnoreCase(""))
									map.put("District", rows.get("District").toString());
								else
									map.put("District", "");

								if (rows.get("City") != null && !rows.get("City").toString().equalsIgnoreCase(""))
									map.put("City", rows.get("City").toString());
								else
									map.put("City", "");

								if (rows.get("EmailId") != null && !rows.get("EmailId").toString().equalsIgnoreCase(""))
									map.put("EmailId", rows.get("EmailId").toString());
								else
									map.put("EmailId", "");

								if (rows.get("Availability") != null
										&& !rows.get("Availability").toString().equalsIgnoreCase(""))
									map.put("Availability", rows.get("Availability").toString());
								else
									map.put("Availability", "");

								if (rows.get("IsWillingPlasma") != null
										&& !rows.get("IsWillingPlasma").toString().equalsIgnoreCase(""))
									map.put("IsWillingPlasma", rows.get("IsWillingPlasma").toString());
								else
									map.put("IsWillingPlasma", "");

								finalList.add(map);
								response.put("success", true);
								response.put("message", "Successfully login!");
								response.put("data", finalList);

							}
						} else {
							response.put("message", "Invalid Password!");
						}
					} else {
						response.put("message", "Invalid Username!");
					}
				} else {
					response.put("message", "username should not be empty");
				}
			} else {
				response.put("message", "password should not be empty");
			}

		} catch (

		Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public Map searchRegisteredUsers(Register register) {

		List finalList = new ArrayList();
		Map map = null;
		Map response = new HashMap();
		response.put("success", false);
		response.put("message", "Invalid inputs!");
		response.put("data", finalList);

		try {

			String bloodGroup = register.getBloodGroup();
			String country = register.getCountry();
			String state = register.getState();
			String district = register.getDistrict();
			String city = register.getCity();

			String COUNT_QUERY = "SELECT COUNT(Id) FROM tblUsers WHERE 1=1";

			if (bloodGroup != null && bloodGroup.length() > 0) {
				COUNT_QUERY = COUNT_QUERY + " AND BloodGroup = '" + bloodGroup + "'";
			}

			if (country != null && country.length() > 0) {
				COUNT_QUERY = COUNT_QUERY + " AND Country = '" + country + "'";
			}

			if (state != null && state.length() > 0) {
				COUNT_QUERY = COUNT_QUERY + " AND State = '" + state + "'";
			}

			if (district != null && district.length() > 0) {
				COUNT_QUERY = COUNT_QUERY + " AND District = '" + district + "'";
			}

			if (city != null && city.length() > 0) {
				COUNT_QUERY = COUNT_QUERY + " AND City = '" + city + "'";
			}

			int count = jdbcTemplate.queryForObject(COUNT_QUERY, new Object[] {}, Integer.class);

			String SEARCH_QUERY = "SELECT FullName,Availability,MobileNumber,BloodGroup FROM tblUsers WHERE 1=1";

			if (bloodGroup != null && bloodGroup.length() > 0) {
				SEARCH_QUERY = SEARCH_QUERY + " AND BloodGroup = '" + bloodGroup + "'";
			}

			if (country != null && country.length() > 0) {
				SEARCH_QUERY = SEARCH_QUERY + " AND Country = '" + country + "'";
			}

			if (state != null && state.length() > 0) {
				SEARCH_QUERY = SEARCH_QUERY + " AND State = '" + state + "'";
			}

			if (district != null && district.length() > 0) {
				SEARCH_QUERY = SEARCH_QUERY + " AND District = '" + district + "'";
			}

			if (city != null && city.length() > 0) {
				SEARCH_QUERY = SEARCH_QUERY + " AND City = '" + city + "'";
			}

			List<Map<String, Object>> searchList = jdbcTemplate.queryForList(SEARCH_QUERY);
			if (searchList != null && searchList.size() > 0) {

				for (Map<String, Object> rows : searchList) {
					map = new HashMap();
					if (rows.get("FullName") != null && !rows.get("FullName").toString().equalsIgnoreCase(""))
						map.put("FullName", rows.get("FullName").toString());
					else
						map.put("FullName", "");

					if (rows.get("BloodGroup") != null && !rows.get("BloodGroup").toString().equalsIgnoreCase(""))
						map.put("BloodGroup", rows.get("BloodGroup").toString());
					else
						map.put("BloodGroup", "");

					if (rows.get("MobileNumber") != null && !rows.get("MobileNumber").toString().equalsIgnoreCase(""))
						map.put("MobileNumber", rows.get("MobileNumber").toString());
					else
						map.put("MobileNumber", "");

					if (rows.get("Availability") != null && !rows.get("Availability").toString().equalsIgnoreCase(""))
						map.put("Availability", rows.get("Availability").toString());
					else
						map.put("Availability", "");

					finalList.add(map);
					response.put("success", true);
					response.put("message", "Successfully login!");
					response.put("data", finalList);
					response.put("count", count);
				}

			} else {
				response.put("message", "Sorry! No donors found.");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public Map getCountryNames() {
		List finalList = new ArrayList();
		Map map = null;
		Map response = new HashMap();
		response.put("success", false);
		response.put("message", "Invalid inputs!");
		response.put("data", finalList);

		try {
			String QUERY = "SELECT * FROM tblLKCountries";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(QUERY);

			for (Map<String, Object> rows : list) {
				map = new HashMap();
				if (rows.get("CountryName") != null && rows.get("CountryName").toString().length() > 0) {
					map.put("CountryName", rows.get("CountryName").toString());
					finalList.add(map);
				}
			}
			response.put("success", true);
			response.put("message", "Successfully retrived Countries!");
			response.put("data", finalList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public Map getStateNames(String country) {
		List finalList = new ArrayList();
		Map map = null;
		Map response = new HashMap();
		response.put("success", false);
		response.put("message", "Invalid inputs!");
		response.put("data", finalList);

		try {
			String QUERY = "SELECT * FROM tblLKStates WHERE CountryName = '" + country + "'";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(QUERY);

			for (Map<String, Object> rows : list) {
				map = new HashMap();
				if (rows.get("StateName") != null && rows.get("StateName").toString().length() > 0) {
					map.put("StateName", rows.get("StateName").toString());
					finalList.add(map);
				}
			}
			response.put("success", true);
			response.put("message", "Successfully retrived states!");
			response.put("data", finalList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public Map getBloodTypes() {
		List finalList = new ArrayList();
		Map map = null;
		Map response = new HashMap();
		response.put("success", false);
		response.put("message", "Invalid inputs!");
		response.put("data", finalList);

		try {
			String QUERY = "select * from tblLKBloodGroup";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(QUERY);

			for (Map<String, Object> rows : list) {
				map = new HashMap();
				if (rows.get("BloodType") != null && rows.get("BloodType").toString().length() > 0) {
					map.put("BloodType", rows.get("BloodType").toString());
					finalList.add(map);
				}
			}
			response.put("success", true);
			response.put("message", "Successfully retrived blood types!");
			response.put("data", finalList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public Map getDistrictNames(String state) {
		List finalList = new ArrayList();
		Map map = null;
		Map response = new HashMap();
		response.put("success", false);
		response.put("message", "Invalid inputs!");
		response.put("data", finalList);

		try {
			String QUERY = "SELECT * FROM tblLKDistricts WHERE StateName = '" + state + "'";
			List<Map<String, Object>> list = jdbcTemplate.queryForList(QUERY);

			for (Map<String, Object> rows : list) {
				map = new HashMap();
				if (rows.get("DistrictName") != null && rows.get("DistrictName").toString().length() > 0) {
					map.put("DistrictName", rows.get("DistrictName").toString());
					finalList.add(map);
				}
			}
			response.put("success", true);
			response.put("message", "Successfully retrived districts!");
			response.put("data", finalList);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return response;
	}

	public void insertStateNames() throws FileNotFoundException, IOException, ParseException {
		JSONParser jsonParser = new JSONParser();

		try (FileReader reader = new FileReader("D://india.json")) {
			// Read JSON file
			Object obj = jsonParser.parse(reader);
			JSONObject employeeList = (JSONObject) obj;
			System.out.println(employeeList.get("states"));
			parseEmployeeObject(employeeList);

		}

	}

	public static void main(String[] args) {
		RegisterDao dao = new RegisterDao();
		// JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();

		try (FileReader reader = new FileReader("D://india.json")) {
			// Read JSON file
			Object obj = jsonParser.parse(reader);

			JSONObject employeeList = (JSONObject) obj;
			System.out.println(employeeList.get("states"));
			dao.parseEmployeeObject(employeeList);
			for (int i = 0; i < employeeList.size(); i++) {

				// Iterate over employee array
//			employeeList.forEach(emp -> parseEmployeeObject((JSONObject) emp));
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	private void parseEmployeeObject(JSONObject employee) {
		// Get employee object within list
		JSONArray employeeObject = (JSONArray) employee.get("states");
		for (int i = 0; i < employeeObject.size(); i++) {
			JSONObject jSONObject = (JSONObject) employeeObject.get(i);

			String state = (String) jSONObject.get("state");
			List<String> districts = (List<String>) jSONObject.get("districts");
			for (String dist : districts) {
				String INSER_QUERY = "INSERT INTO tblLKDistricts (DistrictName,StateName) VALUES ('" + dist + "','"
						+ state + "')";
				jdbcTemplate.update(INSER_QUERY);
			}

		}

		// Get employee first name
//		String firstName = (String) employeeObject.get("firstName");
//		System.out.println(firstName);
//
//		// Get employee last name
//		String lastName = (String) employeeObject.get("lastName");
//		System.out.println(lastName);
//
//		// Get employee website name
//		String website = (String) employeeObject.get("website");
//		System.out.println(website);
	}

}
