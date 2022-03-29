import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import dto.Car;
import dto.User;

import java.util.Arrays;
import java.util.List;

public class Main {

    public static void main(String args[]) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        User user = new User();
        user.setAge(10);
        user.setName("홍길동");

        Car car1 = new Car();
        car1.setName("AUDI");
        car1.setNumber("1111");

        Car car2 = new Car();
        car2.setName("BMW");
        car2.setNumber("2222");

        List<Car> carList = Arrays.asList(car1, car2);
        user.setCar(carList);

        String json = objectMapper.writeValueAsString(user);
        System.out.println(json);

        User parsing = objectMapper.readValue(json, User.class);
        System.out.println(parsing);

        // node parsing
        //json 에서 값 가져오기
        JsonNode jsonNode = objectMapper.readTree(json);
        String name = jsonNode.get("name").asText();
        int age = jsonNode.get("age").asInt();


//        {
//            "name":"홍길동",
//            "age":10,
//            "car":[{
//                "name":"AUDI","number":"1111"
//                    },
//                {"name":"BMW","number":"2222"
//                }]
//        }


// car은 json의 노드임
        JsonNode cars = jsonNode.get("car");
        ArrayNode arrayNode = (ArrayNode)cars;
        System.out.println(name);
        System.out.println(age);

        List<Car> _car = objectMapper.convertValue(arrayNode, new TypeReference<List<Car>>() {});
        System.out.println(_car);

        //json 값 변형하기
        //순수 json 으로 되어있는 body를 바꿀 때 ,,
       ObjectNode objectNode = (ObjectNode)jsonNode;
        objectNode.put("name","abcd");
        System.out.println(objectNode.toPrettyString());

    }
}