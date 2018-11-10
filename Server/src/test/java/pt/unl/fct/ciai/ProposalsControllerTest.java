package pt.unl.fct.ciai;

import com.fasterxml.jackson.core.JsonProcessingException;
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
import pt.unl.fct.ciai.model.*;
import pt.unl.fct.ciai.repository.*;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.Optional;

@RunWith(SpringRunner.class)
@WebMvcTest(controllers = ProposalsController.class, secure = false)
@Import({ProposalResourceAssembler.class, SectionResourceAssembler.class,
	ReviewResourceAssembler.class, CommentResourceAssembler.class,
	UserResourceAssembler.class})
public class ProposalsControllerTest {

	@Autowired
	private MockMvc mvc;
	@MockBean
	private ProposalsRepository proposalsRepository;
	@MockBean
	private SectionsRepository sectionsRepository;
	@MockBean
	private ReviewsRepository reviewsRepository;
	@MockBean
	private CommentsRepository commentsRepository;
	@MockBean
	private UsersRepository usersRepository;
	@Autowired
	private ProposalResourceAssembler proposalAssembler;
	@Autowired
	private SectionResourceAssembler sectionAssembler;
	@Autowired
	private ReviewResourceAssembler reviewAssembler;
	@Autowired
	private CommentResourceAssembler commentAssembler;
	@Autowired
	private UserResourceAssembler userAssembler;
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

		Review r = create_Review_1();
		r.setProposal(p1);
		Section s=create_Section_1();
		s.setProposal(p1);
		Comment c=create_Comment_1();
		c.setProposal(p1);

		p1.addReview(r);
		p1.addSection(s);
		p1.addComment(c);

