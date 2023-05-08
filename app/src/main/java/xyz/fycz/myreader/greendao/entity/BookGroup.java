package xyz.fycz.myreader.greendao.entity;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author fengyue
 * @date 2020/9/23 22:18
 */
@Entity
public class BookGroup {
    @Id
    private String id;

    @NotNull
    private int num;

    @NotNull
    private String name;

    private String desc;

    @Generated(hash = 1387727906)
    public BookGroup(String id, int num, @NotNull String name, String desc) {
        this.id = id;
        this.num = num;
        this.name = name;
        this.desc = desc;
    }

    @Generated(hash = 511810489)
    public BookGroup() {
    }

    public String getId() {
        return this.id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesc() {
        return this.desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public int getNum() {
        return this.num;
    }

    public void setNum(int num) {
        this.num = num;
    }


}
