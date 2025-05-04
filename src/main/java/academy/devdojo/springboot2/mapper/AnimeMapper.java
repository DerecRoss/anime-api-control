package academy.devdojo.springboot2.mapper;

import academy.devdojo.springboot2.domain.Anime;
import academy.devdojo.springboot2.requests.AnimeDto;

import academy.devdojo.springboot2.requests.AnimePostRequestBody;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring") // case need dependency injection
public abstract class AnimeMapper {
    public static final AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class); // create call method this wrapper

    public abstract Anime toAnime(AnimePostRequestBody animeDto);

    public abstract Anime toAnime(AnimeDto animeDto);
}
