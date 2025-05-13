package academy.devdojo.springboot2.util;

import academy.devdojo.springboot2.domain.Anime;

public class AnimeGenerator {
    public static Anime animeGeneratorToBeSaved(){
        return Anime.builder()
                .name("Hajime no Ippo")
                .build();
    }

    public static Anime animeGeneratorValid(){
        return Anime.builder()
                .id(1L)
                .name("Hajime no Ippo")
                .build();
    }

    public static Anime animeGeneratorValidUpdateAnime(){
        return Anime.builder()
                .id(1L)
                .name("Ippo")
                .build();
    }
}
