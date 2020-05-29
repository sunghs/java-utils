package sunghs.java.utils.string;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class StringMapperTest {

    @Test
    public void StringMapperTest() {
        Map<String, String> map = new HashMap<>();
        map.put("map1", "123123");
        map.put("aaaaaaaa", "234234");
        map.put("123qweasdzxc", "345345");

        String str = "가가가가가가가나나나나다다다라라라라라${map1}가가가가나나다라라라 " +
                "\r\n" +
                "abcedfg ${aaaaaaaa} test1234567890 zxcv ${123qweasdzxc} " +
                "\r\n" +
                "\r\n" +
                "testtest ${nomapping} << nomapping 없는 부분" +
                "\r\n" +
                "1234567890";

        String doReplaced = StringMapper.doReplace(str, map);

        String doReplacedRegEx = StringMapper.doReplaceWithRegEx(str, map);

        String expected = "가가가가가가가나나나나다다다라라라라라123123가가가가나나다라라라 " +
                "\r\n" +
                "abcedfg 234234 test1234567890 zxcv 345345 " +
                "\r\n" +
                "\r\n" +
                "testtest  << nomapping 없는 부분" +
                "\r\n" +
                "1234567890";

        Assertions.assertEquals(doReplaced, doReplacedRegEx);
        Assertions.assertEquals(doReplaced, expected);
        Assertions.assertEquals(doReplacedRegEx, expected);
    }
}
