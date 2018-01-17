package com.zjr.technologytest;

import com.zjr.technologytest.entity.Student;
import com.zjr.technologytest.util.JsonUtils;
import com.zjr.technologytest.util.RedisUtil;
import org.assertj.core.util.Lists;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.*;
import java.util.concurrent.TimeUnit;

/**
 * redisTemplate.opsForValue();//操作字符串
 * redisTemplate.opsForHash();//操作hash
 * redisTemplate.opsForList();//操作list
 * redisTemplate.opsForSet();//操作set
 * redisTemplate.opsForZSet();//操作有序set
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TechnologytestApplicationTests {

    @Autowired
    private RedisTemplate redisTemplate;


    //用于解决从服务器上查看key乱码的问题(字符串出现类似这样的：\xe5\xbc\xa0\xe4\xb8\x89\xe4\xb8\xb0\)
    @Autowired(required = false)
    public void setRedisTemplate(RedisTemplate redisTemplate) {
        RedisSerializer stringSerializer = new StringRedisSerializer();
        redisTemplate.setKeySerializer(stringSerializer);
        redisTemplate.setValueSerializer(stringSerializer);
        redisTemplate.setHashKeySerializer(stringSerializer);
        redisTemplate.setHashValueSerializer(stringSerializer);
        this.redisTemplate = redisTemplate;
    }

    @Test
    public void test1() {
        //存储字符串到redis中
        redisTemplate.opsForValue().set("wangwu", "333");
    }

    @Test
    public void test2() {
        Object object = redisTemplate.opsForValue().get("wangwu");
        System.out.println(object);
    }

    @Test
    public void test3() {
        Set<String> set = new HashSet<String>();
        set.add("set1");
        set.add("set2");
        //set集合转为json字符串
        String json = JsonUtils.toJson(set);
        //存入redis中
        redisTemplate.opsForValue().set("setkey", json);
    }

    @Test
    public void test4() {
        //从redis中取出key为setkey的value值
        Object object = redisTemplate.opsForValue().get("setkey");
        System.out.println(object);
    }

    @Test
    public void test5() {
        List<String> list = new ArrayList<String>();
        list.add("list1");
        list.add("list2");
        //list转为json字符串
        String json = JsonUtils.toJson(list);
        //存入redis中
        redisTemplate.opsForValue().set("listkey", json);
    }

    @Test
    public void test6() {
        Object object = redisTemplate.opsForValue().get("listkey");
        System.out.println(object);
    }

    @Test
    public void test7() {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("zh", 12);
        map.put("jp", "japan");
        map.put("tw", 10L);
        //map转为json字符串
        String json = JsonUtils.toJson(map);
        //存入redis中
        redisTemplate.opsForValue().set("mapkey", json);
    }

    @Test
    public void test8() {
        Object object = redisTemplate.opsForValue().get("mapkey");
        System.out.println(object);
    }

    @Test
    public void test9() {
        Student student = new Student();
        student.setAge(100);
        student.setName("张三丰");
        //对象转为json字符串
        String json = JsonUtils.toJson(student);
        //存入redis中
        redisTemplate.opsForValue().set("objkey", json);
    }

    @Test
    public void test10() {
        Object object = redisTemplate.opsForValue().get("objkey");
        System.out.println(object);
    }

    @Test
    public void test11() {
        //删除redis中key为objkey的数据
        redisTemplate.delete("objkey");
    }

    @Test
    public void test12() {
        //存入redis中，只保存20秒，20秒后移除这个key(20秒后失效)
        redisTemplate.opsForValue().set("zaoliu", "我是赵六", 20, TimeUnit.SECONDS);
    }

    @Test
    public void test13() {
        //列表名称
        String keys = "LIST:";
        //把ZAO,SHANG,HAO,NI存入列表
        redisTemplate.opsForList().leftPush(keys, "ZAO");
        redisTemplate.opsForList().leftPush(keys, "SHANG");
        redisTemplate.opsForList().rightPush(keys, "HAO");
        redisTemplate.opsForList().leftPush(keys, "NI");

    }

    @Test
    public void test14() {
        //表示从列表名称为LIST:的列表中取0号索引元素至3号索引元素
        List list = redisTemplate.opsForList().range("LIST:", 0, 3);
        System.out.println(list);
        for (int i = 0; i < list.size(); i++) {
            System.out.println(list.get(i));
        }
        System.out.println(list.get(2));

    }

    @Test
    public void test15() {
        //获取redis中所有的key
        Set keys = redisTemplate.keys("*");
        System.out.println(keys);
    }

    @Test
    public void test16() {
        //给redis中为mapkey的key设置失效时间，30秒后失效
        Boolean flag = redisTemplate.expire("mapkey", 30, TimeUnit.SECONDS);
        System.out.println(flag);
    }

    @Test
    public void test17() {
        String keys = "HASH";
        //存储hash
        redisTemplate.opsForHash().put(keys, "zao", "22");
        redisTemplate.opsForHash().put(keys, "xing", "zhang");
    }

    @Test
    public void test18() {
        Object object = redisTemplate.opsForHash().get("HASH", "zao");
        System.out.println(object);
    }

    @Test
    public void test19() {
        Map map = redisTemplate.opsForHash().entries("HASH");
        Iterator iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            System.out.println("key=" + entry.getKey() + ",value=" + entry.getValue());
        }
    }

    @Test
    public void test20() {
        //删除哈希表中key为xing的数据
        redisTemplate.opsForHash().delete("HASH", "xing");
    }

    @Test
    public void test21() {
        //删除key为HASH的哈希表（删除名称为HASH的哈希表）
        redisTemplate.delete("HASH");
    }

    @Test
    public void test22() {
        String keys = "SET";
        //存储set集合，无序，不重复
        redisTemplate.opsForSet().add(keys, "zhangsan");
        redisTemplate.opsForSet().add(keys, "lisi");
        redisTemplate.opsForSet().add(keys, "lisi");
    }

    @Test
    public void test23() {
        //获取set集合中key为SET的成员
        Set set = redisTemplate.opsForSet().members("SET");
        Iterator iterator = set.iterator();
        while (iterator.hasNext()) {
            Object obj = iterator.next();
            System.out.println(obj);
        }
    }

    /**
     * Redis 有序集合和集合一样也是string类型元素的集合,且不允许重复的成员。
     * 不同的是每个元素都会关联一个double类型的分数。redis正是通过分数来为集合中的成员进行从小到大的排序。
     * 有序集合的成员是唯一的,但分数(score)却可以重复。
     */
    @Test
    public void test24() {
        redisTemplate.opsForZSet().add("ZSET", "abc", 10);
        redisTemplate.opsForZSet().add("ZSET", "ghj", 20);
        redisTemplate.opsForZSet().add("ZSET", "qwe", 2);

    }
    @Test
    public void test25() {
        Set zset = redisTemplate.opsForZSet().range("ZSET", 0, 4);
        Iterator iterator = zset.iterator();
        while(iterator.hasNext()){
            Object object = iterator.next();
            System.out.println(object);
        }
    }
}
