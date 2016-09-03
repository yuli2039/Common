package com.y.greendaotest.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * 只需定义属性，然后makeProject就会自动生成数据库文件(gen下)和实体的get、set方法
 *
 * @author yu
 *         Create on 16/9/3.
 */
@Entity
public class User {

    /*
       (一) @Entity 定义实体
            @nameInDb 在数据库中的名字，如不写则为实体中类名
            @indexes 索引
            @createInDb 是否创建表，默认为true,false时不创建
            @schema 指定架构名称为实体
            @active 无论是更新生成都刷新
        (二) @Id
        (三) @NotNull 不为null
        (四) @Unique 唯一约束
        (五) @ToMany 一对多
        (六) @OrderBy 排序
        (七) @ToOne 一对一
        (八) @Transient 不存储在数据库中
        (九) @generated 由greendao产生的构造函数或方法
    */

    @Id
    private Long id;
    @NotNull
    private String name;
    private int age;
    private boolean sex;
    @Generated(hash = 1890432606)
    public User(Long id, @NotNull String name, int age, boolean sex) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.sex = sex;
    }
    @Generated(hash = 586692638)
    public User() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getAge() {
        return this.age;
    }
    public void setAge(int age) {
        this.age = age;
    }
    public boolean getSex() {
        return this.sex;
    }
    public void setSex(boolean sex) {
        this.sex = sex;
    }

}
