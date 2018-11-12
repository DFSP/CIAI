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
import org.springframework.test.web.servlet.MvcResult;

import com.fasterxml.jackson.databind.ObjectMapper;

import pt.unl.fct.ciai.assembler.CompanyResourceAssembler;
import pt.unl.fct.ciai.assembler.EmployeeResourcesAssembler;
import pt.unl.fct.ciai.controller.CompaniesController;
import pt.unl.fct.ciai.model.Company;
import pt.unl.fct.ciai.model.Employee;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.repository.CompaniesRepository;
import pt.unl.fct.ciai.repository.EmployeesRepository;
import pt.unl.fct.ciai.service.CompaniesService;

import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CompaniesController.class, secure = false)
@Import({CompanyResourceAssembler.class, EmployeeResourcesAssembler.class})
public class CompaniesControllerTest {

	@Autowired
	private MockMvc mvc;

	@MockBean
	private CompaniesService companiesService;
	@Autowired
	private CompanyResourceAssembler companyAssembler;
	@Autowired
	private EmployeeResourcesAssembler employeeAssembler;
	private final ObjectMapper objectMapper;

	private static final String ROOT = "http://localhost";
	
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
		joao.setRole(User.Role.ROLE_COMPANY_ADMIN);
		joao.setCity("Almada");
		joao.setAddress("Caparica");
		joao.setZipCode("1234-992");
		joao.setCellPhone("+351 918888888");
		joao.setHomePhone("+351 221212121");
		joao.setGender(User.Gender.MALE);
		joao.setSalary(1000.0);
		joao.setBirthday(new Date());
		fct.addEmployee(joao);		

		Employee luis = new Employee();	
		luis.setId(2L);			
		luis.setCompany(fct);
		luis.setFirstName("Luis");
		luis.setLastName("Martins");
		luis.setUsername("lmartins");
		luis.setPassword("password");
		luis.setEmail("lmartins@email.com");
		luis.setRole(User.Role.ROLE_COMPANY_ADMIN);
		luis.setCity("Almada");
		luis.setAddress("Caparica");
		luis.setZipCode("1234-1111");
		luis.setCellPhone("+351 912222222");
		luis.setHomePhone("+351 221111111");
		luis.setGender(User.Gender.MALE);
		luis.setSalary(1500.0);
		luis.setBirthday(new Date());
		fct.addEmployee(luis);

		Employee daniel = new Employee();
		daniel.setId(3L);				
		daniel.setCompany(fct);
		daniel.setFirstName("Daniel");
		daniel.setLastName("Pimenta");
		daniel.setUsername("dpimenta");
		daniel.setPassword("password");
		daniel.setEmail("dpimenta@email.com");
		daniel.setRole(User.Role.ROLE_COMPANY_ADMIN);
		daniel.setCity("Almada");
		daniel.setAddress("Caparica");
		daniel.setZipCode("1234-999");
		daniel.setCellPhone("+351 919999999");
		daniel.setHomePhone("+351 221000000");
		daniel.setGender(User.Gender.MALE);
		daniel.setSalary(750.0);
		daniel.setBirthday(new Date());
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
		manuel.setGender(User.Gender.MALE);
		manuel.setSalary(1250.0);
		manuel.setBirthday(new Date());
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
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		Resource<Company> istResource = companyAssembler.toResource(ist);
		
		given(companiesService.getCompanies("")).willReturn(Arrays.asList(fct, ist));

