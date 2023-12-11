import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/** 
 * @author JiwonLee
 *
 * State of NFA-ε
 *  
 */
public class NFAEState {
	private static int stateCnt = 0; // 총 State갯수 -> 식별자 생성에 이용
	private int ID;
    private Map<Character, List<NFAEState>> nextStates;
    
    
    public NFAEState() {
    	ID = stateCnt;
        nextStates = new HashMap<>();
        stateCnt++;
    }
    
    public static int getStateCount() {
    	return stateCnt;
    }

    public int getID() {
    	return ID;
    }
    
    public Map<Character, List<NFAEState>> getNextStates(){
    	return nextStates;
    }
    
    public void put(char symbol, NFAEState state) {
        nextStates.putIfAbsent(symbol, new ArrayList<>());//key값이 존재하지 않으면 -> key와 value를 Map에 저장하고 null을 반환
        nextStates.get(symbol).add(state);
    }
    
    @Override
    public boolean equals(Object s){
        if(s instanceof NFAEState){
            return ID == ((NFAEState)s).ID;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return ID;
    }
}