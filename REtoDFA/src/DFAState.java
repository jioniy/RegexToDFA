import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.HashSet;

/** 
 * @author JiwonLee
 *
 * State of DFA
 *  
 */
public class DFAState {
	private static int stateCnt = 0; // 총 State갯수 -> 식별자 생성에 이용
	private int ID;
	private Set<NFAEState> NFAStateSet; 
	private Map<Character, Set<NFAEState>> tempNextStates;
    private Map<Character, DFAState> nextStates;
    
    
    
    public DFAState(Set<NFAEState> NFAStateSet) {
    	ID = stateCnt;
        tempNextStates = new HashMap<>();
        nextStates = new HashMap<>();
    	this.NFAStateSet = new HashSet<>(NFAStateSet);
    }
    public static void plusStateCount() {
    	stateCnt++;
    }
    
    public static int getStateCount() {
    	return stateCnt;
    }

    public int getID() {
    	return ID;
    }
    
    public Set<NFAEState> getNFAStateSet(){
    	return NFAStateSet;
    }
    
    public Map<Character, Set<NFAEState>> getTempNextStates(){
    	return tempNextStates;
    }
    public Map<Character, DFAState> getNextStates(){
    	return nextStates;
    }
    
    public void putNextState(char symbol, DFAState state) {
        nextStates.put(symbol, state);//key값이 존재하지 않으면 -> key와 value를 Map에 저장하고 null을 반환
    }
    
    
    
    public void putTempNextStates(char symbol, List<NFAEState> states) {
        tempNextStates.putIfAbsent(symbol, new HashSet<>());//key값이 존재하지 않으면 -> key와 value를 Map에 저장하고 null을 반환
        for(NFAEState ns : states) {
        	tempNextStates.get(symbol).add(ns);
        }
    }
    
    
    public boolean isSameState(Set<NFAEState> NFAStateSet) {//중복된 값 찾기 -> 총 nfa state 수가 모두 일치하면 동일한 State
    	List<NFAEState> resultList1 = this.NFAStateSet.stream()
    			.filter(e -> NFAStateSet.stream().anyMatch(Predicate.isEqual(e))).collect(Collectors.toList());
    	List<NFAEState> resultList2 = NFAStateSet.stream()
    			.filter(e -> this.NFAStateSet.stream().anyMatch(Predicate.isEqual(e))).collect(Collectors.toList());
    	
    	if(resultList1.size()==this.NFAStateSet.size()&&resultList2.size()==NFAStateSet.size()&&this.NFAStateSet.size()==NFAStateSet.size()) return true;
    	return false;
    }
    
    
    @Override
    public boolean equals(Object s){
        if(s instanceof DFAState){
            return isSameState(((DFAState)s).NFAStateSet);
        } else {
            return false;
        }
    }
    
    @Override
    public int hashCode(){
        return ID;
    }
}
