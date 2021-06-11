package es.examplecorp;
import redis.clients.jedis.Jedis;
import spark.ModelAndView;
import spark.servlet.SparkApplication;
import spark.template.handlebars.HandlebarsTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static spark.Spark.*;
import static spark.Spark.get;

public class HelloWorld implements SparkApplication {

    static Logger logger = LoggerFactory.getLogger(HelloWorld.class);

    @Override
    public void init() {

//        String text = System.getenv("HELLOWORLD");
        String text = "DevOps";

        Map map = new HashMap();
        map.put("Name", text);


        get("/",(req,res) -> "hello World");

        // Hello World with Template
        get("/hello",(req,res) -> new ModelAndView(map,"hello.hbs"),new HandlebarsTemplateEngine());

        // Hello World with external Database
        get("/redis",(req,res) -> "This text comes from Database: " + redisContent("hello"));

        // loader io
        get("/loaderio-c1be3a6de2480430d7e499ae5d5beaf1/", (req,res) -> "loaderio-c1be3a6de2480430d7e499ae5d5beaf1");

        // Not working: it says req.params is empty
        // post("/redis",(req,res) -> redisSetContent("hello", req.params("msg")));

    }

    private static String redisSetContent(String key, String value) {
        logger.debug("key: " + key + "; value: " + value);
        Jedis jedis = new Jedis("localhost",6379);
        jedis.set(key,value);
        return value;
    }

    private static String redisContent(String key) {
        Jedis jedis = new Jedis("localhost",6379);
        return jedis.get("hello");
    }
}
