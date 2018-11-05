package pt.unl.fct.ciai;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.hateoas.MediaTypes;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.hal.Jackson2HalModule;
import org.springframework.http.HttpHeaders;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import com.fasterxml.jackson.databind.ObjectMapper;

import pt.unl.fct.ciai.assemblers.CompanyResourceAssembler;
import pt.unl.fct.ciai.assemblers.EmployeeResourceAssembler;
import pt.unl.fct.ciai.controller.CompaniesController;
import pt.unl.fct.ciai.model.Company;
import pt.unl.fct.ciai.model.Employee;
import pt.unl.fct.ciai.repository.CompaniesRepository;
import pt.unl.fct.ciai.repository.EmployeesRepository;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CompaniesController.class, secure = false)
@Import({CompanyResourceAssembler.class, EmployeeResourceAssembler.class})
public class CompaniesControllerTest {

	@Autowired
	private MockMvc mvc;
	@Autowired
	private CompaniesController companiesController;
	@MockBean
	private CompaniesRepository companiesRepository;
	@MockBean
	private EmployeesRepository employeesRepository;
	@Autowired
	private CompanyResourceAssembler companyAssembler;
	@Autowired
	private EmployeeResourceAssembler employeeAssembler;
	private ObjectMapper objectMapper;

	public CompaniesControllerTest() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jackson2HalModule());
	}

	private Company createFCTCompany() {
		Company fct = new Company();
		fct.setId(1L);
		fct.setName("FCT");
		fct.setCity("Almada");
		fct.setZipCode("2825-149");
		fct.setAddress("Calçada de Alfazina 2");
		fct.setPhone("+351 212948300");
		fct.setEmail("fct@email.pt");
		fct.setFax("+351 212954461");

		Employee joao = new Employee();
		joao.setId(1L);			
		joao.setCompany(fct);
		joao.setFirstName("João");
		joao.setLastName("Reis");
		joao.setUsername("jreis");
		joao.setPassword("password");
		joao.setEmail("jreis@email.com");
		joao.setRole("ADMIN");
		joao.setCity("Almada");
		joao.setAddress("Caparica");
		joao.setZipCode("1234-992");
		joao.setCellPhone("+351 918888888");
		joao.setHomePhone("+351 221212121");
		joao.setGender("M");
		joao.setSalary(1000.0);
		joao.setBirthday("01/11/2018");
		fct.addEmployee(joao);		

		Employee luis = new Employee();	
		luis.setId(2L);			
		luis.setCompany(fct);
		luis.setFirstName("Luis");
		luis.setLastName("Martins");
		luis.setUsername("lmartins");
		luis.setPassword("password");
		luis.setEmail("lmartins@email.com");
		luis.setRole("ADMIN");
		luis.setCity("Almada");
		luis.setAddress("Caparica");
		luis.setZipCode("1234-1111");
		luis.setCellPhone("+351 912222222");
		luis.setHomePhone("+351 221111111");
		luis.setGender("M");
		luis.setSalary(1500.0);
		luis.setBirthday("05/11/2018");
		fct.addEmployee(luis);

		Employee daniel = new Employee();
		daniel.setId(3L);				
		daniel.setCompany(fct);
		daniel.setFirstName("Daniel");
		daniel.setLastName("Pimenta");
		daniel.setUsername("dpimenta");
		daniel.setPassword("password");
		daniel.setEmail("dpimenta@email.com");
		daniel.setRole("ADMIN");
		daniel.setCity("Almada");
		daniel.setAddress("Caparica");
		daniel.setZipCode("1234-999");
		daniel.setCellPhone("+351 919999999");
		daniel.setHomePhone("+351 221000000");
		daniel.setGender("M");
		daniel.setSalary(500.0);
		daniel.setBirthday("03/11/2018");
		fct.addEmployee(daniel);

		return fct;
	}

	private Company createISTCompany() {
		Company ist = new Company();
		ist.setId(2L);
		ist.setName("IST");
		ist.setCity("Lisboa");
		ist.setZipCode("1023-240");
		ist.setAddress("Av. Rovisco Pais 1");
		ist.setPhone("+351 214233200");
		ist.setEmail("ist@ist.pt");
		ist.setFax("+351 214233268");

		Employee manuel = new Employee();	
		manuel.setId(3L);
		manuel.setCompany(ist);
		manuel.setFirstName("Manuel");
		manuel.setLastName("Coelho");
		manuel.setUsername("mcoelho");
		manuel.setPassword("password");
		manuel.setEmail("mcoelho@email.com");
		manuel.setCity("Lisboa");
		manuel.setAddress("Lisboa");
		manuel.setZipCode("4321-999");
		manuel.setCellPhone("+351 912345678");
		manuel.setHomePhone("+351 222111222");
		manuel.setGender("M");
		manuel.setSalary(1250.0);
		manuel.setBirthday("04/11/2018");
		ist.addEmployee(manuel);

		return ist;
	}

	private Company createISCTECompany() {
		Company iscte = new Company();
		iscte.setId(3L);
		iscte.setName("ISCTE");
		iscte.setCity("Lisboa");
		iscte.setZipCode("1649-026");
		iscte.setAddress("Av. das Forças Armadas");
		iscte.setPhone("+351 210464014");
		iscte.setEmail("iscte@email.pt");
		iscte.setFax("+351 217964710");

		return iscte;
	}

	@Test
	public void testGetCompanies() throws Exception {
		Company fct = createFCTCompany();
		Company ist = createISTCompany();
		given(companiesRepository.findAll()).willReturn(Arrays.asList(fct, ist));
		
		mvc.perform(get("http://localhost/partners"))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$._embedded.companies", hasSize(2)))
		.andExpect(jsonPath("$._embedded.companies[0].id", is(1)))
		.andExpect(jsonPath("$._embedded.companies[0].name", is("FCT")))
		.andExpect(jsonPath("$._embedded.companies[0].city", is("Almada")))
		.andExpect(jsonPath("$._embedded.companies[0].zipCode", is("2825-149")))
		.andExpect(jsonPath("$._embedded.companies[0].address", is("Calçada de Alfazina 2")))
		.andExpect(jsonPath("$._embedded.companies[0].phone", is("+351 212948300")))
		.andExpect(jsonPath("$._embedded.companies[0].email", is("fct@email.pt")))
		.andExpect(jsonPath("$._embedded.companies[0].fax", is("+351 212954461")))
		.andExpect(jsonPath("$._embedded.companies[0]._links.self.href", is(String.format("http://localhost/partners/%d", fct.getId()))))
		.andExpect(jsonPath("$._embedded.companies[0]._links.companies.href", is("http://localhost/partners")))
		.andExpect(jsonPath("$._embedded.companies[1].id", is(2)))
		.andExpect(jsonPath("$._embedded.companies[1].name", is("IST")))
		.andExpect(jsonPath("$._embedded.companies[1].city", is("Lisboa")))
		.andExpect(jsonPath("$._embedded.companies[1].zipCode", is("1023-240")))
		.andExpect(jsonPath("$._embedded.companies[1].address", is("Av. Rovisco Pais 1")))
		.andExpect(jsonPath("$._embedded.companies[1].phone", is("+351 214233200")))
		.andExpect(jsonPath("$._embedded.companies[1].email", is("ist@ist.pt")))
		.andExpect(jsonPath("$._embedded.companies[1].fax", is("+351 214233268")))
		.andExpect(jsonPath("$._embedded.companies[1]._links.self.href", is(String.format("http://localhost/partners/%d", ist.getId()))))
		.andExpect(jsonPath("$._embedded.companies[1]._links.companies.href", is("http://localhost/partners")))
		.andExpect(jsonPath("$._links.self.href", is("http://localhost/partners")));
		
		 verify(companiesRepository, times(1)).findAll();
	}

	@Test
	public void testAddCompany() throws Exception {

		Company iscte = createISCTECompany();

		given(companiesRepository.save(iscte)).willReturn(iscte);
		given(companiesRepository.findById(iscte.getId())).willReturn(Optional.of(iscte));

		String json = objectMapper.writeValueAsString(iscte);
		mvc.perform(post("http://localhost/partners")
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
		.andExpect(status().isCreated())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is(3)))
		.andExpect(jsonPath("$.name", is("ISCTE")))
		.andExpect(jsonPath("$.city", is("Lisboa")))
		.andExpect(jsonPath("$.zipCode", is("1649-026")))
		.andExpect(jsonPath("$.address", is("Av. das Forças Armadas")))
		.andExpect(jsonPath("$.phone", is("+351 210464014")))
		.andExpect(jsonPath("$.email", is("iscte@email.pt")))
		.andExpect(jsonPath("$.fax", is("+351 217964710")))
		.andExpect(jsonPath("$._links.self.href", is(String.format("http://localhost/partners/%d", iscte.getId()))))
		.andExpect(jsonPath("$._links.companies.href", is("http://localhost/partners")))
		.andExpect(jsonPath("$._links.employees.href", is(String.format("http://localhost/partners/%d/employees", iscte.getId()))));

		verify(companiesRepository, times(1)).save(iscte);
		
		mvc.perform(get(String.format("http://localhost/partners/%d", iscte.getId())))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is(3)))
		.andExpect(jsonPath("$.name", is("ISCTE")))
		.andExpect(jsonPath("$.city", is("Lisboa")))
		.andExpect(jsonPath("$.zipCode", is("1649-026")))
		.andExpect(jsonPath("$.address", is("Av. das Forças Armadas")))
		.andExpect(jsonPath("$.phone", is("+351 210464014")))
		.andExpect(jsonPath("$.email", is("iscte@email.pt")))
		.andExpect(jsonPath("$.fax", is("+351 217964710")))
		.andExpect(jsonPath("$._links.self.href", is(String.format("http://localhost/partners/%d", iscte.getId()))))
		.andExpect(jsonPath("$._links.companies.href", is("http://localhost/partners")))
		.andExpect(jsonPath("$._links.employees.href", is(String.format("http://localhost/partners/%d/employees", iscte.getId()))));
		
		verify(companiesRepository, times(1)).findById(iscte.getId());
	}

	@Test
	public void testGetCompany() throws Exception {
		Company fct = createFCTCompany();
		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));

		mvc.perform(get(String.format("http://localhost/partners/%d", fct.getId())))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is(1)))
		.andExpect(jsonPath("$.name", is("FCT")))
		.andExpect(jsonPath("$.city", is("Almada")))
		.andExpect(jsonPath("$.zipCode", is("2825-149")))
		.andExpect(jsonPath("$.address", is("Calçada de Alfazina 2")))
		.andExpect(jsonPath("$.phone", is("+351 212948300")))
		.andExpect(jsonPath("$.email", is("fct@email.pt")))
		.andExpect(jsonPath("$.fax", is("+351 212954461")))
		.andExpect(jsonPath("$._links.self.href", is(String.format("http://localhost/partners/%d", fct.getId()))))
		.andExpect(jsonPath("$._links.companies.href", is("http://localhost/partners")))
		.andExpect(jsonPath("$._links.employees.href", is(String.format("http://localhost/partners/%d/employees", fct.getId()))));
		
		verify(companiesRepository, times(1)).findById(fct.getId());
	}

	@Test
	public void testUpdateCompany() throws Exception {
		Company fct = createFCTCompany();

		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));
		given(companiesRepository.save(fct)).willReturn(fct);
		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));

		mvc.perform(get(String.format("http://localhost/partners/%d", fct.getId())))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is(1)))
		.andExpect(jsonPath("$.name", is("FCT")))
		.andExpect(jsonPath("$.city", is("Almada")))
		.andExpect(jsonPath("$.zipCode", is("2825-149")))
		.andExpect(jsonPath("$.address", is("Calçada de Alfazina 2")))
		.andExpect(jsonPath("$.phone", is("+351 212948300")))
		.andExpect(jsonPath("$.email", is("fct@email.pt")))
		.andExpect(jsonPath("$.fax", is("+351 212954461")))
		.andExpect(jsonPath("$._links.self.href", is(String.format("http://localhost/partners/%d", fct.getId()))))
		.andExpect(jsonPath("$._links.companies.href", is("http://localhost/partners")))
		.andExpect(jsonPath("$._links.employees.href", is(String.format("http://localhost/partners/%d/employees", fct.getId()))));
		
		verify(companiesRepository, times(1)).findById(fct.getId());
		
		fct.setEmail("fct.unl@email.pt");
		String json = objectMapper.writeValueAsString(fct);
		mvc.perform(put(String.format("http://localhost/partners/%d", fct.getId()))
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
		.andExpect(status().isNoContent());

		verify(companiesRepository, times(1)).save(fct);
		verify(companiesRepository, times(2)).findById(fct.getId());
		
		mvc.perform(get(String.format("http://localhost/partners/%d", fct.getId())))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is(1)))
		.andExpect(jsonPath("$.name", is("FCT")))
		.andExpect(jsonPath("$.city", is("Almada")))
		.andExpect(jsonPath("$.zipCode", is("2825-149")))
		.andExpect(jsonPath("$.address", is("Calçada de Alfazina 2")))
		.andExpect(jsonPath("$.phone", is("+351 212948300")))
		.andExpect(jsonPath("$.email", is("fct.unl@email.pt")))
		.andExpect(jsonPath("$.fax", is("+351 212954461")))
		.andExpect(jsonPath("$._links.self.href", is(String.format("http://localhost/partners/%d", fct.getId()))))
		.andExpect(jsonPath("$._links.companies.href", is("http://localhost/partners")))
		.andExpect(jsonPath("$._links.employees.href", is(String.format("http://localhost/partners/%d/employees", fct.getId()))));
		
		verify(companiesRepository, times(3)).findById(fct.getId());
	}

	@Test
	public void testDeleteCompany() throws Exception {
		Company fct = createFCTCompany();
	
		when(companiesRepository.findById(fct.getId()))
		.thenReturn(Optional.of(fct))
		.thenReturn(Optional.of(fct))
		.thenReturn(Optional.ofNullable(null));
		
		mvc.perform(get(String.format("http://localhost/partners/%d", fct.getId())))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is("FCT")));

		verify(companiesRepository, times(1)).findById(fct.getId());
		
		mvc.perform(delete(String.format("http://localhost/partners/%d", fct.getId())))
		.andExpect(status().isNoContent());

		verify(companiesRepository, times(2)).findById(fct.getId());
		verify(companiesRepository, times(1)).delete(fct);

		mvc.perform(get(String.format("http://localhost/partners/%d", fct.getId())))
		.andExpect(status().isNotFound());
		
		verify(companiesRepository, times(3)).findById(fct.getId());
	}

	@Test
	public void testGetEmployees() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> resource = companyAssembler.toResource(fct);
		String employeesHref = String.format("http://localhost%s", resource.getLink("employees").getHref());
		
		given(companiesRepository.findById(fct.getId()))
		.willReturn(Optional.of(fct));

		mvc.perform(get(employeesHref))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$._embedded.employees", hasSize(3)))
		
		.andExpect(jsonPath("$._embedded.employees[0].id", is(1)))
		.andExpect(jsonPath("$._embedded.employees[0].firstName", is("João")))
		.andExpect(jsonPath("$._embedded.employees[0].lastName", is("Reis")))
		.andExpect(jsonPath("$._embedded.employees[0].username", is("jreis")))
		.andExpect(jsonPath("$._embedded.employees[0].email", is("jreis@email.com")))
		.andExpect(jsonPath("$._embedded.employees[0].role", is("ADMIN")))
		.andExpect(jsonPath("$._embedded.employees[0].city", is("Almada")))
		.andExpect(jsonPath("$._embedded.employees[0].address", is("Caparica")))
		.andExpect(jsonPath("$._embedded.employees[0].zipCode", is("1234-992")))
		.andExpect(jsonPath("$._embedded.employees[0].cellPhone", is("+351 918888888")))
		.andExpect(jsonPath("$._embedded.employees[0].homePhone", is("+351 221212121")))
		.andExpect(jsonPath("$._embedded.employees[0].gender", is("M")))
		.andExpect(jsonPath("$._embedded.employees[0].salary", is(1000.0)))
		.andExpect(jsonPath("$._embedded.employees[0].birthday", is("01/11/2018")))
		.andExpect(jsonPath("$._embedded.employees[0]._links.self.href", is(String.format("%s/1", employeesHref))))
		.andExpect(jsonPath("$._embedded.employees[0]._links.employees.href", is(employeesHref)))

		.andExpect(jsonPath("$._embedded.employees[1].id", is(2)))
		.andExpect(jsonPath("$._embedded.employees[1].firstName", is("Luis")))
		.andExpect(jsonPath("$._embedded.employees[1].lastName", is("Martins")))
		.andExpect(jsonPath("$._embedded.employees[1].username", is("lmartins")))
		.andExpect(jsonPath("$._embedded.employees[1].email", is("lmartins@email.com")))
		.andExpect(jsonPath("$._embedded.employees[1].role", is("ADMIN")))
		.andExpect(jsonPath("$._embedded.employees[1].city", is("Almada")))
		.andExpect(jsonPath("$._embedded.employees[1].address", is("Caparica")))
		.andExpect(jsonPath("$._embedded.employees[1].zipCode", is("1234-1111")))
		.andExpect(jsonPath("$._embedded.employees[1].cellPhone", is("+351 912222222")))
		.andExpect(jsonPath("$._embedded.employees[1].homePhone", is("+351 221111111")))
		.andExpect(jsonPath("$._embedded.employees[1].gender", is("M")))
		.andExpect(jsonPath("$._embedded.employees[1].salary", is(1500.0)))
		.andExpect(jsonPath("$._embedded.employees[1].birthday", is("05/11/2018")))
		.andExpect(jsonPath("$._embedded.employees[1]._links.self.href", is(String.format("%s/2", employeesHref))))
		.andExpect(jsonPath("$._embedded.employees[1]._links.employees.href", is(employeesHref)))

		.andExpect(jsonPath("$._embedded.employees[2].id", is(3)))
		.andExpect(jsonPath("$._embedded.employees[2].firstName", is("Daniel")))
		.andExpect(jsonPath("$._embedded.employees[2].lastName", is("Pimenta")))
		.andExpect(jsonPath("$._embedded.employees[2].username", is("dpimenta")))
		.andExpect(jsonPath("$._embedded.employees[2].email", is("dpimenta@email.com")))
		.andExpect(jsonPath("$._embedded.employees[2].role", is("ADMIN")))
		.andExpect(jsonPath("$._embedded.employees[2].city", is("Almada")))
		.andExpect(jsonPath("$._embedded.employees[2].address", is("Caparica")))
		.andExpect(jsonPath("$._embedded.employees[2].zipCode", is("1234-999")))
		.andExpect(jsonPath("$._embedded.employees[2].cellPhone", is("+351 919999999")))
		.andExpect(jsonPath("$._embedded.employees[2].homePhone", is("+351 221000000")))
		.andExpect(jsonPath("$._embedded.employees[2].gender", is("M")))
		.andExpect(jsonPath("$._embedded.employees[2].salary", is(500.0)))
		.andExpect(jsonPath("$._embedded.employees[2].birthday", is("03/11/2018")))
		.andExpect(jsonPath("$._embedded.employees[2]._links.self.href", is(String.format("%s/3", employeesHref))))
		.andExpect(jsonPath("$._embedded.employees[2]._links.employees.href", is(employeesHref)))

		.andExpect(jsonPath("$._links.self.href", is(employeesHref)));
		
		verify(companiesRepository, times(1)).findById(fct.getId());	
	}

	@Test
	public void testAddEmployee() throws Exception {	
		//		Resource<Company> company = this.getFirstCompany();
		//		long firstId = companies.get(0).getId();
		//		// Add contact to a company {id}
		//        User user = new User();
		//        user.setUsername("NewEmployee");
		//		Employee employee = new Employee();
		//		//employee.setUserId(user.getId());
		//		String json = mapper.writeValueAsString(employee);
		//		this.mockMvc.perform(post("/companies/" + firstId + "/employees")
		//				.contentType(MediaType.APPLICATION_JSON_UTF8)
		//				.content(json))
		//		.andExpect(status().isOk());
		//		// Get employees from a company {id}
		//		this.mockMvc.perform(get("/companies/" + firstId + "/employees"))
		//		.andExpect(status().isOk())
		//		.andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
		//		.andExpect(jsonPath("$", hasSize(1)))
		//		.andExpect(jsonPath("$[0].username").value("NewEmployee"));
		//		//.andExpect(jsonPath("$[0].username".is("NewUser"));
	}

	@Test
	public void testGetEmployee() throws Exception {

	}

	@Test
	public void updateEmployee() throws Exception {

	}

	@Test
	public  void testDeleteEmployee() throws Exception {		

	}

}
