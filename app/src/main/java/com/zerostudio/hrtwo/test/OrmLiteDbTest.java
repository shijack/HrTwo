package com.zerostudio.hrtwo.test;

import android.test.AndroidTestCase;

import com.j256.ormlite.dao.Dao;
import com.zerostudio.hrtwo.db.dao.bean.Article;
import com.zerostudio.hrtwo.db.dao.bean.Student;
import com.zerostudio.hrtwo.db.dao.bean.User;
import com.zerostudio.hrtwo.db.dao.ormlite.ArticleDao;
import com.zerostudio.hrtwo.db.DatabaseHelper;
import com.zerostudio.hrtwo.db.dao.ormlite.UserDao;
import com.zerostudio.hrtwo.utils.L;

import java.sql.SQLException;
import java.util.List;

public class OrmLiteDbTest extends AndroidTestCase {
    public void testAddArticle() {
        User u = new User();
        u.setName("张鸿洋");
        new UserDao(getContext()).add(u);
        Article article = new Article();
        article.setTitle("ORMLite的使用");
        article.setUser(u);
        new ArticleDao(getContext()).add(article);

    }

    public void testGetArticleById() {
        Article article = new ArticleDao(getContext()).get(1);
        L.e(article.getUser() + " , " + article.getTitle());
    }

    public void testGetArticleWithUser() {

        Article article = new ArticleDao(getContext()).getArticleWithUser(1);
        L.e(article.getUser() + " , " + article.getTitle());
    }

    public void testListArticlesByUserId() {

        List<Article> articles = new ArticleDao(getContext()).listByUserId(1);
        L.e(articles.toString());
    }

    public void testGetUserById() {
        User user = new UserDao(getContext()).get(1);
        L.e(user.getName());
        if (user.getArticles() != null)
            for (Article article : user.getArticles()) {
                L.e(article.toString());
            }
    }

    public void testAddStudent() throws SQLException {
        Dao dao = DatabaseHelper.getInstance(getContext()).getDao(Student.class);
        Student student = new Student();
        student.setDao(dao);
        student.setName("张鸿洋");
        student.create();
    }


}
