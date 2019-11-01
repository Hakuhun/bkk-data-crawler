package hu.oe.bakonyi.bkk.bkkweathercrawler.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather.Coord;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.WritingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@WritingConverter
@Component
public class RedisWritingConverter implements Converter<Coord, byte[]> {
    Jackson2JsonRedisSerializer<Coord> mapper;

    public RedisWritingConverter() {
        this.mapper = new Jackson2JsonRedisSerializer<Coord>(Coord.class);;
        mapper.setObjectMapper(new ObjectMapper());
    }

    @Override
    public byte[] convert(Coord source) {
        return mapper.serialize(source);
    }
}
