package com.jaehong.projectclassjaehongdev.utils.annotation;


import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.transaction.annotation.Transactional;


@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Transactional
@DataJpaTest
@TestProfile
public @interface RepositoryTest {
}
