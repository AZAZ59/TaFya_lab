import java.security.NoSuchAlgorithmException;

public class Checkers {

    enum UnsignedConstState {
        Start, B, Finish, Error
    }
    enum SignedConstState {
        Start, A, B, Finish, Error
    }
    enum IdentifierState {
        Start, A, Finish, Error

    }
    enum SignState {
        Start, Finish, Error
    }
    enum OperState {
        Start, Finish, Error;
    }
    enum AssignState {
        Start, Finish, Error,
        B, SP1;
    }
    enum TermState {
        Start, Finish, Error;
    }
    enum EndValState {
        S, A, B, C, SP1, SP2, F, E
    }
    enum StartValState {
        Start, Finish, Error;
    }
    enum AllState {
        S, A, B, C, E, G, H, I, J, K, L, M, N, O,F,Error;
    }
    enum DefaultState {
        Start, Finish, Error;
    }

    private static char[] str;
    private static int pos;

    public static boolean CheckAll() throws MyException {
        System.out.println(Variables.margin+"All()");
        Variables.margin+=" ";
        str = Variables.str;
        pos = Variables.pos;
        AllState state= AllState.S;
        while (true) {
            switch (state) {
                case S:
                    if (str[pos] == 'f' && str[pos+1] == 'o' && str[pos+2] == 'r') {
                        pos += 3;
                        state = AllState.A;
                    }else {
                        state = AllState.Error;
                        throw new MyException("Ожидался for");
                    }
                    break;
                case A:
                    if(str[pos]==' '){
                        pos++;state=AllState.B;
                    }else {
                        state=AllState.Error;
                        throw new MyException("ожидался пробел");
                    }
                    break;
                case B:
                    if(str[pos]==' '){
                        pos++;state=AllState.B;
                    }else if(CheckIdent()){
                        state=AllState.C;
                    }else state=AllState.Error;
                    break;
                case C:
                    if(str[pos]==' '){
                        pos++;state=AllState.C;
                    }else if(str[pos]==':'&&str[pos+1]=='='){
                        pos+=2;
                        state=AllState.E;
                    }else {
                        state=AllState.Error;
                        throw new MyException("Ожидался :=");
                    }
                    break;
                case E:
                    if(str[pos]==' '){
                        pos++;state=AllState.E;
                    }else if(CheckStartVal()){
                        state=AllState.G;
                    }else state=AllState.Error;
                    break;
                case G:
                    if(str[pos]==' '){
                        pos++;state=AllState.H;
                    }else {
                        state=AllState.Error;
                        throw new MyException("ожидался пробел");
                    }
                    break;
                case H:
                    if(str[pos]==' '){
                        pos++;state=AllState.H;
                    }else
                    if(Variables.input.indexOf("downto", pos) != -1){
                        state=AllState.I;
                        Variables.IsTo=false;
                        pos+=6;
                    }else if(Variables.input.indexOf("to", pos) != -1 ){
                        state=AllState.I;
                        Variables.IsTo=true;
                        pos+=2;
                    }
                    else {
                        state=AllState.Error;
                        throw new MyException("ожидался to или downto");
                    }
                    break;
                case I:
                    if(str[pos]==' '){
                        pos++;state=AllState.J;
                    }else {
                        state=AllState.Error;
                        throw new MyException("ожидался пробел");
                    }
                    break;
                case J:
                    if(str[pos]==' '){
                        pos++;state=AllState.J;
                    }else if(CheckEndVal()){
                        state=AllState.K;
                    }else state=AllState.Error;
                    break;
                case K:////////////////////////////////////////////////////
                    /*if(str[pos]==' '){
                        pos++;state=AllState.L;
                    }else state=AllState.Error;*/
                    state=AllState.L;
                    break;
                case L:
                    if(str[pos]==' '){
                        pos++;state=AllState.L;
                    }else if(str[pos]=='d'&&str[pos+1]=='o'){
                        pos+=2;
                        state=AllState.M;
                        Variables.endFlag=false;
                    }else {
                        state=AllState.Error;
                        throw new MyException("ожидался do");
                    }
                    break;
                case M:
                    if(str[pos]==' '){
                        pos++;state=AllState.N;
                    }else {
                        state=AllState.Error;
                        throw new MyException("ожидался пробел");
                    }
                    break;
                case N:
                    if(str[pos]==' '){
                        pos++;state=AllState.N;
                    }else if(CheckAssign()){
                        state=AllState.O;
                    }else state=AllState.Error;
                    break;
                case O:
                    if(str[pos]==' '){
                        pos++;state=AllState.O;
                    }else if(str[pos]==';'){
                        state=AllState.F;
                    }else {
                        state=AllState.Error;
                        throw new MyException("ожидался ;");
                    }
                    break;
                case F:
                    Variables.setVar(str, pos);
                    if(pos==str.length-1) return true;
                    else throw new MyException("после ; не ожидается символов");
                case Error:
                default:
                    Variables.setVar(str, pos);
                    System.err.println("!!!!");
                    return false;
            }
        }
    }

