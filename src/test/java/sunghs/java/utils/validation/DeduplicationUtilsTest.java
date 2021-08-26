package sunghs.java.utils.validation;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import sunghs.java.utils.validation.model.StudentInfo;

import java.util.List;

@Slf4j
class DeduplicationUtilsTest {

    @Test
    void duplicationTest() {
        StudentInfo s1 = new StudentInfo(1, "홍길동", 15);
        StudentInfo s2 = new StudentInfo(2, "김길동", 12);
        StudentInfo s3 = new StudentInfo(3, "박길동", 18);
        StudentInfo s4 = new StudentInfo(4, "이길동", 21);
        StudentInfo s5 = new StudentInfo(5, "홍길동", 16);
        StudentInfo s6 = new StudentInfo(6, "최길동", 11);

        List<StudentInfo> list = List.of(s1, s2, s3, s4, s5, s6);

        // 이름이 같은 사람은 제거
        List<StudentInfo> distinct = DeduplicationUtils.deduplication(list, StudentInfo::getName);

        // 학번이 5인 홍길동이 제거 되었으니 size 는 5
        Assertions.assertEquals(5, distinct.size());

        distinct.forEach(studentInfo -> log.info(studentInfo.toString()));
    }
}
