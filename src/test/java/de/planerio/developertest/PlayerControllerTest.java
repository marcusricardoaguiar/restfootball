package de.planerio.developertest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import de.planerio.developertest.constants.Constants;
import de.planerio.developertest.controllers.PlayerController;
import de.planerio.developertest.exceptions.*;
import de.planerio.developertest.services.dtos.CountryDTO;
import de.planerio.developertest.services.dtos.LeagueDTO;
import de.planerio.developertest.services.dtos.PlayerDTO;
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
class PlayerControllerTest {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

	@Autowired
	private PlayerController playerController;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() {
		assertThat(playerController).isNotNull();
	}

	@Test
	public void shouldListAllPlayersTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItalyDTO = new TeamDTO();
		teamItalyDTO.setName("Juventus");
		teamItalyDTO.setLeague(leagueItalyDTO);
		teamItalyDTO = TeamControllerTest.shouldCreateTeam(teamItalyDTO, this.mockMvc);

		PlayerDTO playerItaly = new PlayerDTO();
		playerItaly.setName("Cristiano", "Ronaldo");
		playerItaly.setTeam(teamItalyDTO);
		playerItaly.setShirtNumber(9);
		playerItaly.setPosition("ST");
		playerItaly = shouldCreatePlayer(playerItaly, this.mockMvc);

		CountryDTO countryGermanyDTO = new CountryDTO();
		countryGermanyDTO.setName("Germany");
		countryGermanyDTO.setLanguage("de");
		countryGermanyDTO = CountryControllerTest.shouldCreateCountry(countryGermanyDTO, this.mockMvc);

		LeagueDTO leagueGermanyDTO = new LeagueDTO();
		leagueGermanyDTO.setName("Bundesleague");
		leagueGermanyDTO.setCountry(countryGermanyDTO);
		leagueGermanyDTO = LeagueControllerTest.shouldCreateLeague(leagueGermanyDTO, this.mockMvc);

		TeamDTO teamGermanyDTO = new TeamDTO();
		teamGermanyDTO.setName("Bayern Munchen");
		teamGermanyDTO.setLeague(leagueGermanyDTO);
		teamGermanyDTO = TeamControllerTest.shouldCreateTeam(teamGermanyDTO, this.mockMvc);

		PlayerDTO playerGermany = new PlayerDTO();
		playerGermany.setName("Emanuel", "Neuer");
		playerGermany.setTeam(teamGermanyDTO);
		playerGermany.setPosition("GK");
		playerGermany.setShirtNumber(11);
		playerGermany = shouldCreatePlayer(playerGermany, this.mockMvc);

		CountryDTO countryEnglandDTO = new CountryDTO();
		countryEnglandDTO.setName("England");
		countryEnglandDTO.setLanguage("en");
		countryEnglandDTO = CountryControllerTest.shouldCreateCountry(countryEnglandDTO, this.mockMvc);

		LeagueDTO leagueEnglandDTO = new LeagueDTO();
		leagueEnglandDTO.setName("Premier League");
		leagueEnglandDTO.setCountry(countryEnglandDTO);
		leagueEnglandDTO = LeagueControllerTest.shouldCreateLeague(leagueEnglandDTO, this.mockMvc);

		TeamDTO teamEnglandDTO = new TeamDTO();
		teamEnglandDTO.setName("Macherter City");
		teamEnglandDTO.setLeague(leagueEnglandDTO);
		teamEnglandDTO = TeamControllerTest.shouldCreateTeam(teamEnglandDTO, this.mockMvc);

		PlayerDTO playerEngland = new PlayerDTO();
		playerEngland.setName("Gabriel", "Jesus");
		playerEngland.setTeam(teamEnglandDTO);
		playerEngland.setPosition("CF");
		playerEngland.setShirtNumber(9);
		playerEngland = shouldCreatePlayer(playerEngland, this.mockMvc);

		// List all players

