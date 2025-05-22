package academy.devdojo.springboot2.integration;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.repository.AnimeRepository;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.util.AnimeGenerator;
import academy.devdojo.springboot2.util.AnimePostRequestBodyCreator;
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
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase // configuration of DB use the value in memory
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
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

    @Test
    @DisplayName("ListAll return list of Anime in Page Object when successful")
    void returnListAllOfAnimesInsideList_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeGenerator.animeGeneratorToBeSaved());
        String expectedName = savedAnime.getName();

        List<Anime> animes = testRestTemplate.exchange("/animes/all",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById return Anime when successful")
    void FindById_returnAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeGenerator.animeGeneratorToBeSaved());
        Long expectedId = savedAnime.getId();

        Anime anime = testRestTemplate.getForObject("/animes/{id}", Anime.class, expectedId);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("FindByName return list of Anime when successful")
    void FindByName_returnListOfAnime_WhenSuccessful(){
        Anime savedAnime = animeRepository.save(AnimeGenerator.animeGeneratorToBeSaved());
        String expectedName = savedAnime.getName();
        String url = String.format("/animes/find?name=%s", expectedName);
        List<Anime> animes = testRestTemplate.exchange(url,
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(animes.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName return empty list of Anime when successful")
    void FindByName_ReturnEmptyListOfAnime_WhenAnimeIsNotFound(){
        List<Anime> animes = testRestTemplate.exchange("/animes/find?name=berserk",
                HttpMethod.GET,
                null,
                new ParameterizedTypeReference<List<Anime>>() {
                }).getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save return Anime save when successful")
    void Save_returnAnimeSave_WhenSuccessful(){

        AnimePostRequestBody animePostRequestBody = AnimePostRequestBodyCreator.createAnimePostRequestBody();

        ResponseEntity<Anime> animeResponseEntity = testRestTemplate.postForEntity("/animes", animePostRequestBody, Anime.class);

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(animeResponseEntity.getBody()).isNotNull();
    }

    @Test
    @DisplayName("replace update Anime when successful")
    void replace_returnAnime_WhenSuccessful(){

        Anime savedAnime = animeRepository.save(AnimeGenerator.animeGeneratorToBeSaved());

        savedAnime.setName("New name");

            ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes",
                    HttpMethod.PUT, new HttpEntity<>(savedAnime), new ParameterizedTypeReference<Void>() {
                    });

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete deleted Anime when successful")
    void delete_removeAnime_WhenSuccessful(){

        Anime savedAnime = animeRepository.save(AnimeGenerator.animeGeneratorToBeSaved());

        ResponseEntity<Void> animeResponseEntity = testRestTemplate.exchange("/animes/{id}",
                HttpMethod.DELETE, null, new ParameterizedTypeReference<Void>() {
                }, savedAnime.getId());

        Assertions.assertThat(animeResponseEntity).isNotNull();
        Assertions.assertThat(animeResponseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
