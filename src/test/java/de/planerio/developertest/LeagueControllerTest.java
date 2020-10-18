package de.planerio.developertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import de.planerio.developertest.controllers.LeagueController;
import de.planerio.developertest.exceptions.*;
import de.planerio.developertest.services.dtos.CountryDTO;
import de.planerio.developertest.services.dtos.LeagueDTO;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class LeagueControllerTest {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

	@Autowired
	private LeagueController leagueController;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() {
		assertThat(leagueController).isNotNull();
	}

	@Test
	public void shouldListAllLeaguesTest() throws Exception {
		CountryDTO countryFranceDTO = new CountryDTO();
		countryFranceDTO.setName("Italy");
		countryFranceDTO.setLanguage("it");
		countryFranceDTO = CountryControllerTest.shouldCreateCountry(countryFranceDTO, this.mockMvc);

		LeagueDTO leagueFrance = new LeagueDTO();
		leagueFrance.setName("Serie A");
		leagueFrance.setCountry(countryFranceDTO);
		leagueFrance = shouldCreateLeague(leagueFrance, this.mockMvc);

		CountryDTO countryGermanyDTO = new CountryDTO();
		countryGermanyDTO.setName("Germany");
		countryGermanyDTO.setLanguage("de");
		countryGermanyDTO = CountryControllerTest.shouldCreateCountry(countryGermanyDTO, this.mockMvc);

		LeagueDTO leagueGermany = new LeagueDTO();
		leagueGermany.setName("Bundesleague");
		leagueGermany.setCountry(countryGermanyDTO);
		leagueGermany = shouldCreateLeague(leagueGermany, this.mockMvc);

		CountryDTO countryEnglandDTO = new CountryDTO();
		countryEnglandDTO.setName("England");
		countryEnglandDTO.setLanguage("en");
		countryEnglandDTO = CountryControllerTest.shouldCreateCountry(countryEnglandDTO, this.mockMvc);

		LeagueDTO leagueEngland = new LeagueDTO();
		leagueEngland.setName("Premier League");
		leagueEngland.setCountry(countryEnglandDTO);
		leagueEngland = shouldCreateLeague(leagueEngland, this.mockMvc);

		// List all leagues

		MvcResult result = this.mockMvc.perform(get("/leagues"))
				.andDo(print())
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		JSONObject jsonObj = new JSONObject(content);

		Assertions.assertEquals(Integer.parseInt(jsonObj.get("size").toString()), 3);

		// List all leagues with pagination

		result = this.mockMvc.perform(get("/leagues?page=0&size=2"))
				.andDo(print())
				.andExpect(status().isOk()).andReturn();
		content = result.getResponse().getContentAsString();
		jsonObj = new JSONObject(content);

		Assertions.assertEquals(Integer.parseInt(jsonObj.get("size").toString()), 2);

		// List all leagues filtering by language

		result = this.mockMvc.perform(get("/leagues?language=it"))
				.andDo(print())
				.andExpect(status().isOk()).andReturn();
		content = result.getResponse().getContentAsString();
		jsonObj = new JSONObject(content);

		Assertions.assertEquals(Integer.parseInt(jsonObj.get("size").toString()), 1);

		shouldDeleteLeague(leagueFrance, this.mockMvc);
		shouldDeleteLeague(leagueGermany, this.mockMvc);
		shouldDeleteLeague(leagueEngland, this.mockMvc);

		CountryControllerTest.shouldDeleteCountry(countryFranceDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryGermanyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryEnglandDTO, this.mockMvc);
	}

	@Test
	public void shouldCreateLeagueTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setName("Italy");
		countryDTO.setLanguage("it");
		countryDTO = CountryControllerTest.shouldCreateCountry(countryDTO, this.mockMvc);

		LeagueDTO league = new LeagueDTO();
		league.setName("France");
		league.setCountry(countryDTO);
		LeagueDTO leagueDTO = shouldCreateLeague(league, this.mockMvc);
		shouldDeleteLeague(leagueDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldDeleteLeagueTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setName("Germany");
		countryDTO.setLanguage("de");
		countryDTO = CountryControllerTest.shouldCreateCountry(countryDTO, this.mockMvc);

		LeagueDTO league = new LeagueDTO();
		league.setName("Bundesleague");
		league.setCountry(countryDTO);
		LeagueDTO leagueDTO = shouldCreateLeague(league, this.mockMvc);
		shouldDeleteLeague(leagueDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldGetLeagueByIdTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setName("Italy");
		countryDTO.setLanguage("it");
		countryDTO = CountryControllerTest.shouldCreateCountry(countryDTO, this.mockMvc);

		LeagueDTO league = new LeagueDTO();
		league.setName("Serie A");
		league.setCountry(countryDTO);
		LeagueDTO leagueDTO = shouldCreateLeague(league, this.mockMvc);
		this.mockMvc.perform(get("/league/" + leagueDTO.getId())).andDo(print()).andExpect(status().isOk());
		shouldDeleteLeague(leagueDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldPatchLeagueTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setName("Italy");
		countryDTO.setLanguage("it");
		countryDTO = CountryControllerTest.shouldCreateCountry(countryDTO, this.mockMvc);

		LeagueDTO league = new LeagueDTO();
		league.setName("Serie A");
		league.setCountry(countryDTO);
		LeagueDTO leagueDTO = shouldCreateLeague(league, this.mockMvc);
		leagueDTO.setName("Serie A2");
		leagueDTO.setCountry(countryDTO);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(leagueDTO);

		this.mockMvc.perform(patch("/league/" + leagueDTO.getId())
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk());
		shouldDeleteLeague(leagueDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreateLeagueNameAlreadyExistTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setName("England");
		countryDTO.setLanguage("en");
		countryDTO = CountryControllerTest.shouldCreateCountry(countryDTO, this.mockMvc);

		LeagueDTO league = new LeagueDTO();
		league.setName("Premier League");
		league.setCountry(countryDTO);
		league = shouldCreateLeague(league, this.mockMvc);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(league);

		MvcResult result = this.mockMvc.perform(post("/league")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new NameAlreadyExistException().getMessage());
		shouldDeleteLeague(league, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreateLeagueNonNameProvidedTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setName("England");
		countryDTO.setLanguage("en");
		countryDTO = CountryControllerTest.shouldCreateCountry(countryDTO, this.mockMvc);

		LeagueDTO league = new LeagueDTO();
		league.setCountry(countryDTO);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(league);

		MvcResult result = this.mockMvc.perform(post("/league")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new NameEmptyException().getMessage());
		CountryControllerTest.shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreateLeagueNonCountryProvidedTest() throws Exception {
		LeagueDTO league = new LeagueDTO();
		league.setName("Premier League");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(league);

		MvcResult result = this.mockMvc.perform(post("/league")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new SchemaInvalidException().getMessage());
	}

	@Test
	public void shouldNotCreateLeagueMoreThanOnePerCountryTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setName("England");
		countryDTO.setLanguage("en");
		countryDTO = CountryControllerTest.shouldCreateCountry(countryDTO, this.mockMvc);

		LeagueDTO leagueOneDTO = new LeagueDTO();
		leagueOneDTO.setName("Premier League");
		leagueOneDTO.setCountry(countryDTO);
		leagueOneDTO = shouldCreateLeague(leagueOneDTO, this.mockMvc);

		LeagueDTO leagueTwoDTO = new LeagueDTO();
		leagueTwoDTO.setName("Premier League 2");
		leagueTwoDTO.setCountry(countryDTO);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(leagueTwoDTO);

		MvcResult result = this.mockMvc.perform(post("/league")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new LeaguesByCountryException().getMessage());
		shouldDeleteLeague(leagueOneDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreateLeagueCountryNotFoundTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setId(100);

		LeagueDTO league = new LeagueDTO();
		league.setName("Premier League");
		league.setCountry(countryDTO);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(league);

		MvcResult result = this.mockMvc.perform(post("/league")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new NotFoundException().getMessage());
	}

	/*
	 * Auxiliary methods for tests
	 */

	public static LeagueDTO shouldCreateLeague(LeagueDTO leagueDTO, MockMvc mockMvc) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(leagueDTO);

		MvcResult result = mockMvc.perform(post("/league")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		Gson g = new Gson();
		return g.fromJson(content, LeagueDTO.class);
	}

	public static void shouldDeleteLeague(LeagueDTO createdLeagueDTO, MockMvc mockMvc) throws Exception {
		mockMvc.perform(delete("/league/" + createdLeagueDTO.getId())).andDo(print()).andExpect(status().isOk());
	}
}
