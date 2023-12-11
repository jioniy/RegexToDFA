import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DFAState {
	private static int stateCnt = 0; // 총 State갯수 -> 식별자 생성에 이용
	private int ID;
	private Set<NFAEState> NFAStateSet; 
    private Map<Character, DFAState> nextStates;
    
    
    public DFAState() {
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
    
    public void put(char symbol, DFAState state) {
        nextStates.put(symbol, state);//key값이 존재하지 않으면 -> key와 value를 Map에 저장하고 null을 반환
    }
    
    @Override
    public boolean equals(Object s){
        if(s instanceof DFAState){
            return ID == ((DFAState)s).ID;
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return ID;
    }
}
