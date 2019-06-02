package com.gentooboy.javabom.boardapi.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.gentooboy.javabom.boardapi.model.articles.Article;
import com.gentooboy.javabom.boardapi.model.articles.Attributes;
import com.gentooboy.javabom.boardapi.model.articles.Links;
import com.gentooboy.javabom.boardapi.service.ArticlesService;
import java.util.ArrayList;
import java.util.List;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

@RunWith(SpringRunner.class)
@ActiveProfiles("test")
@WebMvcTest(ArticlesController.class)
public class ArticlesControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private ArticlesService service;

  private final String BASE_URL = "/articles";

  private Article article1;
  private Article article2;

  @Before
  public void setup() {

    article1 = Article.builder()
        .type("articles")
        .id("1")
        .attributes(new Attributes("Initial Article", "This is content of article"))
        .links(new Links("https://board-api/api/v1/articles/1"))
        .build();
    article2 = Article.builder()
        .type("articles")
        .id("2")
        .attributes(new Attributes("New Article", "This is content of new article"))
        .links(new Links("https://board-api/api/v1/articles/2"))
        .build();
  }

  @Test
  public void getArticleListReturnSuccessSingleData() throws Exception {
    List<Article> articleList = new ArrayList<>();
    articleList.add(article1);

    when(service.findAllArticles())
        .thenReturn(articleList);


    final ResultActions actions = mockMvc.perform(get(BASE_URL))
        .andDo(print());

    actions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(1))
        .andExpect(jsonPath("$.data[0].type").value(article1.getType()))
        .andExpect(jsonPath("$.data[0].id").value(article1.getId()))
        .andExpect(jsonPath("$.data[0].attributes.title").value(article1.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data[0].attributes.body").value(article1.getAttributes().getBody()))
        .andExpect(jsonPath("$.data[0].links.self").value(article1.getLinks().getSelf()));
  }

  @Test
  public void getArticleListReturnSuccessMultipleData() throws Exception {
    List<Article> articleList = new ArrayList<>();
    articleList.add(article1);
    articleList.add(article2);

    when(service.findAllArticles())
        .thenReturn(articleList);

    final ResultActions actions = mockMvc.perform(get(BASE_URL))
        .andDo(print());

    actions
        .andExpect(status().isOk())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(2))
        .andExpect(jsonPath("$.data[0].type").value(article1.getType()))
        .andExpect(jsonPath("$.data[0].id").value(article1.getId()))
        .andExpect(jsonPath("$.data[0].attributes.title").value(article1.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data[0].attributes.body").value(article1.getAttributes().getBody()))
        .andExpect(jsonPath("$.data[0].links.self").value(article1.getLinks().getSelf()))
        .andExpect(jsonPath("$.data[1].type").value(article2.getType()))
        .andExpect(jsonPath("$.data[1].id").value(article2.getId()))
        .andExpect(jsonPath("$.data[1].attributes.title").value(article2.getAttributes().getTitle()))
        .andExpect(jsonPath("$.data[1].attributes.body").value(article2.getAttributes().getBody()))
        .andExpect(jsonPath("$.data[1].links.self").value(article2.getLinks().getSelf()));
  }

  @Test
  public void getArticleListReturnNoConetnt() throws Exception {
    List<Article> articleList= new ArrayList<>();
    when(service.findAllArticles())
        .thenReturn(articleList);

    final ResultActions actions = mockMvc.perform(get(BASE_URL))
        .andDo(print());

    actions
        .andExpect(status().isNoContent())
        .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
        .andExpect(jsonPath("$.data.size()").value(0));
  }

}