    public static boolean CheckUnsignedConst() throws MyException {
        System.out.println(Variables.margin+"UnsignedConst()");
        Variables.margin+=" ";
        UnsignedConstState state = UnsignedConstState.Start;
        StringBuilder sb = new StringBuilder();
        while (true) {
            switch (state) {
                case Start:
                    if (str[pos] >= '0' && str[pos] <= '9') {
                        state = UnsignedConstState.B;
                        sb.append(str[pos]);
                        pos++;
                    } else {
                        state = UnsignedConstState.Error;
                        //throw new MyException("ожидалась цифра");
                    }
                    break;
                case B:
                    if (str[pos] >= '0' && str[pos] <= '9') {
                        state = UnsignedConstState.B;
                        sb.append(str[pos]);
                        pos++;
                    } else {
                        state = UnsignedConstState.Finish;
                    }
                    break;
                case Finish:
                    Variables.setVar(str, pos);
                    int val=Integer.parseInt(sb.toString());
                    //-32768  +32767
                    if(val>=-32768&&val<=32767) {
                        Variables.const_list.add(sb.toString());
                        return true;
                    }
                    else {
                        throw new MyException("константа "+val+" не в ходит в диапазон -32768 .. 32767");
                    }
                case Error:
                default:
                    Variables.setVar(str, pos);
                    ////th new NotImplementedException();
                    return false;
            }
        }

    }

    public static boolean CheckSignedConst() throws MyException {
        System.out.println("SignedConst()");
        SignedConstState state = SignedConstState.Start;
        StringBuilder sb = new StringBuilder();
        while (true) {
            switch (state) {
                case Start:
                    if (str[pos] == '+' || str[pos] == '-') {
                        state = SignedConstState.A;
                        sb.append(str[pos]);
                        pos++;
                    } else if (str[pos] >= '0' && str[pos] <= '9') {
                        state = SignedConstState.B;
                        sb.append(str[pos]);
                        pos++;
                    } else {
                        state = SignedConstState.Error;
//                        throw new MyException("ожидалась цифра или знак");
                    }
                    break;
                case A:
                    if (str[pos] >= '0' && str[pos] <= '9') {
                        state = SignedConstState.B;
                        sb.append(str[pos]);
                        pos++;
                    } else {
                        state = SignedConstState.Error;
                        throw new MyException("ожидалась цифра");
                    }
                    break;
                case B:
                    if (str[pos] >= '0' && str[pos] <= '9') {
                        state = SignedConstState.B;
                        sb.append(str[pos]);
                        pos++;
                    } else {
                        state = SignedConstState.Finish;
                    }
                    break;
                case Finish:
                    Variables.setVar(str, pos);
                    int val=Integer.parseInt(sb.toString());
                            //-32768  +32767
                    if(val>=-32768&&val<=32767) {
                        Variables.const_list.add(sb.toString());
                        return true;
                    }
                    else {
                        throw new MyException("константа "+val+" не в ходит в диапазон -32768 .. 32767");
                    }
                case Error:
                default:
                    Variables.setVar(str, pos);
                    //th new NotImplementedException();
                    return false;
            }
        }

    }

