package academy.devdojo.springboot2.controller;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.service.AnimeService;
import academy.devdojo.springboot2.util.AnimeGenerator;
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
import org.springframework.test.context.junit.jupiter.SpringExtension;

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
                .thenReturn(animePage); // what's return mockito await for this class invocation this method
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
}