/**
 * Created with IntelliJ IDEA.
 *
 * @author Charlie
 *         To change this template use File | Settings | File Templates.
 */
public class HelloService {

    public String test(){
        if(1==1){
            throw new RuntimeException("sds");
        }
        return "hello world#";
    }

}
