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

import org.springframework.test.web.servlet.MvcResult;
import pt.unl.fct.ciai.assembler.*;
import pt.unl.fct.ciai.controller.ProposalsController;
import pt.unl.fct.ciai.exception.NotFoundException;
import pt.unl.fct.ciai.model.*;
import pt.unl.fct.ciai.service.ProposalsService;
import pt.unl.fct.ciai.service.UsersService;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.*;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProposalsController.class, secure = false)
@Import({ProposalResourceAssembler.class, SectionResourcesAssembler.class,
	ReviewResourcesAssembler.class, CommentResourcesAssembler.class,
	UserResourcesAssembler.class, StaffResourcesAssembler.class,
	MemberResourceAssembler.class})
public class ProposalsControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private ProposalsService proposalsService;
	@MockBean
	private UsersService usersService;

	@Autowired
	private ProposalResourceAssembler proposalAssembler;
	@Autowired
	private SectionResourcesAssembler sectionAssembler;
	@Autowired
	private ReviewResourcesAssembler reviewAssembler;
	@Autowired
	private CommentResourcesAssembler commentAssembler;
	@Autowired
	private StaffResourcesAssembler staffAssembler;
	@Autowired
	private UserResourcesAssembler userAssembler;
	private final ObjectMapper objectMapper;

	private static final String ROOT = "http://localhost";

	public ProposalsControllerTest() {
		objectMapper = new ObjectMapper();
		objectMapper.registerModule(new Jackson2HalModule());
	}

	private Proposal createProposal_1() {
		Proposal p1 = new Proposal();
		p1.setId(1L);
		p1.setTitle("Proposal 1");
		p1.setDescription("Proposal 1 - Description");
		p1.setCreationDate(new Date());

		Review r1 = create_Review_1();
		r1.setProposal(p1);

		Section s1=create_Section_1();
		s1.setProposal(p1);

		Comment c1=create_Comment_1();
		c1.setProposal(p1);

		User u1 = create_User_1();
		p1.setProposer(u1);
		p1.addStaff(u1);
		u1.addProposal(p1);
		p1.addReviewBid(u1);
		u1.addBid(p1);

		p1.addReview(r1);
		p1.addSection(s1);
		p1.addComment(c1);

		return p1;
	}
	private Proposal createProposal_2() {
		Proposal p2 = new Proposal();
		p2.setId(2L);
		p2.setTitle("Proposal 2");
		p2.setDescription("Proposal 2 - Description");
		p2.setCreationDate(new Date());

		Section s1=create_Section_1();
		s1.setProposal(p2);
		p2.addSection(s1);

		Section s2=create_Section_2();
		s2.setProposal(p2);
		p2.addSection(s2);

		Review r1 = create_Review_1();
		r1.setProposal(p2);
		p2.addReview(r1);

		Review r2 = create_Review_2();
		r2.setProposal(p2);
		p2.addReview(r2);

		Comment c1=create_Comment_1();
		c1.setProposal(p2);
		p2.addComment(c1);

		Comment c2=create_Comment_2();
		c2.setProposal(p2);
		p2.addComment(c2);

		User u1 = create_User_1();
		p2.setProposer(u1);
		u1.addProposal(p2);


		User bidder2 = create_User_2();
		bidder2.addBid(p2);
		p2.addReviewBid(bidder2);

		return p2;
	}

	private Review create_Review_1() {
		Review r = new Review();
		r.setClassification(3);
		r.setTitle("Review 1");
		r.setText("Text of Review 1");
		r.setSummary("Summary of review 1");
		return r;
	}
	private Review create_Review_2() {
		Review r = new Review();
		r.setClassification(5);
		r.setTitle("Review 2");
		r.setText("Text of Review 2");
		r.setSummary("Summary of review 2");
		return r;
	}

	private Section create_Section_1(){
		Section s = new Section();
		s.setTitle("Title of section 1");
		s.setDescription("Description of section 1");
		s.setBudget(500);
		s.setGoals("Goals of section 1");
		s.setMaterial("Material of section 1");
		s.setWorkPlan("WorkPlan of section 1");
		return s;
	}
	private Section create_Section_2(){
		Section s = new Section();
		s.setBudget(1000);
		s.setTitle("Title of section 2");
		s.setDescription("Description of section 2");
		s.setGoals("Goals of section 2");
		s.setMaterial("Material of section 2");
		s.setWorkPlan("WorkPlan of section 2");
		return s;
	}

	private Comment create_Comment_1(){
		Comment c = new Comment();
		c.setTitle("Comment 1");
		c.setText("Text of comment 1");
		c.setCreationDate(new Date());
		return c;
	}
	private Comment create_Comment_2(){
		Comment c = new Comment();
		c.setTitle("Comment 2");
		c.setText("Text of comment 2");
		c.setCreationDate(new Date());
		return c;
	}

	private User create_User_1(){
		User u = new User();
		u.setUsername("user1");
		u.setFirstName("Utilizador");
		u.setLastName("Uno");
		u.setEmail("user1@mail.com");
		return u;
	}

	private User create_User_2(){
		User u = new User();
		u.setUsername("user2");
		u.setFirstName("User");
		u.setLastName("Dois");
		u.setEmail("utilizador2@mail.com");
		return u;
	}

	@Test
	public void testGetProposals() throws Exception {
		Proposal p1 = createProposal_1();
		Proposal p2 = createProposal_2();

		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);
		Resource<Proposal> p2Resource = proposalAssembler.toResource(p2);

		given(proposalsService.getProposals("")).willReturn(Arrays.asList(p1, p2));

		String href = p1Resource.getLink("proposals").getHref();
		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.proposals", hasSize(2)))
				.andExpect(jsonPath("$._embedded.proposals[0].id", is((int)p1.getId())))
				.andExpect(jsonPath("$._embedded.proposals[0].title", is(p1.getTitle())))
				.andExpect(jsonPath("$._embedded.proposals[0].description", is(p1.getDescription())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.self.href", is(ROOT + p1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.proposals.href", is(ROOT + p1Resource.getLink("proposals").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1].id", is((int)p2.getId())))
				.andExpect(jsonPath("$._embedded.proposals[1].title", is(p2.getTitle())))
				.andExpect(jsonPath("$._embedded.proposals[1].description", is(p2.getDescription())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.self.href", is(ROOT + p2Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.proposals.href", is(ROOT + p2Resource.getLink("proposals").getHref())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + href)))
				.andExpect(jsonPath("$._links.root.href", is(ROOT + "/")));

		verify(proposalsService, times(1)).getProposals("");

	}

	@Test
	public void testAddProposal() throws Exception {
		Proposal p1 = createProposal_1();
		p1.setId(0);
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		given(proposalsService.addProposal(p1)).willReturn(p1);
		given(proposalsService.getProposal(p1.getId())).willReturn(Optional.of(p1));

		String json = "{\"title\":\"Proposal 1\",\"description\":\"Proposal 1 - Description\"}";

		mvc.perform(post(p1Resource.getLink("proposals").getHref())
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)p1.getId())))
				.andExpect(jsonPath("$.title", is(p1.getTitle())))
				.andExpect(jsonPath("$.description", is(p1.getDescription())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + p1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.proposals.href", is(ROOT + p1Resource.getLink("proposals").getHref())));

		verify(proposalsService, times(1)).addProposal(p1);

		performGetProposal(p1);

		verify(proposalsService, times(1)).getProposal(p1.getId());
	}

	@Test
	public void testGetProposal() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		given(proposalsService.getProposal(p1.getId())).willReturn(Optional.of(p1));

		performGetProposal(p1);

		verify(proposalsService, times(1)).getProposal(p1.getId());
	}

	@Test
	public void testUpdateProposal() throws Exception {
		String json = "{\"id\":1,\"title\":\"Proposal 1\",\"description\":\"New description updated\"}";

		Proposal p1 = objectMapper.readValue(json, Proposal.class);
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		given(proposalsService.getProposal(p1.getId())).willReturn(Optional.of(p1));

		performGetProposal(p1);

		verify(proposalsService, times(1)).getProposal(p1.getId());

		given(proposalsService.addProposal(p1)).willReturn(p1);
		given(proposalsService.getProposal(p1.getId())).willReturn(Optional.of(p1));

		mvc.perform(put(p1Resource.getLink("self").getHref())
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
				.andExpect(status().isNoContent());

		verify(proposalsService, times(1)).updateProposal(p1);

		performGetProposal(p1);

		verify(proposalsService, times(3)).getProposal(p1.getId());
	}

	@Test
	public void testDeleteProposal() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> proposalResource = proposalAssembler.toResource(p1);

		when(proposalsService.getProposal(p1.getId()))
				.thenReturn(Optional.of(p1))
				.thenThrow(NotFoundException.class);

		String href = proposalResource.getLink("self").getHref();

		performGetProposal(p1);

		verify(proposalsService, times(1)).getProposal(p1.getId());

		mvc.perform(delete(href))
				.andExpect(status().isNoContent());

		verify(proposalsService, times(1)).deleteProposal(p1.getId());

		mvc.perform(get(href))
				.andExpect(status().isNotFound());

		verify(proposalsService, times(2)).getProposal(p1.getId());
	}


	@Test
	public void testGetSections() throws Exception {
		Proposal p1 = createProposal_2();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		Iterator<Section> it = p1.getSections().get().iterator();

		Section s1 = it.next();
		Resource<Section> s1Resource = sectionAssembler.toResource(s1);

		Section s2 = it.next();
		Resource<Section> s2Resource = sectionAssembler.toResource(s2);

		given(proposalsService.getProposal(p1.getId())).willReturn(Optional.of(p1));
		given(proposalsService.getSections(p1.getId(), "")).willReturn(p1.getSections().get());

		String href = p1Resource.getLink("sections").getHref();
		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.sections", hasSize(2)))

				.andExpect(jsonPath("$._embedded.sections[0].id", is((int)s1.getId())))
				.andExpect(jsonPath("$._embedded.sections[0].title", is(s1.getTitle())))
				.andExpect(jsonPath("$._embedded.sections[0].description", is(s1.getDescription())))
				.andExpect(jsonPath("$._embedded.sections[0].material", is(s1.getMaterial())))
				.andExpect(jsonPath("$._embedded.sections[0].workPlan", is(s1.getWorkPlan())))
				.andExpect(jsonPath("$._embedded.sections[0].goals", is(s1.getGoals())))
				.andExpect(jsonPath("$._embedded.sections[0]._links.self.href", is(ROOT + s1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.sections[0]._links.sections.href", is(ROOT + s1Resource.getLink("sections").getHref())))

				.andExpect(jsonPath("$._embedded.sections[1].id", is((int)s2.getId())))
				.andExpect(jsonPath("$._embedded.sections[1].title", is(s2.getTitle())))
				.andExpect(jsonPath("$._embedded.sections[1].description", is(s2.getDescription())))
				.andExpect(jsonPath("$._embedded.sections[1].material", is(s2.getMaterial())))
				.andExpect(jsonPath("$._embedded.sections[1].workPlan", is(s2.getWorkPlan())))
				.andExpect(jsonPath("$._embedded.sections[1].goals", is(s2.getGoals())))
				.andExpect(jsonPath("$._embedded.sections[1]._links.self.href", is(ROOT + s2Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.sections[1]._links.sections.href", is(ROOT + s2Resource.getLink("sections").getHref())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + href)));

		verify(proposalsService, times(1)).getSections(p1.getId(), "");
	}

	@Test
	public void testAddSection() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));

		String href = p1Resource.getLink("sections").getHref();
		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.sections", hasSize(2)));

		verify(proposalsService, times(1)).getProposal(p1.getId());

		Section s1 = create_Section_1();
		Resource<Section> s1Resource = sectionAssembler.toResource(s1);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));
		when(proposalsService.addProposal(p1)).thenReturn(p1);
		when(proposalsService.addSection(s1.getId(), s1)).thenReturn(s1);

		String json = objectMapper.writeValueAsString(s1);
		mvc.perform(post(href)
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)s1.getId())))
				.andExpect(jsonPath("$.title", is(s1.getTitle())))
				.andExpect(jsonPath("$.discription", is(s1.getDescription())))
				.andExpect(jsonPath("$.material", is(s1.getMaterial())))
				.andExpect(jsonPath("$.workPlan", is(s1.getWorkPlan())))
				.andExpect(jsonPath("$.budget", is(s1.getBudget())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + s1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.sections.href", is(ROOT + href)));

		verify(proposalsService, times(2)).getProposal(p1.getId());
		verify(proposalsService, times(1)).addProposal(p1);
		verify(proposalsService, times(1)).addSection(s1.getId(), s1);

		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.sections", hasSize(3)));

		verify(proposalsService, times(3)).getProposal(p1.getId());
	}


	@Test
	public void testGetSection() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);
		Section s1 = create_Section_1();
		s1.setProposal(p1);
		Resource<Section> s1Resource = sectionAssembler.toResource(s1);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));
		given(proposalsService.getSection(p1.getId(), s1.getId()))
				.willReturn(Optional.of(s1));

		performGetSection(s1);

		verify(proposalsService, times(1)).getProposal(0L);
		verify(proposalsService, times(1)).getSection(p1.getId(), s1.getId());

	}

	@Test
	public void testUpdateSection() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		Section s1 = create_Section_1();
		Resource<Section> s1Resource = sectionAssembler.toResource(s1);
		String href = s1Resource.getLink("self").getHref();

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));
		given(proposalsService.getSection(p1.getId(), s1.getId()))
				.willReturn(Optional.of(s1));

		performGetSection(s1);

		verify(proposalsService, times(1)).getProposal(p1.getId());
		verify(proposalsService, times(1)).getSection(p1.getId(), s1.getId());

		given(proposalsService.addSection(p1.getId(), s1)).willReturn(s1);
		given(proposalsService.getProposal(p1.getId())).willReturn(Optional.of(p1));
		given(proposalsService.getSection(p1.getId(), s1.getId())).willReturn(Optional.of(s1));

		s1.setTitle("Novo titulo");
		String json = objectMapper.writeValueAsString(s1);
		mvc.perform(put(href)
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
				.andExpect(status().isNoContent());

		verify(proposalsService, times(1)).addSection(p1.getId(), s1);
		verify(proposalsService, times(2)).getProposal(p1.getId());
		verify(proposalsService, times(2)).getSection(p1.getId(), s1.getId());

		performGetSection(s1);

		verify(proposalsService, times(3)).getProposal(p1.getId());
		verify(proposalsService, times(3)).getSection(p1.getId(), s1.getId());
	}

	@Test
	public void testDeleteSection() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> proposalResource = proposalAssembler.toResource(p1);
		Section s1 = p1.getSections().get().iterator().next();
		String href = sectionAssembler.toResource(s1).getLink("self").getHref();

		given(proposalsService.getProposal(p1.getId())).willReturn(Optional.of(p1));

		when(proposalsService.getSection(p1.getId(), s1.getId()))
				.thenReturn(Optional.of(s1))
				.thenReturn(Optional.of(s1))
				.thenReturn(Optional.ofNullable(null));

		performGetSection(s1);

		verify(proposalsService, times(1)).getProposal(p1.getId());
		verify(proposalsService, times(1)).getSection(p1.getId(), s1.getId());

		mvc.perform(delete(href))
				.andExpect(status().isNotFound());

		verify(proposalsService, times(2)).getProposal(p1.getId());
		verify(proposalsService, times(2)).getSection(p1.getId(), s1.getId());
		verify(proposalsService, times (1)).deleteSection(p1.getId(), s1.getId());

		mvc.perform(get(href))
				.andExpect(status().isNotFound());

		verify(proposalsService, times(3)).getProposal(p1.getId());
		verify(proposalsService, times(3)).getSection(p1.getId(), s1.getId());

	}

	@Test
	public void testGetReviews() throws Exception {
		Proposal p1 = createProposal_2();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);
		Iterator<Review> it = p1.getReviews().get().iterator();

		Review r1 =  it.next();
		Resource<Review> r1Resource = reviewAssembler.toResource(r1);

		Review r2 = it.next();
		Resource<Review> r2Resource = reviewAssembler.toResource(r2);

		Review r3 = it.next();
		Resource<Review> r3Resource = reviewAssembler.toResource(r3);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));

		String href = p1Resource.getLink("reviews").getHref();
		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.reviews", hasSize(2)))
				.andExpect(jsonPath("$._embedded.reviews[0].id", is((int)r1.getId())))
				.andExpect(jsonPath("$._embedded.reviews[0].title", is(r1.getTitle())))
				.andExpect(jsonPath("$._embedded.reviews[0].text", is(r1.getText())))
				.andExpect(jsonPath("$._embedded.reviews[0].summary", is(r1.getSummary())))
				.andExpect(jsonPath("$._embedded.reviews[0].classification", is(r1.getClassification())))
				.andExpect(jsonPath("$._embedded.reviews[0]._links.self.href", is(ROOT + r1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.reviews[0]._links.reviews.href", is(ROOT + r1Resource.getLink("reviews").getHref())))

				.andExpect(jsonPath("$._embedded.reviews[1].id", is((int)r2.getId())))
				.andExpect(jsonPath("$._embedded.reviews[1].title", is(r2.getTitle())))
				.andExpect(jsonPath("$._embedded.reviews[1].text", is(r2.getText())))
				.andExpect(jsonPath("$._embedded.reviews[1].summary", is(r2.getSummary())))
				.andExpect(jsonPath("$._embedded.reviews[1].classification", is(r2.getClassification())))
				.andExpect(jsonPath("$._embedded.reviews[1]._links.self.href", is(ROOT + r2Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.reviews[1]._links.reviews.href", is(ROOT + r2Resource.getLink("reviews").getHref())))

				.andExpect(jsonPath("$._embedded.reviews[2].id", is((int)r3.getId())))
				.andExpect(jsonPath("$._embedded.reviews[2].title", is(r3.getTitle())))
				.andExpect(jsonPath("$._embedded.reviews[2].text", is(r3.getText())))
				.andExpect(jsonPath("$._embedded.reviews[2].summary", is(r3.getSummary())))
				.andExpect(jsonPath("$._embedded.reviews[2].classification", is(r3.getClassification())))
				.andExpect(jsonPath("$._embedded.reviews[2]._links.self.href", is(ROOT + r3Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.reviews[2]._links.reviews.href", is(ROOT + r3Resource.getLink("reviews").getHref())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + href)))
				.andExpect(jsonPath("$._links.root.href", is(ROOT + "/")));

		verify(proposalsService, times(1)).getProposal(p1.getId());
	}

	@Test
	public void testAddReview() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));

		String href = p1Resource.getLink("reviews").getHref();
		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.reviews", hasSize(2)));

		verify(proposalsService, times(1)).getProposal(p1.getId());

		Review r1 = create_Review_1();
		Resource<Review> r1Resource = reviewAssembler.toResource(r1);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));
		when(proposalsService.addProposal(p1)).thenReturn(p1);
		when(proposalsService.addReview(p1.getId(), r1)).thenReturn(r1);

		String json = objectMapper.writeValueAsString(r1);
		mvc.perform(post(href)
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)r1.getId())))
				.andExpect(jsonPath("$.title", is(r1.getTitle())))
				.andExpect(jsonPath("$.text", is(r1.getText())))
				.andExpect(jsonPath("$.summary", is(r1.getSummary())))
				.andExpect(jsonPath("$.classification", is(r1.getClassification())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + r1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.reviews.href", is(ROOT + href)));

		verify(proposalsService, times(2)).getProposal(p1.getId());
		verify(proposalsService, times(1)).addProposal(p1);
		verify(proposalsService, times(1)).addReview(p1.getId(), r1);

		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.reviews", hasSize(3)));

		verify(proposalsService, times(3)).getProposal(p1.getId());
	}

	@Test
	public void testGetReview() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);
		Review r1 = create_Review_1();
		Resource<Review> r1Resource = reviewAssembler.toResource(r1);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));
		given(proposalsService.getReview(p1.getId(), r1.getId()))
				.willReturn(Optional.of(r1));

		performGetReview(r1);

		verify(proposalsService, times(1)).getProposal(p1.getId());
		verify(proposalsService, times(1)).getReview(p1.getId(), r1.getId());
	}

	@Test
	public void testUpdateReview() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		Review r1 = create_Review_1();
		Resource<Review> r1Resource = reviewAssembler.toResource(r1);
		String href = r1Resource.getLink("self").getHref();

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));
		given(proposalsService.getReview(p1.getId(), r1.getId()))
				.willReturn(Optional.of(r1));

		performGetReview(r1);

		verify(proposalsService, times(1)).getProposal(p1.getId());
		verify(proposalsService, times(1)).getReview(p1.getId(), r1.getId());

		given(proposalsService.addReview(p1.getId(), r1)).willReturn(r1);
		given(proposalsService.getProposal(p1.getId())).willReturn(Optional.of(p1));
		given(proposalsService.getReview(p1.getId(), r1.getId())).willReturn(Optional.of(r1));

		r1.setTitle("Novo titulo");
		String json = objectMapper.writeValueAsString(r1);
		mvc.perform(put(href)
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
				.andExpect(status().isNoContent());

		verify(proposalsService, times(1)).addReview(p1.getId(), r1);
		verify(proposalsService, times(2)).getProposal(p1.getId());
		verify(proposalsService, times(2)).getReview(p1.getId(), r1.getId());

		performGetReview(r1);

		verify(proposalsService, times(3)).getProposal(p1.getId());
		verify(proposalsService, times(3)).getReview(p1.getId(), r1.getId());
	}

	@Test
	public void testDeleteReview() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> proposalResource = proposalAssembler.toResource(p1);
		Review r1 = p1.getReviews().get().iterator().next();
		String href = reviewAssembler.toResource(r1).getLink("self").getHref();

		given(proposalsService.getProposal(p1.getId())).willReturn(Optional.of(p1));

		when(proposalsService.getReview(p1.getId(), r1.getId()))
				.thenReturn(Optional.of(r1))
				.thenReturn(Optional.of(r1))
				.thenReturn(Optional.ofNullable(null));

		performGetReview(r1);

		verify(proposalsService, times(1)).getProposal(p1.getId());
		verify(proposalsService, times(1)).getReview(p1.getId(), r1.getId());

		mvc.perform(delete(href))
				.andExpect(status().isNotFound());

		verify(proposalsService, times(2)).getProposal(p1.getId());
		verify(proposalsService, times(2)).getReview(p1.getId(), r1.getId());
		verify(proposalsService, times (1)).deleteReview(p1.getId(), r1.getId());

		mvc.perform(get(href))
				.andExpect(status().isNotFound());

		verify(proposalsService, times(3)).getProposal(p1.getId());
		verify(proposalsService, times(3)).getReview(p1.getId(), r1.getId());
	}

	@Test
	public void testGetComments() throws Exception {
		Proposal p1 = createProposal_2();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);
		Iterator<Comment> it = p1.getComments().get().iterator();

		Comment c1 =  it.next();
		Resource<Comment> c1Resource = commentAssembler.toResource(c1);

		Comment c2 = it.next();
		Resource<Comment> c2Resource = commentAssembler.toResource(c2);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));

		String href = p1Resource.getLink("comments").getHref();
		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.comments", hasSize(2)))
				.andExpect(jsonPath("$._embedded.comments[0].id", is((int)c1.getId())))
				.andExpect(jsonPath("$._embedded.comments[0].title", is(c1.getTitle())))
				.andExpect(jsonPath("$._embedded.comments[0].text", is(c1.getText())))
				.andExpect(jsonPath("$._embedded.comments[0]._links.self.href", is(ROOT + c1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.comments[0]._links.comments.href", is(ROOT + c1Resource.getLink("comments").getHref())))

				.andExpect(jsonPath("$._embedded.comments[1].id", is((int)c2.getId())))
				.andExpect(jsonPath("$._embedded.comments[1].title", is(c2.getTitle())))
				.andExpect(jsonPath("$._embedded.comments[1].text", is(c2.getText())))
				.andExpect(jsonPath("$._embedded.comments[1]._links.self.href", is(ROOT + c2Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.comments[1]._links.comments.href", is(ROOT + c2Resource.getLink("comments").getHref())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + href)))
				.andExpect(jsonPath("$._links.root.href", is(ROOT + "/")));

		verify(proposalsService, times(1)).getProposal(p1.getId());
	}

	@Test
	public void testAddComment() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));

		String href = p1Resource.getLink("comments").getHref();
		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.comments", hasSize(2)));

		verify(proposalsService, times(1)).getProposal(p1.getId());

		Comment c1 = create_Comment_1();
		Resource<Comment> c1Resource = commentAssembler.toResource(c1);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));
		when(proposalsService.addProposal(p1)).thenReturn(p1);
		when(proposalsService.addComment(p1.getId(), c1)).thenReturn(c1);

		String json = objectMapper.writeValueAsString(c1);
		mvc.perform(post(href)
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)c1.getId())))
				.andExpect(jsonPath("$.title", is(c1.getTitle())))
				.andExpect(jsonPath("$.text", is(c1.getText())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + c1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.comments.href", is(ROOT + href)));

		verify(proposalsService, times(2)).getProposal(p1.getId());
		verify(proposalsService, times(1)).addProposal(p1);
		verify(proposalsService, times(1)).addComment(p1.getId(), c1);

		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.comments", hasSize(3)));

		verify(proposalsService, times(3)).getProposal(p1.getId());
	}

	@Test
	public void testGetComment() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);
		Comment c1 = create_Comment_1();
		Resource<Comment> c1Resource = commentAssembler.toResource(c1);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));
		given(proposalsService.getComment(p1.getId(), c1.getId()))
				.willReturn(Optional.of(c1));

		performGetComment(c1);

		verify(proposalsService, times(1)).getProposal(p1.getId());
		verify(proposalsService, times(1)).getComment(p1.getId(), c1.getId());
	}

	@Test
	public void testUpdateComment() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		Comment c1 = create_Comment_1();
		Resource<Comment> c1Resource = commentAssembler.toResource(c1);
		String href = c1Resource.getLink("self").getHref();

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));
		given(proposalsService.getComment(p1.getId(), c1.getId()))
				.willReturn(Optional.of(c1));

		performGetComment(c1);

		verify(proposalsService, times(1)).getProposal(p1.getId());
		verify(proposalsService, times(1)).getComment(p1.getId(), c1.getId());

		given(proposalsService.addComment(p1.getId(), c1)).willReturn(c1);
		given(proposalsService.getProposal(p1.getId())).willReturn(Optional.of(p1));
		given(proposalsService.getComment(p1.getId(), c1.getId())).willReturn(Optional.of(c1));

		c1.setTitle("Novo titulo");
		String json = objectMapper.writeValueAsString(c1);
		mvc.perform(put(href)
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
				.andExpect(status().isNoContent());

		verify(proposalsService, times(1)).addComment(p1.getId(), c1);
		verify(proposalsService, times(2)).getProposal(p1.getId());
		verify(proposalsService, times(2)).getComment(p1.getId(), c1.getId());

		performGetComment(c1);

		verify(proposalsService, times(3)).getProposal(p1.getId());
		verify(proposalsService, times(3)).getComment(p1.getId(), c1.getId());
	}

	@Test
	public void testDeleteComment() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> proposalResource = proposalAssembler.toResource(p1);
		Comment c1 = p1.getComments().get().iterator().next();
		String href = commentAssembler.toResource(c1).getLink("self").getHref();

		given(proposalsService.getProposal(p1.getId())).willReturn(Optional.of(p1));

		when(proposalsService.getComment(p1.getId(), c1.getId()))
				.thenReturn(Optional.of(c1))
				.thenReturn(Optional.of(c1))
				.thenReturn(Optional.ofNullable(null));

		performGetComment(c1);

		verify(proposalsService, times(1)).getProposal(p1.getId());
		verify(proposalsService, times(1)).getComment(p1.getId(), c1.getId());

		mvc.perform(delete(href))
				.andExpect(status().isNotFound());

		verify(proposalsService, times(2)).getProposal(p1.getId());
		verify(proposalsService, times(2)).getComment(p1.getId(), c1.getId());
		verify(proposalsService, times (1)).deleteComment(p1.getId(), c1.getId());

		mvc.perform(get(href))
				.andExpect(status().isNotFound());

		verify(proposalsService, times(3)).getProposal(p1.getId());
		verify(proposalsService, times(3)).getComment(p1.getId(), c1.getId());
	}

	@Test
	public void testGetBidsUsers() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);
		Iterator<User> it = p1.getReviewBids().get().iterator();

		User bid1 =  it.next();
		Resource<User> bid1Resource = userAssembler.toResource(bid1);

		User bid2 = it.next();
		Resource<User> bid2Resource = userAssembler.toResource(bid2);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));

		String href = p1Resource.getLink("bids").getHref();
		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.bids", hasSize(2)))
				.andExpect(jsonPath("$._embedded.bids[0].id", is((int)bid1.getId())))
				.andExpect(jsonPath("$._embedded.bids[0].firstName", is(bid1.getFirstName())))
				.andExpect(jsonPath("$._embedded.bids[0].lastName", is(bid1.getLastName())))
				.andExpect(jsonPath("$._embedded.bids[0].username", is(bid1.getUsername())))
				.andExpect(jsonPath("$._embedded.bids[0].email", is(bid1.getEmail())))
				.andExpect(jsonPath("$._embedded.bids[0]._links.self.href", is(ROOT + bid1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.bids[0]._links.bids.href", is(ROOT + bid1Resource.getLink("bids").getHref())))

				.andExpect(jsonPath("$._embedded.bids[1].id", is((int)bid2.getId())))
				.andExpect(jsonPath("$._embedded.bids[1].firstName", is(bid2.getFirstName())))
				.andExpect(jsonPath("$._embedded.bids[1].lastName", is(bid2.getLastName())))
				.andExpect(jsonPath("$._embedded.bids[1].username", is(bid2.getUsername())))
				.andExpect(jsonPath("$._embedded.bids[1].email", is(bid2.getEmail())))
				.andExpect(jsonPath("$._embedded.bids[1]._links.self.href", is(ROOT + bid2Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.bids[1]._links.bids.href", is(ROOT + bid2Resource.getLink("bids").getHref())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + href)))
				.andExpect(jsonPath("$._links.root.href", is(ROOT + "/")));

		verify(proposalsService, times(1)).getProposal(p1.getId());
	}

	@Test
	public void testAddUserForBid() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));

		String href = p1Resource.getLink("bids").getHref();
		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.bids", hasSize(2)));

		verify(proposalsService, times(1)).getProposal(p1.getId());

		User bid1 = create_User_1();
		Resource<User> bid1Resource = userAssembler.toResource(bid1);

		given(proposalsService.getProposal(p1.getId()))
				.willReturn(Optional.of(p1));
		when(proposalsService.addProposal(p1)).thenReturn(p1);
		when(usersService.addUser(bid1)).thenReturn(bid1);

		String json = objectMapper.writeValueAsString(bid1);
		mvc.perform(post(href)
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)bid1.getId())))
				.andExpect(jsonPath("$.firstName", is(bid1.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(bid1.getLastName())))
				.andExpect(jsonPath("$.username", is(bid1.getUsername())))
				.andExpect(jsonPath("$.email", is(bid1.getEmail())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + bid1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.bids.href", is(ROOT + href)));

		verify(proposalsService, times(2)).getProposal(p1.getId());
		verify(proposalsService, times(1)).addProposal(p1);
		verify(usersService, times(1)).addUser(bid1);

		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.bids", hasSize(3)));

		verify(proposalsService, times(3)).getProposal(p1.getId());
	}

	@Test
	public void testDeleteUserForBid() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> proposalResource = proposalAssembler.toResource(p1);
		User bid1 = p1.getReviewBids().get().iterator().next();
		String href = userAssembler.toResource(bid1).getLink("self").getHref();

		given(proposalsService.getReviewBid(p1.getId(), bid1.getId())).willReturn(Optional.of(bid1));

		/*when(proposalsService.getReviewBids(bid1.getId(), ""))
				.thenReturn(Collections.singleton(bid1))
				.thenReturn(Collections.emptyList());*/

		performGetBidUser(bid1, p1);

		verify(proposalsService, times(1)).getReviewBid(p1.getId(), bid1.getId());

		mvc.perform(delete(href))
				.andExpect(status().isNotFound());

		verify(proposalsService, times(2)).getProposal(p1.getId());
		verify(usersService, times(2)).getUser(bid1.getId());
		verify(usersService, times (1)).deleteUser(bid1.getId());

		mvc.perform(get(href))
				.andExpect(status().isNotFound());

		verify(proposalsService, times(3)).getProposal(p1.getId());
		verify(usersService, times(3)).getUser(bid1.getId());
	}

	@Test
	public void testNotFoundProposal() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		given(proposalsService.getProposal(p1.getId())).willReturn(Optional.of(p1));

		mvc.perform(get("http://localhost/proposals/100"))
				.andExpect(status().isNotFound());

		verify(proposalsService, times(1)).getProposal(100);
	}






	private MvcResult performGetProposal(Proposal proposal) throws Exception {
		Resource<Proposal> resourceProposal = proposalAssembler.toResource(proposal);
		return mvc.perform(get(resourceProposal.getLink("self").getHref()))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)proposal.getId())))
				.andExpect(jsonPath("$.title", is(proposal.getTitle())))
				.andExpect(jsonPath("$.description", is(proposal.getDescription())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + resourceProposal.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.proposals.href", is(ROOT + resourceProposal.getLink("proposals").getHref())))
				.andReturn();
	}

	private MvcResult performGetSection(Section section) throws Exception {
		Resource<Section> resourceSection = sectionAssembler.toResource(section);
		return mvc.perform(get(resourceSection.getLink("self").getHref()))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)section.getId())))
				.andExpect(jsonPath("$.title", is(section.getTitle())))
				.andExpect(jsonPath("$.material", is(section.getMaterial())))
				.andExpect(jsonPath("$.workPlan", is(section.getWorkPlan())))
				.andExpect(jsonPath("$.budget", is(section.getBudget())))
				.andExpect(jsonPath("$.description", is(section.getDescription())))
				.andExpect(jsonPath("$.goals", is(section.getGoals())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + resourceSection.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.sections.href", is(ROOT + resourceSection.getLink("sections").getHref())))
				.andReturn();
	}

	private MvcResult performGetReview(Review review) throws Exception {
		Resource<Review> resourceReview = reviewAssembler.toResource(review);
		return mvc.perform(get(resourceReview.getLink("self").getHref()))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)review.getId())))
				.andExpect(jsonPath("$.title", is(review.getTitle())))
				.andExpect(jsonPath("$.text", is(review.getText())))
				.andExpect(jsonPath("$.summary", is(review.getSummary())))
				.andExpect(jsonPath("$.classification", is(review.getClassification())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + resourceReview.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.reviews.href", is(ROOT + resourceReview.getLink("reviews").getHref())))
				.andReturn();
	}

	private MvcResult performGetComment(Comment comment) throws Exception {
		Resource<Comment> resourceComment = commentAssembler.toResource(comment);
		return mvc.perform(get(resourceComment.getLink("self").getHref()))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)comment.getId())))
				.andExpect(jsonPath("$.title", is(comment.getTitle())))
				.andExpect(jsonPath("$.text", is(comment.getText())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + resourceComment.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.comments.href", is(ROOT + resourceComment.getLink("comments").getHref())))
				.andReturn();
	}

	private MvcResult performGetBidUser(User bidUser, Proposal proposal) throws Exception {
		Resource<User> resourceBid = staffAssembler.toResource(bidUser, proposal);
		return mvc.perform(get(resourceBid.getLink("self").getHref()))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)bidUser.getId())))
				.andExpect(jsonPath("$.firstName", is(bidUser.getFirstName())))
				.andExpect(jsonPath("$.lastName", is(bidUser.getLastName())))
				.andExpect(jsonPath("$.username", is(bidUser.getUsername())))
				.andExpect(jsonPath("$.email", is(bidUser.getEmail())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + resourceBid.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.comments.href", is(ROOT + resourceBid.getLink("bids").getHref())))
				.andReturn();
	}
}