    public static boolean CheckIdent() throws MyException {
        System.out.println("Ident()");
        IdentifierState state = IdentifierState.Start;
        StringBuilder sb = new StringBuilder();
        while (true) {
            switch (state) {
                case Start:
                    if (str[pos] == '_' || ((str[pos] >= 'a' && str[pos] <= 'z'))) {
                        sb.append(str[pos]);
                        pos++;
                        state = IdentifierState.A;
                    } else {
                        state = IdentifierState.Error;
                        throw new MyException("ожидался знак подчёркивания или буква");
                    }
                    break;
                case A:
                    if ((str[pos] == '_')||(str[pos] >= 'a' && str[pos] <= 'z') || (str[pos] >= '0' && str[pos] <= '9')) {
                        sb.append(str[pos]);
                        pos++;
                        state = IdentifierState.A;
                    } else {
                        state = IdentifierState.Finish;
                    }
                    break;
                case Finish:
                    Variables.setVar(str, pos);
                    String ident=sb.toString();
                    if(ident.matches("(FOR|TO|DOWNTO|DO|DIV|MOD)".toLowerCase())) {
                        throw new MyException("идентификатор "+ident+" есть зарезервированное слово");

                    }else if (ident.length()>8 ) {
                        throw new MyException("идентификатор "+ident+" имеет длину больше 8 символов");
                    }else{
                        Variables.ident_list.add(sb.toString());
                        return true;
                    }

                case Error:
                default:
                    Variables.setVar(str, pos);
                    return false;
                    //th new NotImplementedException();
            }
        }
    }

    public static boolean CheckSign() throws MyException { System.out.println("Sign()");
        //DefaultState state= DefaultState.Start;
        SignState state = SignState.Start;
        while (true) {
            switch (state) {
                case Start:
                    if (str[pos] == '+' || str[pos] == '-') {
                        state = SignState.Finish;
                        pos++;
                    }else {
                        state=SignState.Error;
//                        throw new MyException("ожидался + или -");
                    }
                    break;
                case Finish:
                    Variables.setVar(str, pos);
                    return true;
                case Error:
                default:
                    Variables.setVar(str, pos);
                    return false;
                    //th new NotImplementedException();
            }
        }
    }

    public static boolean CheckOper() throws MyException { System.out.println("Oper()");
        //DefaultState state= DefaultState.Start;
        OperState state = OperState.Start;
        while (true) {
            switch (state) {
                case Start:
                    if (str[pos] == '+' || str[pos] == '-' || str[pos] == '*' || str[pos] == '/') {
                        state = OperState.Finish;
                        pos++;
                    }
                    else if (Variables.input.indexOf("div", pos) != -1 || Variables.input.indexOf("mod", pos) != -1) {
                        pos += 3;
                        state = OperState.Finish;
                    }
                    else {
                        state=OperState.Error;
                        //throw new MyException("ожидался знак операции");
                    }
                    break;
                case Finish:
                    Variables.setVar(str, pos);
                    return true;
                case Error:
                default:
                    Variables.setVar(str, pos);
                    return false;
                    //th new NotImplementedException();
            }
        }
    }

    public static boolean CheckAssign() throws MyException { System.out.println("Assign()");
        AssignState state = AssignState.Start;
        while (true) {
            switch (state) {
                case Start:
                    if (CheckIdent()) {
                        state = AssignState.SP1;
                    } else {
                        state = AssignState.Error;
                    }
                    break;
                case SP1:
                    if (str[pos] == ' ') {
                        pos++;
                        state = AssignState.SP1;
                    } else if (str[pos] == ':' && str[pos + 1] == '=') {
                        pos += 2;
                        state = AssignState.B;
                    } else {
                        state = AssignState.Error;
                    }
                    break;
                case B:
                    if (str[pos] == ' ') {
                        pos++;
                        state = AssignState.B;
                    } else if (CheckEndVal()) {
                        state = AssignState.Finish;
                    } else {
                        state = AssignState.Error;
                    }
                    break;

                case Finish:
                    Variables.setVar(str, pos);
                    return true;
                case Error:
                default:
                    Variables.setVar(str, pos);
                    return false;
                    //th new NotImplementedException();
            }
        }
    }

