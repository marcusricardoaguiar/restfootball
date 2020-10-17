package de.planerio.developertest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import de.planerio.developertest.controllers.CountryController;
import de.planerio.developertest.services.dtos.CountryDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.Charset;

@SpringBootTest
@AutoConfigureMockMvc
class CountryControllerTest {

	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private CountryController countryController;

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void contextLoads() throws Exception {
		assertThat(countryController).isNotNull();
	}

	@Test
	public void shouldListAllCountries() throws Exception {
		this.mockMvc.perform(get("/countries")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void shouldListCountriesWithPageable() throws Exception {
		this.mockMvc.perform(get("/countries?page=0&size=2")).andDo(print()).andExpect(status().isOk());
	}

	@Test
	public void shouldCreateCountry() throws Exception {
		CountryDTO country = new CountryDTO();
		country.setName("France");
		country.setLanguage("fr");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(country);

		this.mockMvc.perform(post("/country")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print())
				.andExpect(status().isOk());
	}

	@Test
	public void shouldNotCreateCountryNonNameProvided() throws Exception {
		CountryDTO country = new CountryDTO();
		country.setLanguage("fr");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(country);

		this.mockMvc.perform(post("/country")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest());
	}

	@Test
	public void shouldNotCreateCountryWrongLanguageProvided() throws Exception {
		CountryDTO country = new CountryDTO();
		country.setName("France");
		country.setLanguage("FR");

		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(SerializationFeature.WRAP_ROOT_VALUE, false);
		ObjectWriter ow = mapper.writer().withDefaultPrettyPrinter();
		String requestJson = ow.writeValueAsString(country);

		this.mockMvc.perform(post("/country")
				.content(requestJson)
				.contentType(APPLICATION_JSON_UTF8))
				.andDo(print()).andExpect(status().isBadRequest());
	}
}
