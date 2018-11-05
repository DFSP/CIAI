package pt.unl.fct.ciai;


import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import pt.unl.fct.ciai.repository.CompaniesRepository;
import pt.unl.fct.ciai.model.Company;
import pt.unl.fct.ciai.model.Employee;
import pt.unl.fct.ciai.model.User;

import java.util.List;
import java.util.stream.Collectors;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.number.OrderingComparison.greaterThan;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest
@Deprecated
public class ControllersTests {

	@Autowired
	private WebApplicationContext wac;
	@Autowired
	private CompaniesRepository companies;
	private MockMvc mockMvc;
	private ObjectMapper mapper;

	@Before
	public void init() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
		this.mapper = new ObjectMapper();

		// Data Fixture
		companies.deleteAll();
		Company ist = new Company();
		ist.setName("ist");
		ist.setEmail("ist@ist.pt");
		ist.setAddress("lisboa");
		companies.save(ist);
		Company fct = new Company();
        fct.setName("fct");
        fct.setEmail("fct@fct.pt");
        fct.setAddress("almada");
		companies.save(fct);
	}

	private List<Company> getCompanies() throws Exception {
		final MvcResult result = this.mockMvc.perform(get("/companies"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(2)))
				.andReturn();
		String listAsString = result.getResponse().getContentAsString();
		return new ObjectMapper().readValue(listAsString, new TypeReference<List<Company>>(){});
	}

	@Test
	public void testGetCompanies() throws Exception {
		List<Company> companies = this.getCompanies();
		List<String> companyNames = companies.stream().map(Company::getName).collect(Collectors.toList());
		Assert.assertTrue(companyNames.contains("ist"));
		Assert.assertTrue(companyNames.contains("fct"));
	}

	@Test
	public void testAddEmployee() throws Exception {
		List<Company> companies = this.getCompanies();
		long firstId = companies.get(0).getId();
		// Add contact to a company {id}
        User user = new User();
        user.setUsername("NewEmployee");
		Employee employee = new Employee();
		//employee.setUserId(user.getId());
		String json = mapper.writeValueAsString(employee);
		this.mockMvc.perform(post("/companies/" + firstId + "/employees")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(json))
		.andExpect(status().isOk());
		// Get employees from a company {id}
		this.mockMvc.perform(get("/companies/" + firstId + "/employees"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$", hasSize(1)))
		.andExpect(jsonPath("$[0].username").value("NewEmployee"));
		//.andExpect(jsonPath("$[0].username".is("NewUser"));
	}

	@Test
	public void testAddRemoveEmployees() throws Exception {
		List<Company> companies = getCompanies();
		long firstCompanyId = companies.get(0).getId();
		// Add employee
        User user = new User();
        user.setUsername("NewEmployee");
        Employee employee = new Employee();
        //employee.setUserId(user.getId());

		String json = mapper.writeValueAsString(employee);
		this.mockMvc.perform(post("/companies/" + firstCompanyId + "/employees")
				.contentType(MediaType.APPLICATION_JSON_UTF8)
				.content(json))
		.andExpect(status().isOk())
		.andReturn();
		// Get employees from a company and expect not empty list
		final MvcResult result = this.mockMvc.perform(get("/companies/" + firstCompanyId + "/employees"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
				.andExpect(jsonPath("$", hasSize(greaterThan(0))))
				.andReturn();
		String list = result.getResponse().getContentAsString();
		List<Employee> employees = mapper.readValue(list,new TypeReference<List<Employee>>(){});
		// Delete employee
		long firstEmployeeId = employees.get(0).getId();

		// TODO: delete of employees not on Employee,
        // but in Company -> this.mockMvc.perform(delete("/company/" + firstCompanyId + "/employees/" + firstEmployeeId))
		this.mockMvc.perform(delete("/employees/" + firstEmployeeId))
		.andExpect(status().isOk());

		// Get employees and expect empty list
		this.mockMvc.perform(get("/companies/" + firstCompanyId + "/employees/"))
		.andExpect(status().isOk())
		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		.andExpect(jsonPath("$", hasSize(0)));
	}
}

