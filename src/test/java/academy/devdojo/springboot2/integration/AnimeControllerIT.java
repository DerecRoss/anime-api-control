package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.util.AnimeGenerator;
import academy.devdojo.springboot2.wrapper.PageableResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase // configuration of DB use the value in memory

public class AnimeControllerIT { //IT -> integration, easy to read with maven execute in pipeline
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Autowired
    private AnimeRepository animeRepository;

    @LocalServerPort // get actual port running in server. if port = 0 tomcat use random port for server.
    private int port;

    @Test
    @DisplayName("List return list of Anime in Page Object when successful")
    void returnListOfAnimesInsidePage_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeGenerator.animeGeneratorToBeSaved());
        String expectedName = savedAnime.getName();

        PageableResponse<Anime> bodyPage = testRestTemplate.exchange("/animes",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<PageableResponse<Anime>>() {
                }).getBody();

        Assertions.assertThat(bodyPage).isNotNull();
        Assertions.assertThat(bodyPage.toList())
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);
        Assertions.assertThat(bodyPage.toList().get(0).getName()).isEqualTo(expectedName);
    }
}
