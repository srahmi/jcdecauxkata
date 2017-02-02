package fr.sri.resource;

import fr.sri.JcdecauxKataApplication;
import fr.sri.domain.Developer;
import fr.sri.domain.ProgrammingLanguage;
import fr.sri.repository.DeveloperRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * Created by srahmi on 01/02/2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = JcdecauxKataApplication.class)
@WebAppConfiguration
@DirtiesContext(classMode= DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class DeveloperControllerTest {

    @Autowired
    DeveloperRepository developerRepository;

    @Autowired
    private WebApplicationContext context;

    private MockMvc mvc;

    private HttpMessageConverter mappingJackson2HttpMessageConverter;

    @Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = asList(converters).stream()
                .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
                .findAny()
                .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }

    @Before
    public void setUp() {
        this.mvc = MockMvcBuilders.webAppContextSetup(this.context).build();
    }

    @Test
    public void shouldReturnAllDevelopers() throws Exception {
        Developer developer1 = new Developer("Saïd");
        developer1.setProgrammingLanguage(new ProgrammingLanguage("Java"));
        Developer developer2 = new Developer("Oumaima");
        developer2.setProgrammingLanguage(new ProgrammingLanguage("Java"));
        Developer developer3 = new Developer("DouDou");
        developer3.setProgrammingLanguage(new ProgrammingLanguage("C++"));

        developerRepository.save(asList(developer1, developer2, developer3));

        this.mvc.perform(get("/developers")).andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void shouldSaveDeveloper() throws Exception {

        String developer = json(new Developer("Saïd"));

        System.out.println(developer);

        this.mvc.perform(post("/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(developer))
                .andExpect(status().isCreated());
    }

    @Test
    public void shouldUpdateDeveloper() throws Exception{
        String developer = json(new Developer("Saïd"));

        this.mvc.perform(post("/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(developer))
                .andExpect(status().isCreated());

        Developer developers = developerRepository.findOne(1L);

        assertThat(developers.getName(), is("Saïd"));

        Long id = developers.getId();

        String newDeveloper = json(new Developer("Oumaima"));
        this.mvc.perform(patch("/developers/" +id)
                .contentType(MediaType.APPLICATION_JSON)
                .content(newDeveloper))
                .andExpect(status().isOk());

        List<Developer> updatedDevelopers = developerRepository.findAll();

        assertThat(updatedDevelopers.get(0).getName(), is("Oumaima"));
    }

    @Test
    public void ShouldAddProgrammingLangageToDev() throws Exception {

        String developer = json(new Developer("Saïd"));

        System.out.println(developer);

        this.mvc.perform(post("/developers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(developer))
                .andExpect(status().isCreated());


        this.mvc.perform(post("/developers/1/programminglanguage")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json(new ProgrammingLanguage("Java"))))
                .andExpect(status().isOk());

        Developer updatedDevelopers = developerRepository.findOne(1L);

        assertThat(updatedDevelopers.getProgrammingLanguage().getName(), is("Java"));

    }

    @Test
    public void shouldReturnDevelopersByProgrammingLanguage() throws Exception {

        Developer developer1 = new Developer("Saïd");
        developer1.setProgrammingLanguage(new ProgrammingLanguage("Java"));
        Developer developer2 = new Developer("Oumaima");
        developer2.setProgrammingLanguage(new ProgrammingLanguage("Java"));
        Developer developer3 = new Developer("DouDou");
        developer3.setProgrammingLanguage(new ProgrammingLanguage("C++"));

        developerRepository.save(asList(developer1, developer2, developer3));

        this.mvc.perform(get("/developers/programminglanguage/Java"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].name", is("Saïd")))
                .andExpect(jsonPath("$[1].name", is("Oumaima")));

        this.mvc.perform(get("/developers/programminglanguage/C++"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("DouDou")));

        this.mvc.perform(get("/developers/programminglanguage/Python"))
                .andExpect(status().isNotFound());
    }

    protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}