    public static boolean CheckTerm() throws MyException {
        System.out.println("Term()");
        TermState state = TermState.Start;
        while (true) {
            switch (state) {
                case Start:
                    if (CheckUnsignedConst()){
                        if(Variables.endFlag==true){
                            Variables.endVal=Integer.parseInt(Variables.const_list.get(Variables.const_list.size()-1));
                        }
                        Variables.endFlag=false;
                        state = TermState.Finish;
                    }
                    else if(CheckIdent()) {
                        state = TermState.Finish;
                    } else state = TermState.Error;
                    break;
                case Finish:
                    Variables.setVar(str, pos);
                    return true;
                case Error:
                default:
                    Variables.setVar(str, pos);
                    return false;
                    //th new NotImplementedException();
            }
        }

    }

    private static boolean CheckEndVal() throws MyException {
        System.out.println("EndVal()");
        EndValState state = EndValState.S;
        while (true) {
            switch (state) {
                case S:
                    if (str[pos] == ' ') {
                        state = EndValState.S;
                        pos++;
                    } else if (CheckSign()) {
                        state = EndValState.A;
                    } else if (CheckTerm()) {
                        state = EndValState.B;
                    } else state = EndValState.E;
                    break;
                case A:
                    if (CheckTerm()) {
                        state = EndValState.B;
                    } else state = EndValState.E;
                    break;
                case B:
                    if (str[pos] == ' ') {
                        pos++;
                        state = EndValState.SP1;
                    } else state = EndValState.SP1;
                    break;
                case SP1:
                    if (str[pos] == ' ') {
                        pos++;
                        state = EndValState.SP1;
                    } else if (CheckOper()) {
                        state = EndValState.C;
                    }else state= EndValState.F;
                    break;
                case C:
                    if (str[pos] == ' ') {
                        pos++;
                        state = EndValState.SP2;
                    } else state = EndValState.SP2;
                    break;
                case SP2:
                    if (str[pos] == ' ') {
                        pos++;
                        state = EndValState.SP1;
                    } else if (CheckTerm()) {
                        state = EndValState.F;
                    } else state = EndValState.E;
                    break;
                case F:
                    Variables.setVar(str, pos);
                    Variables.endFlag=false;
                    return true;
                case E:
                default:
                    Variables.setVar(str, pos);
                    Variables.endFlag=false;
                    return false;
                    //th new NotImplementedException();
            }
        }

    }

    public static boolean CheckStartVal() throws MyException { System.out.println("StartVal()");
        StartValState state = StartValState.Start;
        while (true) {
            switch (state) {
                case Start:
                    if (str[pos] == ' ') {
                        pos++;
                        state = StartValState.Start;
                    } else if (CheckSignedConst()) {
                        Variables.startVal=Integer.parseInt(Variables.const_list.get(Variables.const_list.size()-1));
                        state = StartValState.Finish;
                    }else if ( CheckIdent()){ state = StartValState.Finish;}
                    else state = StartValState.Error;
                    break;
                case Finish:
                    Variables.setVar(str, pos);
                    return true;
                case Error:
                default:
                    Variables.setVar(str, pos);
                    return false;
                    //th new NotImplementedException();
            }
        }
    }

    public static boolean CheckTempl() {
        System.out.println("Templ()");
        DefaultState state = DefaultState.Start;
        while (true) {
            switch (state) {
                case Start:
                    break;
                case Finish:
                    Variables.setVar(str, pos);
                    return true;
                case Error:
                default:
                    Variables.setVar(str, pos);
                    return false;
                    //th new NotImplementedException();
            }
        }
    }

}