package brotherjing.com.stepcountmaster;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * To work on unit tests, switch the Test Artifact in the Build Variants view.
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {

        int a,b,k;
        a = 12;
        b = 23;
        k = 3;

        boolean [] table = new boolean[b-a+1];

        for(int i=0;i<=b-a;++i)table[i]=true;

        for(int i=2;i<k;++i){
            for(int j=a;j<=b;++j){
                if(j%i==0)table[j-a]=false;
            }
        }

        int result = 0;
        for(int j=a;j<=b;++j){
            if(j%k!=0)table[j-a]=false;
            if(table[j-a]){
                result++;
            }
        }

        System.out.println(result);

    }

}