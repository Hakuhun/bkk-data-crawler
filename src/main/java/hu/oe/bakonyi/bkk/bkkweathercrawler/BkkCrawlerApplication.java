package hu.oe.bakonyi.bkk.bkkweathercrawler;

import hu.oe.bakonyi.bkk.bkkweathercrawler.configuration.RedisReaderConverter;
import hu.oe.bakonyi.bkk.bkkweathercrawler.configuration.RedisWritingConverter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.convert.RedisCustomConversions;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Arrays;

@SpringBootApplication
@EnableScheduling
@EnableFeignClients
@EnableRedisRepositories
public class BkkCrawlerApplication {

	public static void main(String[] args) {
		SpringApplication.run(BkkCrawlerApplication.class, args);
	}

	@Bean
	public RedisCustomConversions redisCustomConversions(RedisWritingConverter offsetToBytes,
														 RedisReaderConverter bytesToOffset) {
		return new RedisCustomConversions(Arrays.asList(offsetToBytes, bytesToOffset));
	}
}
