package lesson5;

public abstract class TestClass {

    public static Integer factorial1(int num) {
        if(num < 0)
            return null;
        else{
        int result = 1;
        for(int i=1;i<=num;i++)
            result=result*i;
        return result;
    }
}

    public static Integer factorial2(int num) {
        if(num < 0)
            return null;
        else{
            if (num == 0) return 1;
            return num*factorial2(num-1);
        }
    }


}
