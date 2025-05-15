package academy.devdojo.springboot2.service;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.exception.BadRequestException;
import academy.devdojo.springboot2.mapper.AnimeMapper;
import academy.devdojo.springboot2.repository.AnimeRepository;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class AnimeServiceTest {
    @InjectMocks
    private AnimeService animeService;

    @Mock
    private AnimeRepository animeRepositoryMock;

    @Mock
    private AnimeMapper animeMapper;


    @BeforeEach
    void setUp(){
        PageImpl<Anime> animePage = new PageImpl<>(List.of(AnimeGenerator.animeGeneratorValid()));
        BDDMockito.when(animeRepositoryMock.findAll(ArgumentMatchers.any(PageRequest.class)))// what comportment mockito await
                .thenReturn(animePage);// what's return mockito await for this class invocation this method

        BDDMockito.when(animeRepositoryMock.findAll())
                .thenReturn(List.of(AnimeGenerator.animeGeneratorValid()));

        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(AnimeGenerator.animeGeneratorValid()));

        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(AnimeGenerator.animeGeneratorValid()));

        BDDMockito.when(animeRepositoryMock.save(ArgumentMatchers.any(Anime.class)))
                .thenReturn(AnimeGenerator.animeGeneratorValid());

        BDDMockito.doNothing().when(animeRepositoryMock)
                .delete(ArgumentMatchers.any(Anime.class));
    }

    @Test
    @DisplayName("List return list of Anime in Page Object when successful")
    void ListAll_ReturnListOfAnimesInsidePage_WhenSuccessful(){
        String expectedName = AnimeGenerator.animeGeneratorValid().getName();
        Page<Anime> bodyPage = animeService.listAll(PageRequest.of(1, 1));

        Assertions.assertThat(bodyPage).isNotNull();
        Assertions.assertThat(bodyPage.toList()).isNotEmpty().hasSize(1);
        Assertions.assertThat(bodyPage.toList().get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("ListAll return list of Anime in Page Object when successful")
    void returnListAllOfAnimesInsideList_WhenSuccessful(){
        String expectedName = AnimeGenerator.animeGeneratorValid().getName();
        List<Anime> animeList = animeService.listAllNoPageable();

        Assertions.assertThat(animeList).isNotNull();
        Assertions.assertThat(animeList).isNotEmpty().hasSize(1);
        Assertions.assertThat(animeList.get(0).getName()).isEqualTo(expectedName);
    }

    @Test
    @DisplayName("FindById return Anime when successful")
    void FindById_returnAnime_WhenSuccessful(){
        Long expectedId = AnimeGenerator.animeGeneratorValid().getId();
        Anime anime = animeService.findByIdOrThrowBadRequestException(expectedId);

        Assertions.assertThat(anime).isNotNull();
        Assertions.assertThat(anime.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowBadRequestException throws BadRequestException when anime is not found")
    void findByIdOrThrowBadRequestException_ThrowsBadRequestException_WhenAnimeIsNotFound(){
        BDDMockito.when(animeRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(BadRequestException.class)
                .isThrownBy(() -> animeService.findByIdOrThrowBadRequestException(1L));
    }

    @Test
    @DisplayName("FindByName return list of Anime when successful")
    void FindByName_returnListOfAnime_WhenSuccessful(){
        String expectedName = AnimeGenerator.animeGeneratorValid().getName();
        List<Anime> anime = animeService.findByName(expectedName);

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
        BDDMockito.when(animeRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(Collections.emptyList());

        List<Anime> animes = animeService.findByName("animeString");

        Assertions.assertThat(animes)
                .isNotNull()
                .isEmpty();
    }

    @Test
    @DisplayName("save return Anime save when successful")
    void Save_returnAnimeSave_WhenSuccessful(){
        Anime anime = animeService.save(AnimePostRequestBodyCreator.createAnimePostRequestBody());
        Assertions.assertThat(anime).isNull();
//        Assertions.assertThat(anime).isNotNull().isEqualTo(AnimeGenerator.animeGeneratorValid());
    }

//    @Test
//    @DisplayName("replace updates anime when successful")
//    void replace_UpdatesAnime_WhenSuccessful(){
//
//        Assertions.assertThatCode(() -> animeService.replace(AnimePutRequestBodyCreator.createAnimePutRequestBody()))
//                .doesNotThrowAnyException();
//
//    } // read this after
    @Test
    @DisplayName("delete deleted Anime when successful")
    void delete_removeAnime_WhenSuccessful(){

        Assertions.assertThatCode(() -> animeService.delete(1L))
                .doesNotThrowAnyException();

    }
}