		String href = fctResource.getLink("companies").getHref();
		mvc.perform(get(href))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$._embedded.companies", hasSize(2)))
		.andExpect(jsonPath("$._embedded.companies[0].id", is((int)fct.getId())))
		.andExpect(jsonPath("$._embedded.companies[0].name", is(fct.getName())))
		.andExpect(jsonPath("$._embedded.companies[0].city", is(fct.getCity())))
		.andExpect(jsonPath("$._embedded.companies[0].zipCode", is(fct.getZipCode())))
		.andExpect(jsonPath("$._embedded.companies[0].address", is(fct.getAddress())))
		.andExpect(jsonPath("$._embedded.companies[0].phone", is(fct.getPhone())))
		.andExpect(jsonPath("$._embedded.companies[0].email", is(fct.getEmail())))
		.andExpect(jsonPath("$._embedded.companies[0].fax", is(fct.getFax())))
		.andExpect(jsonPath("$._embedded.companies[0]._links.self.href", is(ROOT + fctResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.companies[0]._links.companies.href", is(ROOT + fctResource.getLink("companies").getHref())))
		.andExpect(jsonPath("$._embedded.companies[0]._links.employees.href", is(ROOT + fctResource.getLink("employees").getHref())))
		.andExpect(jsonPath("$._embedded.companies[1].id", is((int)ist.getId())))
		.andExpect(jsonPath("$._embedded.companies[1].name", is(ist.getName())))
		.andExpect(jsonPath("$._embedded.companies[1].city", is(ist.getCity())))
		.andExpect(jsonPath("$._embedded.companies[1].zipCode", is(ist.getZipCode())))
		.andExpect(jsonPath("$._embedded.companies[1].address", is(ist.getAddress())))
		.andExpect(jsonPath("$._embedded.companies[1].phone", is(ist.getPhone())))
		.andExpect(jsonPath("$._embedded.companies[1].email", is(ist.getEmail())))
		.andExpect(jsonPath("$._embedded.companies[1].fax", is(ist.getFax())))
		.andExpect(jsonPath("$._embedded.companies[1]._links.self.href", is(ROOT + istResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.companies[1]._links.companies.href", is(ROOT + istResource.getLink("companies").getHref())))
		.andExpect(jsonPath("$._embedded.companies[1]._links.employees.href", is(ROOT + istResource.getLink("employees").getHref())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + href)))
		.andExpect(jsonPath("$._links.root.href", is(ROOT + "/")));

		verify(companiesService, times(1)).getCompanies("");
	}

	@Test
	public void testAddCompany() throws Exception {
		Company iscte = createISCTECompany();
		iscte.setId(0);
		Resource<Company> iscteResource = companyAssembler.toResource(iscte);

		given(companiesService.getCompany(iscte.getId())).willReturn(Optional.of(iscte));
		given(companiesService.addCompany(iscte)).willReturn(iscte);

		String json = "{\"name\":\"ISCTE\",\"city\":\"Lisboa\",\"zipCode\":\"1649-026\"," +
				"\"address\":\"Av. das Forças Armadas\",\"phone\":\"+351 210464014\"," +
				"\"email\":\"iscte@email.pt\",\"fax\":\"+351 217964710\"}";
		System.out.println(json);
		mvc.perform(post(iscteResource.getLink("companies").getHref())
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
		.andExpect(status().isCreated())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is((int)iscte.getId())))
		.andExpect(jsonPath("$.name", is(iscte.getName())))
		.andExpect(jsonPath("$.city", is(iscte.getCity())))
		.andExpect(jsonPath("$.zipCode", is(iscte.getZipCode())))
		.andExpect(jsonPath("$.address", is(iscte.getAddress())))
		.andExpect(jsonPath("$.phone", is(iscte.getPhone())))
		.andExpect(jsonPath("$.email", is(iscte.getEmail())))
		.andExpect(jsonPath("$.fax", is(iscte.getFax())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + iscteResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._links.companies.href", is(ROOT + iscteResource.getLink("companies").getHref())))
		.andExpect(jsonPath("$._links.employees.href", is(ROOT + iscteResource.getLink("employees").getHref())));

		verify(companiesService, times(1)).addCompany(iscte);

		performGet(iscte);

		verify(companiesService, times(1)).getCompany(iscte.getId());
	}

	@Test
	public void testGetCompany() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		
		given(companiesService.getCompany(fct.getId())).willReturn(Optional.of(fct));

		performGet(fct);

		verify(companiesService, times(1)).getCompany(fct.getId());
	}

	@Test
	public void testUpdateCompany() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		
		given(companiesService.getCompany(fct.getId())).willReturn(Optional.of(fct));

		performGet(fct);

		verify(companiesService, times(1)).getCompany(fct.getId());

		given(companiesService.getCompany(fct.getId())).willReturn(Optional.of(fct));
		
		fct.setEmail("fct.unl@email.pt");
		String json = objectMapper.writeValueAsString(fct);
		mvc.perform(put(fctResource.getLink("self").getHref())
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
		.andExpect(status().isNoContent());

		verify(companiesService, times(1)).updateCompany(fct);

		performGet(fct);

		verify(companiesService, times(2)).getCompany(fct.getId());
	}

	@Test
	public void testUpdateCompanyBadRequest() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);

		given(companiesService.getCompany(fct.getId())).willReturn(Optional.of(fct));

		performGet(fct);

		verify(companiesService, times(1)).getCompany(fct.getId());

		given(companiesService.getCompany(fct.getId())).willReturn(Optional.of(fct));

		fct.setEmail("fct.unl@email.pt");
		fct.setId(2);
		String json = objectMapper.writeValueAsString(fct);
		mvc.perform(put(fctResource.getLink("self").getHref())
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
				.andExpect(status().isBadRequest());
	}

	@Test
	public void testDeleteCompany() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);

		when(companiesService.getCompany(fct.getId()))
		.thenReturn(Optional.of(fct))
		.thenReturn(Optional.empty());

		String href = fctResource.getLink("self").getHref();
		
		mvc.perform(get(href))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is(fct.getName())));

