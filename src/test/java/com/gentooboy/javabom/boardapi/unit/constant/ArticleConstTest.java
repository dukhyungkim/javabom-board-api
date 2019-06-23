package com.gentooboy.javabom.boardapi.unit.constant;

import static org.assertj.core.api.Assertions.assertThat;

import com.gentooboy.javabom.boardapi.constant.ArticleConst;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import org.junit.Test;

public class ArticleConstTest {

  @Test
  public void whenNewArticleConst_ReturnInstance()
      throws NoSuchMethodException, IllegalAccessException, InvocationTargetException, InstantiationException {
    Constructor<ArticleConst> constructor = ArticleConst.class.getDeclaredConstructor();

    assertThat(Modifier.isPrivate(constructor.getModifiers())).isTrue();

    constructor.setAccessible(true);
    assertThat(constructor.newInstance()).isNotNull();
  }
}