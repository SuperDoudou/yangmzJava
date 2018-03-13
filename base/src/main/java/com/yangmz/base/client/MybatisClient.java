package com.yangmz.base.client;

import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class MybatisClient {

    private SqlSessionFactory sqlSessionFactory = null;
    private String confPath = null;

    public MybatisClient(String confPath){
        String path = this.getClass().getResource(confPath).getPath();
        Reader reader = null;
        try {
            reader = new FileReader(path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        sqlSessionFactory = new SqlSessionFactoryBuilder().build(reader);
        try {
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public SqlSession openSession(){
        SqlSession session = sqlSessionFactory.openSession();
        return session;
    }
    public void closeSession(SqlSession session){
        session.close();
    }

    public String getConfPath() {
        return confPath;
    }

    public void setConfPath(String confPath) {
        this.confPath = confPath;
    }
}