		verify(companiesService, times(1)).getCompany(fct.getId());

		mvc.perform(delete(href))
		.andExpect(status().isNoContent());

		verify(companiesService, times(1)).deleteCompany(fct.getId());

		mvc.perform(get(href))
		.andExpect(status().isNotFound());

		verify(companiesService, times(2)).getCompany(fct.getId());
	}

	@Test
	public void testGetEmployees() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);

		Iterator<Employee> it = fct.getEmployees().get().iterator();
		
		Employee joao = it.next();
		Resource<Employee> joaoResource = employeeAssembler.toResource(joao);
		
		Employee luis = it.next();
		Resource<Employee> luisResource = employeeAssembler.toResource(luis);
		
		Employee daniel = it.next();
		Resource<Employee> danielResource = employeeAssembler.toResource(daniel);
		
		given(companiesService.getCompany(fct.getId())).willReturn(Optional.of(fct));
		given(companiesService.getEmployees(fct.getId(), "")).willReturn(fct.getEmployees().get());

		String href = fctResource.getLink("employees").getHref();
		mvc.perform(get(href))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$._embedded.employees", hasSize(3)))

		.andExpect(jsonPath("$._embedded.employees[0].id", is((int)joao.getId())))
		.andExpect(jsonPath("$._embedded.employees[0].firstName", is(joao.getFirstName())))
		.andExpect(jsonPath("$._embedded.employees[0].lastName", is(joao.getLastName())))
		.andExpect(jsonPath("$._embedded.employees[0].username", is(joao.getUsername())))
		.andExpect(jsonPath("$._embedded.employees[0].email", is(joao.getEmail())))
		.andExpect(jsonPath("$._embedded.employees[0].role", is(joao.getRole().name())))
		.andExpect(jsonPath("$._embedded.employees[0].city", is(joao.getCity())))
		.andExpect(jsonPath("$._embedded.employees[0].address", is(joao.getAddress())))
		.andExpect(jsonPath("$._embedded.employees[0].zipCode", is(joao.getZipCode())))
		.andExpect(jsonPath("$._embedded.employees[0].cellPhone", is(joao.getCellPhone())))
		.andExpect(jsonPath("$._embedded.employees[0].homePhone", is(joao.getHomePhone())))
		.andExpect(jsonPath("$._embedded.employees[0].gender", is(joao.getGender().name())))
		.andExpect(jsonPath("$._embedded.employees[0].salary", is(joao.getSalary())))
		.andExpect(jsonPath("$._embedded.employees[0]._links.self.href", is(ROOT + joaoResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.employees[0]._links.employees.href", is(ROOT + joaoResource.getLink("employees").getHref())))

		.andExpect(jsonPath("$._embedded.employees[1].id", is((int)luis.getId())))
		.andExpect(jsonPath("$._embedded.employees[1].firstName", is(luis.getFirstName())))
		.andExpect(jsonPath("$._embedded.employees[1].lastName", is(luis.getLastName())))
		.andExpect(jsonPath("$._embedded.employees[1].username", is(luis.getUsername())))
		.andExpect(jsonPath("$._embedded.employees[1].email", is(luis.getEmail())))
		.andExpect(jsonPath("$._embedded.employees[1].role", is(luis.getRole().name())))
		.andExpect(jsonPath("$._embedded.employees[1].city", is(luis.getCity())))
		.andExpect(jsonPath("$._embedded.employees[1].address", is(luis.getAddress())))
		.andExpect(jsonPath("$._embedded.employees[1].zipCode", is(luis.getZipCode())))
		.andExpect(jsonPath("$._embedded.employees[1].cellPhone", is(luis.getCellPhone())))
		.andExpect(jsonPath("$._embedded.employees[1].homePhone", is(luis.getHomePhone())))
		.andExpect(jsonPath("$._embedded.employees[1].gender", is(luis.getGender().name())))
		.andExpect(jsonPath("$._embedded.employees[1].salary", is(luis.getSalary())))
		.andExpect(jsonPath("$._embedded.employees[1]._links.self.href", is(ROOT + luisResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.employees[1]._links.employees.href", is(ROOT + luisResource.getLink("employees").getHref())))

		.andExpect(jsonPath("$._embedded.employees[2].id", is((int)daniel.getId())))
		.andExpect(jsonPath("$._embedded.employees[2].firstName", is(daniel.getFirstName())))
		.andExpect(jsonPath("$._embedded.employees[2].lastName", is(daniel.getLastName())))
		.andExpect(jsonPath("$._embedded.employees[2].username", is(daniel.getUsername())))
		.andExpect(jsonPath("$._embedded.employees[2].email", is(daniel.getEmail())))
		.andExpect(jsonPath("$._embedded.employees[2].role", is(daniel.getRole().name())))
		.andExpect(jsonPath("$._embedded.employees[2].city", is(daniel.getCity())))
		.andExpect(jsonPath("$._embedded.employees[2].address", is(daniel.getAddress())))
		.andExpect(jsonPath("$._embedded.employees[2].zipCode", is(daniel.getZipCode())))
		.andExpect(jsonPath("$._embedded.employees[2].cellPhone", is(daniel.getCellPhone())))
		.andExpect(jsonPath("$._embedded.employees[2].homePhone", is(daniel.getHomePhone())))
		.andExpect(jsonPath("$._embedded.employees[2].gender", is(daniel.getGender().name())))
		.andExpect(jsonPath("$._embedded.employees[2].salary", is(daniel.getSalary())))
		.andExpect(jsonPath("$._embedded.employees[2]._links.self.href", is(ROOT + danielResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.employees[2]._links.employees.href", is(ROOT + danielResource.getLink("employees").getHref())))

		.andExpect(jsonPath("$._links.self.href", is(ROOT + href)));

		verify(companiesService, times(1)).getEmployees(fct.getId(), "");
	}

	@Test
	public void testAddEmployee() throws Exception {	
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);

		given(companiesService.getCompany(fct.getId())).willReturn(Optional.of(fct));
		given(companiesService.getEmployees(fct.getId(), "")).willReturn(fct.getEmployees().get());

		String href = fctResource.getLink("employees").getHref();
		mvc.perform(get(href))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$._embedded.employees", hasSize(3)));

		verify(companiesService, times(1)).getEmployees(fct.getId(), "");
		
		Employee andre = new Employee();
		andre.setFirstName("Andre");
		andre.setLastName("Oliveira");
		andre.setUsername("aoliveira");
		andre.setPassword("password");
		andre.setEmail("aoliveira@email.com");
		andre.setCity("Lisboa");
		andre.setAddress("Lisboa");
		andre.setZipCode("4322-939");
		andre.setCellPhone("+351 916785678");
		andre.setHomePhone("+351 212117922");
		andre.setGender(User.Gender.MALE);
		andre.setSalary(1250.0);
		andre.setBirthday(new Date());
		andre.setCompany(fct);
		Resource<Employee> andreResource = employeeAssembler.toResource(andre);
		
		given(companiesService.getCompany(fct.getId())).willReturn(Optional.of(fct));
		when(companiesService.addEmployee(fct.getId(), andre)).thenReturn(andre);

		String json = "{\"firstName\":\"Andre\",\"lastName\":\"Oliveira\",\"username\":\"aoliveira\"," +
				"\"password\":\"password\",\"email\":\"aoliveira@email.com\",\"role\":null,\"city\":\"Lisboa\"," +
				"\"address\":\"Lisboa\",\"zipCode\":\"4322-939\",\"cellPhone\":\"+351 916785678\"," +
				"\"homePhone\":\"+351 212117922\",\"gender\":\"MALE\",\"salary\":1250.0,\"birthday\":1542060894866}," +
				"\"company\":{\"id\":1,\"name\":\"FCT\",\"city\":\"Almada\",\"zipCode\":\"2825-149\"," +
				"\"address\":\"Calçada de Alfazina 2\",\"phone\":\"+351 212948300\",\"email\":\"fct@email.pt\"," +
				"\"fax\":\"+351 212954461\"}}";
		mvc.perform(post(href)
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
		.andExpect(status().isCreated())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is((int)andre.getId())))
		.andExpect(jsonPath("$.firstName", is(andre.getFirstName())))
		.andExpect(jsonPath("$.lastName", is(andre.getLastName())))
		.andExpect(jsonPath("$.username", is(andre.getUsername())))
		.andExpect(jsonPath("$.email", is(andre.getEmail())))
		.andExpect(jsonPath("$.role").isEmpty())
		.andExpect(jsonPath("$.city", is(andre.getCity())))
		.andExpect(jsonPath("$.address", is(andre.getAddress())))
		.andExpect(jsonPath("$.zipCode", is(andre.getZipCode())))
		.andExpect(jsonPath("$.cellPhone", is(andre.getCellPhone())))
		.andExpect(jsonPath("$.homePhone", is(andre.getHomePhone())))
		.andExpect(jsonPath("$.gender", is(andre.getGender().name())))
		.andExpect(jsonPath("$.salary", is(andre.getSalary())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + andreResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._links.employees.href", is(ROOT + href)));

		verify(companiesService, times(1)).addEmployee(fct.getId(), andre);


		given(companiesService.getEmployees(fct.getId(), "")).willReturn(fct.addEmployee(andre).getEmployees().get());

		mvc.perform(get(href))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$._embedded.employees", hasSize(4)));
		
		verify(companiesService, times(2)).getEmployees(fct.getId(), "");
	}

	@Test
	public void testGetEmployee() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		Employee firstEmployee = fct.getEmployees().get().iterator().next();
		Resource<Employee> firstEmployeeResource = employeeAssembler.toResource(firstEmployee);

		given(companiesService.getEmployee(fct.getId(), firstEmployee.getId())).willReturn(Optional.of(firstEmployee));
		
		performGet(firstEmployee);

		verify(companiesService, times(1)).getEmployee(fct.getId(), firstEmployee.getId());
	}

	@Test
	public void testUpdateEmployee() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		Employee firstEmployee = fct.getEmployees().get().iterator().next();
		Resource<Employee> firstEmployeeResource = employeeAssembler.toResource(firstEmployee);
		String href = firstEmployeeResource.getLink("self").getHref();

		given(companiesService.getEmployee(fct.getId(), firstEmployee.getId())).willReturn(Optional.of(firstEmployee));

		performGet(firstEmployee);

		verify(companiesService, times(1)).getEmployee(fct.getId(), firstEmployee.getId());

		given(companiesService.updateEmployee(fct.getId(), firstEmployee)).willReturn(firstEmployee);
		given(companiesService.getCompany(fct.getId())).willReturn(Optional.of(fct));
		given(companiesService.getEmployee(fct.getId(), firstEmployee.getId())).willReturn(Optional.of(firstEmployee));
		
		firstEmployee.setEmail("outroemail.pt");
		String json = objectMapper.writeValueAsString(firstEmployee);
		mvc.perform(put(href)
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
		.andExpect(status().isNoContent());

		verify(companiesService, times(1)).updateEmployee(fct.getId(), firstEmployee);

		performGet(firstEmployee);

		verify(companiesService, times(2)).getEmployee(fct.getId(), firstEmployee.getId());
	}

	@Test
	public void testDeleteEmployee() throws Exception {		
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		Employee firstEmployee = fct.getEmployees().get().iterator().next();
		String href = employeeAssembler.toResource(firstEmployee).getLink("self").getHref();

		given(companiesService.getCompany(fct.getId())).willReturn(Optional.of(fct));
		when(companiesService.getEmployee(fct.getId(), firstEmployee.getId()))
				.thenReturn(Optional.of(firstEmployee))
				.thenReturn(Optional.empty());

		performGet(firstEmployee);

		verify(companiesService, times(1)).getEmployee(fct.getId(), firstEmployee.getId());

		mvc.perform(delete(href))
		.andExpect(status().isNoContent());

		verify(companiesService, times(1)).deleteEmployee(fct.getId(), firstEmployee.getId());

		mvc.perform(get(href))
		.andExpect(status().isNotFound());

		verify(companiesService, times(2)).getEmployee(fct.getId(), firstEmployee.getId());
	}

	@Test
	public void testNotFoundCompany() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);

		given(companiesService.getCompany(fct.getId())).willReturn(Optional.of(fct));

		mvc.perform(get("http://localhost/companies/999"))
		.andExpect(status().isNotFound());

		verify(companiesService, times(1)).getCompany(999);
	}
	
	@Test
	public void testBadRequestUpdateCompany() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		String href = fctResource.getLink("self").getHref();

		given(companiesService.getCompany(fct.getId())).willReturn(Optional.of(fct));

		performGet(fct);

		verify(companiesService, times(1)).getCompany(fct.getId());

		given(companiesService.updateCompany(fct)).willReturn(fct);
		given(companiesService.getCompany(fct.getId())).willReturn(Optional.of(fct));

		fct.setId(2);
		String json = objectMapper.writeValueAsString(fct);
		mvc.perform(put(href)
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
				.andExpect(status().isBadRequest());
	}
	
	private MvcResult performGet(Company company) throws Exception {
		Resource<Company> resourceCompany = companyAssembler.toResource(company);
		return mvc.perform(get(resourceCompany.getLink("self").getHref()))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is((int)company.getId())))
		.andExpect(jsonPath("$.name", is(company.getName())))
		.andExpect(jsonPath("$.city", is(company.getCity())))
		.andExpect(jsonPath("$.zipCode", is(company.getZipCode())))
		.andExpect(jsonPath("$.address", is(company.getAddress())))
		.andExpect(jsonPath("$.phone", is(company.getPhone())))
		.andExpect(jsonPath("$.email", is(company.getEmail())))
		.andExpect(jsonPath("$.fax", is(company.getFax())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + resourceCompany.getLink("self").getHref())))
		.andExpect(jsonPath("$._links.companies.href", is(ROOT + resourceCompany.getLink("companies").getHref())))
		.andExpect(jsonPath("$._links.employees.href", is(ROOT + resourceCompany.getLink("employees").getHref())))
		.andReturn();
	}
	
	private MvcResult performGet(Employee employee) throws Exception {
		Resource<Employee> employeeResource = employeeAssembler.toResource(employee);		
		return mvc.perform(get(employeeResource.getLink("self").getHref()))
		.andExpect(jsonPath("$.id", is((int)employee.getId())))
		.andExpect(jsonPath("$.firstName", is(employee.getFirstName())))
		.andExpect(jsonPath("$.lastName", is(employee.getLastName())))
		.andExpect(jsonPath("$.username", is(employee.getUsername())))
		.andExpect(jsonPath("$.email", is(employee.getEmail())))
		.andExpect(jsonPath("$.role", is(employee.getRole().name())))
		.andExpect(jsonPath("$.city", is(employee.getCity())))
		.andExpect(jsonPath("$.address", is(employee.getAddress())))
		.andExpect(jsonPath("$.zipCode", is(employee.getZipCode())))
		.andExpect(jsonPath("$.cellPhone", is(employee.getCellPhone())))
		.andExpect(jsonPath("$.homePhone", is(employee.getHomePhone())))
		.andExpect(jsonPath("$.gender", is(employee.getGender().name())))
		.andExpect(jsonPath("$.salary", is(employee.getSalary())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + employeeResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._links.employees.href", is(ROOT + employeeResource.getLink("employees").getHref())))
		.andReturn();
	}
	
}