		return p1;
	}
	private Proposal createProposal_2() {
		Proposal p2 = new Proposal();
		p2.setId(2L);
		p2.setTitle("Proposal 2");
		p2.setDescription("Proposal 2 - Description");
		p2.setCreationDate(new Date());

		Review r = create_Review_2();
		r.setProposal(p2);
		Section s= create_Section_2();
		s.setProposal(p2);
		Comment c=create_Comment_2();
		c.setProposal(p2);

		p2.addReview(r);
		p2.addSection(s);
		p2.addComment(c);

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

	@Test
	public void testGetProposals() throws Exception {
		Proposal p1 =  createProposal_1();
		Proposal p2 = createProposal_2();

		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);
		Resource<Proposal> p2Resource = proposalAssembler.toResource(p2);

		given(proposalsRepository.findAll()).willReturn(Arrays.asList(p1, p2));

		String href = p1Resource.getLink("proposals").getHref();
		mvc.perform(get(href))
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.proposals", hasSize(2)))
				.andExpect(jsonPath("$._embedded.proposals[0].id", is((int)p1.getId())))
				.andExpect(jsonPath("$._embedded.proposals[0].title", is(p1.getTitle())))
				.andExpect(jsonPath("$._embedded.proposals[0].description", is(p1.getDescription())))
				.andExpect(jsonPath("$._embedded.proposals[0].date", is(p1.getCreationDate())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.self.href", is(ROOT + p1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.proposals.href", is(ROOT + p1Resource.getLink("proposals").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.reviews.href", is(ROOT + p1Resource.getLink("reviews").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.sections.href", is(ROOT + p1Resource.getLink("sections").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[0]._links.comments.href", is(ROOT + p1Resource.getLink("comments").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1].id", is((int)p2.getId())))
				.andExpect(jsonPath("$._embedded.proposals[1].title", is(p2.getTitle())))
				.andExpect(jsonPath("$._embedded.proposals[1].description", is(p2.getDescription())))
				.andExpect(jsonPath("$._embedded.proposals[1].date", is(p2.getCreationDate())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.self.href", is(ROOT + p2Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.proposals.href", is(ROOT + p2Resource.getLink("proposals").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.reviews.href", is(ROOT + p2Resource.getLink("reviews").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.sections.href", is(ROOT + p2Resource.getLink("sections").getHref())))
				.andExpect(jsonPath("$._embedded.proposals[1]._links.comments.href", is(ROOT + p2Resource.getLink("comments").getHref())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + href)))
				.andExpect(jsonPath("$._links.root.href", is(ROOT + "/")));

		verify(proposalsRepository, times(1)).findAll();

	}

	@Test
	public void testAddProposal() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		given(proposalsRepository.save(p1)).willReturn(p1);
		given(proposalsRepository.findById(p1.getId())).willReturn(Optional.of(p1));

		String json = objectMapper.writeValueAsString(p1);
		mvc.perform(post(p1Resource.getLink("proposals").getHref())
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
				.andExpect(status().isCreated())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)p1.getId())))
				.andExpect(jsonPath("$.name", is(p1.getTitle())))
				.andExpect(jsonPath("$.description", is(p1.getDescription())))
				.andExpect(jsonPath("$.date", is(p1.getCreationDate())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + p1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.proposals.href", is(ROOT + p1Resource.getLink("proposals").getHref())))
				.andExpect(jsonPath("$._links.reviews.href", is(ROOT + p1Resource.getLink("reviews").getHref())))
				.andExpect(jsonPath("$._links.sections.href", is(ROOT + p1Resource.getLink("sections").getHref())))
				.andExpect(jsonPath("$._links.comments.href", is(ROOT + p1Resource.getLink("comments").getHref())));

		verify(proposalsRepository, times(1)).save(p1);

		performGetProposal(p1);

		verify(proposalsRepository, times(1)).findById(p1.getId());
	}

	@Test
	public void testGetProposal() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		given(proposalsRepository.findById(p1.getId())).willReturn(Optional.of(p1));

		performGetProposal(p1);

		verify(proposalsRepository, times(1)).findById(p1.getId());
	}

	@Test
	public void testUpdateProposal() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		given(proposalsRepository.findById(p1.getId())).willReturn(Optional.of(p1));

		performGetProposal(p1);

		verify(proposalsRepository, times(1)).findById(p1.getId());

		given(proposalsRepository.save(p1)).willReturn(p1);
		given(proposalsRepository.findById(p1.getId())).willReturn(Optional.of(p1));

		p1.setTitle("New title");
		String json = objectMapper.writeValueAsString(p1);
		mvc.perform(put(p1Resource.getLink("self").getHref())
				.accept(MediaTypes.HAL_JSON_UTF8_VALUE)
				.contentType(MediaTypes.HAL_JSON_UTF8_VALUE)
				.content(json))
				.andExpect(status().isNoContent());

		verify(proposalsRepository, times(1)).save(p1);
		verify(proposalsRepository, times(2)).findById(p1.getId());

		performGetProposal(p1);

		verify(proposalsRepository, times(3)).findById(p1.getId());
	}

	@Test
	public void testDeleteProposal() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		when(proposalsRepository.findById(p1.getId()))
				.thenReturn(Optional.of(p1))
				.thenReturn(Optional.ofNullable(null));
		String href = p1Resource.getLink("self").getHref();

		performGetProposal(p1);

		verify(proposalsRepository, times(1)).findById(p1.getId());

		mvc.perform(delete(href))
				.andExpect(status().isNoContent());

		verify(proposalsRepository, times(2)).findById(p1.getId());
		verify(proposalsRepository, times(1)).delete(p1);

		mvc.perform(get(href))
				.andExpect(status().isNotFound());

		verify(proposalsRepository, times(3)).findById(p1.getId());
	}


	@Test
	public void testGetSections() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);
		Iterator<Section> it = p1.getSections().get().iterator();

		Section s1 =  it.next();
		Resource<Section> s1Resource = sectionAssembler.toResource(s1);

		Section s2 = it.next();
		Resource<Section> s2Resource = sectionAssembler.toResource(s2);

		given(proposalsRepository.findById(p1.getId()))
				.willReturn(Optional.of(p1));

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
				.andExpect(jsonPath("$._embedded.sections[0].goals", is(s1.getGoals())))
				.andExpect(jsonPath("$._embedded.sections[0].workPlan", is(s1.getWorkPlan())))
				.andExpect(jsonPath("$._embedded.sections[0].budget", is(s1.getBudget())))
				.andExpect(jsonPath("$._embedded.sections[0]._links.self.href", is(ROOT + s1Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.sections[0]._links.sections.href", is(ROOT + s1Resource.getLink("sections").getHref())))

				.andExpect(jsonPath("$._embedded.sections[1].id", is((int)s2.getId())))
				.andExpect(jsonPath("$._embedded.sections[1].title", is(s2.getTitle())))
				.andExpect(jsonPath("$._embedded.sections[1].description", is(s2.getDescription())))
				.andExpect(jsonPath("$._embedded.sections[1].material", is(s2.getMaterial())))
				.andExpect(jsonPath("$._embedded.sections[1].goals", is(s2.getGoals())))
				.andExpect(jsonPath("$._embedded.sections[1].workPlan", is(s2.getWorkPlan())))
				.andExpect(jsonPath("$._embedded.sections[1].budget", is(s2.getBudget())))
				.andExpect(jsonPath("$._embedded.sections[1]._links.self.href", is(ROOT + s2Resource.getLink("self").getHref())))
				.andExpect(jsonPath("$._embedded.sections[1]._links.sections.href", is(ROOT + s2Resource.getLink("sections").getHref())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + href)))
				.andExpect(jsonPath("$._links.root.href", is(ROOT + "/")));

		verify(proposalsRepository, times(1)).findById(p1.getId());
	}

	@Test
	public void testAddSection() throws Exception {
		Proposal p1 = createProposal_1();
		Resource<Proposal> p1Resource = proposalAssembler.toResource(p1);

		given(proposalsRepository.findById(p1.getId()))
				.willReturn(Optional.of(p1));

		String href = p1Resource.getLink("sections").getHref();
		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.sections", hasSize(2)));

		verify(proposalsRepository, times(1)).findById(p1.getId());

		Section s1 = create_Section_1();
		Resource<Section> s1Resource = sectionAssembler.toResource(s1);

		given(proposalsRepository.findById(p1.getId()))
				.willReturn(Optional.of(p1));
		when(proposalsRepository.save(p1)).thenReturn(p1);
		when(sectionsRepository.save(s1)).thenReturn(s1);

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

		verify(proposalsRepository, times(2)).findById(p1.getId());
		verify(proposalsRepository, times(1)).save(p1);
		verify(sectionsRepository, times(1)).save(s1);

		mvc.perform(get(href))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$._embedded.sections", hasSize(3)));

		verify(proposalsRepository, times(3)).findById(p1.getId());
	}


	@Test
	public void testGetSection() {
		//TODO
	}

	@Test
	public void testUpdateSection() {
		//TODO
	}

	@Test
	public void testDeleteSection() {
		//TODO
	}

	@Test
	public void testGetReviews() {
		//TODO
	}

	@Test
	public void testAddReview() {
		//TODO
	}

	@Test
	public void testGetReview() {
		//TODO
	}

	@Test
	public void testUpdateReview() {
		//TODO
	}

	@Test
	public void testDeleteReview() {
		//TODO
	}

	@Test
	public void testGetComments() {
		//TODO
	}

	@Test
	public void testAddComment() {
		//TODO
	}

	@Test
	public void testGetComment() {
		//TODO
	}

	@Test
	public void testUpdateComment() {
		//TODO
	}

	@Test
	public void testDeleteComment() {
		//TODO
	}

	@Test
	public void testGetBiddingUsers() {
		//TODO
	}

	@Test
	public void testAddUserForBidding() {
		//TODO
	}

	@Test
	public void testDeleteUserForBidding() {
		//TODO
	}

	private MvcResult performGetProposal(Proposal proposal) throws Exception {
		Resource<Proposal> resourceProposal = proposalAssembler.toResource(proposal);
		return mvc.perform(get(resourceProposal.getLink("self").getHref()))
				.andExpect(status().isOk())
				.andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(content().contentType(MediaTypes.HAL_JSON_UTF8_VALUE))
				.andExpect(jsonPath("$.id", is((int)proposal.getId())))
				.andExpect(jsonPath("$.name", is(proposal.getTitle())))
				.andExpect(jsonPath("$.description", is(proposal.getDescription())))
				.andExpect(jsonPath("$.date", is(proposal.getCreationDate())))
				.andExpect(jsonPath("$._links.self.href", is(ROOT + resourceProposal.getLink("self").getHref())))
				.andExpect(jsonPath("$._links.proposals.href", is(ROOT + resourceProposal.getLink("proposals").getHref())))
				.andExpect(jsonPath("$._links.reviews.href", is(ROOT + resourceProposal.getLink("reviews").getHref())))
				.andExpect(jsonPath("$._links.sections.href", is(ROOT + resourceProposal.getLink("sections").getHref())))
				.andExpect(jsonPath("$._links.comments.href", is(ROOT + resourceProposal.getLink("comments").getHref())))
				.andReturn();
	}

}
