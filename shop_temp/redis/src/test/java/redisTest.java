import com.qf.entity.User;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.JdkSerializationRedisSerializer;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

@RunWith(value = SpringJUnit4ClassRunner.class)
@ContextConfiguration(value = "classpath:applicationContext.xml")
public class redisTest {
    @Autowired
    private RedisTemplate redisTemplate;

    @Test
    public void JedisTest(){
        Jedis jedis=new Jedis("120.24.173.48",6379);
        jedis.auth("root");
        jedis.set("name","张三");
        System.out.println(jedis.get("name"));
        jedis.close();
    }
    @Test
    public void springTest(){
        redisTemplate.setValueSerializer(new JdkSerializationRedisSerializer());
        User user = new User();
        user.setId(1);
        user.setName("zs");
        user.setAge(12);
        redisTemplate.opsForValue().set("user",user);
        User name = (User) redisTemplate.opsForValue().get("user");
        System.out.println(name);
    }
}
