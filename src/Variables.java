import java.math.BigInteger;
import java.util.ArrayList;

/**
 * Created by azaz on 17.03.15.
 */
public class Variables {
    static int startVal=Integer.MIN_VALUE,endVal=Integer.MIN_VALUE;
    static boolean endFlag;
    static boolean IsTo=true;
    static String input;
    static int pos;
    static char[] str;
    static enum Error{
        GenError,NoError
    }
    static Error error;
    static String margin="";
    static void setVar(char[] s,int p){
        str=s;
        pos=p;
    }
    static ArrayList<String> const_list= new ArrayList<>();
    static ArrayList<String> ident_list= new ArrayList<>();

    private Variables() {

    }
}
