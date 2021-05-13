package com.covid.register;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

	@Autowired
	private RegisterDao registerDao;

	@PostMapping("/register")
	@CrossOrigin
	private Map createUserRegistration(@RequestBody Register register) {
		Map result = registerDao.createUserRegistration(register);
		return result;
	}

	@PutMapping("/register")
	@CrossOrigin
	private Map updateUserRegistration(@RequestBody Register register) {
		Map result = registerDao.updateUserRegistration(register);
		return result;
	}

	@PostMapping("/login")
	@CrossOrigin
	private Map userLogin(@RequestBody Register register) {
		Map result = registerDao.userLogin(register);
		return result;
	}

	@PostMapping("/search")
	@CrossOrigin
	private Map searchRegisteredUsers(@RequestBody Register register) {
		Map result = registerDao.searchRegisteredUsers(register);
		return result;
	}

	@GetMapping("/countries")
	@CrossOrigin
	private Map getCountryNames() {
		Map result = registerDao.getCountryNames();
		return result;
	}

	@GetMapping("/blood-types")
	@CrossOrigin
	private Map getBloodTypes() {
		Map result = registerDao.getBloodTypes();
		return result;
	}

	@GetMapping("/states/{country}")
	@CrossOrigin
	private Map getStateNames(@PathVariable(value = "country") String countryName) {
		Map result = registerDao.getStateNames(countryName);
		return result;
	}

	@GetMapping("/districts/{state}")
	@CrossOrigin
	private Map getDistrictNames(@PathVariable(value = "state") String stateName) {
		Map result = registerDao.getDistrictNames(stateName);
		return result;
	}

	@GetMapping("/check/username/{username}")
	@CrossOrigin
	private Map isUserNameExisted(@PathVariable(value = "username") String username) {
		Map result = registerDao.isUserNameExisted(username);
		return result;
	}

	@GetMapping("/insert")
	@CrossOrigin
	private Map insertStateNames() throws FileNotFoundException, IOException, ParseException {
		Map result = null;
		registerDao.insertStateNames();
		return result;
	}

}
