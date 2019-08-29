package lyw.demo.service;

import lyw.demo.pojo.Column_value;

public interface SignUpService {
    //报名增加栏目值
    void signUp(Column_value column_value);

    void keep(Column_value column_value);

    //修改栏目值
    void update(Column_value column_value);
}
