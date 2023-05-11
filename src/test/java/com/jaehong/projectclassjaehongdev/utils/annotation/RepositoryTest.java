package com.jaehong.projectclassjaehongdev.utils.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@TestProfile
@DataJpaTest
//@Transactional dataJpaTest 에 Transactional 이 선언되어 있음
public @interface RepositoryTest {
}