		MvcResult result = this.mockMvc.perform(get("/players"))
				.andDo(print())
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		JSONObject jsonObj = new JSONObject(content);

		Assertions.assertEquals(Integer.parseInt(jsonObj.get("size").toString()), 3);

		// List all players with pagination

		result = this.mockMvc.perform(get("/players?page=0&size=2"))
				.andDo(print())
				.andExpect(status().isOk()).andReturn();
		content = result.getResponse().getContentAsString();
		jsonObj = new JSONObject(content);

		Assertions.assertEquals(Integer.parseInt(jsonObj.get("size").toString()), 2);

		// List all players filtering by position

		result = this.mockMvc.perform(get("/players?position=ST"))
				.andDo(print())
				.andExpect(status().isOk()).andReturn();
		content = result.getResponse().getContentAsString();
		jsonObj = new JSONObject(content);

		Assertions.assertEquals(Integer.parseInt(jsonObj.get("size").toString()), 1);

		// List all players filtering by defense position ASC

		result = this.mockMvc.perform(get("/players?onlyDefense=ASC"))
				.andDo(print())
				.andExpect(status().isOk()).andReturn();
		content = result.getResponse().getContentAsString();
		jsonObj = new JSONObject(content);

		Assertions.assertEquals(Integer.parseInt(jsonObj.get("size").toString()), 1);

		// List all players filtering by defense position DESC

		result = this.mockMvc.perform(get("/players?onlyDefense=DESC"))
				.andDo(print())
				.andExpect(status().isOk()).andReturn();
		content = result.getResponse().getContentAsString();
		jsonObj = new JSONObject(content);

		Assertions.assertEquals(Integer.parseInt(jsonObj.get("size").toString()), 1);

		// List all players filtering by defense position - wrong parameter

