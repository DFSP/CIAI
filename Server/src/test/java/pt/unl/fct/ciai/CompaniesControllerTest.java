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
import java.util.Iterator;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = CompaniesController.class, secure = false)
@Import({CompanyResourceAssembler.class, EmployeeResourceAssembler.class})
public class CompaniesControllerTest {

	@Autowired
	private MockMvc mvc;
//	@Autowired
//	private CompaniesController companiesController;
	@MockBean
	private CompaniesRepository companiesRepository;
	@MockBean
	private EmployeesRepository employeesRepository;
	@Autowired
	private CompanyResourceAssembler companyAssembler;
	@Autowired
	private EmployeeResourceAssembler employeeAssembler;
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
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		Resource<Company> istResource = companyAssembler.toResource(ist);
		
		given(companiesRepository.findAll()).willReturn(Arrays.asList(fct, ist));

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

		verify(companiesRepository, times(1)).findAll();
	}

	@Test
	public void testAddCompany() throws Exception {
		Company iscte = createISCTECompany();
		Resource<Company> iscteResource = companyAssembler.toResource(iscte);

		given(companiesRepository.save(iscte)).willReturn(iscte);
		given(companiesRepository.findById(iscte.getId())).willReturn(Optional.of(iscte));

		String json = objectMapper.writeValueAsString(iscte);
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

		verify(companiesRepository, times(1)).save(iscte);

		mvc.perform(get(iscteResource.getLink("self").getHref()))
		.andExpect(status().isOk())
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

		verify(companiesRepository, times(1)).findById(iscte.getId());
	}

	@Test
	public void testGetCompany() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		
		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));

		mvc.perform(get(fctResource.getLink("self").getHref()))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is((int)fct.getId())))
		.andExpect(jsonPath("$.name", is(fct.getName())))
		.andExpect(jsonPath("$.city", is(fct.getCity())))
		.andExpect(jsonPath("$.zipCode", is(fct.getZipCode())))
		.andExpect(jsonPath("$.address", is(fct.getAddress())))
		.andExpect(jsonPath("$.phone", is(fct.getPhone())))
		.andExpect(jsonPath("$.email", is(fct.getEmail())))
		.andExpect(jsonPath("$.fax", is(fct.getFax())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + fctResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._links.companies.href", is(ROOT + fctResource.getLink("companies").getHref())))
		.andExpect(jsonPath("$._links.employees.href", is(ROOT + fctResource.getLink("employees").getHref())));

		verify(companiesRepository, times(1)).findById(fct.getId());
	}

	@Test
	public void testUpdateCompany() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		
		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));

		mvc.perform(get(fctResource.getLink("self").getHref()))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is((int)fct.getId())))
		.andExpect(jsonPath("$.name", is(fct.getName())))
		.andExpect(jsonPath("$.city", is(fct.getCity())))
		.andExpect(jsonPath("$.zipCode", is(fct.getZipCode())))
		.andExpect(jsonPath("$.address", is(fct.getAddress())))
		.andExpect(jsonPath("$.phone", is(fct.getPhone())))
		.andExpect(jsonPath("$.email", is(fct.getEmail())))
		.andExpect(jsonPath("$.fax", is(fct.getFax())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + fctResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._links.companies.href", is(ROOT + fctResource.getLink("companies").getHref())))
		.andExpect(jsonPath("$._links.employees.href", is(ROOT + fctResource.getLink("employees").getHref())));

		verify(companiesRepository, times(1)).findById(fct.getId());

		given(companiesRepository.save(fct)).willReturn(fct);
		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));
		
		fct.setEmail("fct.unl@email.pt");
		String json = objectMapper.writeValueAsString(fct);
		mvc.perform(put(fctResource.getLink("self").getHref())
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
		.andExpect(status().isNoContent());

		verify(companiesRepository, times(1)).save(fct);
		verify(companiesRepository, times(2)).findById(fct.getId());

		mvc.perform(get(fctResource.getLink("self").getHref()))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is((int)fct.getId())))
		.andExpect(jsonPath("$.name", is(fct.getName())))
		.andExpect(jsonPath("$.city", is(fct.getCity())))
		.andExpect(jsonPath("$.zipCode", is(fct.getZipCode())))
		.andExpect(jsonPath("$.address", is(fct.getAddress())))
		.andExpect(jsonPath("$.phone", is(fct.getPhone())))
		.andExpect(jsonPath("$.email", is(fct.getEmail())))
		.andExpect(jsonPath("$.fax", is(fct.getFax())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + fctResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._links.companies.href", is(ROOT + fctResource.getLink("companies").getHref())))
		.andExpect(jsonPath("$._links.employees.href", is(ROOT + fctResource.getLink("employees").getHref())));

		verify(companiesRepository, times(3)).findById(fct.getId());
	}

	@Test
	public void testUpdateCompany_BadRequest() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);

		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));

		mvc.perform(get(fctResource.getLink("self").getHref()))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)fct.getId())))
				.andExpect(jsonPath("$.name", is(fct.getName())))
				.andExpect(jsonPath("$.city", is(fct.getCity())))
				.andExpect(jsonPath("$.zipCode", is(fct.getZipCode())))
				.andExpect(jsonPath("$.address", is(fct.getAddress())))
				.andExpect(jsonPath("$.phone", is(fct.getPhone())))
				.andExpect(jsonPath("$.email", is(fct.getEmail())))
				.andExpect(jsonPath("$.fax", is(fct.getFax())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + fctResource.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.companies.href", is(ROOT + fctResource.getLink("companies").getHref())))
				.andExpect(jsonPath("$._links.employees.href", is(ROOT + fctResource.getLink("employees").getHref())));

		verify(companiesRepository, times(1)).findById(fct.getId());

		given(companiesRepository.save(fct)).willReturn(fct);
		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));

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
	public void testUpdateCompany_NotFound() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);

		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));

		mvc.perform(get("http://localhost/partners/2"))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)fct.getId())))
				.andExpect(jsonPath("$.name", is(fct.getName())))
				.andExpect(jsonPath("$.city", is(fct.getCity())))
				.andExpect(jsonPath("$.zipCode", is(fct.getZipCode())))
				.andExpect(jsonPath("$.address", is(fct.getAddress())))
				.andExpect(jsonPath("$.phone", is(fct.getPhone())))
				.andExpect(jsonPath("$.email", is(fct.getEmail())))
				.andExpect(jsonPath("$.fax", is(fct.getFax())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + fctResource.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.companies.href", is(ROOT + fctResource.getLink("companies").getHref())))
				.andExpect(jsonPath("$._links.employees.href", is(ROOT + fctResource.getLink("employees").getHref())));

		verify(companiesRepository, times(1)).findById(fct.getId());


	}

	@Test
	public void testDeleteCompany() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);

		when(companiesRepository.findById(fct.getId()))
		.thenReturn(Optional.of(fct))
		.thenReturn(Optional.of(fct))
		.thenReturn(Optional.ofNullable(null));

		String href = fctResource.getLink("self").getHref();
		
		mvc.perform(get(href))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.name", is(fct.getName())));

		verify(companiesRepository, times(1)).findById(fct.getId());

		mvc.perform(delete(href))
		.andExpect(status().isNoContent());

		verify(companiesRepository, times(2)).findById(fct.getId());
		verify(companiesRepository, times(1)).delete(fct);

		mvc.perform(get(href))
		.andExpect(status().isNotFound());

		verify(companiesRepository, times(3)).findById(fct.getId());
	}

	@Test
	public void testGetEmployees() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);

		Iterator<Employee> it = fct.getEmployees().iterator();
		
		Employee joao = it.next();
		Resource<Employee> joaoResource = employeeAssembler.toResource(joao);
		
		Employee luis = it.next();
		Resource<Employee> luisResource = employeeAssembler.toResource(luis);
		
		Employee daniel = it.next();
		Resource<Employee> danielResource = employeeAssembler.toResource(daniel);
		
		given(companiesRepository.findById(fct.getId()))
		.willReturn(Optional.of(fct));

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
		.andExpect(jsonPath("$._embedded.employees[0].role", is(joao.getRole())))
		.andExpect(jsonPath("$._embedded.employees[0].city", is(joao.getCity())))
		.andExpect(jsonPath("$._embedded.employees[0].address", is(joao.getAddress())))
		.andExpect(jsonPath("$._embedded.employees[0].zipCode", is(joao.getZipCode())))
		.andExpect(jsonPath("$._embedded.employees[0].cellPhone", is(joao.getCellPhone())))
		.andExpect(jsonPath("$._embedded.employees[0].homePhone", is(joao.getHomePhone())))
		.andExpect(jsonPath("$._embedded.employees[0].gender", is(joao.getGender())))
		.andExpect(jsonPath("$._embedded.employees[0].salary", is(joao.getSalary())))
		.andExpect(jsonPath("$._embedded.employees[0].birthday", is(joao.getBirthday())))
		.andExpect(jsonPath("$._embedded.employees[0]._links.self.href", is(ROOT + joaoResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.employees[0]._links.employees.href", is(ROOT + joaoResource.getLink("employees").getHref())))

		.andExpect(jsonPath("$._embedded.employees[1].id", is((int)luis.getId())))
		.andExpect(jsonPath("$._embedded.employees[1].firstName", is(luis.getFirstName())))
		.andExpect(jsonPath("$._embedded.employees[1].lastName", is(luis.getLastName())))
		.andExpect(jsonPath("$._embedded.employees[1].username", is(luis.getUsername())))
		.andExpect(jsonPath("$._embedded.employees[1].email", is(luis.getEmail())))
		.andExpect(jsonPath("$._embedded.employees[1].role", is(luis.getRole())))
		.andExpect(jsonPath("$._embedded.employees[1].city", is(luis.getCity())))
		.andExpect(jsonPath("$._embedded.employees[1].address", is(luis.getAddress())))
		.andExpect(jsonPath("$._embedded.employees[1].zipCode", is(luis.getZipCode())))
		.andExpect(jsonPath("$._embedded.employees[1].cellPhone", is(luis.getCellPhone())))
		.andExpect(jsonPath("$._embedded.employees[1].homePhone", is(luis.getHomePhone())))
		.andExpect(jsonPath("$._embedded.employees[1].gender", is(luis.getGender())))
		.andExpect(jsonPath("$._embedded.employees[1].salary", is(luis.getSalary())))
		.andExpect(jsonPath("$._embedded.employees[1].birthday", is(luis.getBirthday())))
		.andExpect(jsonPath("$._embedded.employees[1]._links.self.href", is(ROOT + luisResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.employees[1]._links.employees.href", is(ROOT + luisResource.getLink("employees").getHref())))

		.andExpect(jsonPath("$._embedded.employees[2].id", is((int)daniel.getId())))
		.andExpect(jsonPath("$._embedded.employees[2].firstName", is(daniel.getFirstName())))
		.andExpect(jsonPath("$._embedded.employees[2].lastName", is(daniel.getLastName())))
		.andExpect(jsonPath("$._embedded.employees[2].username", is(daniel.getUsername())))
		.andExpect(jsonPath("$._embedded.employees[2].email", is(daniel.getEmail())))
		.andExpect(jsonPath("$._embedded.employees[2].role", is(daniel.getRole())))
		.andExpect(jsonPath("$._embedded.employees[2].city", is(daniel.getCity())))
		.andExpect(jsonPath("$._embedded.employees[2].address", is(daniel.getAddress())))
		.andExpect(jsonPath("$._embedded.employees[2].zipCode", is(daniel.getZipCode())))
		.andExpect(jsonPath("$._embedded.employees[2].cellPhone", is(daniel.getCellPhone())))
		.andExpect(jsonPath("$._embedded.employees[2].homePhone", is(daniel.getHomePhone())))
		.andExpect(jsonPath("$._embedded.employees[2].gender", is(daniel.getGender())))
		.andExpect(jsonPath("$._embedded.employees[2].salary", is(daniel.getSalary())))
		.andExpect(jsonPath("$._embedded.employees[2].birthday", is(daniel.getBirthday())))
		.andExpect(jsonPath("$._embedded.employees[2]._links.self.href", is(ROOT + danielResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.employees[2]._links.employees.href", is(ROOT + danielResource.getLink("employees").getHref())))

		.andExpect(jsonPath("$._links.self.href", is(ROOT + href)));

		verify(companiesRepository, times(1)).findById(fct.getId());	
	}

	@Test
	public void testAddEmployee() throws Exception {	
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		
		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));

		String href = fctResource.getLink("employees").getHref();
		mvc.perform(get(href))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$._embedded.employees", hasSize(3)));

		verify(companiesRepository, times(1)).findById(fct.getId());
		
		Employee andre = new Employee();	
		andre.setId(4L);
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
		andre.setGender("M");
		andre.setSalary(1250.0);
		andre.setBirthday("06/11/2018");
		andre.setCompany(fct);
		Resource<Employee> andreResource = employeeAssembler.toResource(andre);
		
		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));
		when(companiesRepository.save(fct)).thenReturn(fct);
		when(employeesRepository.save(andre)).thenReturn(andre);
		
		String json = objectMapper.writeValueAsString(andre);
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
		.andExpect(jsonPath("$.gender", is(andre.getGender())))
		.andExpect(jsonPath("$.salary", is(andre.getSalary())))
		.andExpect(jsonPath("$.birthday", is(andre.getBirthday())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + andreResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._links.employees.href", is(ROOT + href)));
		
		verify(companiesRepository, times(2)).findById(fct.getId());
		verify(companiesRepository, times(1)).save(fct);
		verify(employeesRepository, times(1)).save(andre);
		
		mvc.perform(get(href))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$._embedded.employees", hasSize(4)));
		
		verify(companiesRepository, times(3)).findById(fct.getId());
	}

	@Test
	public void testGetEmployee() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		Employee firstEmployee = fct.getEmployees().iterator().next();
		Resource<Employee> firstEmployeeResource = employeeAssembler.toResource(firstEmployee);
		
		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));
		given(employeesRepository.findById(firstEmployee.getId())).willReturn(Optional.of(firstEmployee));
		
		mvc.perform(get(firstEmployeeResource.getLink("self").getHref()))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is((int)firstEmployee.getId())))
		.andExpect(jsonPath("$.firstName", is(firstEmployee.getFirstName())))
		.andExpect(jsonPath("$.lastName", is(firstEmployee.getLastName())))
		.andExpect(jsonPath("$.username", is(firstEmployee.getUsername())))
		.andExpect(jsonPath("$.email", is(firstEmployee.getEmail())))
		.andExpect(jsonPath("$.role", is(firstEmployee.getRole())))
		.andExpect(jsonPath("$.city", is(firstEmployee.getCity())))
		.andExpect(jsonPath("$.address", is(firstEmployee.getAddress())))
		.andExpect(jsonPath("$.zipCode", is(firstEmployee.getZipCode())))
		.andExpect(jsonPath("$.cellPhone", is(firstEmployee.getCellPhone())))
		.andExpect(jsonPath("$.homePhone", is(firstEmployee.getHomePhone())))
		.andExpect(jsonPath("$.gender", is(firstEmployee.getGender())))
		.andExpect(jsonPath("$.salary", is(firstEmployee.getSalary())))
		.andExpect(jsonPath("$.birthday", is(firstEmployee.getBirthday())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + firstEmployeeResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._links.employees.href", is(ROOT + firstEmployeeResource.getLink("employees").getHref())));
		
		verify(companiesRepository, times(1)).findById(fct.getId());	
		verify(employeesRepository, times(1)).findById(firstEmployee.getId());
	}

	@Test
	public void testUpdateEmployee() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		Employee firstEmployee = fct.getEmployees().iterator().next();
		Resource<Employee> firstEmployeeResource = employeeAssembler.toResource(firstEmployee);

		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));
		given(employeesRepository.findById(firstEmployee.getId())).willReturn(Optional.of(firstEmployee));

		mvc.perform(get(firstEmployeeResource.getLink("self").getHref()))
		.andExpect(jsonPath("$.id", is((int)firstEmployee.getId())))
		.andExpect(jsonPath("$.firstName", is(firstEmployee.getFirstName())))
		.andExpect(jsonPath("$.lastName", is(firstEmployee.getLastName())))
		.andExpect(jsonPath("$.username", is(firstEmployee.getUsername())))
		.andExpect(jsonPath("$.email", is(firstEmployee.getEmail())))
		.andExpect(jsonPath("$.role", is(firstEmployee.getRole())))
		.andExpect(jsonPath("$.city", is(firstEmployee.getCity())))
		.andExpect(jsonPath("$.address", is(firstEmployee.getAddress())))
		.andExpect(jsonPath("$.zipCode", is(firstEmployee.getZipCode())))
		.andExpect(jsonPath("$.cellPhone", is(firstEmployee.getCellPhone())))
		.andExpect(jsonPath("$.homePhone", is(firstEmployee.getHomePhone())))
		.andExpect(jsonPath("$.gender", is(firstEmployee.getGender())))
		.andExpect(jsonPath("$.salary", is(firstEmployee.getSalary())))
		.andExpect(jsonPath("$.birthday", is(firstEmployee.getBirthday())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + firstEmployeeResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._links.employees.href", is(ROOT + firstEmployeeResource.getLink("employees").getHref())));

		verify(companiesRepository, times(1)).findById(fct.getId());
		verify(employeesRepository, times(1)).findById(fct.getId());

		given(employeesRepository.save(firstEmployee)).willReturn(firstEmployee);
		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));
		given(employeesRepository.findById(firstEmployee.getId())).willReturn(Optional.of(firstEmployee));
		
		firstEmployee.setEmail("outroemail@email.pt");
		String json = objectMapper.writeValueAsString(firstEmployee);
		mvc.perform(put(firstEmployeeResource.getLink("self").getHref())
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
		.andExpect(status().isNoContent());

		verify(employeesRepository, times(1)).save(firstEmployee);
		verify(companiesRepository, times(2)).findById(fct.getId());
		verify(employeesRepository, times(2)).findById(firstEmployee.getId());

		mvc.perform(get(firstEmployeeResource.getLink("self").getHref()))
		.andExpect(jsonPath("$.id", is((int)firstEmployee.getId())))
		.andExpect(jsonPath("$.firstName", is(firstEmployee.getFirstName())))
		.andExpect(jsonPath("$.lastName", is(firstEmployee.getLastName())))
		.andExpect(jsonPath("$.username", is(firstEmployee.getUsername())))
		.andExpect(jsonPath("$.email", is(firstEmployee.getEmail())))
		.andExpect(jsonPath("$.role", is(firstEmployee.getRole())))
		.andExpect(jsonPath("$.city", is(firstEmployee.getCity())))
		.andExpect(jsonPath("$.address", is(firstEmployee.getAddress())))
		.andExpect(jsonPath("$.zipCode", is(firstEmployee.getZipCode())))
		.andExpect(jsonPath("$.cellPhone", is(firstEmployee.getCellPhone())))
		.andExpect(jsonPath("$.homePhone", is(firstEmployee.getHomePhone())))
		.andExpect(jsonPath("$.gender", is(firstEmployee.getGender())))
		.andExpect(jsonPath("$.salary", is(firstEmployee.getSalary())))
		.andExpect(jsonPath("$.birthday", is(firstEmployee.getBirthday())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + firstEmployeeResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._links.employees.href", is(ROOT + firstEmployeeResource.getLink("employees").getHref())));

		verify(companiesRepository, times(3)).findById(fct.getId());
		verify(employeesRepository, times(3)).findById(firstEmployee.getId());
	}

	@Test
	public void testDeleteEmployee() throws Exception {		
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);
		Employee firstEmployee = fct.getEmployees().iterator().next();
		Resource<Employee> firstEmployeeResource = employeeAssembler.toResource(firstEmployee);
		
		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));
		
		when(employeesRepository.findById(firstEmployee.getId()))
		.thenReturn(Optional.of(firstEmployee))
		.thenReturn(Optional.of(firstEmployee))
		.thenReturn(Optional.ofNullable(null));
		
		String href = firstEmployeeResource.getLink("self").getHref();
		
		mvc.perform(get(href))
		.andExpect(status().isOk())
		.andExpect(jsonPath("$.firstName", is(firstEmployee.getFirstName())));
		
		verify(companiesRepository, times(1)).findById(fct.getId());
		verify(employeesRepository, times(1)).findById(firstEmployee.getId());

		mvc.perform(delete(href))
		.andExpect(status().isNoContent());

		verify(companiesRepository, times(2)).findById(fct.getId());
		verify(employeesRepository, times(2)).findById(firstEmployee.getId());
		verify(employeesRepository, times(1)).delete(firstEmployee);

		mvc.perform(get(href))
		.andExpect(status().isNotFound());

		verify(companiesRepository, times(3)).findById(fct.getId());
		verify(employeesRepository, times(3)).findById(firstEmployee.getId());
	}

	@Test
	public void testBadRequestUpdateCompany() throws Exception {
		Company fct = createFCTCompany();
		Resource<Company> fctResource = companyAssembler.toResource(fct);

		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));

		mvc.perform(get(fctResource.getLink("self").getHref()))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)fct.getId())))
				.andExpect(jsonPath("$.name", is(fct.getName())))
				.andExpect(jsonPath("$.city", is(fct.getCity())))
				.andExpect(jsonPath("$.zipCode", is(fct.getZipCode())))
				.andExpect(jsonPath("$.address", is(fct.getAddress())))
				.andExpect(jsonPath("$.phone", is(fct.getPhone())))
				.andExpect(jsonPath("$.email", is(fct.getEmail())))
				.andExpect(jsonPath("$.fax", is(fct.getFax())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + fctResource.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.companies.href", is(ROOT + fctResource.getLink("companies").getHref())))
				.andExpect(jsonPath("$._links.employees.href", is(ROOT + fctResource.getLink("employees").getHref())));

		verify(companiesRepository, times(1)).findById(fct.getId());

		given(companiesRepository.save(fct)).willReturn(fct);
		given(companiesRepository.findById(fct.getId())).willReturn(Optional.of(fct));

		fct.setEmail("fct.unl@email.pt");
		fct.setId(2);
		String json = objectMapper.writeValueAsString(fct);
		mvc.perform(put(fctResource.getLink("self").getHref())
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
	
}
