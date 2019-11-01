package hu.oe.bakonyi.bkk.bkkweathercrawler.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import hu.oe.bakonyi.bkk.bkkweathercrawler.model.weather.Coord;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.data.convert.ReadingConverter;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.stereotype.Component;

@ReadingConverter
@Component
public class RedisReaderConverter implements Converter<byte[], Coord> {
    Jackson2JsonRedisSerializer<Coord> mapper;

    public RedisReaderConverter() {
        this.mapper  = new Jackson2JsonRedisSerializer<Coord>(Coord.class);
        mapper.setObjectMapper(new ObjectMapper());
    }

    @Override
    public Coord convert(byte[] source) {
        return mapper.deserialize(source);
    }
}