		result = this.mockMvc.perform(get("/players?onlyDefense=X"))
				.andDo(print())
				.andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new WrongParameterException().getMessage());

		shouldDeletePlayer(playerItaly, this.mockMvc);
		shouldDeletePlayer(playerGermany, this.mockMvc);
		shouldDeletePlayer(playerEngland, this.mockMvc);

		TeamControllerTest.shouldDeleteTeam(teamItalyDTO, this.mockMvc);
		TeamControllerTest.shouldDeleteTeam(teamGermanyDTO, this.mockMvc);
		TeamControllerTest.shouldDeleteTeam(teamEnglandDTO, this.mockMvc);

		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueGermanyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueEnglandDTO, this.mockMvc);

		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryGermanyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryEnglandDTO, this.mockMvc);
	}

	@Test
	public void shouldCreatePlayerTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItalyDTO = new TeamDTO();
		teamItalyDTO.setName("Juventus");
		teamItalyDTO.setLeague(leagueItalyDTO);
		teamItalyDTO = TeamControllerTest.shouldCreateTeam(teamItalyDTO, this.mockMvc);

		PlayerDTO playerItaly = new PlayerDTO();
		playerItaly.setName("Cristiano", "Ronaldo");
		playerItaly.setTeam(teamItalyDTO);
		playerItaly.setShirtNumber(9);
		playerItaly.setPosition("ST");
		playerItaly = shouldCreatePlayer(playerItaly, this.mockMvc);

		shouldDeletePlayer(playerItaly, this.mockMvc);
		TeamControllerTest.shouldDeleteTeam(teamItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
	}

	@Test
	public void shouldDeletePlayerTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItalyDTO = new TeamDTO();
		teamItalyDTO.setName("Juventus");
		teamItalyDTO.setLeague(leagueItalyDTO);
		teamItalyDTO = TeamControllerTest.shouldCreateTeam(teamItalyDTO, this.mockMvc);

		PlayerDTO playerItaly = new PlayerDTO();
		playerItaly.setName("Cristiano", "Ronaldo");
		playerItaly.setTeam(teamItalyDTO);
		playerItaly.setShirtNumber(9);
		playerItaly.setPosition("ST");
		playerItaly = shouldCreatePlayer(playerItaly, this.mockMvc);

		shouldDeletePlayer(playerItaly, this.mockMvc);
		TeamControllerTest.shouldDeleteTeam(teamItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
	}

	@Test
	public void shouldGetPlayerByIdTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItalyDTO = new TeamDTO();
		teamItalyDTO.setName("Juventus");
		teamItalyDTO.setLeague(leagueItalyDTO);
		teamItalyDTO = TeamControllerTest.shouldCreateTeam(teamItalyDTO, this.mockMvc);

		PlayerDTO playerItaly = new PlayerDTO();
		playerItaly.setName("Cristiano", "Ronaldo");
		playerItaly.setTeam(teamItalyDTO);
		playerItaly.setShirtNumber(9);
		playerItaly.setPosition("ST");
		playerItaly = shouldCreatePlayer(playerItaly, this.mockMvc);
		this.mockMvc.perform(get("/player/" + playerItaly.getId())).andDo(print()).andExpect(status().isOk());

		shouldDeletePlayer(playerItaly, this.mockMvc);
		TeamControllerTest.shouldDeleteTeam(teamItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
	}

	@Test
	public void shouldPatchPlayerTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItalyDTO = new TeamDTO();
		teamItalyDTO.setName("Juventus");
		teamItalyDTO.setLeague(leagueItalyDTO);
		teamItalyDTO = TeamControllerTest.shouldCreateTeam(teamItalyDTO, this.mockMvc);

		PlayerDTO playerItaly = new PlayerDTO();
		playerItaly.setName("Cristiano", "Ronaldo");
		playerItaly.setTeam(teamItalyDTO);
		playerItaly.setShirtNumber(9);
		playerItaly.setPosition("ST");
		playerItaly = shouldCreatePlayer(playerItaly, this.mockMvc);
		playerItaly.setName("Paulo", "Dybala");
		playerItaly.setShirtNumber(10);
		playerItaly.setPosition("CAM");
		playerItaly.setTeam(teamItalyDTO);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(playerItaly);

		this.mockMvc.perform(patch("/player/" + playerItaly.getId())
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk());

		shouldDeletePlayer(playerItaly, this.mockMvc);
		TeamControllerTest.shouldDeleteTeam(teamItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreatePlayerNameAlreadyExistTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItalyDTO = new TeamDTO();
		teamItalyDTO.setName("Juventus");
		teamItalyDTO.setLeague(leagueItalyDTO);
		teamItalyDTO = TeamControllerTest.shouldCreateTeam(teamItalyDTO, this.mockMvc);

		PlayerDTO playerItaly = new PlayerDTO();
		playerItaly.setName("Cristiano", "Ronaldo");
		playerItaly.setTeam(teamItalyDTO);
		playerItaly.setShirtNumber(9);
		playerItaly.setPosition("ST");
		playerItaly = shouldCreatePlayer(playerItaly, this.mockMvc);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(playerItaly);

		MvcResult result = this.mockMvc.perform(post("/player")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new NameAlreadyExistException().getMessage());

		shouldDeletePlayer(playerItaly, this.mockMvc);
		TeamControllerTest.shouldDeleteTeam(teamItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreatePlayerNonNameProvidedTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItalyDTO = new TeamDTO();
		teamItalyDTO.setName("Juventus");
		teamItalyDTO.setLeague(leagueItalyDTO);
		teamItalyDTO = TeamControllerTest.shouldCreateTeam(teamItalyDTO, this.mockMvc);

		PlayerDTO playerItaly = new PlayerDTO();
		playerItaly.setTeam(teamItalyDTO);
		playerItaly.setShirtNumber(9);
		playerItaly.setPosition("ST");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(playerItaly);

		MvcResult result = this.mockMvc.perform(post("/player")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new NameEmptyException().getMessage());

		TeamControllerTest.shouldDeleteTeam(teamItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreateTwoPlayersOnSameTeamWithSameShirtNumberTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItalyDTO = new TeamDTO();
		teamItalyDTO.setName("Juventus");
		teamItalyDTO.setLeague(leagueItalyDTO);
		teamItalyDTO = TeamControllerTest.shouldCreateTeam(teamItalyDTO, this.mockMvc);

		PlayerDTO playerOneItaly = new PlayerDTO();
		playerOneItaly.setName("Cristiano", "Ronaldo");
		playerOneItaly.setTeam(teamItalyDTO);
		playerOneItaly.setShirtNumber(9);
		playerOneItaly.setPosition("ST");
		playerOneItaly = shouldCreatePlayer(playerOneItaly, this.mockMvc);

		PlayerDTO playerTwoItaly = new PlayerDTO();
		playerTwoItaly.setName("Paulo", "Dybala");
		playerTwoItaly.setTeam(teamItalyDTO);
		playerTwoItaly.setShirtNumber(9);
		playerTwoItaly.setPosition("CAM");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(playerTwoItaly);

		MvcResult result = this.mockMvc.perform(post("/player")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new ShirtNumberByTeamException().getMessage());

		shouldDeletePlayer(playerOneItaly, this.mockMvc);
		TeamControllerTest.shouldDeleteTeam(teamItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreatePlayerWrongPositionProvidedTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItalyDTO = new TeamDTO();
		teamItalyDTO.setName("Juventus");
		teamItalyDTO.setLeague(leagueItalyDTO);
		teamItalyDTO = TeamControllerTest.shouldCreateTeam(teamItalyDTO, this.mockMvc);

		PlayerDTO playerItaly = new PlayerDTO();
		playerItaly.setName("Cristiano", "Ronaldo");
		playerItaly.setTeam(teamItalyDTO);
		playerItaly.setShirtNumber(9);
		playerItaly.setPosition("st");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(playerItaly);

		MvcResult result = this.mockMvc.perform(post("/player")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new PositionInvalidException().getMessage());

		TeamControllerTest.shouldDeleteTeam(teamItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreatePlayerWrongShirtNumberProvidedTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItalyDTO = new TeamDTO();
		teamItalyDTO.setName("Juventus");
		teamItalyDTO.setLeague(leagueItalyDTO);
		teamItalyDTO = TeamControllerTest.shouldCreateTeam(teamItalyDTO, this.mockMvc);

		PlayerDTO playerItaly = new PlayerDTO();
		playerItaly.setName("Cristiano", "Ronaldo");
		playerItaly.setTeam(teamItalyDTO);
		playerItaly.setShirtNumber(100);
		playerItaly.setPosition("ST");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(playerItaly);

		MvcResult result = this.mockMvc.perform(post("/player")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new ShirtNumberException().getMessage());

		TeamControllerTest.shouldDeleteTeam(teamItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreatePlayerNoShirtNumberProvidedTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItalyDTO = new TeamDTO();
		teamItalyDTO.setName("Juventus");
		teamItalyDTO.setLeague(leagueItalyDTO);
		teamItalyDTO = TeamControllerTest.shouldCreateTeam(teamItalyDTO, this.mockMvc);

		PlayerDTO playerItaly = new PlayerDTO();
		playerItaly.setName("Cristiano", "Ronaldo");
		playerItaly.setTeam(teamItalyDTO);
		playerItaly.setPosition("ST");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(playerItaly);

		MvcResult result = this.mockMvc.perform(post("/player")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new ShirtNumberException().getMessage());

		TeamControllerTest.shouldDeleteTeam(teamItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreatePlayerNoPositionProvidedTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItalyDTO = new TeamDTO();
		teamItalyDTO.setName("Juventus");
		teamItalyDTO.setLeague(leagueItalyDTO);
		teamItalyDTO = TeamControllerTest.shouldCreateTeam(teamItalyDTO, this.mockMvc);

		PlayerDTO playerItaly = new PlayerDTO();
		playerItaly.setName("Cristiano", "Ronaldo");
		playerItaly.setTeam(teamItalyDTO);
		playerItaly.setShirtNumber(9);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(playerItaly);

		MvcResult result = this.mockMvc.perform(post("/player")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new PositionInvalidException().getMessage());

		TeamControllerTest.shouldDeleteTeam(teamItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreatePlayerNonTeamProvidedTest() throws Exception {
		PlayerDTO player = new PlayerDTO();
		player.setName("Cristiano", "Ronaldo");
		player.setPosition("CF");
		player.setShirtNumber(9);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(player);

		MvcResult result = this.mockMvc.perform(post("/player")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new SchemaInvalidException().getMessage());
	}

	@Test
	public void shouldNotCreatePlayerTeamNotFoundTest() throws Exception {
		TeamDTO teamDTO = new TeamDTO();
		teamDTO.setId(100);

		PlayerDTO player = new PlayerDTO();
		player.setName("Cristiano", "Ronaldo");
		player.setTeam(teamDTO);
		player.setPosition("CF");
		player.setShirtNumber(9);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(player);

		MvcResult result = this.mockMvc.perform(post("/player")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new NotFoundException().getMessage());
	}

	@Test
	public void shouldNotCreateMoreThan25PlayersOnSameTeamTest() throws Exception {
		CountryDTO countryItalyDTO = new CountryDTO();
		countryItalyDTO.setName("Italy");
		countryItalyDTO.setLanguage("it");
		countryItalyDTO = CountryControllerTest.shouldCreateCountry(countryItalyDTO, this.mockMvc);

		LeagueDTO leagueItalyDTO = new LeagueDTO();
		leagueItalyDTO.setName("Serie A");
		leagueItalyDTO.setCountry(countryItalyDTO);
		leagueItalyDTO = LeagueControllerTest.shouldCreateLeague(leagueItalyDTO, this.mockMvc);

		TeamDTO teamItalyDTO = new TeamDTO();
		teamItalyDTO.setName("Juventus");
		teamItalyDTO.setLeague(leagueItalyDTO);
		teamItalyDTO = TeamControllerTest.shouldCreateTeam(teamItalyDTO, this.mockMvc);

		List<PlayerDTO> players = new ArrayList<>();
		for (int i=0; i < Constants.PLAYERS_PER_TEAM; i++){
			PlayerDTO player = new PlayerDTO();
			player.setName("Player", i + "");
			player.setTeam(teamItalyDTO);
			player.setShirtNumber(i+1);
			player.setPosition("ST");
			players.add(shouldCreatePlayer(player, this.mockMvc));
		}

		PlayerDTO playerX = new PlayerDTO();
		playerX.setName("Player", "X");
		playerX.setTeam(teamItalyDTO);
		playerX.setShirtNumber(99);
		playerX.setPosition("ST");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(playerX);

		MvcResult result = this.mockMvc.perform(post("/player")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new PlayerByTeamException().getMessage());

		for (PlayerDTO playerDTO : players) shouldDeletePlayer(playerDTO, this.mockMvc);
		TeamControllerTest.shouldDeleteTeam(teamItalyDTO, this.mockMvc);
		LeagueControllerTest.shouldDeleteLeague(leagueItalyDTO, this.mockMvc);
		CountryControllerTest.shouldDeleteCountry(countryItalyDTO, this.mockMvc);
	}

	/*
	 * Auxiliary methods for tests
	 */

	public static PlayerDTO shouldCreatePlayer(PlayerDTO playerDTO, MockMvc mockMvc) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(playerDTO);

		MvcResult result = mockMvc.perform(post("/player")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		Gson g = new Gson();
		return g.fromJson(content, PlayerDTO.class);
	}

	public static void shouldDeletePlayer(PlayerDTO createdPlayerDTO, MockMvc mockMvc) throws Exception {
		mockMvc.perform(delete("/player/" + createdPlayerDTO.getId())).andDo(print()).andExpect(status().isOk());
	}
}
