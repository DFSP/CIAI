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
import java.util.Iterator;
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

import pt.unl.fct.ciai.assembler.BidProposalResourceAssembler;
import pt.unl.fct.ciai.assembler.ProposalResourceAssembler;
import pt.unl.fct.ciai.assembler.UserProposalResourceAssembler;
import pt.unl.fct.ciai.assembler.UserResourcesAssembler;
import pt.unl.fct.ciai.controller.UsersController;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.Proposal;
import pt.unl.fct.ciai.model.User;
import pt.unl.fct.ciai.service.ProposalsService;
import pt.unl.fct.ciai.service.UsersService;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = UsersController.class, secure = false)
@Import({UserResourcesAssembler.class, UserProposalResourceAssembler.class, BidProposalResourceAssembler.class})
public class UsersControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private UsersService usersService;
	@Autowired
	private UserResourcesAssembler userAssembler;
	@Autowired
	private UserProposalResourceAssembler userProposalAssembler;
	@Autowired
	private BidProposalResourceAssembler bidProposalAssembler;
	private final ObjectMapper objectMapper;

	private static final String ROOT = "http://localhost";

	public UsersControllerTest() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jackson2HalModule());
	}

	private Proposal createProposal1() {
		return new Proposal()
				.id(1L)
				.title("A proposal title")
				.description("A very detailed description about this proposal")
				.approved();
	}
	private Proposal createProposal2() {
		return new Proposal()
				.id(2L)
				.title("A proposal title 2")
				.description("Description")
				.approved();
	}

	private User createJoaoUser() {
		Proposal p1 = createProposal1();
		Proposal p2 = createProposal2();
		return new User()
				.id(1L)
				.firstName("João")
				.lastName("Reis")
				.username("jreis")
				.email("jreis@email.com")
				.role(User.Role.ROLE_COMPANY_ADMIN)
				.password("password")
				.addBid(p1)
				.addBid(p2);
	}

	private User createLuisUser() {
		Proposal p1 = createProposal1();
		Proposal p2 = createProposal2();
		return new User()
		.id(2L)
		.firstName("Luis")
		.lastName("Martins")
		.username("lmartins")
		.email("lmartins@email.com")
		.role(User.Role.ROLE_COMPANY_ADMIN)
		.password("password")
				.addProposal(p1)
				.addProposal(p2);
	}

	private User createDanielUser() {
		return new User()
		.id(3L)
		.firstName("Daniel")
		.lastName("Pimenta")
		.username("dpimenta")
		.email("dpimenta@email.com")
		.role(User.Role.ROLE_COMPANY_ADMIN)
		.password("password");
	}

	private User createManuelUser() {
		return new User()
		.id(4L)
		.firstName("Manuel")
		.lastName("Coelho")
		.username("mcoelho")
		.email("mcoelho@email.com")
		.password("password");
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
		.andExpect(jsonPath("$._embedded.users[0].role", is(joao.getRole().name())))
		.andExpect(jsonPath("$._embedded.users[0]._links.self.href", is(ROOT + joaoResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.users[0]._links.users.href", is(ROOT + joaoResource.getLink("users").getHref())))
		.andExpect(jsonPath("$._embedded.users[1].id", is((int)luis.getId())))
		.andExpect(jsonPath("$._embedded.users[1].firstName", is(luis.getFirstName())))
		.andExpect(jsonPath("$._embedded.users[1].lastName", is(luis.getLastName())))
		.andExpect(jsonPath("$._embedded.users[1].username", is(luis.getUsername())))
		.andExpect(jsonPath("$._embedded.users[1].email", is(luis.getEmail())))
		.andExpect(jsonPath("$._embedded.users[1].role", is(luis.getRole().name())))
		.andExpect(jsonPath("$._embedded.users[1]._links.self.href", is(ROOT + luisResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.users[1]._links.users.href", is(ROOT + luisResource.getLink("users").getHref())))
		.andExpect(jsonPath("$._embedded.users[2].id", is((int)daniel.getId())))
		.andExpect(jsonPath("$._embedded.users[2].firstName", is(daniel.getFirstName())))
		.andExpect(jsonPath("$._embedded.users[2].lastName", is(daniel.getLastName())))
		.andExpect(jsonPath("$._embedded.users[2].username", is(daniel.getUsername())))
		.andExpect(jsonPath("$._embedded.users[2].email", is(daniel.getEmail())))
		.andExpect(jsonPath("$._embedded.users[2].role", is(daniel.getRole().name())))
		.andExpect(jsonPath("$._embedded.users[2]._links.self.href", is(ROOT + danielResource.getLink("self").getHref())))
		.andExpect(jsonPath("$._embedded.users[2]._links.users.href", is(ROOT + danielResource.getLink("users").getHref())))
		.andExpect(jsonPath("$._links.self.href", is(ROOT + href)))
		.andExpect(jsonPath("$._links.root.href", is(ROOT + "/")));

		verify(usersService, times(1)).getUsers("");
	}

	@Test
	public void testAddUser() throws Exception {
		User manuel = createManuelUser();
		manuel.setId(0);
		Resource<User> manuelResource = userAssembler.toResource(manuel);

		given(usersService.addUser(manuel)).willReturn(manuel);
		given(usersService.getUser(manuel.getId())).willReturn(Optional.of(manuel));

		String json = "{\"firstName\":\"Manuel\",\"lastName\":\"Coelho\",\"username\":\"mcoelho\"," +
				"\"password\":\"password\",\"email\":\"mcoelho@email.com\"}";
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
	public void testGetProposals() throws Exception {
		User luis = createLuisUser();
		Resource<User> luisResource = userAssembler.toResource(luis);
		Iterator<Proposal> it = luis.getProposals().get().iterator();

		Proposal p1 = it.next();
		p1.setProposer(luis);
		Resource<Proposal> prop1Resource = userProposalAssembler.toResource(p1, luis);

		Proposal p2 = it.next();
		p2.setProposer(luis);
		Resource<Proposal> prop2Resource = userProposalAssembler.toResource(p2, luis);

		given(usersService.getUser(luis.getId())).willReturn(Optional.of(luis));
		given(usersService.getProposals(luis.getId(), "")).willReturn(Arrays.asList(p1, p2));

		String href = luisResource.getLink("proposals").getHref();
		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.proposals", hasSize(2)))
				.andExpect(jsonPath("$._embedded.proposals[0].id", is((int)p1.getId())))
				.andExpect(jsonPath("$._embedded.proposals[0].title", is(p1.getTitle())))
				.andExpect(jsonPath("$._embedded.proposals[0].description", is(p1.getDescription())))
				.andExpect(jsonPath("$._embedded.proposals[0].creationDate", is(p1.getCreationDate())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.self.href", is(ROOT + prop1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.proposals.href", is(ROOT + prop1Resource.getLink("proposals").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.reviews.href", is(ROOT + prop1Resource.getLink("reviews").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.reviewBids.href", is(ROOT + prop1Resource.getLink("reviewBids").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.sections.href", is(ROOT + prop1Resource.getLink("sections").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.comments.href", is(ROOT + prop1Resource.getLink("comments").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.members.href", is(ROOT + prop1Resource.getLink("members").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.staff.href", is(ROOT + prop1Resource.getLink("staff").getHref())))

				.andExpect(jsonPath("$._embedded.proposals[1].id", is((int)p2.getId())))
				.andExpect(jsonPath("$._embedded.proposals[1].title", is(p2.getTitle())))
				.andExpect(jsonPath("$._embedded.proposals[1].description", is(p2.getDescription())))
				.andExpect(jsonPath("$._embedded.proposals[1].creationDate", is(p2.getCreationDate())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.self.href", is(ROOT + prop2Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.proposals.href", is(ROOT + prop2Resource.getLink("proposals").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.reviews.href", is(ROOT + prop2Resource.getLink("reviews").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.reviewBids.href", is(ROOT + prop2Resource.getLink("reviewBids").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.sections.href", is(ROOT + prop2Resource.getLink("sections").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.comments.href", is(ROOT + prop2Resource.getLink("comments").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.members.href", is(ROOT + prop2Resource.getLink("members").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.staff.href", is(ROOT + prop2Resource.getLink("staff").getHref())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + href)))
				.andExpect(jsonPath("$._links.root.href", is(ROOT + "/")));

		verify(usersService, times(1)).getProposals(luis.getId(), "");
	}

	@Test
	public void testGetProposal() throws Exception {
		User luis = createLuisUser();
		Proposal prop1 = createProposal1();
		prop1.setProposer(luis);
		prop1.addStaff(luis);
		luis.addProposal(prop1);

		Resource<User> luisResource = userAssembler.toResource(luis);
		Resource<Proposal> prop1Resource = userProposalAssembler.toResource(prop1, luis);

		given(usersService.getUser(luis.getId())).willReturn(Optional.of(luis));
		given(usersService.getProposal(luis.getId(), prop1.getId())).willReturn(Optional.of(prop1));

		mvc.perform(get(prop1Resource.getLink("self").getHref()))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)prop1.getId())))
				.andExpect(jsonPath("$.title", is(prop1.getTitle())))
				.andExpect(jsonPath("$.description", is(prop1.getDescription())))
				.andExpect(jsonPath("$.creationDate", is(prop1.getCreationDate())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + prop1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.proposals.href", is(ROOT + prop1Resource.getLink("proposals").getHref())))
				.andExpect(jsonPath("$._links.reviews.href", is(ROOT + prop1Resource.getLink("reviews").getHref())))
				.andExpect(jsonPath("$._links.sections.href", is(ROOT + prop1Resource.getLink("sections").getHref())))
				.andExpect(jsonPath("$._links.comments.href", is(ROOT + prop1Resource.getLink("comments").getHref())))
				.andExpect(jsonPath("$._links.members.href", is(ROOT + prop1Resource.getLink("members").getHref())))
				.andExpect(jsonPath("$._links.reviewBids.href", is(ROOT + prop1Resource.getLink("reviewBids").getHref())))
				.andExpect(jsonPath("$._links.staff.href", is(ROOT + prop1Resource.getLink("staff").getHref())));


		verify(usersService, times(1)).getUser(luis.getId());
		verify(usersService, times(1)).getProposal(luis.getId(), prop1.getId());
	}

	@Test
	public void testGetBid() throws Exception {
		User joao = createJoaoUser();
		Proposal prop1 = createProposal1();
		prop1.setProposer(joao);
		prop1.addStaff(joao);
		joao.addProposal(prop1);

		Resource<User> joaoResource = userAssembler.toResource(joao);
		Resource<Proposal> prop1Resource = bidProposalAssembler.toResource(prop1, joao);

		given(usersService.getUser(joao.getId())).willReturn(Optional.of(joao));
		given(usersService.getBid(joao.getId(), prop1.getId())).willReturn(Optional.of(prop1));

		mvc.perform(get(prop1Resource.getLink("self").getHref()))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)prop1.getId())))
				.andExpect(jsonPath("$.title", is(prop1.getTitle())))
				.andExpect(jsonPath("$.description", is(prop1.getDescription())))
				.andExpect(jsonPath("$.creationDate", is(prop1.getCreationDate())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + prop1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.bids.href", is(ROOT + prop1Resource.getLink("bids").getHref())))
				.andExpect(jsonPath("$._links.reviews.href", is(ROOT + prop1Resource.getLink("reviews").getHref())))
				.andExpect(jsonPath("$._links.sections.href", is(ROOT + prop1Resource.getLink("sections").getHref())))
				.andExpect(jsonPath("$._links.comments.href", is(ROOT + prop1Resource.getLink("comments").getHref())))
				.andExpect(jsonPath("$._links.members.href", is(ROOT + prop1Resource.getLink("members").getHref())))
				.andExpect(jsonPath("$._links.staff.href", is(ROOT + prop1Resource.getLink("staff").getHref())));

		verify(usersService, times(1)).getUser(joao.getId());
		verify(usersService, times(1)).getBid(joao.getId(), prop1.getId());
	}

	@Test
	public void testGetBids() throws Exception {
		User joao = createJoaoUser();
		Resource<User> joaoResource = userAssembler.toResource(joao);
		System.out.println(joao.getBids().get());
		Iterator<Proposal> it = joao.getBids().get().iterator();


		Proposal bid1 = it.next();
		bid1.setProposer(joao);
		bid1.addReviewBid(joao);
		Resource<Proposal> bid1Resource = bidProposalAssembler.toResource(bid1, joao);

		Proposal bid2 = it.next();
		bid2.setProposer(joao);
		bid2.addReviewBid(joao);
		Resource<Proposal> bid2Resource = bidProposalAssembler.toResource(bid2, joao);

		given(usersService.getUser(joao.getId())).willReturn(Optional.of(joao));
		given(usersService.getBids(joao.getId(), "")).willReturn(Arrays.asList(bid1, bid2));

		String href = joaoResource.getLink("bids").getHref();

		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.proposals", hasSize(2)))
				.andExpect(jsonPath("$._embedded.proposals[0].id", is((int)bid1.getId())))
				.andExpect(jsonPath("$._embedded.proposals[0].title", is(bid1.getTitle())))
				.andExpect(jsonPath("$._embedded.proposals[0].description", is(bid1.getDescription())))
				.andExpect(jsonPath("$._embedded.proposals[0].creationDate", is(bid1.getCreationDate())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.self.href", is(ROOT + bid1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.bids.href", is(ROOT + bid1Resource.getLink("bids").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.reviews.href", is(ROOT + bid1Resource.getLink("reviews").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.sections.href", is(ROOT + bid1Resource.getLink("sections").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.comments.href", is(ROOT + bid1Resource.getLink("comments").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.members.href", is(ROOT + bid1Resource.getLink("members").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.staff.href", is(ROOT + bid1Resource.getLink("staff").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.reviewBids.href", is(ROOT + bid1Resource.getLink("reviewBids").getHref())))

				.andExpect(jsonPath("$._embedded.proposals[1].id", is((int)bid2.getId())))
				.andExpect(jsonPath("$._embedded.proposals[1].title", is(bid2.getTitle())))
				.andExpect(jsonPath("$._embedded.proposals[1].description", is(bid2.getDescription())))
				.andExpect(jsonPath("$._embedded.proposals[1].creationDate", is(bid2.getCreationDate())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.self.href", is(ROOT + bid2Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.bids.href", is(ROOT + bid2Resource.getLink("bids").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.reviews.href", is(ROOT + bid2Resource.getLink("reviews").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.sections.href", is(ROOT + bid2Resource.getLink("sections").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.comments.href", is(ROOT + bid2Resource.getLink("comments").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.members.href", is(ROOT + bid2Resource.getLink("members").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.staff.href", is(ROOT + bid2Resource.getLink("staff").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.reviewBids.href", is(ROOT + bid2Resource.getLink("reviewBids").getHref())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + href)))
				.andExpect(jsonPath("$._links.root.href", is(ROOT + "/")));

		verify(usersService, times(1)).getBids(joao.getId(), "");
	}

	@Test
	public void testNotFoundUser() throws Exception {
		User manuel = createManuelUser();
		Resource<User> manuelResource = userAssembler.toResource(manuel);

		given(usersService.getUser(manuel.getId())).willReturn(Optional.of(manuel));

		mvc.perform(get("http://localhost/users/2"))
		.andExpect(status().isNotFound());

		verify(usersService, times(1)).getUser(2L);
	}

	@Test
	public void testNotFoundProposal() throws Exception{
		User luis = createLuisUser();
		Resource<User> luisResource = userAssembler.toResource(luis);

		given(usersService.getUser(luis.getId())).willReturn(Optional.of(luis));

		mvc.perform(get("http://localhost/users/"+luis.getId()+"/proposals/999"))
				.andExpect(status().isNotFound());

		verify(usersService, times(1)).getUser(luis.getId());
		verify(usersService, times(1)).getProposal(luis.getId(), 999);
	}

	@Test
	public void testNotFoundBid() throws Exception{
		User manuel = createManuelUser();
		Resource<User> manuelResource = userAssembler.toResource(manuel);

		given(usersService.getUser(manuel.getId())).willReturn(Optional.of(manuel));

		mvc.perform(get("http://localhost/users/"+manuel.getId()+"/bids/999"))
				.andExpect(status().isNotFound());

		verify(usersService, times(1)).getUser(manuel.getId());
		verify(usersService, times(1)).getBid(manuel.getId(),999);
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
