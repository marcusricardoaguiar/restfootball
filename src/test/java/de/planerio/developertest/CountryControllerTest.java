package de.planerio.developertest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.gson.Gson;
import de.planerio.developertest.controllers.CountryController;
import de.planerio.developertest.exceptions.LanguageInvalidException;
import de.planerio.developertest.exceptions.NameAlreadyExistException;
import de.planerio.developertest.exceptions.NameEmptyException;
import de.planerio.developertest.services.dtos.CountryDTO;
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

@SpringBootTest
@AutoConfigureMockMvc
class CountryControllerTest {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), StandardCharsets.UTF_8);

	@Autowired
	private CountryController countryController;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() {
		assertThat(countryController).isNotNull();
	}

	@Test
	public void shouldListAllCountriesTest() throws Exception {
		CountryDTO countryFrance = new CountryDTO();
		countryFrance.setName("France");
		countryFrance.setLanguage("fr");
		countryFrance = shouldCreateCountry(countryFrance, this.mockMvc);

		CountryDTO countryGermany = new CountryDTO();
		countryGermany.setName("Germany");
		countryGermany.setLanguage("de");
		countryGermany = shouldCreateCountry(countryGermany, this.mockMvc);

		CountryDTO countryEngland = new CountryDTO();
		countryEngland.setName("England");
		countryEngland.setLanguage("en");
		countryEngland = shouldCreateCountry(countryEngland, this.mockMvc);

		MvcResult result = this.mockMvc.perform(get("/countries"))
				.andDo(print())
				.andExpect(status().isOk()).andReturn();
		String content = result.getResponse().getContentAsString();
		JSONObject jsonObj = new JSONObject(content);

		Assertions.assertEquals(Integer.parseInt(jsonObj.get("size").toString()),3);


		result = this.mockMvc.perform(get("/countries?page=0&size=2"))
				.andDo(print())
				.andExpect(status().isOk()).andReturn();
		content = result.getResponse().getContentAsString();
		jsonObj = new JSONObject(content);

		Assertions.assertEquals(Integer.parseInt(jsonObj.get("size").toString()), 2);

		shouldDeleteCountry(countryFrance, this.mockMvc);
		shouldDeleteCountry(countryGermany, this.mockMvc);
		shouldDeleteCountry(countryEngland, this.mockMvc);
	}

	@Test
	public void shouldCreateCountryTest() throws Exception {
		CountryDTO country = new CountryDTO();
		country.setName("France");
		country.setLanguage("fr");
		CountryDTO countryDTO = shouldCreateCountry(country, this.mockMvc);
		shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldDeleteCountryTest() throws Exception {
		CountryDTO country = new CountryDTO();
		country.setName("Germany");
		country.setLanguage("de");
		CountryDTO countryDTO = shouldCreateCountry(country, this.mockMvc);
		shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldGetCountryByIdTest() throws Exception {
		CountryDTO country = new CountryDTO();
		country.setName("Italy");
		country.setLanguage("it");
		CountryDTO countryDTO = shouldCreateCountry(country, this.mockMvc);
		this.mockMvc.perform(get("/country/" + countryDTO.getId())).andDo(print()).andExpect(status().isOk());
		shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldPatchCountryTest() throws Exception {
		CountryDTO country = new CountryDTO();
		country.setName("Italy");
		country.setLanguage("it");
		CountryDTO countryDTO = shouldCreateCountry(country, this.mockMvc);
		countryDTO.setName("Updated Italy");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(countryDTO);

		this.mockMvc.perform(patch("/country/" + countryDTO.getId())
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk());
		shouldDeleteCountry(countryDTO, this.mockMvc);
	}

	@Test
	public void shouldNotCreateCountryNameAlreadyExistTest() throws Exception {
		CountryDTO country = new CountryDTO();
		country.setName("England");
		country.setLanguage("en");
		country = shouldCreateCountry(country, this.mockMvc);

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(country);

		MvcResult result = this.mockMvc.perform(post("/country")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new NameAlreadyExistException().getMessage());
		shouldDeleteCountry(country, this.mockMvc);
	}

	@Test
	public void shouldNotCreateCountryNonNameProvidedTest() throws Exception {
		CountryDTO country = new CountryDTO();
		country.setLanguage("fr");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(country);

		MvcResult result = this.mockMvc.perform(post("/country")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new NameEmptyException().getMessage());
	}

	@Test
	public void shouldNotCreateCountryWrongLanguageProvidedTest() throws Exception {
		CountryDTO country = new CountryDTO();
		country.setName("Germany");
		country.setLanguage("DE");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(country);

		MvcResult result = this.mockMvc.perform(post("/country")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest()).andReturn();

		String message = Objects.requireNonNull(result.getResolvedException()).getMessage();
		Assertions.assertEquals(message, new LanguageInvalidException().getMessage());
	}

	/*
	 * Auxiliary methods for tests
	 */

	public static CountryDTO shouldCreateCountry(CountryDTO countryDTO, MockMvc mockMvc) throws Exception {
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(countryDTO);

		MvcResult result = mockMvc.perform(post("/country")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk())
				.andReturn();

		String content = result.getResponse().getContentAsString();
		Gson g = new Gson();
		return g.fromJson(content, CountryDTO.class);
	}

	public static void shouldDeleteCountry(CountryDTO createdCountryDTO, MockMvc mockMvc) throws Exception {
		mockMvc.perform(delete("/country/" + createdCountryDTO.getId())).andDo(print()).andExpect(status().isOk());
	}
}
