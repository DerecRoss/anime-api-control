package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import academy.devdojo.springboot2.requests.AnimePutRequestBody;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeGenerator;
import academy.devdojo.springboot2.util.AnimePostRequestBodyCreator;
import academy.devdojo.springboot2.util.AnimePutRequestBodyCreator;
import academy.devdojo.springboot2.util.DateUtil;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
class AnimeControllerTest {

    @InjectMocks
    private AnimeController animeController;
    @Mock
    private AnimeService animeServiceMock;

    @Mock
    private DateUtil dateUtil;

    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeGenerator.animeGeneratorValid()));
        BDDMockito.when(animeServiceMock.listAll(ArgumentMatchers.any())) // what comportment mockito await
                .thenReturn(animePage);// what's return mockito await for this class invocation this method

        BDDMockito.when(animeServiceMock.listAllNoPageable())
                .thenReturn(List.of(AnimeGenerator.animeGeneratorValid()));

        BDDMockito.when(animeServiceMock.findByIdOrThrowBadRequestException(ArgumentMatchers.anyLong()))
                .thenReturn(AnimeGenerator.animeGeneratorValid());

        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeGenerator.animeGeneratorValid()));

        BDDMockito.when(animeServiceMock.save(ArgumentMatchers.any(AnimePostRequestBody.class)))
                .thenReturn(AnimeGenerator.animeGeneratorValid());

        BDDMockito.doNothing().when(animeServiceMock)
                .replace(ArgumentMatchers.any(AnimePutRequestBody.class));

        BDDMockito.doNothing().when(animeServiceMock)
                .delete(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("List return list of Anime in Page Object when successful")
        void returnListOfAnimesInsidePage_WhenSuccessful(){
        String expectedName = AnimeGenerator.animeGeneratorValid().getName();
        Page<Anime> bodyPage = animeController.list(null).getBody();

        Assertions.assertThat(bodyPage).isNotNull();
        Assertions.assertThat(bodyPage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(bodyPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll return list of Anime in Page Object when successful")
    void returnListAllOfAnimesInsideList_WhenSuccessful(){
        String expectedName = AnimeGenerator.animeGeneratorValid().getName();
        List<Anime> animeList = animeController.listAll().getBody();

        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList).isNotEmpty().hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById return Anime when successful")
    void FindById_returnAnime_WhenSuccessful(){
        Long expectedId = AnimeGenerator.animeGeneratorValid().getId();
        Anime anime = animeController.findById(expectedId).getBody();

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("FindByName return list of Anime when successful")
    void FindByName_returnListOfAnime_WhenSuccessful(){
        String expectedName = AnimeGenerator.animeGeneratorValid().getName();
        List<Anime> anime = animeController.findByName(expectedName).getBody();

        Assertions.assertThat(anime)
                .isNotNull()
                .isNotEmpty()
                .hasSize(1);

        Assertions.assertThat(anime.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindByName return list of Anime when successful")
    void FindByName_ReturnEmptyListOfAnime_WhenAnimeIsNotFound(){
        // All compartments are not successful put in test
        BDDMockito.when(animeServiceMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animes = animeController.findByName("animeString").getBody();

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save return Anime save when successful")
    void Save_returnAnimeSave_WhenSuccessful(){
        Long expectedId = AnimeGenerator.animeGeneratorValid().getId();
        Anime anime = animeController.save(AnimePostRequestBodyCreator.createAnimePostRequestBody()).getBody();

        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeGenerator.animeGeneratorValid());
    }

    @Test
    @DisplayName("replace update Anime when successful")
    void replace_returnAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
                 .doesNotThrowAnyException();
        ResponseEntity<Void> entity = animeController.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody());
        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    @DisplayName("delete deleted Anime when successful")
    void delete_removeAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeController.delete(1L))
                .doesNotThrowAnyException();
        ResponseEntity<Void> entity = animeController.delete(1L);
        Assertions.assertThat(entity).isNotNull();
        Assertions.assertThat(entity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}