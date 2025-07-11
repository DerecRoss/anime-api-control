package academy.devdojo.springboot2.repository;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.util.AnimeGenerator;
import lombok.extern.log4j.Log4j2;
import org.assertj.core.api.Assertions;
import org.hibernate.exception.ConstraintViolationException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;
@Log4j2
@DataJpaTest
@DisplayName("Test for Anime Repository")
class AnimeRepositoryTest {

    private Anime setUp(){
        return Anime.builder().name("Doctor Stone").build();
    }

    @Autowired
    private AnimeRepository animeRepository;
    @Test
    @DisplayName("Save persist Anime Successful")
    void save_PersistAnimeInDataBase_WhenSuccessful(){
        Anime animeToSave = AnimeGenerator.animeGeneratorToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToSave);
        Assertions.assertThat(animeSaved).isNotNull();

        Assertions.assertThat(animeSaved.getId()).isNotNull();

        Assertions.assertThat(animeSaved.getName()).isEqualTo(animeToSave.getName());


    }

    @Test
    @DisplayName("Save Update Anime Successful")
    void save_UpdateAnimeInDataBase_WhenSuccessful() {
        Anime animeToSave = AnimeGenerator.animeGeneratorToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToSave);
        animeSaved.setName("Dororo");
        Anime animeUpdated = this.animeRepository.save(animeToSave);
        log.info(animeUpdated.getName());
        Assertions.assertThat(animeUpdated).isNotNull();

        Assertions.assertThat(animeUpdated.getId()).isNotNull();

        Assertions.assertThat(animeUpdated.getName()).isEqualTo(animeToSave.getName());

    }

    @Test
    @DisplayName("Delete remove Anime Successful")
    void delete_RemoveAnimeInDataBase_WhenSuccessful(){
        Anime animeToSave = AnimeGenerator.animeGeneratorToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToSave);

        this.animeRepository.delete(animeSaved);

        Optional<Anime> animeOptional = this.animeRepository.findById(animeSaved.getId());
        Assertions.assertThat(animeOptional.isEmpty()).isTrue();
    }

    @Test
    @DisplayName("Find by name return List Animes Successful")
    void findByName_ReturnListOfAnimesInDataBase_WhenSuccessful(){
        Anime animeToSave = AnimeGenerator.animeGeneratorToBeSaved();
        Anime animeSaved = this.animeRepository.save(animeToSave);

        String animeSavedName = animeSaved.getName();

        List<Anime> animes = this.animeRepository.findByName(animeSavedName);
        Assertions.assertThat(animes).isNotEmpty().contains(animeSaved);
        Assertions.assertThat(animes).contains(animeSaved);
    }

    @Test
    @DisplayName("Find by name return empty List when Animes not be found")
    void findByName_ReturnEmptyListOfAnimesInDataBase_WhenAnimeIsNotFound(){
        List<Anime> animes = this.animeRepository.findByName("or 1 = 1 --;");
        Assertions.assertThat(animes).isEmpty();
//      Assertions.assertThat(animes).Null(); -> test fail, null is  empty object
    }

//    @Test
//    @DisplayName("Save throw ConstrainValidationException when Anime name is empty")
//    void save_ThrowConstrainValidationException_WhenAnimeNaneIsEmpty() {
//        Anime anime = new Anime();
////        Assertions.assertThatThrownBy(() -> this.animeRepository.save(anime))
////                .isInstanceOf(ConstraintViolationException.class
////                );
//        Assertions.assertThatExceptionOfType(ConstraintViolationException.class)
//                .isThrownBy(() -> this.animeRepository.save(anime))
//                .withMessage("The name of anime is empty");
//    }

}