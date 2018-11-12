package pt.unl.fct.ciai;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;

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

import pt.unl.fct.ciai.assembler.ProposalResourceAssembler;
import pt.unl.fct.ciai.assembler.UserResourceAssembler;
import pt.unl.fct.ciai.controller.UsersController;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.repository.ProposalsRepository;
import pt.unl.fct.ciai.repository.UsersRepository;
import pt.unl.fct.ciai.service.ProposalsService;
import pt.unl.fct.ciai.service.UsersService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UsersController.class, secure = false)
@Import({UserResourceAssembler.class, ProposalResourceAssembler.class})
public class UsersControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private UsersService usersService;
	@MockBean
	private ProposalsService proposalsService;
	@Autowired
	private UserResourceAssembler userAssembler;
	@Autowired
	private ProposalResourceAssembler proposalAssembler;
	private final ObjectMapper objectMapper;

	private static final String ROOT = "http://localhost";

	public UsersControllerTest() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jackson2HalModule());
	}

	private Proposal createProposal() {
		Proposal proposal = new Proposal().id(1L)
				.title("A proposal title")
				.description("A very detailed description about this proposal")
				.approved();
		return proposal;
	}

	private User createJoaoUser() {
		User joao = new User();
		joao.setId(1L);
		joao.setFirstName("João");
		joao.setLastName("Reis");
		joao.setUsername("jreis");
		joao.setEmail("jreis@email.com");
		joao.setRole(User.Role.ROLE_COMPANY_ADMIN);
		joao.setPassword("password");
		return joao;
	}

	private User createLuisUser() {
		User luis = new User();
		luis.setId(2L);
		luis.setFirstName("Luis");
		luis.setLastName("Martins");
		luis.setUsername("lmartins");
		luis.setEmail("lmartins@email.com");
		luis.setRole(User.Role.ROLE_COMPANY_ADMIN);
		luis.setPassword("password");
		return luis;
	}

	private User createDanielUser() {
		User daniel = new User();
		daniel.setId(3L);
		daniel.setFirstName("Daniel");
		daniel.setLastName("Pimenta");
		daniel.setUsername("dpimenta");
		daniel.setEmail("dpimenta@email.com");
		daniel.setRole(User.Role.ROLE_COMPANY_ADMIN);
		daniel.setPassword("password");
		return daniel;
	}

	private User createManuelUser() {
		User manuel = new User();
		manuel.setId(4L);
		manuel.setFirstName("Manuel");
		manuel.setLastName("Coelho");
		manuel.setUsername("mcoelho");
		manuel.setEmail("mcoelho@email.com");
		manuel.setPassword("password");
		return manuel;
	}

	@Test
	public void testGetUsers() throws Exception {
		User joao = createJoaoUser();		
		User luis = createLuisUser();
		User daniel = createDanielUser();
		Resource<User> joaoResource = userAssembler.toResource(joao);
		Resource<User> luisResource = userAssembler.toResource(luis);
		Resource<User> danielResource = userAssembler.toResource(daniel);

		given(usersService.getUsers("")).willReturn(Arrays.asList(joao, luis, daniel));

		String href = joaoResource.getLink("users").getHref();
		mvc.perform(get(href))
		.andExpect(status().isOk())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$._embedded.users", hasSize(3)))
		.andExpect(jsonPath("$._embedded.users[0].id", is((int)joao.getId())))
		.andExpect(jsonPath("$._embedded.users[0].firstName", is(joao.getFirstName())))
		.andExpect(jsonPath("$._embedded.users[0].lastName", is(joao.getLastName())))
		.andExpect(jsonPath("$._embedded.users[0].username", is(joao.getUsername())))
		.andExpect(jsonPath("$._embedded.users[0].email", is(joao.getEmail())))
		.andExpect(jsonPath("$._embedded.users[0].role", is(joao.getRole())))
		.andExpect(jsonPath("$._embedded.users[0]._links.self.href", is(ROOT + joaoResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.users[0]._links.users.href", is(ROOT + joaoResource.getLink("users").getHref())))
		.andExpect(jsonPath("$._embedded.users[1].id", is((int)luis.getId())))
		.andExpect(jsonPath("$._embedded.users[1].firstName", is(luis.getFirstName())))
		.andExpect(jsonPath("$._embedded.users[1].lastName", is(luis.getLastName())))
		.andExpect(jsonPath("$._embedded.users[1].username", is(luis.getUsername())))
		.andExpect(jsonPath("$._embedded.users[1].email", is(luis.getEmail())))
		.andExpect(jsonPath("$._embedded.users[1].role", is(luis.getRole())))
		.andExpect(jsonPath("$._embedded.users[1]._links.self.href", is(ROOT + luisResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.users[1]._links.users.href", is(ROOT + luisResource.getLink("users").getHref())))
		.andExpect(jsonPath("$._embedded.users[2].id", is((int)daniel.getId())))
		.andExpect(jsonPath("$._embedded.users[2].firstName", is(daniel.getFirstName())))
		.andExpect(jsonPath("$._embedded.users[2].lastName", is(daniel.getLastName())))
		.andExpect(jsonPath("$._embedded.users[2].username", is(daniel.getUsername())))
		.andExpect(jsonPath("$._embedded.users[2].email", is(daniel.getEmail())))
		.andExpect(jsonPath("$._embedded.users[2].role", is(daniel.getRole())))
		.andExpect(jsonPath("$._embedded.users[2]._links.self.href", is(ROOT + danielResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.users[2]._links.users.href", is(ROOT + danielResource.getLink("users").getHref())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + href)))
		.andExpect(jsonPath("$._links.root.href", is(ROOT + "/")));

		verify(usersService, times(1)).getUsers("");
	}

	@Test
	public void testAddUser() throws Exception {
		User manuel = createManuelUser();
		Resource<User> manuelResource = userAssembler.toResource(manuel);

		given(usersService.addUser(manuel)).willReturn(manuel);
		given(usersService.getUser(manuel.getId())).willReturn(Optional.of(manuel));

		String json = objectMapper.writeValueAsString(manuel);
		mvc.perform(post(manuelResource.getLink("users").getHref())
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
		.andExpect(status().isCreated())
		.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
		.andExpect(jsonPath("$.id", is((int)manuel.getId())))
		.andExpect(jsonPath("$.firstName", is(manuel.getFirstName())))
		.andExpect(jsonPath("$.lastName", is(manuel.getLastName())))
		.andExpect(jsonPath("$.username", is(manuel.getUsername())))
		.andExpect(jsonPath("$.email", is(manuel.getEmail())))
		.andExpect(jsonPath("$.role", is(manuel.getRole())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + manuelResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._links.users.href", is(ROOT + manuelResource.getLink("users").getHref())));

		verify(usersService, times(1)).addUser(manuel);

		performGet(manuel);

		verify(usersService, times(1)).getUser(manuel.getId());
	}

	@Test
	public void testGetUser() throws Exception {
		User manuel = createManuelUser();

		given(usersService.getUser(manuel.getId())).willReturn(Optional.of(manuel));

		performGet(manuel);

		verify(usersService, times(1)).getUser(manuel.getId());
	}

	@Test
	public void testUpdateUser() throws Exception {
		String json = "{\"id\":4,\"firstName\":\"Manuel\",\"lastName\":\"Coelho\",\"username\":\"mcoelho\"," +
				"\"email\":\"manuel@email.pt\",\"role\":null," +
				"\"approveProposals\":[{\"id\":1,\"title\":\"A proposal title\"," +
				"\"description\":\"A very detailed description about this proposal\"," +
				"\"state\":\"APPROVED\"}]}";
		User manuel = objectMapper.readValue(json, User.class);
		Resource<User> manuelResource = userAssembler.toResource(manuel);

		given(usersService.getUser(manuel.getId())).willReturn(Optional.of(manuel));

		performGet(manuel);

		verify(usersService, times(1)).getUser(manuel.getId());

		given(usersService.addUser(manuel)).willReturn(manuel);
		given(usersService.getUser(manuel.getId())).willReturn(Optional.of(manuel));

		mvc.perform(put(manuelResource.getLink("self").getHref())
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
		.andExpect(status().isNoContent());

		verify(usersService, times(1)).updateUser(manuel);

		performGet(manuel);

		verify(usersService, times(2)).getUser(manuel.getId());
	}

	@Test
	public void testDeleteUser() throws Exception {
		User manuel = createManuelUser();
		Resource<User> manuelResource = userAssembler.toResource(manuel);

		when(usersService.getUser(manuel.getId()))
				.thenReturn(Optional.of(manuel))
				.thenThrow(NotFoundException.class);

		String href = manuelResource.getLink("self").getHref();

		performGet(manuel);

		verify(usersService, times(1)).getUser(manuel.getId());

		mvc.perform(delete(href))
		.andExpect(status().isNoContent());

		verify(usersService, times(1)).deleteUser(manuel.getId());

		mvc.perform(get(href))
		.andExpect(status().isNotFound());

		verify(usersService, times(2)).getUser(manuel.getId());
	}

	@Test
	public void testNotFoundEmployee() throws Exception {
		User manuel = createManuelUser();
		Resource<User> manuelResource = userAssembler.toResource(manuel);

		given(usersService.getUser(manuel.getId())).willReturn(Optional.of(manuel));

		mvc.perform(get("http://localhost/users/2"))
		.andExpect(status().isNotFound());

		verify(usersService, times(1)).getUser(2L);
	}

	@Test
	public void testBadRequestUpdateUser() throws Exception {
		User manuel = createManuelUser();
		Resource<User> manuelResource = userAssembler.toResource(manuel);
		String href = manuelResource.getLink("self").getHref();
		given(usersService.getUser(manuel.getId())).willReturn(Optional.of(manuel));

		performGet(manuel);

		verify(usersService, times(1)).getUser(manuel.getId());

		given(usersService.addUser(manuel)).willReturn(manuel);
		given(usersService.getUser(manuel.getId())).willReturn(Optional.of(manuel));

		manuel.setId(2L);
		String json = objectMapper.writeValueAsString(manuel);
		mvc.perform(put(href)
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
		.andExpect(status().isBadRequest());
	}

	private MvcResult performGet(User user) throws Exception {
		Resource<User> userResource = userAssembler.toResource(user);
		return mvc.perform(get(userResource.getLink("self").getHref()))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)user.getId())))
				.andExpect(jsonPath("$.firstName", is(user.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(user.getLastName())))
				.andExpect(jsonPath("$.username", is(user.getUsername())))
				.andExpect(jsonPath("$.email", is(user.getEmail())))
				.andExpect(jsonPath("$.role", is(user.getRole())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + userResource.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.users.href", is(ROOT + userResource.getLink("users").getHref()))).andReturn();
	}

}
