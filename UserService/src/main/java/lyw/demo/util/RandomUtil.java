package lyw.demo.util;

public class RandomUtil {

    // 随机生成四位数
    public static int getRandNum(){
        return (int) ((Math.random() * 9 + 1) * 1000);
    }
}
