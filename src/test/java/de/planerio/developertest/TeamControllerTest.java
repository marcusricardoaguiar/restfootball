package de.planerio.developertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import de.planerio.developertest.constants.Constants;
import de.planerio.developertest.controllers.TeamController;
import de.planerio.developertest.exceptions.*;
import de.planerio.developertest.services.dtos.CountryDTO;
import de.planerio.developertest.services.dtos.LeagueDTO;
import de.planerio.developertest.services.dtos.TeamDTO;
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
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class TeamControllerTest {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

	@Autowired
	private TeamController teamController;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() {
		assertThat(teamController).isNotNull();
	}

	@Test
	public void shouldListAllTeamsTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItaly = new TeamDTO();
		teamItaly.setName("Juventus");
		teamItaly.setLeague(leagueItalyDTO);
		teamItaly = shouldCreateTeam(teamItaly, this.mockMvc);

		CountryDTO countryGermanyDTO = new CountryDTO();
		countryGermanyDTO.setName("Germany");
		countryGermanyDTO.setLanguage("de");
		countryGermanyDTO = CountryControllerTest.shouldCreateCountry(countryGermanyDTO, this.mockMvc);

		LeagueDTO leagueGermanyDTO = new LeagueDTO();
		leagueGermanyDTO.setName("Bundesleague");
		leagueGermanyDTO.setCountry(countryGermanyDTO);
		leagueGermanyDTO = LeagueControllerTest.shouldCreateLeague(leagueGermanyDTO, this.mockMvc);

		TeamDTO teamGermany = new TeamDTO();
		teamGermany.setName("Bayern Munchen");
		teamGermany.setLeague(leagueGermanyDTO);
		teamGermany = shouldCreateTeam(teamGermany, this.mockMvc);

		CountryDTO countryEnglandDTO = new CountryDTO();
		countryEnglandDTO.setName("England");
		countryEnglandDTO.setLanguage("en");
		countryEnglandDTO = CountryControllerTest.shouldCreateCountry(countryEnglandDTO, this.mockMvc);

		LeagueDTO leagueEnglandDTO = new LeagueDTO();
		leagueEnglandDTO.setName("Premier League");
		leagueEnglandDTO.setCountry(countryEnglandDTO);
		leagueEnglandDTO = LeagueControllerTest.shouldCreateLeague(leagueEnglandDTO, this.mockMvc);

		TeamDTO teamEngland = new TeamDTO();
		teamEngland.setName("Manchester United");
		teamEngland.setLeague(leagueEnglandDTO);
		teamEngland = shouldCreateTeam(teamEngland, this.mockMvc);

		MvcResult result = this.mockMvc.perform(get("/teams"))
				.andDo(print())
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		JSONObject jsonObj = new JSONObject(content);

		Assertions.assertEquals(Integer.parseInt(jsonObj.get("size").toString()), 3);


		result = this.mockMvc.perform(get("/teams?page=0&size=2"))
				.andDo(print())
				.andExpect(status().isOk()).andReturn();
		content = result.getResponse().getContentAsString();
		jsonObj = new JSONObject(content);

		Assertions.assertEquals(Integer.parseInt(jsonObj.get("size").toString()), 2);

		shouldDeleteTeam(teamItaly, this.mockMvc);
		shouldDeleteTeam(teamGermany, this.mockMvc);
		shouldDeleteTeam(teamEngland, this.mockMvc);

		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueGermanyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueEnglandDTO, this.mockMvc);

		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryGermanyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryEnglandDTO, this.mockMvc);
	}

	@Test
	public void shouldCreateTeamTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setName("Italy");
		countryDTO.setLanguage("it");
		countryDTO = CountryControllerTest.shouldCreateCountry(countryDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO team = new TeamDTO();
		team.setName("Juventus");
		team.setLeague(leagueItalyDTO);
		TeamDTO teamDTO = shouldCreateTeam(team, this.mockMvc);
		shouldDeleteTeam(teamDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldDeleteTeamTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setName("Germany");
		countryDTO.setLanguage("de");
		countryDTO = CountryControllerTest.shouldCreateCountry(countryDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO team = new TeamDTO();
		team.setName("Juventus");
		team.setLeague(leagueItalyDTO);
		TeamDTO teamDTO = shouldCreateTeam(team, this.mockMvc);
		shouldDeleteTeam(teamDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldGetTeamByIdTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setName("Italy");
		countryDTO.setLanguage("it");
		countryDTO = CountryControllerTest.shouldCreateCountry(countryDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO team = new TeamDTO();
		team.setName("Juventus");
		team.setLeague(leagueItalyDTO);
		TeamDTO teamDTO = shouldCreateTeam(team, this.mockMvc);
		this.mockMvc.perform(get("/team/" + teamDTO.getId())).andDo(print()).andExpect(status().isOk());
		shouldDeleteTeam(teamDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldPatchTeamTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setName("Italy");
		countryDTO.setLanguage("it");
		countryDTO = CountryControllerTest.shouldCreateCountry(countryDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO team = new TeamDTO();
		team.setName("Juventus");
		team.setLeague(leagueItalyDTO);
		TeamDTO teamDTO = shouldCreateTeam(team, this.mockMvc);
		teamDTO.setName("Milan");
		teamDTO.setLeague(leagueItalyDTO);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(teamDTO);

		this.mockMvc.perform(patch("/team/" + teamDTO.getId())
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk());
		shouldDeleteTeam(teamDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreateTeamNameAlreadyExistTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setName("England");
		countryDTO.setLanguage("en");
		countryDTO = CountryControllerTest.shouldCreateCountry(countryDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Premier League");
		leagueItalyDTO.setCountry(countryDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO team = new TeamDTO();
		team.setName("Chelsea");
		team.setLeague(leagueItalyDTO);
		TeamDTO teamDTO = shouldCreateTeam(team, this.mockMvc);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(teamDTO);

		MvcResult result = this.mockMvc.perform(post("/team")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new NameAlreadyExistException().getMessage());
		shouldDeleteTeam(teamDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreateTeamNonNameProvidedTest() throws Exception {
		CountryDTO countryDTO = new CountryDTO();
		countryDTO.setName("England");
		countryDTO.setLanguage("en");
		countryDTO = CountryControllerTest.shouldCreateCountry(countryDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Premier League");
		leagueItalyDTO.setCountry(countryDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO team = new TeamDTO();
		team.setLeague(leagueItalyDTO);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(team);

		MvcResult result = this.mockMvc.perform(post("/team")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new NameEmptyException().getMessage());
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreateTeamNonLeagueProvidedTest() throws Exception {
		TeamDTO team = new TeamDTO();
		team.setName("Chelsea");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(team);

		MvcResult result = this.mockMvc.perform(post("/team")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new SchemaInvalidException().getMessage());
	}

	@Test
	public void shouldNotCreateTeamLeagueNotFoundTest() throws Exception {
		LeagueDTO leagueDTO = new LeagueDTO();
		leagueDTO.setId(100);

		TeamDTO team = new TeamDTO();
		team.setName("Premier League");
		team.setLeague(leagueDTO);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(team);

		MvcResult result = this.mockMvc.perform(post("/team")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new NotFoundException().getMessage());
	}

	@Test
	public void shouldNotCreateMoreThan20TeamsOnSameLeagueTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		List<TeamDTO> teams = new ArrayList<>();
		for (int i = 0; i < Constants.TEAMS_PER_LEAGUE; i++){
			TeamDTO team = new TeamDTO();
			team.setName("Team" + i);
			team.setLeague(leagueItalyDTO);
			teams.add(shouldCreateTeam(team, this.mockMvc));
		}

		TeamDTO teamX = new TeamDTO();
		teamX.setName("TeamX");
		teamX.setLeague(leagueItalyDTO);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(teamX);

		MvcResult result = this.mockMvc.perform(post("/team")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new TeamByLeagueException().getMessage());

		for (TeamDTO teamDTO : teams) shouldDeleteTeam(teamDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
	}

	/*
	 * Auxiliary methods for tests
	 */

	public static TeamDTO shouldCreateTeam(TeamDTO teamDTO, MockMvc mockMvc) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(teamDTO);

		MvcResult result = mockMvc.perform(post("/team")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		Gson g = new Gson();
		return g.fromJson(content, TeamDTO.class);
	}

	public static void shouldDeleteTeam(TeamDTO createdTeamDTO, MockMvc mockMvc) throws Exception {
		mockMvc.perform(delete("/team/" + createdTeamDTO.getId())).andDo(print()).andExpect(status().isOk());
	}
}
